package com.dziekanat.springApp.dto;

import com.dziekanat.springApp.model.Group;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {
    private Integer id;
    private String groupName;
    private Integer semester;
    private List<String> students;
    private String[] classes;

    GroupDTO(String groupName, Integer semester, Integer id) {
        this.id = id;
        this.groupName = groupName;
        this.semester = semester;
    }

    public GroupDTO(Group group) {
        this.groupName = group.getName();
        this.semester = group.getSemester();
        this.id = group.getId();
    }
}

