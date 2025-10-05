package com.example.rosales.challenge.service;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
import java.util.Optional;

@Component
public class ExternalPercentageClient {

    private final Double percentage;

    public ExternalPercentageClient(@Value("${external.percentage}") String percentageEnv) {
        Double temp;
        try {
            temp = Double.parseDouble(percentageEnv);
        } catch (Exception e) {
            temp = 10.0;
        }
        this.percentage = temp;
    }

    public Optional<Double> fetchPercentage() {
        return Optional.of(percentage);
    }
}
