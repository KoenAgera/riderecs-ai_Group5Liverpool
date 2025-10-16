package com.riderecs.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementServiceApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("User Management Service is running!");
        System.out.println("Access at: http://localhost:8082");
        System.out.println("H2 Console: http://localhost:8082/h2-console");
        System.out.println("========================================\n");
    }

}
