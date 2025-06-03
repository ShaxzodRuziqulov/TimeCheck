package com.example.timecheck.repository;

import com.example.timecheck.entity.Job;
import com.example.timecheck.entity.enumirated.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("SELECT j FROM Job j WHERE j.jobStatus = :jobStatus")
    List<Job> findByStatus(@Param("jobStatus") JobStatus jobStatus);

    @Query("select count(j) from Job j where j.jobStatus =:jobStatus")
    long countByJobStatus(@Param("jobStatus") JobStatus jobStatus);

    @Query("""
                SELECT j FROM Job j
                LEFT JOIN User u ON u.job = j
                WHERE u.id IS NULL and j.jobStatus =:jobStatus
            """)
    List<Job> findAllUnassignedJobs(JobStatus jobStatus);

    @Query(""" 
            SELECT j FROM Job j
            WHERE (
            j.id NOT IN (
                SELECT u.job.id FROM User u WHERE u.job IS NOT NULL AND u.id <> :userId
            )
            OR j.id IN (
                SELECT u.job.id FROM User u WHERE u.id = :userId))
                AND j.jobStatus = :jobStatus""")
    List<Job> findFreeOrAssignedToUser(@Param("userId") Long userId, @Param("jobStatus") JobStatus jobStatus);


}
