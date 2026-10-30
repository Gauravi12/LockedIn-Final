package com.lockedIn.companion.service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

public class CompanionHttpServer {

    private final BackendClient backendClient;
    public CompanionHttpServer(BackendClient backendClient){
        this.backendClient = backendClient;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/token", exchange -> {
            InputStream inputStream = exchange.getRequestBody();
            String token = new String(inputStream.readAllBytes());
//            System.out.println("Received JWT = " + token);
            backendClient.setJwtToken(token);
            String response = "JWT Saved";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });
        server.start();
        System.out.println("Companion HTTP Server Started on Port 9090");
    }
}
