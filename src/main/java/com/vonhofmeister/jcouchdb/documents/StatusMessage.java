package com.vonhofmeister.jcouchdb.documents;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class StatusMessage {
    
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ERROR_CLIENT = 400;
    public static final int ERROR_NOT_FOUND = 404;
    public static final int ERROR_SERVER = 500;
    
    
    private final String message;
    private final Boolean ok;
    private final int status;

    public StatusMessage(String message) {
        this(message, true);
        
    }
    public StatusMessage(String message,int status) {
        this(message, null,status);
        
    }
    
    public StatusMessage(Boolean ok) {
        this(null, ok);
    }
    
    public StatusMessage(Boolean ok, int status) {
        this(null, ok,status);
    }
    
    public StatusMessage(String message, Boolean ok) {
        this(message, ok, ok ? OK : ERROR_CLIENT);
    }

    public StatusMessage(String message, Boolean ok, int status) {
        this.message = message;
        this.ok = ok;
        this.status = status;
    }

    
    
    public String getMessage() {
        return message;
    }

    public Boolean getOk() {
        return ok;
    }

    public int getStatus() {
        return status;
    }
}
