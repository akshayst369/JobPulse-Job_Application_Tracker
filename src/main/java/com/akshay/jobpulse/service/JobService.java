package com.akshay.jobpulse.service;

import com.akshay.jobpulse.dto.PaginatedResponse;
import com.akshay.jobpulse.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import com.akshay.jobpulse.model.JobApplication;
import com.akshay.jobpulse.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
    }

    public JobApplication updateJob(Long id, JobApplication updatedJob) {
        JobApplication existing = getJobById(id);
        existing.setCompanyName(updatedJob.getCompanyName());
        existing.setRole(updatedJob.getRole());
        existing.setLocation(updatedJob.getLocation());
        existing.setJobLink(updatedJob.getJobLink());
        existing.setPriority(updatedJob.getPriority());
        existing.setAppliedDate(updatedJob.getAppliedDate());
        existing.setNotes(updatedJob.getNotes());
        return jobRepository.save(existing);
    }

    @Transactional
    public void deleteJob(Long id) {
        JobApplication job = getJobById(id);
        job.setDeleted(true);
        jobRepository.save(job);
    }

    public PaginatedResponse<JobApplication> getJobsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<JobApplication> pageResult = jobRepository.findByIsDeletedFalse(pageable);
        return new PaginatedResponse<>(pageResult);
    }
}