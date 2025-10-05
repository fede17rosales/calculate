CREATE TABLE IF NOT EXISTS call_log (
    id SERIAL PRIMARY KEY,
    endpoint VARCHAR(255),
    error TEXT,
    params TEXT,
    response TEXT,
    timestamp TIMESTAMP
);
