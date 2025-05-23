package com.example.timecheck.repository;

import com.example.timecheck.entity.Department;
import com.example.timecheck.entity.Job;
import com.example.timecheck.entity.enumirated.DepartmentStatus;
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

    //    @Query("select j from Job j where j.id NOT IN (SELECT u.job.id from User u where u.job is not null )")
    @Query("""
                SELECT j FROM Job j
                LEFT JOIN User u ON u.job = j
                WHERE u.id IS NULL and j.jobStatus =:jobStatus
            """)
    List<Job> findAllUnassignedJobs(JobStatus jobStatus);

}
