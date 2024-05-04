package com.example.junitworkshop2.service;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
    private static final String ID_SEQ = "employee_id_seq";

    /**
     * Идентификатор.
     */
    @ToString.Include
    @SequenceGenerator(name = ID_SEQ, sequenceName = ID_SEQ, allocationSize = 100)
    @GeneratedValue(generator = ID_SEQ, strategy = GenerationType.SEQUENCE)
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
