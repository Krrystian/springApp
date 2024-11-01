package com.dziekanat.springApp.dto;

import java.util.List;

public class StudentIdsDTO {
    private List<Integer> studentsIds;

    public List<Integer> getStudentsIds() {
        return studentsIds;
    }

    public void setStudentsIds(List<Integer> studentsIds) {
        this.studentsIds = studentsIds;
    }
}
