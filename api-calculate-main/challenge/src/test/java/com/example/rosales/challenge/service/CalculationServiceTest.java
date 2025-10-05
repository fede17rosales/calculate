package com.example.rosales.challenge.service;

import com.example.rosales.challenge.service.dto.CalcResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CalculationServiceTest {

    @Test
    @DisplayName("Calcula el porcentaje correctamente")
    public void calculate_applies_percentage_correctly() {
        ExternalPercentageClient client = Mockito.mock(ExternalPercentageClient.class);
        Mockito.when(client.fetchPercentage()).thenReturn(java.util.Optional.of(20.0));
        PercentageService percentageService = new PercentageService(client);
        CalculationService calc = new CalculationService(percentageService);

        CalcResponse res = calc.calculate(10.0, 5.0);
        assertEquals(15.0, res.sum());
        assertEquals(20.0, res.percentage());
        assertEquals(18.0, res.finalValue(), 1e-6);
    }

}