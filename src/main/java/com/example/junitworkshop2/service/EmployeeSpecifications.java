package com.example.junitworkshop2.service;

import com.example.junitworkshop2.controller.EmployeeFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Спецификации поиска сотрудников.
 *
 * @author Roman_Erzhukov
 */
@Component
public class EmployeeSpecifications {
    Specification<Employee> findByFilter(EmployeeFilter filter) {
        throw new UnsupportedOperationException();
    }
}
