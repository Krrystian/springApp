package com.dziekanat.springApp.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDTO {
    private Integer id;
    private String title;
    private String content;
    private String authorFullName;

    @JsonCreator
    public AnnouncementDTO(@JsonProperty("title") String title,
                           @JsonProperty("content") String content) {
        this.title = title;
        this.content = content;
    }
}
