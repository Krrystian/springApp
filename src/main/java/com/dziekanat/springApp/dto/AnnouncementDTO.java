package com.dziekanat.springApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnnouncementDTO {
    private Integer id;
    private String title;
    private String content;
    private String authorFullName;
}
