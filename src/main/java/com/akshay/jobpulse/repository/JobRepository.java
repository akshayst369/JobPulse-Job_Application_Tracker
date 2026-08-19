package com.akshay.jobpulse.repository;

import com.akshay.jobpulse.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByIsDeletedFalse();
    Page<JobApplication> findByIsDeletedFalse(Pageable pageable);
}