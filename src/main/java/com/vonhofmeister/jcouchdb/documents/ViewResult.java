package com.vonhofmeister.jcouchdb.documents;

import com.vonhofmeister.jcouchdb.documents.ViewResult.Row;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
public final class ViewResult {
    private final Collection<Row> rows = new ArrayList<Row>();

    public ViewResult() {
        
    }
    
    public ViewResult(Collection<JsonNode> docs) {
        addDocs(docs);
    }
    
    public Collection<Row> getRows() {
        return Collections.unmodifiableCollection(rows);
    }
    public void addDoc(JsonNode doc) {
        rows.add(new Row(doc.get("_id").getTextValue(),doc));
    }
    
    public void addDocs(Collection<JsonNode> docs) {
        for(JsonNode doc:docs)
            addDoc(doc);
    }
    
    @JsonProperty("total_rows")
    public int getTotalRows() {
        return rows.size();
    }
    
    @JsonProperty("offset")
    public int getOffset() {
        return 0;
    }
    
    public static class Row {
        private String id;
        private String key;
        private JsonNode value = null;
        private JsonNode doc;

        public Row(String id, JsonNode doc) {
            this.id = id;
            this.key = id;
            this.doc = doc;
        }

        public JsonNode getDoc() {
            return doc;
        }

        public String getId() {
            return id;
        }

        public String getKey() {
            return key;
        }

        public JsonNode getValue() {
            return value;
        }
    }
}
