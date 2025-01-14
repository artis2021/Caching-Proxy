package org.cachingproxy;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class ProxyServer {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter port: ");
        int port = scanner.nextInt();

        System.out.print("Enter Proxy Server URL: ");
        String proxy = scanner.next();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.start();

        System.out.println("Proxy Server started on port " + port + " forwarding requests to " + proxy);



    }
}
