package com.vonhofmeister.jcouchdb;

import com.vonhofmeister.jcouchdb.http.HttpHandler;
import com.vonhofmeister.jcouchdb.http.HttpServer;
import com.vonhofmeister.jcouchdb.store.MemDataStore;

/**
 * 
 * @author Henrik Hofmeister (@vonhofdk)
 */
public class JCouchDb {
    
    public static final String VERSION  = "0.1-ALPHA";
    public static final String NAME     = "JCouchDb";

    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer(5999);
        server.setHandler(new HttpHandler(new MemDataStore()));
        server.start();
        server.join();
    }
}
