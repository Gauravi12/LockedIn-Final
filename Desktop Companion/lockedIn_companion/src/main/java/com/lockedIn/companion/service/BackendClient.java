package com.lockedIn.companion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lockedIn.companion.dto.ContentResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.lockedIn.companion.config.CompanionConfig.BACKEND_URL;


public class BackendClient {
    HttpClient client = HttpClient.newHttpClient();
    private String jwtToken;

    public void setJwtToken(String jwtToken){
        this.jwtToken = jwtToken;
    }

    public String getSessionStatus(){
        try {
            System.out.println("JWT = " + jwtToken);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_URL + "session/status"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request , HttpResponse.BodyHandlers.ofString());

            return response.body();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void pauseSession(){
        try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BACKEND_URL + "session/pause"))
                        .header("Authorization", "Bearer " + jwtToken)
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();

                HttpResponse<String> response = client.send(request , HttpResponse.BodyHandlers.ofString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resumeSession(){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_URL + "session/resume"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request , HttpResponse.BodyHandlers.ofString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addDistraction(long idleTime){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_URL + "session/add/distraction"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type" , "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(idleTime)))
                    .build();
            HttpResponse<String> response = client.send(request , HttpResponse.BodyHandlers.ofString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ContentResponse checkContent(String content){
        try {
            String json = "{\"content\" : \"" + content +
                    "\", \"source\" : \"desktop\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_URL + "content/check"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type" , "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request , HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(
                    response.body(),
                    ContentResponse.class
            );

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void endSession(){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_URL + "session/end"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
