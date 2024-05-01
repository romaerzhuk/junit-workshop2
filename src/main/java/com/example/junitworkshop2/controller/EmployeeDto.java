package com.example.junitworkshop2.controller;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сотрудник.
 *
 * @param id        идентификатор
 * @param name      имя сотрудника
 * @param startDate дата трудоустройства
 * @param endDate   дата увольнения
 * @param salary    зарплата
 * @author Roman_Erzhukov
 */
@Builder
public record EmployeeDto(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal salary) {
}
