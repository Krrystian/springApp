package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.GradeDTO;
import com.dziekanat.springApp.model.Grade;
import com.dziekanat.springApp.model.Student;
import com.dziekanat.springApp.model.Class;
import com.dziekanat.springApp.repository.GradeRepository;
import com.dziekanat.springApp.repository.StudentRepository;
import com.dziekanat.springApp.repository.ClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                grade.getStudent().getId(),
                grade.getStudent().getFullName(),
                grade.getClasses().getId(),
                grade.getClasses().getName(),
                grade.getGrade(),
                grade.getDate()
        )).collect(Collectors.toList());

        logger.info("Fetched all grades: {}", gradeDTOs);
        return ResponseEntity.ok(gradeDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeDTO> getGradeById(@PathVariable Integer id) {
        return gradeRepository.findById(id)
                .map(grade -> new GradeDTO(
                        grade.getId(),
                        grade.getStudent().getId(),
                        grade.getStudent().getFullName(),
                        grade.getClasses().getId(),
                        grade.getClasses().getName(),
                        grade.getGrade(),
                        grade.getDate()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GradeDTO> createGrade(@RequestBody GradeDTO gradeDTO) {
        Optional<Student> studentOptional = studentRepository.findById(gradeDTO.getStudentId());
        Optional<Class> classOptional = classRepository.findById(gradeDTO.getClassId());

        if (studentOptional.isPresent() && classOptional.isPresent()) {
            Grade grade = new Grade(
                    studentOptional.get(),
                    classOptional.get(),
                    gradeDTO.getGrade(),
                    gradeDTO.getDate()
            );
            Grade savedGrade = gradeRepository.save(grade);

            GradeDTO responseDTO = new GradeDTO(
                    savedGrade.getId(),
                    savedGrade.getStudent().getId(),
                    savedGrade.getStudent().getFullName(),
                    savedGrade.getClasses().getId(),
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
            grade.setGrade(gradeDTO.getGrade());
            grade.setDate(gradeDTO.getDate());
            Grade updatedGrade = gradeRepository.save(grade);

            GradeDTO responseDTO = new GradeDTO(
                    updatedGrade.getId(),
                    updatedGrade.getStudent().getId(),
                    updatedGrade.getStudent().getFullName(),
                    updatedGrade.getClasses().getId(),
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
                grade.getStudent().getId(),
                grade.getStudent().getFullName(),
                grade.getClasses().getId(),
                grade.getClasses().getName(),
                grade.getGrade(),
                grade.getDate()
        )).collect(Collectors.toList());
        
        //ewetualnie stworzyć nowy konstruktor z samymi ocenami i studentami
        /*
        List<GradeDTO> gradeDTOs = grades.stream().map(grade -> new GradeDTO(
                grade.getId(),
                grade.getStudent().getId(),
                grade.getClasses().getId(),
                grade.getGrade(),
                grade.getDate()
        )).collect(Collectors.toList());
         */
        return ResponseEntity.ok(gradeDTOs);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeDTO>> getGradesByStudent(@PathVariable Integer studentId) {
        List<Grade> grades = gradeRepository.findAllByStudentId(studentId);

        List<GradeDTO> gradeDTOs = grades.stream().map(grade -> new GradeDTO(
                grade.getId(),
                grade.getStudent().getId(),
                grade.getStudent().getFullName(),
                grade.getClasses().getId(),
                grade.getClasses().getName(),
                grade.getGrade(),
                grade.getDate()
        )).collect(Collectors.toList());

        //ewentualnie stworzyć nowy konstruktor z samymi informacjami o ocenach i klasach?
        /*
        List<GradeDTO> gradeDTOs = grades.stream().map(grade -> new GradeDTO(
                grade.getId(),
                grade.getStudent().getId(),
                grade.getClasses().getId(),
                grade.getGrade(),
                grade.getDate()
        )).collect(Collectors.toList());
         */
        return ResponseEntity.ok(gradeDTOs);
    }
}
