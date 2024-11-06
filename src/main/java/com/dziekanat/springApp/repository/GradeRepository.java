package com.dziekanat.springApp.repository;

import com.dziekanat.springApp.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {

    @Query("SELECT g FROM Grade g WHERE g.classes.id = :classId")
    List<Grade> findAllByClassId(Integer classId);

    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId")
    List<Grade> findAllByStudentId(Integer studentId);
}