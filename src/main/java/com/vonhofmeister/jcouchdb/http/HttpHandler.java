package com.vonhofmeister.jcouchdb.http;

import com.vonhofmeister.jcouchdb.documents.StatusMessage;
import com.vonhofmeister.jcouchdb.store.DataStore;
import com.vonhofmeister.jcouchdb.store.Parameters;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
public class HttpHandler extends AbstractHandler {
    private final ObjectMapper om = new ObjectMapper();
    private final DataStore store;

    public HttpHandler(DataStore store) {
        this.store = store;
    }

    public void handle(
            String path,
            Request request, HttpServletRequest httpReq,
            HttpServletResponse httpResp) throws IOException, ServletException {

        Object output;
        try {
            output = this.route(path,httpReq);
        } catch (Throwable ex) {
            output = new StatusMessage(ex.getMessage(),StatusMessage.ERROR_SERVER);
        }
        
        formatOutput(httpResp,output);
        
        request.setHandled(true);
    }
    
    public void formatOutput(HttpServletResponse httpResp,Object output) throws IOException {
        
        if (output instanceof HttpResponse) {
            HttpResponse resp = (HttpResponse) output;
            httpResp.addHeader("Content-type", resp.getMimeType());
            httpResp.setStatus(resp.getStatusCode());
            httpResp.getOutputStream().write(resp.getData());
        } else {
            httpResp.addHeader("Content-type", "application/json");
            if (output instanceof StatusMessage)
                httpResp.setStatus(((StatusMessage)output).getStatus());
            else
                httpResp.setStatus(200);
            om.writeValue(httpResp.getOutputStream(), output);
        }
            
        
        
    }
    
    private Object handleUtils(String path,HttpServletRequest httpReq) throws IOException {
        if (path.equals("/") || path.isEmpty())
            path = "index.html";
        if (path.charAt(0) != '/') {
            path = "/www/" + path;
        } else {
            path = "/www" + path;
        }
        
        InputStream fileStream = getClass().getResourceAsStream(path);
        byte[] bytes = IOUtils.toByteArray(fileStream);
        String mimeType = getMimeType(path, "text/html");
        return new HttpResponse(200, mimeType, bytes);
    }
    
    private String getMimeType(String path,String defaultType) {
        String[] parts = path.toLowerCase().split("/\\?/i");
        if (parts[0].endsWith(".ico"))
            return "image/icon";
        if (parts[0].endsWith(".jpg"))
            return "image/jpeg";
        if (parts[0].endsWith(".png"))
            return "image/png";
        if (parts[0].endsWith(".html"))
            return "text/html";
        if (parts[0].endsWith(".js"))
            return "text/javascript";
        if (parts[0].endsWith(".css"))
            return "text/css";
        return defaultType;
    } 
    
    private Object route(String path,HttpServletRequest httpReq) throws IOException {
        
        final Parameters parms = new Parameters(httpReq.getParameterMap());
        
        if (path.isEmpty() || path.equals("/")) {
            return store.getServerInfo();
        }
        
        if (path.startsWith("/_utils")) {
            return handleUtils(path.substring(7), httpReq);
        }
        
        if (path.equals("/_all_dbs")) {
            return store.getDbs();
        }
        
        if (path.equals("/_session")) {
            return store.getSession();
        }
        if (path.equals("/_active_tasks")) {
            return store.getTasks();
        }
        if (path.equals("/_config")) {
            return store.getConfig();
        }
        if (path.startsWith("/_config/query_servers")) {
            Map<String,String> out = new HashMap<String, String>();
            out.put("javascript", "bin/couchjs share/couchdb/server/main.js");
            return out;
        }
        if (path.startsWith("/_config/native_query_servers")) {
            Map<String,String> out = new HashMap<String, String>();
            return out;
        }
        
        
        if (path.equals("/_uuids")) {
            return store.getUUIDs();
        }
        
        if (path.startsWith("/_")) {
            return new StatusMessage("Invalid path",StatusMessage.ERROR_CLIENT);
        }
        HttpMethod method = HttpMethod.valueOf(httpReq.getMethod().toUpperCase());
        JsonNode body = null;
        if (method.isAnyOf(HttpMethod.POST,HttpMethod.PUT,HttpMethod.DELETE)) {
            if (httpReq.getHeader("Content-type") != null 
                    && httpReq.getHeader("Content-type").equalsIgnoreCase("application/json"))
                if (httpReq.getInputStream().available() > 0)
                    body = om.readTree(httpReq.getInputStream());
        }
        String[] parts = path.split("/");
        switch ((parts.length - 1)) {
            case 1:
                if (method.equals(HttpMethod.PUT))
                    return store.createDb(parts[1]);
                else if (method.equals(HttpMethod.GET))
                    return store.getDbInfo(parts[1]);
                else if (method.equals(HttpMethod.DELETE))
                    return store.deleteDb(parts[1]);
                else
                    return new StatusMessage("HTTP Method not supported for this method",StatusMessage.ERROR_CLIENT);
                
            case 2:
                if (parts[2].equals("_all_docs")) {
                    if (!method.equals(HttpMethod.GET))
                        return new StatusMessage("Only GET supported for this method",StatusMessage.ERROR_CLIENT);
                    return store.queryAllDocs(parts[1], parms);
                }
                if (parts[2].startsWith("_")) {
                    return new StatusMessage("Invalid path",StatusMessage.ERROR_CLIENT);
                }
                return handleDocument(method,parts[1],parts[2],parms,body);
                
                
            case 3:
                if (!parts[2].equals("_design")) {
                    //Design documents are allowed to have / in the path
                    return handleDocument(method,parts[1],String.format("%s/%s",parts[2],parts[3]),parms,body);
                }

                if (!parts[2].startsWith("_")) {
                    //Document attachment
                    //@TODO: Handle PUT etc.
                    return store.getAttachment(parts[1],parts[2],parts[3],parms);
                }

                return new StatusMessage("Invalid path",StatusMessage.ERROR_CLIENT);
            case 4:
                if (parts[2].equals("_design")) {
                    if (parts[4].equals("_view")) {
                        if (!method.equals(HttpMethod.GET))
                            return new StatusMessage("Only GET supported for this method",StatusMessage.ERROR_CLIENT);
                        if (parms.hasAny("key","startkey","endkey"))
                            return store.queryView(parts[1],parts[3],parts[5], parms);
                        else
                            return store.getViewInfo(parts[1],parts[3],parts[5]);
                    }
                }
        }
        return new StatusMessage("Invalid path",StatusMessage.ERROR_CLIENT);
    }
    
    private Object handleDocument(HttpMethod method,String dbName,String docId,Parameters parms,JsonNode body) {
        if (method.equals(HttpMethod.GET))
            return store.getDocument(dbName,docId,parms);
        else if (method.equals(HttpMethod.PUT))
            return store.putDocument(dbName,body);
        else if (method.equals(HttpMethod.DELETE))
            return store.deleteDocument(dbName,body);
        else if (method.equals(HttpMethod.HEAD))
            return store.getDocumentHeader(dbName,docId);
        else
            return new StatusMessage("HTTP Method not supported for this method",StatusMessage.ERROR_CLIENT);
    }
    public static enum HttpMethod {
        GET,PUT,POST,HEAD,DELETE,OPTION;

        private boolean isAnyOf(HttpMethod ... methods) {
            for(HttpMethod m:methods) {
                if (m.equals(this)) 
                    return true;
            }
            return false;
        }
    }
}
