package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.EmployeeDTO;
import com.dziekanat.springApp.model.Employee;
import com.dziekanat.springApp.model.Role;
import com.dziekanat.springApp.model.User;
import com.dziekanat.springApp.repository.EmployeeRepository;
import com.dziekanat.springApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        logger.info("Fetched all employees: {}", employees);

        List<EmployeeDTO> employeeDTOs = employees.stream().map(employee ->
                new EmployeeDTO(
                        employee.getUser().getId(),
                        employee.getUser().getFirstName(),
                        employee.getUser().getLastName(),
                        employee.getUser().getUsername(),
                        employee.getPosition(),
                        employee.getFaculty(),
                        employee.getAcademicTitle()
                )
        ).toList();

        return ResponseEntity.ok(employeeDTOs);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody Employee employee, @RequestParam Integer userId) {
        logger.debug("Creating employee with userId: {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            if (!existingUser.getRole().equals(Role.PRACOWNIK)) {
                return ResponseEntity.badRequest().body(null);
            }
            employee.setUser(existingUser);

            Employee savedEmployee = employeeRepository.save(employee);
            logger.info("Employee created successfully: {}", savedEmployee);
            logger.info("user: {}", savedEmployee.getUser().getFirstName());
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    savedEmployee.getUser().getId(),
                    savedEmployee.getUser().getFirstName(),
                    savedEmployee.getUser().getLastName(),
                    savedEmployee.getUser().getUsername(),
                    savedEmployee.getPosition(),
                    savedEmployee.getFaculty(),
                    savedEmployee.getAcademicTitle()
            );
            return ResponseEntity.ok(employeeDTO);
        } else {
            logger.error("User with ID {} not found", userId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Integer id) {
        logger.debug("Fetching employee by ID: {}", id);
        return employeeRepository.findById(id)
                .map(employee -> {
                    EmployeeDTO employeeDTO = new EmployeeDTO(
                            employee.getUser().getId(),
                            employee.getUser().getFirstName(),
                            employee.getUser().getLastName(),
                            employee.getUser().getUsername(),
                            employee.getPosition(),
                            employee.getFaculty(),
                            employee.getAcademicTitle()
                    );
                    logger.info("Fetched employee: {}", employeeDTO);
                    return ResponseEntity.ok(employeeDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Integer id, @RequestBody Employee updatedEmployee) {
        logger.debug("Updating employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(employee -> {
                    if(updatedEmployee.getPosition() != null){
                        employee.setPosition(updatedEmployee.getPosition());
                    }
                    if(updatedEmployee.getFaculty() != null){
                        employee.setFaculty(updatedEmployee.getFaculty());
                    }
                    if(updatedEmployee.getAcademicTitle() != null) {
                        employee.setAcademicTitle(updatedEmployee.getAcademicTitle());
                    }
                    employeeRepository.save(employee);

                    EmployeeDTO employeeDTO = new EmployeeDTO(
                            employee.getUser().getId(),
                            employee.getUser().getFirstName(),
                            employee.getUser().getLastName(),
                            employee.getUser().getUsername(),
                            employee.getPosition(),
                            employee.getFaculty(),
                            employee.getAcademicTitle()
                    );
                    return ResponseEntity.ok(employeeDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable Integer id) {
        logger.debug("Deleting employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(employee -> {
                    employeeRepository.delete(employee);
                    logger.info("Employee deleted successfully with ID: {}", id);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/faculty/{facultyName}")   //np. GET /employee/faculty/Informatyka
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByFaculty(@PathVariable String facultyName) {
        logger.debug("Fetching employees by faculty: {}", facultyName);

        List<Employee> employees = employeeRepository.findByFaculty(facultyName);
        List<EmployeeDTO> employeeDTOs = employees.stream().map(employee -> new EmployeeDTO(
                employee.getUser().getId(),
                employee.getUser().getFirstName(),
                employee.getUser().getLastName(),
                employee.getUser().getUsername(),
                employee.getPosition(),
                employee.getFaculty(),
                employee.getAcademicTitle()
        )).collect(Collectors.toList());

        logger.info("Found employees for faculty {}: {}", facultyName, employeeDTOs);
        return ResponseEntity.ok(employeeDTOs);
    }
}
