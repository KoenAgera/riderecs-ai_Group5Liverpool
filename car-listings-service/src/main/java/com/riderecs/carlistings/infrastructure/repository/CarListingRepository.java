package com.riderecs.carlistings.infrastructure.repository;

import com.riderecs.carlistings.domain.model.CarListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarListingRepository extends JpaRepository<CarListing, Long> {
    List<CarListing> findByStatus(String status);
    List<CarListing> findBySellerId(Long sellerId);
    List<CarListing> findByMakeAndModel(String make, String model);
    List<CarListing> findByPriceBetween(Double minPrice, Double maxPrice);
}
