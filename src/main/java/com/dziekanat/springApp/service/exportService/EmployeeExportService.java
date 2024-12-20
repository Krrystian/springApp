package com.dziekanat.springApp.service.exportService;

import com.dziekanat.springApp.dto.EmployeeDTO;
import com.dziekanat.springApp.model.Employee;
import com.dziekanat.springApp.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeExportService {

    private final EmployeeRepository employeeRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeExportService.class);

    @Autowired
    public EmployeeExportService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDTO> exportAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        logger.info("Fetched all employees: {}", employees);
        return employees.stream()
                .map(employee -> new EmployeeDTO(
                        employee.getId(),
                        employee.getUser().getFirstName(),
                        employee.getUser().getLastName(),
                        employee.getUser().getUsername(),
                        employee.getPosition(),
                        employee.getFaculty(),
                        employee.getAcademicTitle()))
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> exportEmployeesByFaculty(String faculty) {
        List<Employee> employees = employeeRepository.findByFaculty(faculty);
        return employees.stream()
                .map(employee -> new EmployeeDTO(
                        employee.getId(),
                        employee.getUser().getFirstName(),
                        employee.getUser().getLastName(),
                        employee.getUser().getUsername(),
                        employee.getPosition(),
                        employee.getFaculty(),
                        employee.getAcademicTitle()))
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> exportEmployeesByPosition(String position) {
        List<Employee> employees = employeeRepository.findByPosition(position);
        return employees.stream()
                .map(employee -> new EmployeeDTO(
                        employee.getId(),
                        employee.getUser().getFirstName(),
                        employee.getUser().getLastName(),
                        employee.getUser().getUsername(),
                        employee.getPosition(),
                        employee.getFaculty(),
                        employee.getAcademicTitle()))
                .collect(Collectors.toList());
    }

    public EmployeeDTO exportEmployeeById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        return new EmployeeDTO(
                employee.getId(),
                employee.getUser().getFirstName(),
                employee.getUser().getLastName(),
                employee.getUser().getUsername(),
                employee.getPosition(),
                employee.getFaculty(),
                employee.getAcademicTitle());
    }
}
