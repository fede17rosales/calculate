package com.example.rosales.challenge.repository;

import com.example.rosales.challenge.entity.CallLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallLogRepository extends JpaRepository<CallLog, Long> {
    Page<CallLog> findAll(Pageable pageable);
}
