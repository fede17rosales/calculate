package com.example.rosales.challenge.service;

import com.example.rosales.challenge.entity.CallLog;
import com.example.rosales.challenge.repository.CallLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CallLogService {

    private final CallLogRepository repository;

    public CallLogService(CallLogRepository repository) {
        this.repository = repository;
    }

    @Async
    public void logAsync(String endpoint, String params, String response, String error) {
        try {
            CallLog log = new CallLog();
            log.setEndpoint(endpoint);
            log.setParams(params.replace("num1=", "").replace("num2=", ""));
            log.setResponse(response);
            log.setError(error);
            log.setTimestamp(Instant.now());
            repository.save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
