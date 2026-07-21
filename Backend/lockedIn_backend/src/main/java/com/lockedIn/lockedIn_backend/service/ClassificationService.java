package com.lockedIn.lockedIn_backend.service;

import com.lockedIn.lockedIn_backend.ai.AiClassificationService;
import com.lockedIn.lockedIn_backend.ai.RoastService;
import com.lockedIn.lockedIn_backend.dto.ContentRequest;
import com.lockedIn.lockedIn_backend.dto.ContentResponse;
import com.lockedIn.lockedIn_backend.entity.ContentClassification;
import com.lockedIn.lockedIn_backend.repository.ContentClassificationRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClassificationService {

    private final ContentClassificationRepo contentClassificationRepo;
    private final AiClassificationService aiClassificationService;
    private final RoastService roastService;

    public ClassificationService(ContentClassificationRepo contentClassificationRepo , AiClassificationService aiClassificationService , RoastService roastService){
        this.contentClassificationRepo = contentClassificationRepo;
        this.aiClassificationService = aiClassificationService;
        this.roastService = roastService;
    }

    public ContentResponse checkContent(ContentRequest request){
        Optional<ContentClassification> contentClassification = contentClassificationRepo.findByContentAndSource(request.getContent() , request.getSource());

        ContentResponse response = new ContentResponse();

        if(contentClassification.isPresent()){
            response.setClassification(contentClassification.get().getClassification());
            response.setAllowed(contentClassification.get().isAllowed());
        }else{
            String classify = aiClassificationService.classify(request);
            response.setClassification(classify);

            response.setAllowed(classify.equals("EDUCATIONAL") || classify.equals("PRODUCTIVE"));

            ContentClassification contentClassification1 = new ContentClassification();
            contentClassification1.setContent(request.getContent());
            contentClassification1.setSource(request.getSource());
            contentClassification1.setClassification(classify);
            contentClassification1.setAllowed(response.isAllowed());

            contentClassificationRepo.save(contentClassification1);
        }

        if(response.isAllowed()){
            response.setRoast(null);
        }else{
            response.setRoast(roastService.generateRoast(request));
        }

        return response;
    }

}
