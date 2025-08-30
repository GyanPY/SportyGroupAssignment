# Sports Tracker Service

A Spring Boot–based microservice that tracks “live” sports events.  
For each live event, the service periodically polls an external REST API, transforms the response into a message, and publishes it to Kafka.  

---

## 🚀 Features

- **REST API** to update event status (`live` ↔ `not live`)
- **Scheduler** that polls external API every 10 seconds for live events
- **Kafka integration**: publishes transformed event messages
- **Basic error handling & logging**
- **Unit tests** for:
  - Status updates
  - Scheduled calls
  - Message publishing (success + failure)

---

## 🛠️ Tech Stack

- Java 17  
- Spring Boot 3.2.x  
- Spring Web  
- Spring Kafka  
- Spring Scheduling
- JUnit 5 + Mockito (for testing)  

---

## ⚙️ Setup & Run

### 1. Prerequisites
- Java 17+
- Maven 3.8+

### 2. Run
 - mvn clean install
 - mvn spring-boot:run

## Test Cover

### Run all unit tests:

 - mvn test


### Tests cover:

✅ Event status updates (in-memory state)

✅ Scheduler triggering and polling external API

✅ Kafka publishing (success)

✅ Kafka publishing (failure gracefully handled)

## 💡 Design Decisions

Spring Boot chosen for productivity, auto-config, and wide ecosystem support.

In-memory map (ConcurrentHashMap) used for event state → simple, no DB overhead for assignment.

Spring Scheduling used for polling every 10s (configurable via application.properties).

Kafka retries handled via producer configuration (acks=all, retries=3, retry.backoff.ms=100).

### Error Handling:

@ControllerAdvice to catch validation/other errors.

Logged all state changes, API failures, and publishing errors.

### Testing:
Mockito used to simulate Kafka & external API → no real infra needed for tests.

## How I verified/improved:

Reviewed and cleaned code


## AI Usage

Test cases with Mockito (some parts for refactoring)

This README template


