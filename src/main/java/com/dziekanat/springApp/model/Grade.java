package com.dziekanat.springApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "classes_id", referencedColumnName = "id", nullable = false)
    private Class classes;

    @Column(name = "grade", nullable = false)
    private Double grade;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public Grade() {
        super();
    }

    public Grade(Student student, Class classes, Double grade, LocalDate date) {
        this.student = student;
        this.classes = classes;
        this.grade = grade;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", student=" + student.getFullName() +
                ", classes=" + classes.getName() +
                ", grade=" + grade +
                ", date=" + date +
                '}';
    }
}
