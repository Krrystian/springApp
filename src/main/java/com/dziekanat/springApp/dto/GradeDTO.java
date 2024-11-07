package com.dziekanat.springApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class GradeDTO {
    private Integer id;
    private String studentFullName;
    private String className;
    private Double grade;
    private LocalDate date;
}
