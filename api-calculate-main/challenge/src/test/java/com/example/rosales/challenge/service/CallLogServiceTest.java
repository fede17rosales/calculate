package com.example.rosales.challenge.service;

import com.example.rosales.challenge.entity.CallLog;
import com.example.rosales.challenge.repository.CallLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallLogServiceTest {

    @Mock
    private CallLogRepository repository;

    @InjectMocks
    private CallLogService service;

    @BeforeEach
    void setUp() {
        repository = mock(CallLogRepository.class);
        service = new CallLogService(repository);
    }

    @Test
    @DisplayName("Servicio funciona correctamente")
    void logasync_should_save_callLog() {
        String endpoint = "/calculate";
        String params = "5,3";
        String response = "8.0";
        String error = null;

        service.logAsync(endpoint, params, response, error);

        ArgumentCaptor<CallLog> captor = ArgumentCaptor.forClass(CallLog.class);
        verify(repository, times(1)).save(captor.capture());

        CallLog savedLog = captor.getValue();
        assertNotNull(savedLog);
        assertEquals(endpoint, savedLog.getEndpoint());
        assertEquals(params, savedLog.getParams());
        assertEquals(response, savedLog.getResponse());
        assertEquals(error, savedLog.getError());
        assertNotNull(savedLog.getTimestamp());
        assertTrue(savedLog.getTimestamp().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Servicio no funciona correctamente cuando enviamos parametros en null")
    void logasync_should_not_save_when_params_is_null() {
        String endpoint = "/calculate";
        String params = null;
        String response = "8.0";
        String error = null;

        service.logAsync(endpoint, params, response, error);

        verify(repository, never()).save(any(CallLog.class));
    }

}