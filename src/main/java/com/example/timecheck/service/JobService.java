package com.example.timecheck.service;

import com.example.timecheck.entity.Department;
import com.example.timecheck.entity.Job;
import com.example.timecheck.entity.Position;
import com.example.timecheck.entity.enumirated.JobStatus;
import com.example.timecheck.repository.DepartmentRepository;
import com.example.timecheck.repository.JobRepository;
import com.example.timecheck.repository.PositionRepository;
import com.example.timecheck.repository.UserRepository;
import com.example.timecheck.service.dto.JobDto;
import com.example.timecheck.service.mapper.JobMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final DepartmentRepository departmentRepository;
    private final JobMapper jobMapper;
    private final UserRepository userRepository;
    private final PositionRepository positionRepository;

    public JobService(JobRepository jobRepository, DepartmentRepository departmentRepository, JobMapper jobMapper, UserRepository userRepository, PositionRepository positionRepository) {
        this.jobRepository = jobRepository;
        this.departmentRepository = departmentRepository;
        this.jobMapper = jobMapper;
        this.userRepository = userRepository;
        this.positionRepository = positionRepository;
    }

    public JobDto create(JobDto jobDto) {
        Job job = jobMapper.toEntity(jobDto);

        job.setJobStatus(JobStatus.ACTIVE);
        Position position = positionRepository.findById(jobDto.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position not found"));

        job.setPosition(position);
        if (jobDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(jobDto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            job.setDepartment(department);
        }

        job = jobRepository.save(job);
        return jobMapper.toDto(job);
    }


    public JobDto update(JobDto jobDto) {
        Job job = jobRepository.findById(jobDto.getId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setJobStatus(jobDto.getJobStatus());

        if (jobDto.getDepartmentId() != null) {
            Department department = new Department();
            department.setId(jobDto.getDepartmentId());
            job.setDepartment(department);
        }
        if (jobDto.getPositionId() != null) {
            Position position = new Position();
            position.setId(jobDto.getPositionId());
            job.setPosition(position);
        }

        job = jobRepository.save(job);
        return jobMapper.toDto(job);
    }

    public List<JobDto> findAll() {
        return jobRepository.findAll()
                .stream()
                .map(jobMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<JobDto> findAllActive() {
        return jobRepository.findByStatus(JobStatus.ACTIVE)
                .stream()
                .map(jobMapper::toDto)
                .collect(Collectors.toList());
    }

    public JobDto findById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return jobMapper.toDto(job);
    }

    public Job delete(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setJobStatus(JobStatus.DELETED);
        return jobRepository.save(job);
    }

    public void deleted(Long id) {
        Job job = jobRepository.getReferenceById(id);
        jobRepository.delete(job);
    }

    public long countByActiveJob() {
        return jobRepository.countByJobStatus(JobStatus.ACTIVE);
    }

    public List<Job> getFreeJobs() {
        return jobRepository.findAllUnassignedJobs(JobStatus.ACTIVE);
    }

    public List<Job> getFreeOrAssignedToUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        return jobRepository.findFreeOrAssignedToUser(userId, JobStatus.ACTIVE);
    }

}
