package com.example.junitworkshop2.controller;

import lombok.Builder;

/**
 * Фильтр поиска сотрудников.
 *
 * @param id   идентификатор
 * @param name имя сотрудника
 * @author Roman_Erzhukov
 */
@Builder
public record EmployeeFilter(Long id, String name) {
}
