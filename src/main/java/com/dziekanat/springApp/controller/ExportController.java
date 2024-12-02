package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.EmployeeDTO;
import com.dziekanat.springApp.dto.StudentDTO;
import com.dziekanat.springApp.service.exportService.EmployeeExportService;
import com.dziekanat.springApp.service.exportService.StudentExportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/export")
public class ExportController {

    private final EmployeeExportService employeeExportService;
    private final StudentExportService studentExportService;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);


    @Autowired
    public ExportController(EmployeeExportService employeeExportService, StudentExportService studentExportService) {
        this.employeeExportService = employeeExportService;
        this.studentExportService = studentExportService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/employees")
    public ResponseEntity<String> exportAllEmployees(@RequestParam String filePath) {
        List<EmployeeDTO> employees = employeeExportService.exportAllEmployees();
        try {
            objectMapper.writeValue(new File(filePath), employees);
            return ResponseEntity.ok("Export of all employees successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting employees: " + e.getMessage());
        }
    }

    @GetMapping("/employees/faculty/{faculty}")
    public ResponseEntity<String> exportEmployeesByFaculty(@PathVariable String faculty, @RequestParam String filePath) {
        List<EmployeeDTO> employees = employeeExportService.exportEmployeesByFaculty(faculty);
        try {
            objectMapper.writeValue(new File(filePath), employees);
            return ResponseEntity.ok("Export of employees by faculty successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting employees by faculty: " + e.getMessage());
        }
    }

    @GetMapping("/employees/position/{position}")
    public ResponseEntity<String> exportEmployeesByPosition(@PathVariable String position, @RequestParam String filePath) {
        List<EmployeeDTO> employees = employeeExportService.exportEmployeesByPosition(position);
        try {
            objectMapper.writeValue(new File(filePath), employees);
            return ResponseEntity.ok("Export of employees by position successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting employees by position: " + e.getMessage());
        }
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<String> exportEmployeeById(@PathVariable Integer id, @RequestParam String filePath) {
        EmployeeDTO employee = employeeExportService.exportEmployeeById(id);
        try {
            objectMapper.writeValue(new File(filePath), employee);
            return ResponseEntity.ok("Export of employee by ID successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting employee by ID: " + e.getMessage());
        }
    }

    @GetMapping("/students/all")
    public ResponseEntity<String> exportAllStudents(@RequestParam String filePath) {
        List<StudentDTO> students = studentExportService.exportAllStudents();
        try {
            objectMapper.writeValue(new File(filePath), students);
            return ResponseEntity.ok("Export of all students successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting students: " + e.getMessage());
        }
    }

    @GetMapping("/students/download/android")
    public ResponseEntity<Resource> downloadAllStudents() {
        logger.info("Metoda downloadAllStudents została wywołana");

        List<StudentDTO> students = studentExportService.exportAllStudents();
        try {
            File tempFile = File.createTempFile("students", ".txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                for (StudentDTO student : students) {
                    writer.write(String.format(
                            "ID: %d, Name: %s %s, Username: %s, Index: %s, Year: %d, Faculty: %s, Specialization: %s, GroupID: %s%n",
                            student.getId(),
                            student.getFirstName(),
                            student.getLastName(),
                            student.getUsername(),
                            student.getStudentIndex(),
                            student.getYearOfStudy(),
                            student.getFaculty(),
                            student.getSpecialization(),
                            student.getGroupId() != null ? student.getGroupId() : "None"
                    ));
                }
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));
            logger.debug("Plik tymczasowy został utworzony: {}", tempFile.getAbsolutePath());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.txt")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(resource);
        } catch (IOException e) {
            logger.error("Wystąpił błąd podczas eksportu studentów", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/students/names")
    public ResponseEntity<String> exportAllStudentsNames(@RequestParam String filePath) {
        List<StudentDTO> studentsNames = studentExportService.exportAllStudentsNamesAndSurnames();
        try {
            objectMapper.writeValue(new File(filePath), studentsNames);
            return ResponseEntity.ok("Export of student names and surnames successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting student names: " + e.getMessage());
        }
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<String> exportStudentById(@PathVariable Integer id, @RequestParam String filePath) {
        StudentDTO student = studentExportService.exportStudentById(id);
        try {
            objectMapper.writeValue(new File(filePath), student);
            return ResponseEntity.ok("Export of student by ID successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting student by ID: " + e.getMessage());
        }
    }

    @GetMapping("/students/group/{groupId}")
    public ResponseEntity<String> exportStudentsByGroupId(@PathVariable Integer groupId, @RequestParam String filePath) {
        List<String> students = studentExportService.exportStudentsByGroupId(groupId);
        try {
            objectMapper.writeValue(new File(filePath), students);
            return ResponseEntity.ok("Export of students by group ID successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting students by group ID: " + e.getMessage());
        }
    }
}