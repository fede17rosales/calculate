package com.example.rosales.challenge.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExternalPercentageClientTest {

    @Test
    @DisplayName("Retornamos porcentaje por default cuando no seteamos la variable de entorno")
    void fetchPercentage_should_return_default_when_env_not_set() {
        ExternalPercentageClient client = new ExternalPercentageClient("10.0");
        Optional<Double> result = client.fetchPercentage();

        assertTrue(result.isPresent());
        assertEquals(10.0, result.get());
    }

    @Test
    @DisplayName("Retornamos porcentaje Correcto")
    void fetch_percentage_should_return_value_from_env() {
        ExternalPercentageClient client = new ExternalPercentageClient("12.5");
        Optional<Double> result = client.fetchPercentage();

        assertTrue(result.isPresent());
        assertEquals(12.5, result.get());
    }

    @Test
    @DisplayName("Retornamos porcentaje por default cuando la variable es invalida")
    void fetch_percentage_should_return_default_when_env_invalid() {
        ExternalPercentageClient client = new ExternalPercentageClient("invalid");
        Optional<Double> result = client.fetchPercentage();

        assertTrue(result.isPresent());
        assertEquals(10.0, result.get());
    }
}