package com.assignment.sportstracker.dto;

public class ExternalApiResponse {
    private String eventId;
    private String currentScore;

    public ExternalApiResponse() {}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(String currentScore) {
        this.currentScore = currentScore;
    }
}
