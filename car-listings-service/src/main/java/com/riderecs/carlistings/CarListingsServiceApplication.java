package com.riderecs.carlistings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarListingsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarListingsServiceApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("Car Listings Service is running!");
        System.out.println("Access at: http://localhost:8081");
        System.out.println("H2 Console: http://localhost:8081/h2-console");
        System.out.println("========================================\n");
    }

}
