package com.riderecs.aiinsights.presentation.controller;

import com.riderecs.aiinsights.application.service.AIService;
import com.riderecs.aiinsights.domain.model.CarInsight;
import com.riderecs.aiinsights.infrastructure.repository.CarInsightRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ai-insights")
public class AIInsightsController {

    private final AIService aiService;
    private final CarInsightRepository carInsightRepository;

    public AIInsightsController(AIService aiService, CarInsightRepository carInsightRepository) {
        this.aiService = aiService;
        this.carInsightRepository = carInsightRepository;
    }

    @GetMapping("/insights/{carId}")
    public ResponseEntity<CarInsight> getInsightForCar(@PathVariable Long carId) {
        Optional<CarInsight> insight = carInsightRepository.findByCarId(carId);
        return insight.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/insights")
    public ResponseEntity<List<CarInsight>> getAllInsights() {
        List<CarInsight> insights = carInsightRepository.findAll();
        return ResponseEntity.ok(insights);
    }

    @PostMapping("/price-recommendation")
    public ResponseEntity<PriceRecommendationResponse> getPriceRecommendation(@RequestBody PriceRecommendationRequest request) {
        String recommendation = aiService.generatePriceRecommendation(
                request.getMake(),
                request.getModel(),
                request.getYear(),
                request.getMileage(),
                request.getCondition(),
                request.getCurrentPrice()
        );

        PriceRecommendationResponse response = new PriceRecommendationResponse();
        response.setRecommendation(recommendation);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/buyer-assistant")
    public ResponseEntity<BuyerAssistantResponse> askBuyerAssistant(@RequestBody BuyerAssistantRequest request) {
        String answer = aiService.answerBuyerQuery(request.getQuery(), request.getContext());

        BuyerAssistantResponse response = new BuyerAssistantResponse();
        response.setAnswer(answer);
        return ResponseEntity.ok(response);
    }

    public static class PriceRecommendationRequest {
        private String make;
        private String model;
        private int year;
        private int mileage;
        private String condition;
        private double currentPrice;

        public String getMake() { return make; }
        public void setMake(String make) { this.make = make; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public int getYear() { return year; }
        public void setYear(int year) { this.year = year; }
        public int getMileage() { return mileage; }
        public void setMileage(int mileage) { this.mileage = mileage; }
        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }
        public double getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
    }

    public static class PriceRecommendationResponse {
        private String recommendation;

        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    }

    public static class BuyerAssistantRequest {
        private String query;
        private String context;

        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
        public String getContext() { return context; }
        public void setContext(String context) { this.context = context; }
    }

    public static class BuyerAssistantResponse {
        private String answer;

        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
    }
}
