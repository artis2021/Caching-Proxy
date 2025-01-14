# Caching Proxy Server

A high-performance caching proxy server implemented in Java that optimizes API request handling by caching responses from an origin server. The server supports dynamic cache management, request forwarding, and multithread-safe operations.

## Key Features

- *Dynamic Caching*:
    - Caches API responses using unique cache keys based on HTTP methods, request URIs, and SHA-256 hashed request body (for POST/PUT requests with JSON payloads).

- *Cache Management*:
    - Provides an endpoint GET /clear-cache to clear all cached data on demand without forwarding the request to the origin server.

- *Request Forwarding*:
    - Transparently forwards uncached requests to the origin server, caches the response, and sends it back to the client.

- *Scalability and Efficiency*:
    - Thread-safe operations for concurrent request handling.
    - Adds X-Cache headers (HIT, MISS, CLEAR) to responses for visibility into caching behavior.

## Usage Instructions

### Prerequisites
- *Java 21* installed
- *Maven* installed
- Basic knowledge of command-line arguments


