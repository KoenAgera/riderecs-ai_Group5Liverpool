package com.riderecs.carlistings.presentation.controller;

import com.riderecs.carlistings.application.service.CarListingService;
import com.riderecs.carlistings.domain.model.CarListing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/car-listings")
public class CarListingController {

    private final CarListingService carListingService;

    public CarListingController(CarListingService carListingService) {
        this.carListingService = carListingService;
    }

    @PostMapping
    public ResponseEntity<CarListing> createCarListing(@RequestBody CarListing carListing) {
        CarListing createdListing = carListingService.createCarListing(carListing);
        return new ResponseEntity<>(createdListing, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CarListing>> getAllCarListings() {
        List<CarListing> listings = carListingService.getAllCarListings();
        return ResponseEntity.ok(listings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarListing> getCarListingById(@PathVariable Long id) {
        Optional<CarListing> listing = carListingService.getCarListingById(id);
        return listing.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public ResponseEntity<List<CarListing>> getAvailableCarListings() {
        List<CarListing> listings = carListingService.getAvailableCarListings();
        return ResponseEntity.ok(listings);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CarListing>> searchByMakeAndModel(
            @RequestParam String make,
            @RequestParam String model) {
        List<CarListing> listings = carListingService.searchByMakeAndModel(make, model);
        return ResponseEntity.ok(listings);
    }

    @GetMapping("/search/price")
    public ResponseEntity<List<CarListing>> searchByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<CarListing> listings = carListingService.searchByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(listings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarListing> updateCarListing(
            @PathVariable Long id,
            @RequestBody CarListing carListing) {
        CarListing updatedListing = carListingService.updateCarListing(id, carListing);
        if (updatedListing != null) {
            return ResponseEntity.ok(updatedListing);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarListing(@PathVariable Long id) {
        carListingService.deleteCarListing(id);
        return ResponseEntity.noContent().build();
    }
}
