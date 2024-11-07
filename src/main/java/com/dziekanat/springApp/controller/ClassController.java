package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.ClassDTO;
import com.dziekanat.springApp.dto.GroupDTO;
import com.dziekanat.springApp.model.Class;
import com.dziekanat.springApp.model.Employee;
import com.dziekanat.springApp.model.Group;
import com.dziekanat.springApp.repository.ClassRepository;
import com.dziekanat.springApp.repository.EmployeeRepository;
import com.dziekanat.springApp.repository.GroupRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/classes")
public class ClassController {

    private final ClassRepository classRepository;
    private final EmployeeRepository employeeRepository;
    private final GroupRepository groupRepository;

    public ClassController(ClassRepository classRepository, EmployeeRepository employeeRepository, GroupRepository groupRepository) {
        this.classRepository = classRepository;
        this.employeeRepository = employeeRepository;
        this.groupRepository = groupRepository;
    }

    @GetMapping
    public ResponseEntity<List<ClassDTO>> getAllClasses() {
        List<Class> classes = classRepository.findAll();
        List<ClassDTO> classDTOs = classes.stream().map(c -> new ClassDTO(
                c.getId(),
                c.getName(),
                c.getEmployee().getFullName(),
                c.getGroup().getName()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(classDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassDTO> getClassById(@PathVariable Integer id) {
        return classRepository.findById(id)
                .map(c -> new ClassDTO(
                        c.getId(),
                        c.getName(),
                        c.getEmployee().getFullName(),
                        c.getGroup().getName()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClassDTO> createClass(
            @RequestBody Class classEntity,
            @RequestParam Integer employeeId,
            @RequestParam Integer groupId) {

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (employeeOptional.isPresent() && groupOptional.isPresent()) {
            classEntity.setEmployee(employeeOptional.get());
            classEntity.setGroup(groupOptional.get());
            Class savedClass = classRepository.save(classEntity);

            ClassDTO classDTO = new ClassDTO(
                    savedClass.getId(),
                    savedClass.getName(),
                    savedClass.getEmployee().getFullName(),
                    savedClass.getGroup().getName()
            );
            return ResponseEntity.ok(classDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassDTO> updateClass(@PathVariable Integer id, @RequestBody Class classDetails) {
        return classRepository.findById(id)
                .map(existingClass -> {
                    if(classDetails.getName() != null){
                        existingClass.setName(classDetails.getName());
                    }
                    if(classDetails.getEmployee() != null){
                        existingClass.setEmployee(classDetails.getEmployee());
                    }
                    if(classDetails.getGroup() != null){
                        existingClass.setGroup(classDetails.getGroup());
                    }
                    Class updatedClass = classRepository.save(existingClass);
                    ClassDTO classDTO = new ClassDTO(
                            updatedClass.getId(),
                            updatedClass.getName(),
                            updatedClass.getEmployee().getFullName(),
                            updatedClass.getGroup().getName()
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
                        c.getEmployee().getFullName(),
                        c.getGroup().getName()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(classDTOs);
    }
}
