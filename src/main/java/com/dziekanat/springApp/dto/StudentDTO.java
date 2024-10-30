package com.dziekanat.springApp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentDTO extends UserDTO {
    private String studentIndex;
    private Integer yearOfStudy;
    private String faculty;
    private String specialization;

    public StudentDTO(Integer id, String firstName, String lastName, String username, String studentIndex, Integer yearOfStudy, String faculty, String specialization) {
        super(id, firstName, lastName, username);
        this.studentIndex = studentIndex;
        this.yearOfStudy = yearOfStudy;
        this.faculty = faculty;
        this.specialization = specialization;
    }
}

