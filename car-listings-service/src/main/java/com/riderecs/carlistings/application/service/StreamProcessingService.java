package com.riderecs.carlistings.application.service;

import com.riderecs.carlistings.domain.model.CarListing;
import com.riderecs.carlistings.infrastructure.repository.CarListingRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StreamProcessingService {

    private final CarListingRepository carListingRepository;

    public StreamProcessingService(CarListingRepository carListingRepository) {
        this.carListingRepository = carListingRepository;
    }

    public Map<String, Long> getMostPopularMakes() {
        List<CarListing> allListings = carListingRepository.findAll();

        return allListings.stream()
                .collect(Collectors.groupingBy(CarListing::getMake, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Double> getAveragePriceByMake() {
        List<CarListing> allListings = carListingRepository.findAll();

        return allListings.stream()
                .collect(Collectors.groupingBy(
                        CarListing::getMake,
                        Collectors.averagingDouble(CarListing::getPrice)
                ));
    }

    public Map<Integer, Double> getAveragePriceByYear() {
        List<CarListing> allListings = carListingRepository.findAll();

        return allListings.stream()
                .collect(Collectors.groupingBy(
                        CarListing::getYear,
                        Collectors.averagingDouble(CarListing::getPrice)
                ));
    }

    public List<CarListing> getTopExpensiveCars(int limit) {
        List<CarListing> allListings = carListingRepository.findAll();

        return allListings.stream()
                .sorted(Comparator.comparing(CarListing::getPrice).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
