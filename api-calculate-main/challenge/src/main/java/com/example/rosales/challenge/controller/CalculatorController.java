package com.example.rosales.challenge.controller;


import com.example.rosales.challenge.exception.dto.ErrorResponse;
import com.example.rosales.challenge.service.CalculationService;
import com.example.rosales.challenge.service.CallLogService;
import com.example.rosales.challenge.service.dto.CalcResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "Calculation", description = "Operaciones de cálculo")
public class CalculatorController {
    private final CalculationService calculationService;
    private final CallLogService callLogService;

    public CalculatorController(CalculationService calculationService, CallLogService callLogService) {
        this.calculationService = calculationService;
        this.callLogService = callLogService;
    }

    @GetMapping("/calculate")
    @Operation(summary = "Suma dos números", description = "Devuelve el resultado de dos numeros con porcentaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CalcResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CalcResponse> calculate(
            @RequestParam @NotNull Double num1,
            @RequestParam @NotNull Double num2) {
        CalcResponse result = calculationService.calculate(num1, num2);
        callLogService.logAsync("/calculate", String.format("%s,%s", num1, num2), result.toString(), null);
        return ResponseEntity.ok(result);
    }
}
