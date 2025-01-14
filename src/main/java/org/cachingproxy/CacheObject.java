package org.cachingproxy;

import java.time.LocalDateTime;

public class CacheObject {
    private final byte[] responseBody;
    private final int statusCode;
    private final LocalDateTime created;

    public CacheObject(byte[] responseBody, int statusCode) {
        this.responseBody = responseBody;
        this.statusCode = statusCode;
        this.created = LocalDateTime.now();
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
