package com.dziekanat.springApp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GradeDTO {
    private Integer id;
    private Integer studentId;
    private String studentName;
    private Integer classId;
    private String className;
    private Double grade;
    private LocalDate date;

    public GradeDTO(Integer id, Integer studentId, String studentName, Integer classId, String className, Double grade, LocalDate date) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.classId = classId;
        this.className = className;
        this.grade = grade;
        this.date = date;
    }
}
