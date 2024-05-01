package com.example.junitworkshop2.controller;

import static com.example.junitworkshop2.test.extension.UidExtension.uid;
import static com.example.junitworkshop2.test.extension.UidExtension.uidS;
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
        long id = uid();
        String name = uidS();
        long total = uid();
        var page = PageRequest.of(uid(), uid());
        var filter = newEmployeeFilter();
        doReturn(new GenericPage<>(List.of(EmployeeDto.builder()
                .id(id)
                .name(name)
                .build()), total)
        ).when(service).find(page, filter);

        mvc.perform(MockMvcRequestBuilders.get(EmployeeController.URL + "?page={page}&size={size}&name={name}",
                        page.getPageNumber(), page.getPageSize(), filter.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is((int) id)))
                .andExpect(jsonPath("$.data[0].name", is(name)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.total", is((int) total)));
    }

    EmployeeFilter newEmployeeFilter() {
        return EmployeeFilter.builder().name(uidS()).build();
    }
}