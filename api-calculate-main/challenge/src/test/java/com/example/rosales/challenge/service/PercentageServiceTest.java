package com.example.rosales.challenge.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PercentageServiceTest {

    private ExternalPercentageClient client;
    private PercentageService service;

    @BeforeEach
    void setUp() {
        client = mock(ExternalPercentageClient.class);
        service = new PercentageService(client);
    }

    @Test
    @DisplayName("Retorna el valor de la Cache cuando falla")
    public void returns_cached_value_when_external_fails() {
        ExternalPercentageClient client = mock(ExternalPercentageClient.class);
        when(client.fetchPercentage()).thenReturn(java.util.Optional.of(7.5));
        PercentageService svc = new PercentageService(client);

        double first = svc.getPercentage();
        assertEquals(7.5, first);

        when(client.fetchPercentage()).thenReturn(java.util.Optional.empty());
        double second = svc.getPercentage();
        assertEquals(7.5, second);
    }

    @Test
    @DisplayName("Retorna una excepcion cuando no hay cache y el servicio externo falla")
    public void throws_when_no_cache_and_external_fails() {
        ExternalPercentageClient client = mock(ExternalPercentageClient.class);
        when(client.fetchPercentage()).thenReturn(java.util.Optional.empty());
        PercentageService svc = new PercentageService(client);
        try { java.lang.reflect.Field f = PercentageService.class.getDeclaredField("cachedPercentage"); f.setAccessible(true); f.set(svc, null); } catch(Exception ignored){}

        assertThrows(IllegalStateException.class, svc::getPercentage);
    }

    @Test
    @DisplayName("Se actualiza la cache cuando el cliente retorna un valor")
    void scheduled_refresh_should_update_cache_when_client_returns_value() {
        when(client.fetchPercentage()).thenReturn(Optional.of(15.5));

        service.scheduledRefresh();

        assertEquals(15.5, service.getPercentage());
        verify(client, atLeastOnce()).fetchPercentage();
    }

    @Test
    @DisplayName("Se cae el cliente externo y retornamos exception")
    void scheduled_refresh_should_not_throw_when_client_fails() {
        when(client.fetchPercentage()).thenThrow(new RuntimeException("External service down"));

        assertDoesNotThrow(() -> service.scheduledRefresh());

        assertThrows(IllegalStateException.class, () -> service.getPercentage());
    }
}