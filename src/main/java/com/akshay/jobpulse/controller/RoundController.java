package com.akshay.jobpulse.controller;

import com.akshay.jobpulse.model.Round;
import com.akshay.jobpulse.model.RoundStatus;
import com.akshay.jobpulse.service.RoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs/{jobId}/rounds")
public class RoundController {

    @Autowired
    private RoundService roundService;

    @PostMapping
    public ResponseEntity<Round> addRound(@PathVariable Long jobId, @RequestBody Round round) {
        return new ResponseEntity<>(roundService.addRound(jobId, round), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Round>> getRounds(@PathVariable Long jobId) {
        return ResponseEntity.ok(roundService.getRoundsByJob(jobId));
    }

    @PatchMapping("/{roundId}/status")
    public ResponseEntity<Round> updateRoundStatus(
            @PathVariable Long jobId,
            @PathVariable Long roundId,
            @RequestBody Map<String, String> body) {
        RoundStatus status = RoundStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(roundService.updateRoundStatus(jobId, roundId, status));
    }
}