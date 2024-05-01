package com.example.junitworkshop2.service;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сотрудник.
 *
 * @author Roman_Erzhukov
 */
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Accessors(chain = true)
@Entity
public class Employee {
    /**
     * Идентификатор.
     */
    @ToString.Include
    @Id
    private Long id;

    /**
     * Имя сотрудника.
     */
    private String name;

    /**
     * Дата трудоустройства.
     */
    private LocalDate startDate;

    /**
     * Дата увольнения.
     */
    private LocalDate endDate;

    /**
     * Зарплата.
     */
    private BigDecimal salary;
}
