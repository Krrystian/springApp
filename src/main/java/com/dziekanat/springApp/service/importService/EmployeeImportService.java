package com.dziekanat.springApp.service.importService;

import com.dziekanat.springApp.dto.EmployeeDTO;
import com.dziekanat.springApp.model.Employee;
import com.dziekanat.springApp.model.Role;
import com.dziekanat.springApp.model.User;
import com.dziekanat.springApp.repository.EmployeeRepository;
import com.dziekanat.springApp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeImportService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeImportService.class);

<<<<<<< HEAD
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Employee importEmployee(EmployeeDTO employeeDTO) {
        User user = userRepository.findById(employeeDTO.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
=======
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmployeeImportService(EmployeeRepository employeeRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public Employee importEmployee(EmployeeDTO employeeDTO) {
        logger.info("Importing employee with Username: {} and ID: {}", employeeDTO.getUsername(), employeeDTO.getId());

        // Log all fields of employeeDTO to ensure values are present
        logger.debug("EmployeeDTO details: Username={}, FirstName={}, LastName={}, Position={}, Faculty={}, AcademicTitle={}",
                employeeDTO.getUsername(), employeeDTO.getFirstName(), employeeDTO.getLastName(),
                employeeDTO.getPosition(), employeeDTO.getFaculty(), employeeDTO.getAcademicTitle());

        Optional<User> userOptional = userRepository.findById(employeeDTO.getId());
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            // Create a new User with provided details from employeeDTO
            user = new User();
            user.setId(employeeDTO.getId());
            user.setPassword("defaultPassword"); // Set a default password
            user.setUsername(employeeDTO.getUsername()); // Map fields from employeeDTO
            user.setFirstName(employeeDTO.getFirstName());
            user.setLastName(employeeDTO.getLastName());
            user.setRole(Role.PRACOWNIK);

            // Log the User object details before saving
            logger.debug("Creating new user: ID={}, Username={}, FirstName={}, LastName={}, Role={}",
                    user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getRole());

            user = userRepository.save(user);
            logger.info("Created a new user with ID: {} and default password", user.getId());
        }

        // Create and save Employee entity
>>>>>>> 69a9b9b1ba35a88197d97df1ded1a93c25c5f828
        Employee employee = new Employee();
        employee.setUser(user);
        employee.setPosition(employeeDTO.getPosition());
        employee.setFaculty(employeeDTO.getFaculty());
        employee.setAcademicTitle(employeeDTO.getAcademicTitle());

        logger.debug("Saving employee: User ID={}, Position={}, Faculty={}, AcademicTitle={}",
                user.getId(), employee.getPosition(), employee.getFaculty(), employee.getAcademicTitle());

        return employeeRepository.save(employee);
    }


    public List<Employee> importEmployees(List<EmployeeDTO> employeeDTOs) {
<<<<<<< HEAD
=======
        logger.info("Importing a list of {} employees", employeeDTOs.size());

>>>>>>> 69a9b9b1ba35a88197d97df1ded1a93c25c5f828
        List<Employee> employees = employeeDTOs.stream()
                .map(this::importEmployee)
                .toList();

        logger.info("Successfully imported {} employees", employees.size());
        return employees;
    }

    public void importEmployeesFromJson(File jsonFile) throws IOException {
        logger.info("Importing employees from JSON file: {}", jsonFile.getPath());

        List<EmployeeDTO> employeeDTOs = objectMapper.readValue(jsonFile,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EmployeeDTO.class));

        employeeDTOs.forEach(dto -> logger.debug("Deserialized EmployeeDTO: ID={}, Username={}, FirstName={}, LastName={}, Position={}, Faculty={}, AcademicTitle={}",
                dto.getId(), dto.getUsername(), dto.getFirstName(), dto.getLastName(),
                dto.getPosition(), dto.getFaculty(), dto.getAcademicTitle()));

        importEmployees(employeeDTOs);
        logger.info("Successfully imported employees from JSON file");
    }

}
