package com.example.rosales.challenge.controller;

import com.example.rosales.challenge.service.CalculationService;
import com.example.rosales.challenge.service.CallLogService;
import com.example.rosales.challenge.service.dto.CalcResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculatorControllerTest {
    private CalculationService calculationService;
    private CallLogService callLogService;
    private CalculatorController controller;

    @BeforeEach
    void setUp() {
        calculationService = mock(CalculationService.class);
        callLogService = mock(CallLogService.class);
        controller = new CalculatorController(calculationService, callLogService);
    }

    @Test
    @DisplayName("Retorno el calculo correctamente")
    void calculate_should_return_calc_response_and_logcall() {
        Double num1 = 5.0;
        Double num2 = 3.0;

        CalcResponse mockResponse = new CalcResponse(8.0,0,0);
        when(calculationService.calculate(num1, num2)).thenReturn(mockResponse);

        ResponseEntity<CalcResponse> response = controller.calculate(num1, num2);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());

        ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> requestCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> responseCaptor = ArgumentCaptor.forClass(String.class);

        verify(callLogService, times(1))
                .logAsync(pathCaptor.capture(), requestCaptor.capture(), responseCaptor.capture(), isNull());

        assertEquals("/calculate", pathCaptor.getValue());
        assertTrue(requestCaptor.getValue().contains("5.0"));
        assertTrue(requestCaptor.getValue().contains("3.0"));
        assertTrue(responseCaptor.getValue().contains("8"));
    }

    @Test
    @DisplayName("Lanza IllegalArgumentException cuando el servicio arroja error de validación")
    void calculate_should_throw_illegal_argument_exception() {
        Double num1 = 10.0;
        Double num2 = 0.0;

        when(calculationService.calculate(num1, num2))
                .thenThrow(new IllegalArgumentException("No se puede dividir por cero"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.calculate(num1, num2)
        );

        assertEquals("No se puede dividir por cero", ex.getMessage());
    }

    @Test
    @DisplayName("Lanza RuntimeException cuando ocurre un error inesperado")
    void calculate_should_throw_runtime_exception() {
        Double num1 = 2.0;
        Double num2 = 3.0;

        when(calculationService.calculate(num1, num2))
                .thenThrow(new RuntimeException("Error inesperado"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> controller.calculate(num1, num2)
        );

        assertEquals("Error inesperado", ex.getMessage());
    }

}