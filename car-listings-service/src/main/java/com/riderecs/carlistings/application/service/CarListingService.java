package com.riderecs.carlistings.application.service;

import com.riderecs.carlistings.domain.event.CarListingCreatedEvent;
import com.riderecs.carlistings.domain.model.CarListing;
import com.riderecs.carlistings.infrastructure.messaging.KafkaEventPublisher;
import com.riderecs.carlistings.infrastructure.repository.CarListingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarListingService {

    private final CarListingRepository carListingRepository;
    private final KafkaEventPublisher kafkaEventPublisher;

    public CarListingService(CarListingRepository carListingRepository, KafkaEventPublisher kafkaEventPublisher) {
        this.carListingRepository = carListingRepository;
        this.kafkaEventPublisher = kafkaEventPublisher;
    }

    @Transactional
    public CarListing createCarListing(CarListing carListing) {
        CarListing savedListing = carListingRepository.save(carListing);

        CarListingCreatedEvent event = new CarListingCreatedEvent(
                savedListing.getId(),
                savedListing.getMake(),
                savedListing.getModel(),
                savedListing.getYear(),
                savedListing.getPrice(),
                savedListing.getMileage(),
                savedListing.getCondition(),
                savedListing.getSellerId(),
                savedListing.getCreatedAt()
        );

        kafkaEventPublisher.publishCarListingCreated(event);

        return savedListing;
    }

    public List<CarListing> getAllCarListings() {
        return carListingRepository.findAll();
    }

    public Optional<CarListing> getCarListingById(Long id) {
        return carListingRepository.findById(id);
    }

    public List<CarListing> getAvailableCarListings() {
        return carListingRepository.findByStatus("AVAILABLE");
    }

    public List<CarListing> searchByMakeAndModel(String make, String model) {
        return carListingRepository.findByMakeAndModel(make, model);
    }

    public List<CarListing> searchByPriceRange(Double minPrice, Double maxPrice) {
        return carListingRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Transactional
    public CarListing updateCarListing(Long id, CarListing updatedListing) {
        Optional<CarListing> existingListing = carListingRepository.findById(id);
        if (existingListing.isPresent()) {
            CarListing listing = existingListing.get();
            listing.setMake(updatedListing.getMake());
            listing.setModel(updatedListing.getModel());
            listing.setYear(updatedListing.getYear());
            listing.setPrice(updatedListing.getPrice());
            listing.setMileage(updatedListing.getMileage());
            listing.setCondition(updatedListing.getCondition());
            listing.setDescription(updatedListing.getDescription());
            listing.setStatus(updatedListing.getStatus());
            return carListingRepository.save(listing);
        }
        return null;
    }

    @Transactional
    public void deleteCarListing(Long id) {
        carListingRepository.deleteById(id);
    }
}
