package com.lockedIn.lockedIn_backend.ai;

import com.lockedIn.lockedIn_backend.dto.ContentRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RoastService {

    private final ChatClient chatClient;

    public  RoastService(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    public String generateRoast(ContentRequest request){
        String prompt = """
             You are LockedIn, an aggressive accountability coach.
             The user started a study session and got distracted.
             Your job is to roast the user into returning to work immediately.
             Rules:
             - Use Gen Z language.
             - Be sarcastic.
             - Be ruthless but not hateful.
             - Keep it under 25 words.
             - No motivational quotes.
             - No explanations.
             - No emojis.
             - Make the user feel caught wasting time.
             - If the distraction is from Instagram, YouTube entertainment, Netflix, gaming, memes, reels, shorts, or social media, roast accordingly.
             - Generate only the roast text.
             
             Distraction Content: %s
             Distraction Source: %s""".formatted(request.getContent(), request.getSource());

        String roast = chatClient
                .prompt(prompt)
                .call()
                .content();

        if(roast == null || roast.isBlank()){
            return "what the fuck are you doing ? go back to study bro";
        }

        return roast;
    }
}
