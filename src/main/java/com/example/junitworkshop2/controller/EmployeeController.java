package com.example.junitworkshop2.controller;

import com.example.junitworkshop2.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Сотрудники.
 *
 * @author Roman_Erzhukov
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(EmployeeController.URL)
public class EmployeeController {
    public static final String URL = "/employees";

    private final EmployeeService service;

    /**
     * Возвращает сотрудников по фильтру.
     *
     * @param filter   фильтр поиска сотрудников
     * @param pageable параметры страницы
     * @return страницу сотрудников
     */
    @GetMapping
    public GenericPage<EmployeeDto> getPage(EmployeeFilter filter, Pageable pageable) {
        return service.find(pageable, filter);
    }
}
