package com.dziekanat.springApp.exportService;

import com.dziekanat.springApp.dto.StudentDTO;
import com.dziekanat.springApp.model.Student;
import com.dziekanat.springApp.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class StudentExportService {

    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public StudentExportService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.objectMapper = new ObjectMapper();
    }

    public void exportAllStudentsNamesAndSurnames(String filePath) throws IOException {
        List<Student> students = studentRepository.findAll();
        List<StudentDTO> studentNames = students.stream()
                .map(student -> new StudentDTO(student.getStudentIndex(), student.getFullName()))
                .toList();
        objectMapper.writeValue(new File(filePath), studentNames);
    }

    public void exportAllStudents(String filePath) throws IOException {
        List<Student> students = studentRepository.findAll();
        objectMapper.writeValue(new File(filePath), students);
    }

    public void exportStudentById(Integer studentId, String filePath) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        objectMapper.writeValue(new File(filePath), student);
    }

    public void exportStudentsByGroupId(Integer groupId, String filePath) throws IOException {
        List<String> students = studentRepository.findByGroupId(groupId);
        objectMapper.writeValue(new File(filePath), students);
    }
}
