package com.dziekanat.springApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "student_index", unique = true, nullable = false)
    private String studentIndex;

    @Column(name = "year_of_study")
    private Integer yearOfStudy;

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "specialization")
    private String specialization;

    public Student() {
        super();
    }

    public Student(User user, String studentIndex, Integer yearOfStudy, String faculty, String specialization) {
        this.user = user;
        this.studentIndex = studentIndex;
        this.yearOfStudy = yearOfStudy;
        this.faculty = faculty;
        this.specialization = specialization;
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    public boolean isInFaculty(String facultyName) {
        return this.faculty != null && this.faculty.equalsIgnoreCase(facultyName);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentIndex='" + studentIndex + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", yearOfStudy=" + yearOfStudy +
                ", faculty='" + faculty + '\'' +
                ", specialization='" + specialization + '\'' +
                ", username='" + user.getUsername() + '\'' +
                '}';
    }
}
