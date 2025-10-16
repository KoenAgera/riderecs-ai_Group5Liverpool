package com.riderecs.aiinsights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiInsightsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiInsightsServiceApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("AI Insights Service is running!");
        System.out.println("Access at: http://localhost:8084");
        System.out.println("Make sure Ollama is running!");
        System.out.println("========================================\n");
    }

}
