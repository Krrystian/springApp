package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.model.Employee;
import com.dziekanat.springApp.repository.EmployeeRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.dziekanat.springApp.TestHelper.obtainAccessTokenAdmin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    public void testImportEmployees() throws Exception {
        String accessToken = obtainAccessTokenAdmin(mockMvc);

        String jsonData = """
                [
                    {"id":4,"firstName":"Krzysztof","lastName":"Wiezienie","username":"k.cela","position":"adiunkt","faculty":"mechanika","academicTitle":"mgr inz."},
                    {"id":3,"firstName":"Michał","lastName":"Laskowski","username":"m.laskowski","position":"lektor","faculty":"anglistyka","academicTitle":"mgr"}
                ]
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "employees.json",
                "application/json",
                jsonData.getBytes()
        );

        mockMvc.perform(multipart("/import/employees")
                        .file(file)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("")));

        assert employeeRepository.count() == 2;

        Optional<Employee> krzysztof = employeeRepository.findByUserUsername("k.cela");
        Optional<Employee> michal = employeeRepository.findByUserUsername("m.laskowski");

        assert krzysztof.get().getUser().getFirstName().equals("Krzysztof");
        assert michal.get().getUser().getFirstName().equals("Michał");
    }
}
