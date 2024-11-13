package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.GradeDTO;
import com.dziekanat.springApp.dto.StudentDTO;
import com.dziekanat.springApp.dto.StudentWithGradesDTO;
import com.dziekanat.springApp.model.Grade;
import com.dziekanat.springApp.model.Role;
import com.dziekanat.springApp.model.Student;
import com.dziekanat.springApp.model.User;
import com.dziekanat.springApp.repository.GradeRepository;
import com.dziekanat.springApp.repository.StudentRepository;
import com.dziekanat.springApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository, UserRepository userRepository, GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.gradeRepository = gradeRepository;
    }

    //TODO: DO POPRAWY ABY NIE ZWRACAŁO HASEŁ
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
                    student.getSpecialization(),
                    student.getGroup().getId()
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
                    savedStudent.getSpecialization(),
                    savedStudent.getGroup() != null ? savedStudent.getGroup().getId() : null
            );
            return ResponseEntity.ok(studentDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Integer id, @RequestBody Student updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {
                    if (updatedStudent.getStudentIndex() != null) {
                        student.setStudentIndex(updatedStudent.getStudentIndex());
                    }
                    if (updatedStudent.getYearOfStudy() != null) {
                        student.setYearOfStudy(updatedStudent.getYearOfStudy());
                    }
                    if (updatedStudent.getFaculty() != null) {
                        student.setFaculty(updatedStudent.getFaculty());
                    }
                    if (updatedStudent.getSpecialization() != null) {
                        student.setSpecialization(updatedStudent.getSpecialization());
                    }

                    studentRepository.save(student);
                    StudentDTO studentDTO = new StudentDTO(
                            student.getUser().getId(),
                            student.getUser().getFirstName(),
                            student.getUser().getLastName(),
                            student.getUser().getUsername(),
                            student.getStudentIndex(),
                            student.getYearOfStudy(),
                            student.getFaculty(),
                            student.getSpecialization(),
                            student.getGroup().getId()
                    );
                    return ResponseEntity.ok(studentDTO);
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
    @GetMapping("/profile/grades")
    public ResponseEntity<StudentWithGradesDTO> getStudentWithGrades() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username.isEmpty())
            return ResponseEntity.badRequest().build();

        Optional<Student> studentOptional = studentRepository.findByUserUsername(username);
        if (studentOptional.isEmpty() || !studentOptional.get().getUser().getRole().equals(Role.STUDENT))
            return ResponseEntity.notFound().build();

        Student student = studentOptional.get();
        StudentWithGradesDTO studentWithGradesDTO = new StudentWithGradesDTO();
        StudentDTO studentDTO = new StudentDTO(
                student.getUser().getId(),
                student.getUser().getFirstName(),
                student.getUser().getLastName(),
                student.getUser().getUsername(),
                student.getStudentIndex(),
                student.getYearOfStudy(),
                student.getFaculty(),
                student.getSpecialization(),
                student.getGroup().getId()
        );
        List<Grade> gradeList = gradeRepository.findAllByStudentId(student.getId());
        if (!gradeList.isEmpty()){
            List<GradeDTO> gradeDTOs = gradeList.stream().map(grade -> new GradeDTO(
                    grade.getId(),
                    grade.getGrade(),
                    grade.getClasses().getName(),
                    grade.getDate()
            )).toList();
            studentWithGradesDTO.setGrades(gradeDTOs);
        }


        studentWithGradesDTO.setStudent(studentDTO);
        return ResponseEntity.ok(studentWithGradesDTO);
    }
}
