package com.vonhofmeister.jcouchdb.http;

/**
 *
 * @author Henrik Hofmeister (@vonhofdk)
 */
public class HttpResponse {
    private final int statusCode;
    private final String mimeType;
    private final byte[] data;

    public HttpResponse(int statusCode, String mimeType, byte[] data) {
        this.statusCode = statusCode;
        this.mimeType = mimeType;
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
