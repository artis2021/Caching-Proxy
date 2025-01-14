package org.cachingproxy;

import java.time.LocalDateTime;
import java.util.Arrays;

public class CacheObject {
    private byte [] responseBody;
    private int statusCode;
    private LocalDateTime responseTime;
    public CacheObject(byte [] responseBody, int statusCode) {
        this.responseBody = responseBody;
        this.statusCode = statusCode;
        this.responseTime = LocalDateTime.now();
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return "CacheObject{" +
                "responseBody=" + Arrays.toString(responseBody) +
                ", statusCode=" + statusCode +
                ", responseTime=" + responseTime +
                '}';
    }
}
