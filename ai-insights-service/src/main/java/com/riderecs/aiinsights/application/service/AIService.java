package com.riderecs.aiinsights.application.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatLanguageModel chatLanguageModel;

    public AIService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    public String generatePriceRecommendation(String make, String model, int year, int mileage, String condition, double currentPrice) {
        String prompt = String.format(
                "You are a car pricing expert. Based on the following car details, provide a fair market price recommendation in Australian dollars (AUD):\n\n" +
                        "Make: %s\n" +
                        "Model: %s\n" +
                        "Year: %d\n" +
                        "Mileage: %d km\n" +
                        "Condition: %s\n" +
                        "Current Listed Price: $%.2f AUD\n\n" +
                        "Provide a recommended price range and brief justification (2-3 sentences). Format your response as: 'Recommended Price: $X - $Y. Justification: ...'",
                make, model, year, mileage, condition, currentPrice
        );

        return chatLanguageModel.generate(prompt);
    }

    public String answerBuyerQuery(String query, String carListingsContext) {
        String prompt = String.format(
                "You are a helpful car sales assistant for RideRecs, an online car marketplace similar to Carsales.com.au. " +
                        "A potential buyer has the following question: '%s'\n\n" +
                        "Available car listings context:\n%s\n\n" +
                        "Provide a helpful, friendly response to assist the buyer. Be concise and informative.",
                query, carListingsContext
        );

        return chatLanguageModel.generate(prompt);
    }
}
