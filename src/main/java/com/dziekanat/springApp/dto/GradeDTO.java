package com.dziekanat.springApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GradeDTO {
    private Integer id;
    private String studentFullName;
    private String className;
    private Double grade;
    private LocalDate date;

    public GradeDTO(Integer id, Double grade, String className, LocalDate date) {
        this.id = id;
        this.grade = grade;
        this.className = className;
        this.date = date;
    }
}
