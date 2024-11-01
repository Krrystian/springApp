package com.dziekanat.springApp.dto;

import com.dziekanat.springApp.model.Group;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class GroupDTO {
    private String groupName;
    private Integer semester;
    private List<String> students;
    private String[] classes;

    GroupDTO(String groupName, Integer semester) {
        this.groupName = groupName;
        this.semester = semester;
    }

    public GroupDTO(Group group) {
        this.groupName = group.getName();
        this.semester = group.getSemester();
    }
}

