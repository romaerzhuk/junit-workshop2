package com.example.junitworkshop2.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.StreamSupport;

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
        return join(specifications, CriteriaBuilder::and);
    }

    /**
     * Объединяет спецификации по ИЛИ.
     *
     * @param specifications спецификации поиска
     * @param <T>            тип сущности
     * @return {@link Specification}
     */
    public <T> Specification<T> or(Iterable<Specification<T>> specifications) {
        return join(specifications, CriteriaBuilder::or);
    }

    private static <T> Specification<T> join(Iterable<Specification<T>> specifications,
                                             BiFunction<CriteriaBuilder, Predicate[], Predicate> mapper) {
        return (root, query, builder) -> {
            Predicate[] predicates = StreamSupport.stream(specifications.spliterator(), false)
                    .filter(Objects::nonNull)
                    .map(s -> s.toPredicate(root, query, builder))
                    .filter(Objects::nonNull)
                    .toArray(Predicate[]::new);
            return predicates.length == 0 ? null : mapper.apply(builder, predicates);
        };
    }
}
