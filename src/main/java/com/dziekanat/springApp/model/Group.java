package com.dziekanat.springApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "grupa")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @OneToMany(mappedBy = "group")
    private Set<Class> classes;

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    private Set<Student> students;

    public Group() {
        super();
    }

    public Group(String name, Integer semester) {
        this.name = name;
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", semester=" + semester +
                '}';
    }
}
