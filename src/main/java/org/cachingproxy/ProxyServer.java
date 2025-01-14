package org.cachingproxy;

import com.sun.net.httpserver.HttpServer;
import org.cachingproxy.RequestHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class ProxyServer {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        CacheManager cacheManager = new CacheManager();

        System.out.print("Enter port: ");
        int port = scanner.nextInt();

        System.out.print("Enter origin server URL: ");
        String origin = scanner.next();

        scanner.close();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RequestHandler(cacheManager, origin));
        server.start();

        System.out.printf("Proxy server started on port %d. Forwarding requests to %s%n", port, origin);
    }
}