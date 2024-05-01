package com.example.junitworkshop2.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * EmployeeControllerIntegrationTest.
 *
 * @author Roman_Erzhukov
 */
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIntegrationTest {
    @Autowired
    MockMvc mvc;

    @Test
    void getPage() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(2)))
                .andExpect(jsonPath("$.data[0].name", is("simple-name")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.total", is(1)));
    }
}