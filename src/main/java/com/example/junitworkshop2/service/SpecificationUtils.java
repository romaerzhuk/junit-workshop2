package com.example.junitworkshop2.service;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.function.Function;

/**
 * Набор вспомогательных методов для спецификаций.
 *
 * <p>Тестируются только совместно с вызывающими методами.</p>
 *
 * @author Roman_Erzhukov
 */
public class SpecificationUtils {
    public static <T> Specification<T> spec(Object value, Specification<T> spec) {
        return empty(value) ? anySpec() : spec;
    }

    public static <T, V> Specification<T> spec(V value, Function<V, Predicate> function) {
        return (root, query, cb) ->
                value == null ? null : function.apply(value);
    }

    public static <T, V> Specification<T> spec(V value, SingularAttribute<?, ?> attribute, SingularAttribute<?, ?>... attributes) {
        return spec(value, (root, query, cb) -> {
            Path<V> path = path(root, attribute, attributes);
            return value instanceof Collection
                    ? path.in((Collection<?>) value)
                    : cb.equal(path, value);
        });
    }

    public static <T, Z, X, V> Specification<T> spec(V value,
                                                     From<Z, X> from,
                                                     SingularAttribute<?, ?> attribute,
                                                     SingularAttribute<?, ?>... attributes) {
        return spec(value, (root, query, cb) -> {
            Path<X> path = path(from, attribute, attributes);
            return value instanceof Collection
                    ? path.in((Collection<?>) value)
                    : cb.equal(path, value);
        });
    }

    public static <E> Specification<E> anySpec() {
        return (r, cq, cb) -> null;
    }

    public static boolean empty(Object value) {
        return value == null || value instanceof Collection && ((Collection<?>) value).isEmpty();
    }

    @SuppressWarnings("unchecked")
    private static <T, Z, V> Path<V> path(From<T, Z> root, SingularAttribute<?, ?> attribute, SingularAttribute<?, ?>... attributes) {
        Path<?> path = root.get(attribute.getName());
        for (SingularAttribute<?, ?> k : attributes) {
            path = path.get(k.getName());
        }
        return (Path<V>) path;
    }
}
