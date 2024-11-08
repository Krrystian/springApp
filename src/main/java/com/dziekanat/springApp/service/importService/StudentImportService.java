package com.dziekanat.springApp.service.importService;

import com.dziekanat.springApp.dto.StudentDTO;
import com.dziekanat.springApp.model.Role;
import com.dziekanat.springApp.model.Student;
import com.dziekanat.springApp.model.User;
import com.dziekanat.springApp.repository.StudentRepository;
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
public class StudentImportService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeImportService.class);

    private StudentRepository studentRepository;
    private UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public StudentImportService(ObjectMapper objectMapper, UserRepository userRepository, StudentRepository studentRepository) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    public Student importStudent(StudentDTO studentDTO) {
        logger.info("Importing student with username: {} and ID: {}", studentDTO.getUsername(), studentDTO.getStudentIndex());
        logger.debug("StudentDTO details: Username={}, FirstName={}, LastName={}, StudentIndex={}, Faculty={}, YearOfStudy={}, Specialization={}", studentDTO.getUsername(), studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getStudentIndex(), studentDTO.getFaculty(), studentDTO.getYearOfStudy(), studentDTO.getSpecialization());

        Optional<User> userOptional = userRepository.findById(studentDTO.getId());
        User user;
        if(userOptional.isPresent()){
            user = userOptional.get();
        }
        else{
            user = new User();
            user.setId(studentDTO.getId());
            user.setPassword("defaultPassword");
            user.setUsername(studentDTO.getUsername());
            user.setFirstName(studentDTO.getFirstName());
            user.setLastName(studentDTO.getLastName());
            user.setRole(Role.STUDENT);

            logger.debug("Creating new user: ID={}, Username={}, FirstName={}, LastName={}, Role={}",
                    user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getRole());

            user = userRepository.save(user);
            logger.info("Created a new user with ID: {} and default password", user.getId());
        }

        Student student = new Student();
        student.setUser(user);
        student.setStudentIndex(studentDTO.getStudentIndex());
        student.setYearOfStudy(studentDTO.getYearOfStudy());
        student.setFaculty(studentDTO.getFaculty());
        student.setSpecialization(studentDTO.getSpecialization());

        logger.debug("Saving employee: User ID={}, StudentIndex={}, Faculty={}, YearOfStudy={}, Specialization={}",
                user.getId(), student.getStudentIndex(), student.getFaculty(), student.getYearOfStudy(), student.getSpecialization());

        return studentRepository.save(student);
    }

    public List<Student> importStudents(List<StudentDTO> studentDTOs) {
        logger.info("Importing a list of {} students", studentDTOs.size());

        List<Student> students = studentDTOs.stream()
                .map(this::importStudent)
                .toList();
        logger.info("Successfully imported {} students", students.size());
        return students;
    }

    public void importStudentsFromJson(File jsonFile) throws IOException {
        logger.info("Importing students from JSON file: {}", jsonFile.getPath());

        List<StudentDTO> studentDTOs = objectMapper.readValue(jsonFile,
                objectMapper.getTypeFactory().constructCollectionType(List.class, StudentDTO.class));

        studentDTOs.forEach(dto -> logger.debug("Deserialized StudentDTO: ID={}, Username={}, FirstName={}, LastName={}, StudentIndex={}, Faculty={}, YearOfStudy={}, Specialization={}",
                dto.getId(), dto.getUsername(), dto.getFirstName(), dto.getLastName(),
                dto.getStudentIndex(), dto.getFaculty(), dto.getYearOfStudy(), dto.getSpecialization()));

        importStudents(studentDTOs);
        logger.info("Successfully imported students from JSON file");
    }
}
