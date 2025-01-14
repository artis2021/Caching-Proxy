package org.cachingproxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class RequestHandler implements HttpHandler {
    private final CacheManager cacheManager;
    private final String origin;

    public RequestHandler(CacheManager cacheManager, String origin) {
        this.cacheManager = cacheManager;
        this.origin = origin;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        // Handle the clear-cache endpoint
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod()) && "/clear-cache".equals(path)) {
            cacheManager.clearCache();
            sendResponse(exchange, "Cache cleared successfully!".getBytes(), 200, "CLEAR");
            return;
        }

        String cacheKey = generateCacheKey(exchange);
        CacheObject cachedResponse = cacheManager.getCache(cacheKey);
        if (cachedResponse != null) {
            sendResponse(exchange, cachedResponse.getResponseBody(), cachedResponse.getStatusCode(), "HIT");
        } else {
            forwardToOrigin(exchange, cacheKey);
        }
    }

    private String generateCacheKey(HttpExchange exchange) throws IOException {
        StringBuilder cacheKey = new StringBuilder(exchange.getRequestMethod())
                .append(":")
                .append(exchange.getRequestURI());

        // Add request body hash for POST/PUT requests
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod()) || "PUT".equalsIgnoreCase(exchange.getRequestMethod()) || "PATCH".equalsIgnoreCase(exchange.getRequestMethod())) {
            String body = new String(exchange.getRequestBody().readAllBytes());
            if (!body.isEmpty()) {
                String bodyHash = hashBody(body);
                // remove every whitespace character
                bodyHash = bodyHash.replaceAll("\\s", "");
                cacheKey.append(":").append(bodyHash);
            }
        }

        return cacheKey.toString();
    }

    private String hashBody(String body) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(body.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash request body", e);
        }
    }

    private void forwardToOrigin(HttpExchange exchange, String cacheKey) throws IOException {
        String originUrl = origin + exchange.getRequestURI();
        @SuppressWarnings("deprecation")
        HttpURLConnection connection = (HttpURLConnection) new URL(originUrl).openConnection();
        connection.setRequestMethod(exchange.getRequestMethod());

        // Forward request body for POST/PUT
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod()) || "PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                exchange.getRequestBody().transferTo(os);
            }
        }

        // Read response from origin
        int statusCode = connection.getResponseCode();
        byte[] responseBody;
        try (InputStream is = statusCode < 400 ? connection.getInputStream() : connection.getErrorStream()) {
            responseBody = is.readAllBytes();
        }

        // Cache the response and send it back to the client
        cacheManager.addCache(cacheKey, new CacheObject(responseBody, statusCode));
        sendResponse(exchange, responseBody, statusCode, "MISS");
    }

    private void sendResponse(HttpExchange exchange, byte[] responseBody, int statusCode, String cacheStatus) throws IOException {
        exchange.getResponseHeaders().add("X-Cache", cacheStatus);
        exchange.sendResponseHeaders(statusCode, responseBody.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBody);
        }
    }
}