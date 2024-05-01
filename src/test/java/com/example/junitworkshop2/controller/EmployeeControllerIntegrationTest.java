package com.example.junitworkshop2.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.junitworkshop2.service.EmployeeService;
import com.example.junitworkshop2.test.annotation.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

/**
 * EmployeeControllerIntegrationTest.
 *
 * @author Roman_Erzhukov
 */
@IntegrationTest
class EmployeeControllerIntegrationTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    EmployeeService service;

    @Test
    void getPage() throws Exception {
        doReturn(new GenericPage<>(List.of(EmployeeDto.builder()
                .id(1L)
                .name("simple-name2")
                .build()), 3)
        ).when(service).find(PageRequest.of(1, 2), EmployeeFilter.builder()
                .name("simple")
                .build());

        mvc.perform(MockMvcRequestBuilders.get(EmployeeController.URL + "?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("simple-name2")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.total", is(3)));
    }
}