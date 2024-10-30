package com.dziekanat.springApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "position")
    private String position;

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "academic_title")
    private String academicTitle;

    public Employee() {
        super();
    }

    public Employee(User user, String position, String faculty, String academicTitle) {
        this.user = user;
        this.position = position;
        this.faculty = faculty;
        this.academicTitle = academicTitle;
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    public boolean isInFaculty(String facultyName) {
        return this.faculty != null && this.faculty.equalsIgnoreCase(facultyName);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fullName='" + getFullName() + '\'' +
                ", position='" + position + '\'' +
                ", faculty='" + faculty + '\'' +
                ", academicTitle='" + academicTitle + '\'' +
                ", username='" + user.getUsername() + '\'' +
                '}';
    }
}
