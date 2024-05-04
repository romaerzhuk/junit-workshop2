package com.example.junitworkshop2.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Обёртывает типовые простейшие static-методы {@link Specification} для тестирования на моках.
 *
 * @author Roman_Erzhukov
 */
@Component
public class SpecificationHelper {
    /**
     * Объединяет спецификации по И.
     *
     * @param specifications спецификации поиска
     * @param <T>            тип сущности
     * @return {@link Specification}
     */
    public <T> Specification<T> and(Iterable<Specification<T>> specifications) {
        throw new UnsupportedOperationException();
    }

    /**
     * Объединяет спецификации по ИЛИ.
     *
     * @param specifications спецификации поиска
     * @param <T>            тип сущности
     * @return {@link Specification}
     */
    public <T> Specification<T> or(Iterable<Specification<T>> specifications) {
        throw new UnsupportedOperationException();
    }
}
