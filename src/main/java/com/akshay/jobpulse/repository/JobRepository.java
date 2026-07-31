package com.akshay.jobpulse.repository;

import com.akshay.jobpulse.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByIsDeletedFalse();
}