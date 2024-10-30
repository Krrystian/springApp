package com.dziekanat.springApp.repository;

import com.dziekanat.springApp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByStudentIndex(String studentIndex);
}
