package com.akshay.jobpulse.service;

import com.akshay.jobpulse.model.JobApplication;
import com.akshay.jobpulse.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public JobApplication createJob(JobApplication job) {
        return jobRepository.save(job);
    }

    public List<JobApplication> getAllJobs() {
        return jobRepository.findByIsDeletedFalse();
    }

    public JobApplication getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }
}