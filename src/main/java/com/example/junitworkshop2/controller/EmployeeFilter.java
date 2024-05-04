package com.example.junitworkshop2.controller;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

/**
 * Фильтр поиска сотрудников.
 *
 * @param ids          идентификаторы
 * @param name         имя сотрудника
 * @param minStartDate минимальная дата трудоустройства
 * @param maxStartDate максимальная дата трудоустройства
 * @author Roman_Erzhukov
 */
@Builder
public record EmployeeFilter(
        Set<Long> ids,
        String name,
        LocalDate minStartDate,
        LocalDate maxStartDate) {
}
