package com.dziekanat.springApp.repository;

import com.dziekanat.springApp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT CONCAT(s.user.firstName, ' ', s.user.lastName) FROM Student s WHERE s.group.id = :groupId")
    List<String> findByGroupId(Integer groupId);

    @Query("SELECT s FROM Student s WHERE s.user.username = :username")
    Optional<Student> findByUserUsername(String username);

}
