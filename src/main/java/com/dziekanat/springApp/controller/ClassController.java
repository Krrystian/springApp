package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.ClassDTO;
import com.dziekanat.springApp.model.Class;
import com.dziekanat.springApp.repository.ClassRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/classes")
public class ClassController {

    private final ClassRepository classRepository;

    public ClassController(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    @GetMapping
    public ResponseEntity<List<ClassDTO>> getAllClasses() {
        List<Class> classes = classRepository.findAll();
        List<ClassDTO> classDTOs = classes.stream().map(c -> new ClassDTO(
                c.getId(),
                c.getName(),
                c.getEmployee().getId(),
                c.getGroup().getId()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(classDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassDTO> getClassById(@PathVariable Integer id) {
        return classRepository.findById(id)
                .map(c -> new ClassDTO(
                        c.getId(),
                        c.getName(),
                        c.getEmployee().getId(),
                        c.getGroup().getId()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClassDTO> createClass(@RequestBody Class classEntity) {
        Class savedClass = classRepository.save(classEntity);
        ClassDTO classDTO = new ClassDTO(
                savedClass.getId(),
                savedClass.getName(),
                savedClass.getEmployee().getId(),
                savedClass.getGroup().getId()
        );
        return ResponseEntity.ok(classDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassDTO> updateClass(@PathVariable Integer id, @RequestBody Class classDetails) {
        return classRepository.findById(id)
                .map(existingClass -> {
                    existingClass.setName(classDetails.getName());
                    existingClass.setEmployee(classDetails.getEmployee());
                    existingClass.setGroup(classDetails.getGroup());
                    Class updatedClass = classRepository.save(existingClass);
                    ClassDTO classDTO = new ClassDTO(
                            updatedClass.getId(),
                            updatedClass.getName(),
                            updatedClass.getEmployee().getId(),
                            updatedClass.getGroup().getId()
                    );
                    return ResponseEntity.ok(classDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClass(@PathVariable Integer id) {
        return classRepository.findById(id).map(existingClass -> {
                    classRepository.delete(existingClass);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ClassDTO>> getClassesByEmployeeId(@PathVariable Integer employeeId) {
        List<Class> classes = classRepository.findByEmployeeId(employeeId);
        List<ClassDTO> classDTOs = classes.stream()
                .map(c -> new ClassDTO(
                        c.getId(),
                        c.getName(),
                        c.getEmployee().getId(), // Assuming you want to return the employee's ID
                        c.getGroup() != null ? c.getGroup().getId() : null // Return group ID or null if not set
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(classDTOs);
    }
}
