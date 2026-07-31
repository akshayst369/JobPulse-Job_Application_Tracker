package com.akshay.jobpulse.repository;

import com.akshay.jobpulse.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
    List<Round> findByJobApplicationId(Long jobId);
}