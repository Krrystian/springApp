package com.dziekanat.springApp.service.importService;

import com.dziekanat.springApp.dto.StudentDTO;
import com.dziekanat.springApp.model.Student;
import com.dziekanat.springApp.model.User;
import com.dziekanat.springApp.repository.StudentRepository;
import com.dziekanat.springApp.repository.UserRepository;
import com.dziekanat.springApp.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class StudentImportService {

    private StudentRepository studentRepository;
    private UserRepository userRepository;
    private AdminService adminService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Student importStudent(StudentDTO studentDTO) {
        adminService.getAuthenticatedAdmin();

        User user = userRepository.findById(studentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Student student = new Student();
        student.setUser(user);
        student.setStudentIndex(studentDTO.getStudentIndex());
        student.setYearOfStudy(studentDTO.getYearOfStudy());
        student.setFaculty(studentDTO.getFaculty());
        student.setSpecialization(studentDTO.getSpecialization());

        return studentRepository.save(student);
    }

    public List<Student> importStudents(List<StudentDTO> studentDTOs) {
        adminService.getAuthenticatedAdmin();

        List<Student> students = studentDTOs.stream()
                .map(this::importStudent)
                .toList();
        return students;
    }

    public void importStudentsFromJson(File jsonFile) throws IOException {
        adminService.getAuthenticatedAdmin();

        List<StudentDTO> studentDTOs = objectMapper.readValue(jsonFile,
                objectMapper.getTypeFactory().constructCollectionType(List.class, StudentDTO.class));

        importStudents(studentDTOs);
    }
}
