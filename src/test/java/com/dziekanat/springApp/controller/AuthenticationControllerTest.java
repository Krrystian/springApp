package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testLoginSuccess() throws Exception {
        String requestBody = """
            {
              "username": "pracownik.pracownik",
              "password": "passwd123"
            }
        """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists())
                .andExpect(jsonPath("$.message").value("User login was successful"))
                .andExpect(jsonPath("$.role").value("PRACOWNIK"));
    }
    @Test
    void testLoginFailure() throws Exception {
        String requestBody = """
            {
              "username": "j.adamczyk",
              "password": "passwd1234"
            }
        """;
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.access_token").doesNotExist())
                .andExpect(jsonPath("$.refresh_token").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist());
    }
    @Test
    void testLoginNoData() throws Exception {
        String requestBody = """
            {
            }
        """;
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.access_token").doesNotExist())
                .andExpect(jsonPath("$.refresh_token").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist());
    }
    @Test
    void testLoginSqlInjectionInUsername() throws Exception {
        String requestBody = """
        {
          "username": "' OR '1'='1",
          "password": "passwd123"
        }
    """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.access_token").doesNotExist())
                .andExpect(jsonPath("$.refresh_token").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist());
    }

    @Test
    void testLoginSqlInjectionInPassword() throws Exception {
        String requestBody = """
        {
          "username": "j.adamczyk",
          "password": "' OR '1'='1"
        }
    """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.access_token").doesNotExist())
                .andExpect(jsonPath("$.refresh_token").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist());
    }

    @Test
    void testLoginSqlInjectionWithDropTable() throws Exception {
        String requestBody = """
        {
          "username": "j.adamczyk'; DROP TABLE users; --",
          "password": "passwd123"
        }
    """;
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.access_token").doesNotExist())
                .andExpect(jsonPath("$.refresh_token").doesNotExist())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist());
    }


    //REGISTER
    @Test
    void testRegisterSuccess() throws Exception {
        String requestBody = """
        {
          "firstName": "PRACOWNIK1000",
          "lastName":"PRACUS",
          "username":"p.pracus1200",
          "password":"passwd123",
          "role":"PRACOWNIK"
        }
    """;

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registration was successful"))
                .andExpect(jsonPath("$.access_token").exists()) // Sprawdzenie, czy access_token istnieje
                .andExpect(jsonPath("$.refresh_token").exists()) // Sprawdzenie, czy refresh_token istnieje
                .andExpect(jsonPath("$.role").value("PRACOWNIK")); // Sprawdzenie roli nowo zarejestrowanego użytkownika
    }

    @Test
    void testRegisterMissingUsername() throws Exception {
        String requestBody = """
        {
          "firstName": "PRACOWNIK3",
          "lastName":"PRACUS2",
          "password":"passwd123",
          "role":"PRACOWNIK"
        }
    """;

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.access_token").doesNotExist())
                .andExpect(jsonPath("$.refresh_token").doesNotExist());
    }

    @Test
    void testRegisterUserExist() throws Exception {
        String requestBody = """
        {
          "username": "j.adamczyk",
          "password": "a"
        }
    """;

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.access_token").doesNotExist())
                .andExpect(jsonPath("$.refresh_token").doesNotExist());
    }
}
