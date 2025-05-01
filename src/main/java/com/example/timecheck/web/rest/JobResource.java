package com.example.timecheck.web.rest;

import com.example.timecheck.entity.Job;
import com.example.timecheck.service.JobService;
import com.example.timecheck.service.dto.JobDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/job")
public class JobResource {
    private final JobService jobService;

    public JobResource(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/create")
    public ResponseEntity<JobDto> addJob(JobDto jobDto) throws URISyntaxException {
        JobDto result = jobService.create(jobDto);
        return ResponseEntity.created(new URI("/api/admin/create" + result.getId())).body(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobDto jobDto) throws URISyntaxException {
        if (!jobDto.getId().equals(id) && jobDto.getId() != 0) {
            return ResponseEntity.badRequest().body("invalid id");
        }
        JobDto result = jobService.update(jobDto);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllJobs() {
        List<JobDto> jobs = jobService.findAll();
        return ResponseEntity.ok().body(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJob(@PathVariable Long id) {
        JobDto job = jobService.findById(id);
        return ResponseEntity.ok().body(job);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        Job result = jobService.delete(id);
        return ResponseEntity.ok().body(result);
    }
}
