package com.lockedIn.companion.dto;

public class ContentResponse {
    private String classification;

    public String getClassification() {
        return classification;
    }

    public ContentResponse(String classification, boolean allowed, String roast) {
        this.classification = classification;
        this.allowed = allowed;
        this.roast = roast;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public String getRoast() {
        return roast;
    }

    public void setRoast(String roast) {
        this.roast = roast;
    }

    private boolean allowed;

    private String roast;

    public ContentResponse() {
    }
}
