package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.GroupDTO;
import com.dziekanat.springApp.dto.StudentIdsDTO;
import com.dziekanat.springApp.model.Group;
import com.dziekanat.springApp.model.Student;
import com.dziekanat.springApp.repository.GroupRepository;
import com.dziekanat.springApp.repository.StudentRepository;
import com.dziekanat.springApp.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;


    public GroupController(GroupRepository groupRepository, AdminService adminService, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }
    
//TODO:TEST
    @GetMapping
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        List<GroupDTO> groupsDTO = new ArrayList<>();
        List<Group> groups = groupRepository.findAll();

        for (Group group : groups) {
            GroupDTO groupDTO = new GroupDTO(group);

            List<String> studentsNames = group.getStudents().stream()
                    .map(Student::getFullName)
                    .collect(Collectors.toList());

            groupDTO.setStudents(studentsNames);

            groupsDTO.add(groupDTO);
        }

        return ResponseEntity.ok(groupsDTO);
    }

//TODO: TEST
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable Integer id) {
        Optional<Group> groupOpt = groupRepository.findById(id);
        if (groupOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Group group = groupOpt.get();
        GroupDTO groupDTO = new GroupDTO(group);
        List<String> studentsNames = group.getStudents().stream()
                .map(Student::getFullName)
                .collect(Collectors.toList());
        groupDTO.setStudents(studentsNames);
        return ResponseEntity.ok(groupDTO);
    }

    @PostMapping
    public Group createGroup(@RequestBody GroupDTO groupDTO) {
        Group group = new Group();
        group.setSemester(groupDTO.getSemester());
        group.setName(groupDTO.getGroupName());

        return groupRepository.save(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Integer id, @RequestBody Group updatedGroup) {
        return groupRepository.findById(id).map(group -> {
            if (updatedGroup.getName() != null) {
                group.setName(updatedGroup.getName());
            }
            if (updatedGroup.getSemester() != null) {
                group.setSemester(updatedGroup.getSemester());
            }
            Group savedGroup = groupRepository.save(group);
            return ResponseEntity.ok(savedGroup);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{groupId}/students")
    public ResponseEntity<GroupDTO> addStudentsToGroup(
            @PathVariable Integer groupId,
            @RequestBody StudentIdsDTO studentIdsDTO
    ) {
        logger.info("Adding students to group with ID: {}", groupId);

        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            logger.warn("Group with ID {} not found", groupId);
            return ResponseEntity.notFound().build();
        }

        Group group = groupOpt.get();
        List<Student> students = studentRepository.findAllById(studentIdsDTO.getStudentsIds());

        if (students.isEmpty()) {
            logger.warn("No students found with IDs {}", studentIdsDTO.getStudentsIds());
            return ResponseEntity.notFound().build();
        }

        logger.info("Found {} students to add to group {}", students.size(), groupId);

        for (Student student : students) {
            student.setGroup(group);
        }

        group.getStudents().addAll(students);
        Group updatedGroup = groupRepository.save(group);

        GroupDTO groupDTO = new GroupDTO(updatedGroup);
        List<String> studentsNames = studentRepository.findByGroupId(groupId);
        groupDTO.setStudents(studentsNames);

        logger.info("Successfully added students to group with ID: {}", groupId);

        return ResponseEntity.ok(groupDTO);
    }

}
