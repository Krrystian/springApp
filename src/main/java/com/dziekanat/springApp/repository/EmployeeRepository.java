package com.dziekanat.springApp.repository;

import com.dziekanat.springApp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e WHERE e.user.username = :username")
    Optional<Employee> findByUserUsername(String username);

    @Query("SELECT e FROM Employee e WHERE e.faculty = :faculty")
    List<Employee> findByFaculty(String faculty);

    @Query("SELECT e FROM Employee e WHERE e.position = :position")
    List<Employee> findByPosition(String position);
}
