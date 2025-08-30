package com.assignment.sportstracker.service;

import com.assignment.sportstracker.dto.ExternalApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    public ExternalApiResponse fetchEventData(String eventId) {
        String apiBaseUrl = "http://mock-api/events/";
        String url = apiBaseUrl + "/events/" + eventId;
        try {
            return restTemplate.getForObject(url, ExternalApiResponse.class);
        } catch (Exception e) {
            log.error("Failed to fetch event {} data from external API: {}", eventId, e.getMessage());
            return null;
        }
    }
}
