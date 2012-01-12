package com.vonhofmeister.jcouchdb.documents;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
public class ServerInfo {
    private final String name;
    private final String version;

    public ServerInfo(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
