package com.lockedIn.lockedIn_backend.controller;

import com.lockedIn.lockedIn_backend.dto.ContentRequest;
import com.lockedIn.lockedIn_backend.dto.ContentResponse;
import com.lockedIn.lockedIn_backend.service.BlockingService;
import com.lockedIn.lockedIn_backend.service.ClassificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
@CrossOrigin(origins = "*")
public class ContentController {

    private final ClassificationService classificationService;
    private final BlockingService blockingService;

    public ContentController(ClassificationService classificationService , BlockingService blockingService){
        this.classificationService = classificationService;
        this.blockingService = blockingService;
    }

    @PostMapping("/check")
    public ContentResponse checkContent(@RequestBody ContentRequest request){
        ContentResponse response = classificationService.checkContent(request);
        blockingService.checkBlock(response , request);
        return response;
    }

}
