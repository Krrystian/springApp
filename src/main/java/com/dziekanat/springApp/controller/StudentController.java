package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.StudentDTO;
import com.dziekanat.springApp.model.Role;
import com.dziekanat.springApp.model.Student;
import com.dziekanat.springApp.model.User;
import com.dziekanat.springApp.repository.StudentRepository;
import com.dziekanat.springApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Integer id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Student student = optionalStudent.get();
            StudentDTO studentDTO = new StudentDTO(
                    student.getUser().getId(),
                    student.getUser().getFirstName(),
                    student.getUser().getLastName(),
                    student.getUser().getUsername(),
                    student.getStudentIndex(),
                    student.getYearOfStudy(),
                    student.getFaculty(),
                    student.getSpecialization()
            );
            return ResponseEntity.ok(studentDTO);
        }
    }


    @PostMapping()
    public ResponseEntity<StudentDTO> createStudent(@RequestBody Student student, @RequestParam Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            if (!existingUser.getRole().equals(Role.STUDENT)) {
                return ResponseEntity.badRequest().body(null);
            }
            student.setUser(existingUser);

            Student savedStudent = studentRepository.save(student);

            StudentDTO studentDTO = new StudentDTO(
                    savedStudent.getUser().getId(),
                    savedStudent.getUser().getFirstName(),
                    savedStudent.getUser().getLastName(),
                    savedStudent.getUser().getUsername(),
                    savedStudent.getStudentIndex(),
                    savedStudent.getYearOfStudy(),
                    savedStudent.getFaculty(),
                    savedStudent.getSpecialization()
            );
            return ResponseEntity.ok(studentDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer id, @RequestBody Student updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {
                    User user = student.getUser();
                    user.setFirstName(updatedStudent.getUser().getFirstName());
                    user.setLastName(updatedStudent.getUser().getLastName());
                    user.setUsername(updatedStudent.getUser().getUsername());
                    user.setPassword(updatedStudent.getUser().getPassword());
                    user.setRole(updatedStudent.getUser().getRole());
                    userRepository.save(user);

                    student.setStudentIndex(updatedStudent.getStudentIndex());
                    student.setYearOfStudy(updatedStudent.getYearOfStudy());
                    student.setFaculty(updatedStudent.getFaculty());
                    student.setSpecialization(updatedStudent.getSpecialization());

                    studentRepository.save(student);
                    return ResponseEntity.ok(student);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
