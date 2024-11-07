package com.dziekanat.springApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO extends UserDTO {
    private String position;
    public AdminDTO(Integer id, String firstName, String lastName, String username, String position) {
        super(id, firstName, lastName, username);
        this.position = position;
    }
}
