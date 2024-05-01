package com.example.junitworkshop2.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Сотрудники.
 *
 * @author Roman_Erzhukov
 */
@RestController
@RequestMapping(EmployeeController.URL)
public class EmployeeController {
    public static final String URL = "/employees";

    /**
     * Возвращает сотрудников по фильтру.
     *
     * @param filter   фильтр поиска сотрудников
     * @param pageable параметры страницы
     * @return страницу сотрудников
     */
    @GetMapping
    public GenericPage<EmployeeDto> getPage(EmployeeFilter filter, Pageable pageable) {
        return new GenericPage<>(List.of(EmployeeDto.builder()
                .id(1L)
                .name("simple-name2")
                .build()), 3);
    }
}
