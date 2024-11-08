package com.dziekanat.springApp.service.exportService;

import com.dziekanat.springApp.dto.StudentDTO;
import com.dziekanat.springApp.model.Student;
import com.dziekanat.springApp.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentExportService {

    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public StudentExportService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.objectMapper = new ObjectMapper();
    }

    public List<StudentDTO> exportAllStudentsNamesAndSurnames() {
        List<Student> students = studentRepository.findAll();
        List<StudentDTO> studentNames = students.stream()
                .map(student -> new StudentDTO(
                        student.getId(),
                        student.getUser().getFirstName(),
                        student.getUser().getLastName(),
                        null,
                        null,
                        null,
                        null,
                        null
                )).toList();
        return studentNames;
    }

    public List<StudentDTO> exportAllStudents() {
        List<Student> students = studentRepository.findAll();

        return students.stream()
                .map(student -> new StudentDTO(
                            student.getId(),
                            student.getUser().getFirstName(),
                            student.getUser().getLastName(),
                            student.getUser().getUsername(),
                            student.getStudentIndex(),
                            student.getYearOfStudy(),
                            student.getFaculty(),
                            student.getSpecialization()
                )).collect(Collectors.toList());
    }

    public StudentDTO exportStudentById(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        return new StudentDTO(
                student.getId(),
                student.getUser().getFirstName(),
                student.getUser().getLastName(),
                student.getUser().getUsername(),
                student.getStudentIndex(),
                student.getYearOfStudy(),
                student.getFaculty(),
                student.getSpecialization()
        );
    }

    public List<String> exportStudentsByGroupId(Integer groupId) {
        return studentRepository.findByGroupId(groupId);
    }
}
