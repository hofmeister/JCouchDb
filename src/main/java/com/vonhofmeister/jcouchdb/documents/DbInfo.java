package com.vonhofmeister.jcouchdb.documents;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
public abstract class DbInfo {
    
    @JsonProperty("db_name")
    public abstract String getName();
    
    @JsonProperty("update_seq")
    public abstract long getUpdateSequence();
    
    @JsonProperty("doc_count")
    public abstract long getDocCount();
    
    @JsonProperty("disk_size")
    public abstract long getDiskSize();
}
