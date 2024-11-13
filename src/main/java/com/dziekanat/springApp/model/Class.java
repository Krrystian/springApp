package com.dziekanat.springApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "class")
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group group;

    public Class() {
        super();
    }

    public Class(String name, Employee employee, Group group) {
        this.name = name;
        this.employee = employee;
        this.group = group;
    }

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employee=" + employee.getFullName() +
                ", group=" + group.getName() +
                '}';
    }
}
