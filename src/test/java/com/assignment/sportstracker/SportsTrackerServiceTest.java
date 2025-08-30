package com.assignment.sportstracker;

import com.assignment.sportstracker.dto.EventStatusRequest;
import com.assignment.sportstracker.dto.ExternalApiResponse;
import com.assignment.sportstracker.scheduler.EventScheduler;
import com.assignment.sportstracker.service.EventService;
import com.assignment.sportstracker.service.ExternalApiService;
import com.assignment.sportstracker.service.MessagePublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class SportsTrackerServiceTest {

    private EventService eventService;
    private ExternalApiService externalApiService;
    private KafkaTemplate<String, String> kafkaTemplate;
    private MessagePublisherService publisherService;
    private EventScheduler scheduler;

    @BeforeEach
    void setup() {
        eventService = new EventService();
        externalApiService = mock(ExternalApiService.class);
        kafkaTemplate = mock(KafkaTemplate.class);
        publisherService = new MessagePublisherService(kafkaTemplate);
        scheduler = new EventScheduler(eventService, externalApiService, publisherService);
    }


    @Test
    void testStatusUpdate() {
        EventStatusRequest req = new EventStatusRequest();
        req.setEventId("E1");
        req.setLive(true);

        eventService.updateEventStatus(req.getEventId(), req.getLive());

        assert eventService.getAllEvents().get("E1").isLive();
    }

    @Test
    void testScheduledPollCallsExternalApiAndPublishes() {
        eventService.updateEventStatus("E2", true);

        ExternalApiResponse response = new ExternalApiResponse();
        response.setEventId("E2");
        response.setCurrentScore("1:0");

        when(externalApiService.fetchEventData("E2")).thenReturn(response);
        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(null));

        scheduler.pollLiveEvents();

        verify(externalApiService, times(1)).fetchEventData("E2");
        verify(kafkaTemplate, times(1)).send("sports-events", "E2", response.toString());
    }

    @Test
    void testMessagePublishingFailure() {
        ExternalApiResponse response = new ExternalApiResponse();
        response.setEventId("E3");
        response.setCurrentScore("2:2");

        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Kafka is down"));

        publisherService.publish("sports-events", response);

        verify(kafkaTemplate, times(3)).send("sports-events", "E3", response.toString());
    }

    @Test
    void testSchedulerSkipsNotLiveEvents() {
        eventService.updateEventStatus("E4", false);

        scheduler.pollLiveEvents();

        verifyNoInteractions(externalApiService);
        verifyNoInteractions(kafkaTemplate);
    }
}
