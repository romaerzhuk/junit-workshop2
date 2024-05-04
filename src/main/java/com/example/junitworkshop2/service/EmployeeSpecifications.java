package com.example.junitworkshop2.service;

import com.example.junitworkshop2.controller.EmployeeFilter;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Спецификации поиска сотрудников.
 *
 * @author Roman_Erzhukov
 */
@Component
public class EmployeeSpecifications {
    /**
     * Возвращает спецификацию поиска сотрудника по фильтру.
     *
     * @param filter фильтр поиска сотрудников
     * @return {@link Specification}
     */
    public Specification<Employee> getByFilter(EmployeeFilter filter) {
        throw new UnsupportedOperationException();
    }

    @VisibleForTesting
    Specification<Employee> getById(Long id) {
        throw new UnsupportedOperationException();
    }

    @VisibleForTesting
    Specification<Employee> getByName(String name) {
        throw new UnsupportedOperationException();
    }
}
