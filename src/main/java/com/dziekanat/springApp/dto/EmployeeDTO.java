package com.dziekanat.springApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO extends UserDTO {
    private String position;
    private String faculty;
    private String academicTitle;

    public EmployeeDTO(Integer id, String firstName, String lastName, String username, String position, String faculty, String academicTitle) {
        super(id, firstName, lastName, username);
        this.position = position;
        this.faculty = faculty;
        this.academicTitle = academicTitle;
    }
}
