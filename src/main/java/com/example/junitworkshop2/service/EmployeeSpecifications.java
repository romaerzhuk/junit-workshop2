package com.example.junitworkshop2.service;

import com.example.junitworkshop2.controller.EmployeeFilter;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;

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
        return getByIdIn(filter.ids())
                .and(getByName(filter.name()))
                .and(getByMinStartDate(filter.minStartDate()))
                .and(getByMaxStartDate(filter.maxStartDate()));
    }

    @VisibleForTesting
    Specification<Employee> getByIdIn(Collection<Long> id) {
        return SpecificationUtils.spec(id, Employee_.id);
    }

    @VisibleForTesting
    Specification<Employee> getByName(String name) {
        return SpecificationUtils.spec(name, Employee_.name);
    }

    @VisibleForTesting
    Specification<Employee> getByMinStartDate(LocalDate date) {
        throw new UnsupportedOperationException();
    }

    @VisibleForTesting
    Specification<Employee> getByMaxStartDate(LocalDate date) {
        throw new UnsupportedOperationException();
    }
}
