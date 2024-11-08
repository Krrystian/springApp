package com.dziekanat.springApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO extends UserDTO {
    private String studentIndex;
    private Integer yearOfStudy;
    private String faculty;
    private String specialization;
    private Integer groupId;

    public StudentDTO(Integer id, String firstName, String lastName, String username, String studentIndex, Integer yearOfStudy, String faculty, String specialization, Integer groupId) {
        super(id, firstName, lastName, username);
        this.studentIndex = studentIndex;
        this.yearOfStudy = yearOfStudy;
        this.faculty = faculty;
        this.specialization = specialization;
        this.groupId = groupId;
    }
    public StudentDTO(Integer id, String firstName, String lastName) {
        super(id, firstName,lastName);
    }

}

