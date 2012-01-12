package com.vonhofmeister.jcouchdb.http;

import java.net.InetSocketAddress;
import org.eclipse.jetty.server.Server;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
public class HttpServer extends Server {

    public HttpServer(InetSocketAddress addr) {
        super(addr);
    }

    public HttpServer(int port) {
        super(port);
    }

    
}
