package com.example.rosales.challenge.controller;

import com.example.rosales.challenge.entity.CallLog;
import com.example.rosales.challenge.repository.CallLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoryControllerTest {

    private CallLogRepository repository;
    private HistoryController controller;

    @BeforeEach
    void setUp() {
        repository = mock(CallLogRepository.class);
        controller = new HistoryController(repository);
    }

    @Test
    @DisplayName("Retorno el historial desde el repositorio correctamente")
    void history_should_return_page_from_repository() {
        int page = 1;
        int size = 2;
        CallLog log1 = new CallLog();
        log1.setId(1L);
        log1.setEndpoint("/calculate");
        log1.setParams("req1");
        log1.setResponse("res1");
        log1.setError(null);

        CallLog log2 = new CallLog();
        log2.setId(2L);
        log2.setEndpoint("/calculate");
        log2.setParams("req2");
        log2.setResponse("res2");
        log2.setError(null);

        List<CallLog> logs = Arrays.asList(log1, log2);

        Page<CallLog> mockPage = new PageImpl<>(logs);
        when(repository.findAll(PageRequest.of(page, size))).thenReturn(mockPage);

        Page<CallLog> result = controller.history(page, size).getBody();

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(logs, result.getContent());

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        verify(repository, times(1)).findAll(pageRequestCaptor.capture());
        assertEquals(page, pageRequestCaptor.getValue().getPageNumber());
        assertEquals(size, pageRequestCaptor.getValue().getPageSize());
    }

    @Test
    @DisplayName("Debe retornar 404 cuando no hay registros")
    void history_should_return_404_when_no_logs() {
        when(repository.findAll(PageRequest.of(0, 10))).thenReturn(Page.empty());

        ResponseEntity<Page<CallLog>> response = controller.history(0, 10);

        assertEquals(404, response.getStatusCodeValue());
        verify(repository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("Debe retornar 400 cuando los parámetros son inválidos")
    void history_should_return_400_for_invalid_params() {
        ResponseEntity<Page<CallLog>> response = controller.history(-1, 10);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Debe retornar 500 en caso de error inesperado")
    void history_should_return_500_for_unexpected_error() {
        when(repository.findAll(PageRequest.of(0, 10)))
                .thenThrow(new RuntimeException("Database down"));

        ResponseEntity<Page<CallLog>> response = controller.history(0, 10);

        assertEquals(500, response.getStatusCodeValue());
        verify(repository, times(1)).findAll(PageRequest.of(0, 10));
    }

}