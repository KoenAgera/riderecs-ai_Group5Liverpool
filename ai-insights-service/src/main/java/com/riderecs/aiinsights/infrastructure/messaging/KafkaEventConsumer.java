package com.riderecs.aiinsights.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riderecs.aiinsights.application.service.AIService;
import com.riderecs.aiinsights.domain.model.CarInsight;
import com.riderecs.aiinsights.infrastructure.repository.CarInsightRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class KafkaEventConsumer {

    private final ObjectMapper objectMapper;
    private final AIService aiService;
    private final CarInsightRepository carInsightRepository;

    public KafkaEventConsumer(ObjectMapper objectMapper, AIService aiService, CarInsightRepository carInsightRepository) {
        this.objectMapper = objectMapper;
        this.aiService = aiService;
        this.carInsightRepository = carInsightRepository;
    }

    @Bean
    public Consumer<String> processCarListingCreatedForAI() {
        return message -> {
            try {
                System.out.println("AI Service received event: " + message);
                JsonNode event = objectMapper.readTree(message);

                Long carId = event.get("carId").asLong();
                String make = event.get("make").asText();
                String model = event.get("model").asText();
                int year = event.get("year").asInt();
                int mileage = event.get("mileage").asInt();
                String condition = event.get("condition").asText();
                double price = event.get("price").asDouble();

                String aiResponse = aiService.generatePriceRecommendation(make, model, year, mileage, condition, price);
                System.out.println("AI Price Recommendation: " + aiResponse);

                Double recommendedPrice = extractRecommendedPrice(aiResponse, price);

                CarInsight insight = new CarInsight();
                insight.setCarId(carId);
                insight.setMake(make);
                insight.setModel(model);
                insight.setYear(year);
                insight.setCurrentPrice(price);
                insight.setRecommendedPrice(recommendedPrice);
                insight.setPriceRationale(aiResponse);

                carInsightRepository.save(insight);

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private Double extractRecommendedPrice(String aiResponse, double fallbackPrice) {
        try {
            String[] parts = aiResponse.split("\\$");
            if (parts.length > 1) {
                String priceStr = parts[1].split("[-\\s]")[0].replaceAll("[^0-9.]", "");
                return Double.parseDouble(priceStr);
            }
        } catch (Exception e) {
            System.out.println("Could not extract price, using fallback");
        }
        return fallbackPrice;
    }
}
