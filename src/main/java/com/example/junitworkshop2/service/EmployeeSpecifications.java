package com.example.junitworkshop2.service;

import com.example.junitworkshop2.controller.EmployeeFilter;
import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Спецификации поиска сотрудников.
 *
 * @author Roman_Erzhukov
 */
@RequiredArgsConstructor
@Component
public class EmployeeSpecifications {
    private final SpecificationHelper helper;

    /**
     * Возвращает спецификацию поиска сотрудника по фильтру.
     *
     * @param filter фильтр поиска сотрудников
     * @return {@link Specification}
     */
    public Specification<Employee> getByFilter(EmployeeFilter filter) {
        return helper.and(List.of(
                getByIdIn(filter.ids()),
                getByName(filter.name()),
                getByMinStartDate(filter.minStartDate()),
                getByMaxStartDate(filter.maxStartDate())));
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
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get(Employee_.startDate), date);
        };
    }

    @VisibleForTesting
    Specification<Employee> getByMaxStartDate(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get(Employee_.startDate), date);
        };
    }
}
