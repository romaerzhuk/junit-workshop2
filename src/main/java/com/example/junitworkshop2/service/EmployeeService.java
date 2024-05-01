package com.example.junitworkshop2.service;

import com.example.junitworkshop2.controller.EmployeeDto;
import com.example.junitworkshop2.controller.EmployeeFilter;
import com.example.junitworkshop2.controller.GenericPage;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Сервис сотрудников.
 *
 * @author Roman_Erzhukov
 */
@Service
public class EmployeeService {
    /**
     * Ищет сотрудников по параметрам фильтра.
     *
     * @param pageable параметры страницы поиска
     * @param filter   параметры фильтрации
     * @return страницу сотрудников
     */
    public GenericPage<EmployeeDto> find(Pageable pageable, EmployeeFilter filter) {
        throw new UnsupportedOperationException();
    }
}
