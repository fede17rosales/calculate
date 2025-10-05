package com.example.rosales.challenge.service;

import com.example.rosales.challenge.service.dto.CalcResponse;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    private final PercentageService percentageService;

    public CalculationService(PercentageService percentageService) {
        this.percentageService = percentageService;
    }

    public CalcResponse calculate(double num1, double num2) {
        double sum = num1 + num2;
        double pct = percentageService.getPercentage();
        double finalValue = sum + (sum * pct / 100.0);
        return new CalcResponse(sum, pct, finalValue);
    }
}
