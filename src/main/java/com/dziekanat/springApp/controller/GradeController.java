package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.GradeDTO;
import com.dziekanat.springApp.model.Grade;
import com.dziekanat.springApp.model.Student;
import com.dziekanat.springApp.model.Class;
import com.dziekanat.springApp.repository.GradeRepository;
import com.dziekanat.springApp.repository.StudentRepository;
import com.dziekanat.springApp.repository.ClassRepository;
import io.jsonwebtoken.lang.Classes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private static final Logger logger = LoggerFactory.getLogger(GradeController.class);

    public GradeController(GradeRepository gradeRepository, StudentRepository studentRepository, ClassRepository classRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
    }

    @GetMapping
    public ResponseEntity<List<GradeDTO>> getAllGrades() {
        List<Grade> grades = gradeRepository.findAll();
        List<GradeDTO> gradeDTOs = grades.stream().map(grade -> new GradeDTO(
                grade.getId(),
                grade.getStudent().getFullName(),
                grade.getClasses().getName(),
                grade.getGrade(),
                grade.getDate()
        )).collect(Collectors.toList());

        logger.info("Fetched all grades: {}", gradeDTOs);
        return ResponseEntity.ok(gradeDTOs);
    }

    @PostMapping
    public ResponseEntity<GradeDTO> createGrade(@RequestBody Grade grade, @RequestParam Integer studentId, @RequestParam Integer classId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Optional<Class> classOptional = classRepository.findById(classId);
        logger.info("Creating grade: {}", grade.getGrade());
        logger.info("Student id: {}", studentId);
        logger.info("Class id: {}", classId);
        if (studentOptional.isPresent() && classOptional.isPresent()) {
            if (grade.getDate() == null) {
                grade.setDate(LocalDate.now());
            }
            grade.setStudent(studentOptional.get());
            grade.setClasses(classOptional.get());
            Grade savedGrade = gradeRepository.save(grade);

            GradeDTO responseDTO = new GradeDTO(
                    savedGrade.getId(),
                    savedGrade.getStudent().getFullName(),
                    savedGrade.getClasses().getName(),
                    savedGrade.getGrade(),
                    savedGrade.getDate()
            );
            return ResponseEntity.ok(responseDTO);
        } else {
            logger.error("Student or Class not found for provided IDs");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeDTO> updateGrade(@PathVariable Integer id, @RequestBody GradeDTO gradeDTO) {
        Optional<Grade> gradeOptional = gradeRepository.findById(id);

        if (gradeOptional.isPresent()) {
            Grade grade = gradeOptional.get();
            if (gradeDTO.getDate() != null) {
                grade.setDate(gradeDTO.getDate());
            }
            if (gradeDTO.getGrade() != null) {
                grade.setGrade(gradeDTO.getGrade());
            }
            Grade updatedGrade = gradeRepository.save(grade);
            GradeDTO responseDTO = new GradeDTO(
                    updatedGrade.getId(),
                    updatedGrade.getStudent().getFullName(),
                    updatedGrade.getClasses().getName(),
                    updatedGrade.getGrade(),
                    updatedGrade.getDate()
            );
            return ResponseEntity.ok(responseDTO);
        } else {
            logger.error("Grade with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Integer id) {
        if (gradeRepository.existsById(id)) {
            gradeRepository.deleteById(id);
            logger.info("Deleted grade with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.error("Grade with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<GradeDTO>> getGradesByClass(@PathVariable Integer classId) {
        List<Grade> grades = gradeRepository.findAllByClassId(classId);

        List<GradeDTO> gradeDTOs = grades.stream().map(grade -> new GradeDTO(
                grade.getId(),
                grade.getStudent().getFullName(),
                grade.getClasses().getName(),
                grade.getGrade(),
                grade.getDate()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(gradeDTOs);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeDTO>> getGradesByStudent(@PathVariable Integer studentId) {

        List<Grade> grades = gradeRepository.findAllByStudentId(studentId);

        List<GradeDTO> gradeDTOs = grades.stream().map(grade -> new GradeDTO(
                grade.getId(),
                grade.getStudent().getFullName(),
                grade.getClasses().getName(),
                grade.getGrade(),
                grade.getDate()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(gradeDTOs);
    }
    @PostMapping("/student/index")
    public ResponseEntity<GradeDTO> createGradeForStudentIndex(@RequestBody Grade grade, @RequestParam String studentIndex, @RequestParam String className) {
        Optional<Student> studentOptional = studentRepository.findByIndex(studentIndex);
        if (studentOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Class> classOptional = classRepository.findByName(className);
        if (classOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        grade.setStudent(studentOptional.get());
        grade.setClasses(classOptional.get());
        if (grade.getDate() == null) {
            grade.setDate(LocalDate.now());
        }

        Grade savedGrade = gradeRepository.save(grade);

        GradeDTO responseDTO = new GradeDTO(
                savedGrade.getId(),
                savedGrade.getStudent().getFullName(),
                savedGrade.getClasses().getName(),
                savedGrade.getGrade(),
                savedGrade.getDate()
        );
        return ResponseEntity.ok(responseDTO);

    }
}
