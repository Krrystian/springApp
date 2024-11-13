
package com.dziekanat.springApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentWithGradesDTO {
    private StudentDTO student;
    private List<GradeDTO> grades;

    public StudentWithGradesDTO(StudentDTO student) {
        this.student = student;
    }

}