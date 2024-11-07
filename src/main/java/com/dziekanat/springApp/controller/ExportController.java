package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.EmployeeDTO;
import com.dziekanat.springApp.service.exportService.EmployeeExportService;
import com.dziekanat.springApp.service.exportService.StudentExportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/export")
public class ExportController {

    private final EmployeeExportService employeeExportService;
    private final StudentExportService studentExportService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExportController(EmployeeExportService employeeExportService, StudentExportService studentExportService) {
        this.employeeExportService = employeeExportService;
        this.studentExportService = studentExportService;
        this.objectMapper = new ObjectMapper();
    }

    // Endpoint do eksportowania wszystkich pracowników do pliku JSON
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

    // Endpoint do eksportowania pracowników wg wydziału do pliku JSON
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

    // Endpoint do eksportowania pracowników wg stanowiska do pliku JSON
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

    // Endpoint do eksportowania pracownika wg ID do pliku JSON
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

    // Endpointy dla studentów działają tak samo jak wcześniej
    @GetMapping("/students/all")
    public ResponseEntity<String> exportAllStudents(@RequestParam String filePath) {
        try {
            studentExportService.exportAllStudents(filePath);
            return ResponseEntity.ok("Export of all students successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting students: " + e.getMessage());
        }
    }

    @GetMapping("/students/names")
    public ResponseEntity<String> exportAllStudentsNames(@RequestParam String filePath) {
        try {
            studentExportService.exportAllStudentsNamesAndSurnames(filePath);
            return ResponseEntity.ok("Export of student names and surnames successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting student names: " + e.getMessage());
        }
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<String> exportStudentById(@PathVariable Integer id, @RequestParam String filePath) {
        try {
            studentExportService.exportStudentById(id, filePath);
            return ResponseEntity.ok("Export of student by ID successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting student by ID: " + e.getMessage());
        }
    }

    @GetMapping("/students/group/{groupId}")
    public ResponseEntity<String> exportStudentsByGroupId(@PathVariable Integer groupId, @RequestParam String filePath) {
        try {
            studentExportService.exportStudentsByGroupId(groupId, filePath);
            return ResponseEntity.ok("Export of students by group ID successful to file: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting students by group ID: " + e.getMessage());
        }
    }
}