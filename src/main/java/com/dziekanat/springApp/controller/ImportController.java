package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.service.exportService.EmployeeExportService;
import com.dziekanat.springApp.service.importService.EmployeeImportService;
import com.dziekanat.springApp.service.importService.StudentImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final EmployeeImportService employeeImportService;
    private final StudentImportService studentImportService;
    private static final Logger logger = LoggerFactory.getLogger(ImportController.class);

    @Autowired
    public ImportController(EmployeeImportService employeeImportService, StudentImportService studentImportService) {
        this.employeeImportService = employeeImportService;
        this.studentImportService = studentImportService;
    }

    @PostMapping("/employees")
    public ResponseEntity<Void> importEmployeesFromJson(@RequestParam("file") MultipartFile jsonFile) throws IOException {
        logger.info("Importing employees {}", jsonFile.getOriginalFilename());
        File tempFile = convertMultipartFileToFile(jsonFile);
        employeeImportService.importEmployeesFromJson(tempFile);
        deleteTempFile(tempFile);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/students")
    public ResponseEntity<Void> importStudentsFromJson(@RequestParam("file") MultipartFile jsonFile) throws IOException {
        File tempFile = convertMultipartFileToFile(jsonFile);
        studentImportService.importStudentsFromJson(tempFile);
        deleteTempFile(tempFile);
        return ResponseEntity.ok().build();
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        Path tempDir = Files.createTempDirectory("temp");
        File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
        file.transferTo(tempFile);
        return tempFile;
    }

    private void deleteTempFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}
