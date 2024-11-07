package com.dziekanat.springApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class StudentDTO extends UserDTO {
    private String studentIndex;
    private Integer yearOfStudy;
    private String faculty;
    private String specialization;
    private String fullName;

    public StudentDTO(Integer id, String firstName, String lastName, String username, String studentIndex, Integer yearOfStudy, String faculty, String specialization) {
        super(id, firstName, lastName, username);
        this.studentIndex = studentIndex;
        this.yearOfStudy = yearOfStudy;
        this.faculty = faculty;
        this.specialization = specialization;
    }
    public StudentDTO(String studentIndex) {
        this.studentIndex = studentIndex;
        this.fullName = getFullName();
    }
}

