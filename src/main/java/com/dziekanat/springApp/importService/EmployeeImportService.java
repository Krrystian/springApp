package com.dziekanat.springApp.importService;

import com.dziekanat.springApp.dto.EmployeeDTO;
import com.dziekanat.springApp.model.Employee;
import com.dziekanat.springApp.model.User;
import com.dziekanat.springApp.repository.EmployeeRepository;
import com.dziekanat.springApp.repository.UserRepository;
import com.dziekanat.springApp.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class EmployeeImportService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Employee importEmployee(EmployeeDTO employeeDTO) {
        adminService.getAuthenticatedAdmin();

        User user = userRepository.findById(employeeDTO.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setPosition(employeeDTO.getPosition());
        employee.setFaculty(employeeDTO.getFaculty());
        employee.setAcademicTitle(employeeDTO.getAcademicTitle());

        return employeeRepository.save(employee);
    }

    public List<Employee> importEmployees(List<EmployeeDTO> employeeDTOs) {
        adminService.getAuthenticatedAdmin();

        List<Employee> employees = employeeDTOs.stream()
                .map(this::importEmployee)
                .toList();
        return employees;
    }

    public void importEmployeesFromJson(File jsonFile) throws IOException {
        adminService.getAuthenticatedAdmin();

        List<EmployeeDTO> employeeDTOs = objectMapper.readValue(jsonFile,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EmployeeDTO.class));

        importEmployees(employeeDTOs);
    }
}