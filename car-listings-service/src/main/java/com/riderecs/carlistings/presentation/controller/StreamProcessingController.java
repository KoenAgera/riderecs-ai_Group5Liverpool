package com.riderecs.carlistings.presentation.controller;

import com.riderecs.carlistings.application.service.StreamProcessingService;
import com.riderecs.carlistings.domain.model.CarListing;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class StreamProcessingController {

    private final StreamProcessingService streamProcessingService;

    public StreamProcessingController(StreamProcessingService streamProcessingService) {
        this.streamProcessingService = streamProcessingService;
    }

    @GetMapping("/popular-makes")
    public ResponseEntity<Map<String, Long>> getMostPopularMakes() {
        Map<String, Long> popularMakes = streamProcessingService.getMostPopularMakes();
        return ResponseEntity.ok(popularMakes);
    }

    @GetMapping("/average-price-by-make")
    public ResponseEntity<Map<String, Double>> getAveragePriceByMake() {
        Map<String, Double> avgPrices = streamProcessingService.getAveragePriceByMake();
        return ResponseEntity.ok(avgPrices);
    }

    @GetMapping("/average-price-by-year")
    public ResponseEntity<Map<Integer, Double>> getAveragePriceByYear() {
        Map<Integer, Double> avgPrices = streamProcessingService.getAveragePriceByYear();
        return ResponseEntity.ok(avgPrices);
    }

    @GetMapping("/top-expensive")
    public ResponseEntity<List<CarListing>> getTopExpensiveCars(@RequestParam(defaultValue = "5") int limit) {
        List<CarListing> expensiveCars = streamProcessingService.getTopExpensiveCars(limit);
        return ResponseEntity.ok(expensiveCars);
    }
}
