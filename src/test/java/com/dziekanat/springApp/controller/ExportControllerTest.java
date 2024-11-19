package com.dziekanat.springApp.controller;

import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.dziekanat.springApp.TestHelper.obtainAccessTokenAdmin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testExportAllEmployees() throws Exception {
        String accessToken = obtainAccessTokenAdmin(mockMvc);

        mockMvc.perform(get("/export/employees")
                        .param("filePath", "test-employees.json")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Export of all employees successful")));
    }

    @Test
    public void testExportEmployeeById() throws Exception {
        String accessToken = obtainAccessTokenAdmin(mockMvc);

        mockMvc.perform(get("/export/employees/{id}", 1)
                        .param("filePath", "test-employee-1.json")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Export of employee by ID successful")));
    }

    @Test
    public void testExportAllStudents() throws Exception {
        String accessToken = obtainAccessTokenAdmin(mockMvc);

        mockMvc.perform(get("/export/students/all")
                        .param("filePath", "test-students.json")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Export of all students successful")));
    }

    @Test
    public void testExportStudentsByGroupId() throws Exception {
        String accessToken = obtainAccessTokenAdmin(mockMvc);

        mockMvc.perform(get("/export/students/group/{groupId}", 1)
                        .param("filePath", "test-students-group-1.json")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Export of students by group ID successful")));
    }
}
