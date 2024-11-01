package com.dziekanat.springApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDTO extends UserDTO {
    private String position;


    public AdminDTO(Integer id, String firstName, String lastName, String username, String position) {
        super(id, firstName, lastName, username);
        this.position = position;
    }

    public AdminDTO(String position) {
        this.position = position;
    }
}
