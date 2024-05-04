package com.example.junitworkshop2.controller;

import lombok.Builder;

import java.util.Set;

/**
 * Фильтр поиска сотрудников.
 *
 * @param ids  идентификаторы
 * @param name имя сотрудника
 * @author Roman_Erzhukov
 */
@Builder
public record EmployeeFilter(Set<Long> ids, String name) {
}
