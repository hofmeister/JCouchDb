package com.vonhofmeister.jcouchdb.store;

import com.vonhofmeister.jcouchdb.JCouchDb;
import com.vonhofmeister.jcouchdb.documents.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
public class MemDataStore implements DataStore{
    private final Map<String, String> config = new HashMap<String, String>();
    private final Map<String,MemDb> dbs = new HashMap<String,MemDb>();
    private final TaskList tasks = new TaskList();

    public Map<String, String> getConfig() {
        return config;
    }

    public Set<String> getDbs() {
        return dbs.keySet();
    }

    public ServerInfo getServerInfo() {
        return new ServerInfo(JCouchDb.NAME, JCouchDb.VERSION);
    }

    public TaskList getTasks() {
        return tasks;
    }

    public Session getSession() {
        return new Session();
    }

    public DbInfo getDbInfo(String dbName) {
        return dbs.get(dbName);
    }

    public StatusMessage createDb(String name) {
        if (dbs.containsKey(name))
            return new StatusMessage(false,412);
        dbs.put(name,new MemDb(name));
        return new StatusMessage(true,201);
    }

    public StatusMessage deleteDb(String name) {
        dbs.remove(name);
        return new StatusMessage(true);
    }

    public ViewResult queryAllDocs(String dbName, Parameters parms) {
        return new ViewResult(dbs.get(dbName).documents.values());
    }

    public JsonNode getDocument(String dbName, String docId, Parameters parms) {
        return dbs.get(dbName).documents.get(docId);
    }

    public JsonNode putDocument(String dbName, JsonNode doc) {
        dbs.get(dbName).documents.put(doc.get("_id").getTextValue(), doc);
        return doc;
    }

    public StatusMessage deleteDocument(String dbName, JsonNode doc) {
        dbs.get(dbName).documents.remove(doc.get("_id").getTextValue());
        return new StatusMessage("ok");
    }

    public JsonNode getDocumentHeader(String dbName, String docId) {
        return dbs.get(dbName).documents.get(docId);
    }

    public Attachment getAttachment(String dbName, String docId, String filename, Parameters parms) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ViewInfo getViewInfo(String dbName, String ddoc, String viewName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ViewResult queryView(String dbName, String ddoc, String viewName, Parameters parms) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UUIDBatch getUUIDs() {
        UUIDBatch uuidBatch = new UUIDBatch();
        uuidBatch.getUuids().add(UUID.randomUUID().toString());
        return uuidBatch;
    }
    
    private static class MemDb extends DbInfo {
        
        @JsonIgnore
        final Map<String,JsonNode> documents = new HashMap<String, JsonNode>();
        final String name;

        public MemDb(String name) {
            this.name = name;
        }

        @Override
        public long getDocCount() {
            return documents.size();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getUpdateSequence() {
            return 0;
        }

        @Override
        public long getDiskSize() {
            return 0;
        }
        
        
    }
    
}