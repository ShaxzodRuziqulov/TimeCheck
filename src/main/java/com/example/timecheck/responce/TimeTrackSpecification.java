package com.example.timecheck.responce;

import com.example.timecheck.entity.Department;
import com.example.timecheck.entity.Job;
import com.example.timecheck.entity.TimeTrack;
import com.example.timecheck.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TimeTrackSpecification {

    public static Specification<TimeTrack> hasDepartmentId(Long departmentId) {
        return (root, query, cb) -> {
            if (departmentId == null) return null;

            Join<TimeTrack, User> userJoin = root.join("user", JoinType.INNER);
            Join<User, Job> jobJoin = userJoin.join("job", JoinType.INNER);
            Join<Job, Department> departmentJoin = jobJoin.join("department", JoinType.INNER);

            return cb.equal(departmentJoin.get("id"), departmentId);
        };
    }

    public static Specification<TimeTrack> dateBetween(LocalDate fromDate, LocalDate toDate) {
        return (root, query, cb) -> {
            if (fromDate != null && toDate != null)
                return cb.between(root.get("date"), fromDate, toDate);
            else if (fromDate != null)
                return cb.greaterThanOrEqualTo(root.get("date"), fromDate);
            else if (toDate != null)
                return cb.lessThanOrEqualTo(root.get("date"), toDate);
            else return null;
        };
    }
}