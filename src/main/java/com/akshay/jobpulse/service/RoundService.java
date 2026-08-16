package com.akshay.jobpulse.service;

import com.akshay.jobpulse.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import com.akshay.jobpulse.model.Round;

import com.akshay.jobpulse.model.RoundStatus;
import com.akshay.jobpulse.model.JobApplication;
import com.akshay.jobpulse.model.JobStatus;
import com.akshay.jobpulse.repository.RoundRepository;
import com.akshay.jobpulse.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoundService {

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private JobRepository jobRepository;

    public Round addRound(Long jobId, Round round) {
    	JobApplication job = jobRepository.findById(jobId)
    	        .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        round.setJobApplication(job);
        return roundRepository.save(round);
    }

    public List<Round> getRoundsByJob(Long jobId) {
        return roundRepository.findByJobApplicationId(jobId);
    }

    @Transactional
    public Round updateRoundStatus(Long jobId, Long roundId, RoundStatus newStatus) {
    	Round round = roundRepository.findById(roundId)
    	        .orElseThrow(() -> new ResourceNotFoundException("Round not found with id: " + roundId));

        if (!round.getJobApplication().getId().equals(jobId)) {
            throw new RuntimeException("Round does not belong to this job");
        }

        round.setStatus(newStatus);
        Round savedRound = roundRepository.save(round);

        // Auto-update job status
        JobApplication job = round.getJobApplication();
        List<Round> allRounds = roundRepository.findByJobApplicationId(jobId);

        boolean anyFailed = allRounds.stream()
                .anyMatch(r -> r.getStatus() == RoundStatus.FAILED);
        boolean allCleared = allRounds.stream()
                .allMatch(r -> r.getStatus() == RoundStatus.CLEARED);

        if (anyFailed) {
            job.setStatus(JobStatus.REJECTED);
        } else if (allCleared) {
            job.setStatus(JobStatus.OFFER);
        } else {
            job.setStatus(JobStatus.ACTIVE);
        }

        jobRepository.save(job);
        return savedRound;
    }

    @Transactional
    public void deleteRound(Long jobId, Long roundId) {
    	JobApplication job = jobRepository.findById(jobId)
    	        .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

    	Round round = roundRepository.findById(roundId)
    	        .orElseThrow(() -> new ResourceNotFoundException("Round not found with id: " + roundId));

        if (!round.getJobApplication().getId().equals(jobId)) {
            throw new RuntimeException("Round does not belong to this job");
        }

        job.getRounds().remove(round);
        roundRepository.delete(round);

        List<Round> remainingRounds = job.getRounds();
        boolean anyFailed = remainingRounds.stream()
                .anyMatch(r -> r.getStatus() == RoundStatus.FAILED);
        boolean allCleared = !remainingRounds.isEmpty() && remainingRounds.stream()
                .allMatch(r -> r.getStatus() == RoundStatus.CLEARED);

        if (anyFailed) {
            job.setStatus(JobStatus.REJECTED);
        } else if (allCleared) {
            job.setStatus(JobStatus.OFFER);
        } else {
            job.setStatus(JobStatus.ACTIVE);
        }

        jobRepository.save(job);
    }
}