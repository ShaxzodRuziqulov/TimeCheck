package com.example.timecheck.repository;

import com.example.timecheck.entity.Department;
import com.example.timecheck.entity.User;
import com.example.timecheck.entity.enumirated.DepartmentStatus;
import com.example.timecheck.entity.enumirated.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.userStatus = :userStatus")
    List<User> findByStatus(@Param("userStatus") UserStatus userStatus);

    @Query("select count(u) from User u where u.userStatus= :userStatus")
    long countByUserStatus(@Param("userStatus") UserStatus userStatus);
}
