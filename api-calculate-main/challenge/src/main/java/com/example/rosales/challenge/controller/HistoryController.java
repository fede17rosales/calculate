package com.example.rosales.challenge.controller;


import com.example.rosales.challenge.entity.CallLog;
import com.example.rosales.challenge.exception.dto.ErrorResponse;
import com.example.rosales.challenge.repository.CallLogRepository;
import com.example.rosales.challenge.service.dto.CalcResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "History", description = "Retorna el historial de cálculos realizados previamente")
public class HistoryController {
    private final CallLogRepository repository;

    public HistoryController(CallLogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/history")
    @Operation(summary = "Obtiene el historial", description = "Lista todos los cálculos previos")
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
    public ResponseEntity<Page<CallLog>> history(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<CallLog> logs = repository.findAll(PageRequest.of(page, size));
            if (logs.isEmpty()) {
                return ResponseEntity.notFound().build(); // 404
            }
            return ResponseEntity.ok(logs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
