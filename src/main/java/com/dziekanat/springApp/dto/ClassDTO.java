package com.dziekanat.springApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ClassDTO extends GroupDTO  {
    private Integer id;
    private String name;
    private Integer employeeId;
    private Integer group;  //ewentualnie GroupDTO ?

    public ClassDTO(Integer id, String name, Integer employeeId, Integer group) {
        this.id = id;
        this.name = name;
        this.employeeId = employeeId;
        this.group = group;
    }
}
