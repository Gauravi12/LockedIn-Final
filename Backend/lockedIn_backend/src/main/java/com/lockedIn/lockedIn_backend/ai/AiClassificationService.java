package com.lockedIn.lockedIn_backend.ai;

import com.lockedIn.lockedIn_backend.dto.ContentRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiClassificationService {
    private final ChatClient chatClient;
    public AiClassificationService(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    public String classify(ContentRequest request){
        String prompt =
                """
                You are a content classifier.
        
                Return ONLY ONE of:
        
                EDUCATIONAL
                PRODUCTIVE
                ENTERTAINMENT
        
                Do not explain.
                Do not add punctuation.
                Do not add extra text.
        
                Content: %s
                Source: %s
                """
                        .formatted(request.getContent(), request.getSource());
        String response = chatClient
                .prompt(prompt)
                .call()
                .content();

        if(response == null){
            return "ENTERTAINMENT";
        }

        String classification = response.trim().toUpperCase();

        if(!classification.equals("EDUCATIONAL")
                && !classification.equals("PRODUCTIVE")
                && !classification.equals("ENTERTAINMENT")){
            return "ENTERTAINMENT";
        }

        return classification;
    }
}
