package com.example.junitworkshop2.service;

import com.example.junitworkshop2.controller.EmployeeDto;
import com.example.junitworkshop2.controller.EmployeeFilter;
import com.example.junitworkshop2.controller.GenericPage;
import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Сервис сотрудников.
 *
 * @author Roman_Erzhukov
 */
@RequiredArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    private final EmployeeSpecifications specifications;

    /**
     * Ищет сотрудников по параметрам фильтра.
     *
     * @param pageable параметры страницы поиска
     * @param filter   параметры фильтрации
     * @return страницу сотрудников
     */
    public GenericPage<EmployeeDto> find(Pageable pageable, EmployeeFilter filter) {
        Page<EmployeeDto> page = repository.findAll(specifications.findByFilter(filter), pageable)
                .map(this::mapToDto);
        return new GenericPage<>(page.getContent(), page.getTotalElements());
    }

    @VisibleForTesting
    EmployeeDto mapToDto(Employee entity) {
        return EmployeeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .salary(entity.getSalary())
                .build();
    }
}
