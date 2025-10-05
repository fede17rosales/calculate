package com.example.rosales.challenge.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "call_log")
public class CallLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant timestamp;
    private String endpoint;

    @Column(columnDefinition = "text")
    private String params;
    @Column(columnDefinition = "text")
    private String response;
    @Column(columnDefinition = "text")
    private String error;

    public void setId(Long id) { this.id = id; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getParams() { return params; }
    public void setParams(String params) { this.params = params; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
