# RideRecs AI - AI-Powered Car Sales Platform

## Overview

RideRecs AI is a microservices-based car sales application inspired by Carsales.com.au. It enables sellers to list vehicles, buyers to search and purchase cars, and leverages AI for intelligent recommendations. Developed for the CSCI318 group project at the University of Wollongong, this project adheres to Domain-Driven Design (DDD) principles, with four bounded contexts implemented as independent Spring Boot microservices. Inter-service communication uses REST APIs for synchronous calls and Apache Kafka for asynchronous event-driven updates (e.g., a new car listing event triggers real-time analytics). Stream processing supports live data queries, such as trending car makes, while agentic AI components powered by Ollama (Llama3 model) deliver features like dynamic price suggestions and a conversational buyer assistant.

Core features include:
- User authentication and management (buyers and sellers)
- Car listing creation, search, and availability checks
- Transaction handling for purchases
- Real-time analytics via stream processing
- AI-driven price recommendations and buyer query assistance

This repository includes full source code, configuration files, a Docker Compose setup for Kafka/Zookeeper, and a complete Postman collection for API testing. **Important: Our team is keeping this repository private until project submission to avoid sharing with other groups, as per the CSCI318 guidelines.**

## Prerequisites

Before setup, ensure the following are installed:
- **Java 17+**: Verify with `java -version` (JDK required for Spring Boot).
- **Maven**: Verify with `mvn -version` (for building projects).
- **Docker**: Verify with `docker --version` (for Kafka and Zookeeper).
- **Ollama**: Download from [ollama.com](https://ollama.com) for local AI inference (no API keys needed).
- **Postman**: Optional, but essential for testing APIs (download from [postman.com](https://www.postman.com/downloads/)).
- **IDE**: IntelliJ IDEA or any IDE of choice.
- **Operating System**: Tested on Windows; adaptable to macOS/Linux.

All services use H2 in-memory database for local development—no external DB setup required. Internet is only needed for initial downloads.

## Project Structure

The monorepo structure organises four microservices for easy management:
```json
riderecs-ai/
├── docker-compose.yml # Kafka + Zookeeper setup
├── RideRecs-Postman-Collection.json # API testing collection
├── user-management-service/ # Bounded context: User auth & profiles (Port 8082)
│ ├── pom.xml
│ ├── src/main/java/... # Full Spring Boot code
│ └── README.md (service-specific notes)
├── car-listings-service/ # Bounded context: Listings & search + stream analytics (Port 8081)
│ ├── pom.xml
│ ├── src/main/java/...
│ └── README.md
├── transactions-service/ # Bounded context: Purchases & payments (Port 8083)
│ ├── pom.xml
│ ├── src/main/java/...
│ └── README.md
└── ai-insights-service/ # Bounded context: AI agents & recommendations (Port 8084)
├── pom.xml
├── src/main/java/...
└── README.md
```
Each service is self-contained with mandatory dependencies: Spring Web, Spring Data JPA, H2, Spring Cloud Stream (for Kafka), and LangChain4j (for AI).

## Setup and Running Instructions

Follow these steps sequentially. Open terminals (Command Prompt on Windows) for each command. Use `Ctrl+C` to stop services.

### 1. Launch Kafka and Zookeeper (Event-Driven Backbone)
Kafka enables async events like `CarListedEvent` or `TransactionCompletedEvent`.

1. In the project root (`riderecs-ai/`), create `docker-compose.yml` by copying this content:
```json
version: '3.8'

services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    hostname: zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
  kafka:
    image: confluentinc/cp-kafka:7.5.0
    hostname: kafka
    container_name: kafka
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
      - "29092:29092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
```

2. Run: `docker-compose up -d` (detached mode).
3. Verify: `docker ps` (shows running containers).
4. To stop: `docker-compose down`.

### 2. Set Up Ollama for AI Components
Ollama runs Llama3 locally for agentic AI (e.g., price analysis via LangChain4j).

1. Install Ollama: Run the Windows installer from [ollama.com/download](https://ollama.com/download).
2. Open a new terminal.
3. Download model: `ollama pull llama3` (~4GB, 5-15 minutes first time).
4. Start server: `ollama serve` (keep terminal open; connects to `http://localhost:11434`).
5. Verify: New terminal > `ollama list` (lists llama3).

### 3. Build and Run Microservices
Build each service individually, then run in dependency order (User Mgmt first, as others reference users).

For each service folder (e.g., `cd user-management-service`):
- Build: `mvn clean install`
- Run: `mvn spring-boot:run` (in separate terminals)

1. **User Management (Port 8082)**: Handles registration/login. Run first.
2. **Car Listings (Port 8081)**: Listings, search, and stream analytics (e.g., popular makes via Cloud Stream).
3. **Transactions (Port 8083)**: Sale processing; listens to Kafka events.
4. **AI Insights (Port 8084)**: AI endpoints; requires Ollama.

Success: Access `http://localhost:8081/api/car-listings` (returns empty array initially). Logs show "Started [Service]Application".

## API Testing with Postman Collection

The included `RideRecs-Postman-Collection.json` covers all endpoints across services. It pre-populates sample data for demos.

### Import Guide 
1. Install/Launch Postman.
2. Copy the JSON from the file into Notepad, save as `RideRecs-Postman-Collection.json` (All Files type, Desktop location).
3. In Postman: Collections > Import > Drag the JSON file.
4. Expand "RideRecs AI - Complete Collection" for folders (e.g., User Management).

Test sequence:
- Register users (e.g., seller ID=1, buyer ID=2).
- Create listings (publishes Kafka events).
- Search/query analytics/AI.
- Process transactions.

This automates use cases, including stream processing triggers.

## Use Case Examples

Demonstrate via Postman (sample inputs only; outputs depend on runtime data). No frontend required—APIs are the interface.

### 1. User Registration (Seller)
**Endpoint**: POST `http://localhost:8082/api/users/register`  
**Input**:
```json
{
"email": "john.seller@example.com",
"password": "password123",
"firstName": "John",
"lastName": "Seller",
"phoneNumber": "0412345678",
"userType": "SELLER"
}
```
*Purpose*: Onboard sellers for listings.

### 2. Create & Search Car Listing
**Endpoint**: POST `http://localhost:8081/api/car-listings`  
**Input**:
```json
{
"make": "Toyota",
"model": "Camry",
"year": 2018,
"price": 25000,
"mileage": 45000,
"condition": "Excellent",
"description": "Well-maintained family sedan",
"sellerId": 1
}
```
**Follow-up**: GET `http://localhost:8081/api/car-listings/search?make=Toyota&model=Camry`  
*Purpose*: List/search cars; auto-publishes event for analytics.

### 3. Stream Processing Analytics
**Endpoint**: GET `http://localhost:8081/api/analytics/popular-makes`  
**Input**: None (run after listings).  
*Purpose*: Real-time top makes from Kafka streams.

### 4. Transaction Creation
**Endpoint**: POST `http://localhost:8083/api/transactions`  
**Input**:
```json
{
"carId": 1,
"buyerId": 2,
"sellerId": 1,
"amount": 25000,
"paymentMethod": "Bank Transfer"
}
```
*Purpose*: Complete sale; triggers completion event.

### 5. AI Price Recommendation
**Endpoint**: POST `http://localhost:8084/api/ai-insights/price-recommendation`  
**Input**:
```json
{
"make": "Honda",
"model": "Civic",
"year": 2020,
"mileage": 30000,
"condition": "Good",
"currentPrice": 22000
}
```
*Purpose*: LLM agent suggests adjustments (via LangChain4j + Ollama).

### 6. AI Buyer Assistant
**Endpoint**: POST `http://localhost:8084/api/ai-insights/buyer-assistant`  
**Input**:
```json
{
"query": "What's the best sedan under $20,000?",
"context": "Available cars: Toyota Camry 2018 ($25,000), Honda Civic 2020 ($22,000), Mazda 3 2019 ($18,500)"
}
```
*Purpose*: Conversational AI for buyer guidance.

Pre-run listings/transactions for context. For stream demos, create multiple listings to see analytics update.

## Troubleshooting
- **Kafka not starting**: Run `docker logs kafka` for errors; ensure ports 2181/9092 free.
- **Ollama issues**: Restart `ollama serve`; re-pull model if needed.
- **Port conflicts**: Use `netstat -ano | findstr :8081` (Windows) to kill processes.
- **Maven errors**: Delete `.m2/repository` folder and retry `mvn clean install`.
- **AI endpoints fail**: Confirm Ollama at `http://localhost:11434` (curl test: `curl http://localhost:11434/api/tags`).

## References
University of Wollongong (2025) *CSCI318 Software Engineering Practices & Principles: Project Specification Spring 2025*. Wollongong: School of Computing and Information Technology.

Spring.io (2024) *Spring Boot Reference Documentation*. Available at: https://docs.spring.io/spring-boot/docs/current/reference/html/ (Accessed: 16 October 2025).

Apache Software Foundation (2024) *Apache Kafka Documentation*. Available at: https://kafka.apache.org/documentation/ (Accessed: 16 October 2025).

Ollama (2024) *Ollama Documentation*. Available at: https://ollama.com/docs (Accessed: 16 October 2025).

LangChain4j (2024) *LangChain4j Documentation*. Available at: https://docs.langchain4j.dev/ (Accessed: 13 October 2025).


