package com.vonhofmeister.jcouchdb.store;

import com.vonhofmeister.jcouchdb.documents.*;
import java.util.Map;
import java.util.Set;
import org.codehaus.jackson.JsonNode;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
public interface DataStore {

    public Map<String, String> getConfig();

    public Set<String> getDbs();

    public ServerInfo getServerInfo();

    public TaskList getTasks();

    public Session getSession();
    
    public UUIDBatch getUUIDs();

    public DbInfo getDbInfo(String dbName);
    
    public StatusMessage createDb(String string);

    public StatusMessage deleteDb(String string);
    
    public ViewResult queryAllDocs(String dbName, Parameters parms);
    
    public JsonNode getDocument(String dbName, String docId, Parameters parms);

    public JsonNode putDocument(String dbName, JsonNode doc);
    
    public StatusMessage deleteDocument(String string, JsonNode body);

    public JsonNode getDocumentHeader(String dbName, String docId);
    
    public Attachment getAttachment(String dbName, String docId, String filename, Parameters parms);
    
    public ViewInfo getViewInfo(String dbName, String ddoc, String viewName);

    public ViewResult queryView(String dbName, String ddoc, String viewName, Parameters parms);

}
