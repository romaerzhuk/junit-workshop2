package com.example.junitworkshop2.test;

import com.example.junitworkshop2.service.EmployeeService;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;

/**
 * TestConfiguration.
 *
 * @author Roman_Erzhukov
 */
@Configuration
public class TestConfiguration {
    @SpyBean
    EmployeeService employeeService;
}
