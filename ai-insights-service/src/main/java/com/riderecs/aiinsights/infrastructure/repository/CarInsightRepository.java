package com.riderecs.aiinsights.infrastructure.repository;

import com.riderecs.aiinsights.domain.model.CarInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarInsightRepository extends JpaRepository<CarInsight, Long> {
    Optional<CarInsight> findByCarId(Long carId);
}
