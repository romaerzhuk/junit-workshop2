package com.example.junitworkshop2.service;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.junitworkshop2.test.annotation.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * SpecificationHelperIntegrationTest.
 *
 * @author Roman_Erzhukov
 */
@IntegrationTest
class SpecificationHelperIntegrationTest {
    @Autowired
    SpecificationHelper subj;

    @Autowired
    EmployeeRepository repository;

    @Autowired
    TransactionOperations transactionOperations;

    @BeforeEach
    @AfterTestClass
    void deleteAll() {
        repository.deleteAllInBatch();
    }

    @ParameterizedTest
    @MethodSource("booleans2")
    void and(boolean firstEmpty, boolean secondEmpty) {
        Long[] id = transactionExecute(() -> IntStream.range(0, 6)
                .mapToObj(i -> newEntity().getId())
                .toArray(Long[]::new));
        List<Long> first = firstEmpty ? List.of(id) : List.of(id[1], id[2], id[3]);
        List<Long> second = secondEmpty ? List.of(id) : List.of(id[2], id[3], id[5]);
        Long[] expected = Stream.of(id)
                .filter(i -> first.contains(i) && second.contains(i))
                .toArray(Long[]::new);
        Specification<Employee> specification = subj.and(asList(findByIds(first), null, findByIds(null), findByIds(second)));

        List<Employee> actual = repository.findAll(specification);

        assertThat(actual)
                .extracting(Employee::getId)
                .containsExactlyInAnyOrder(expected);
    }

    @ParameterizedTest
    @MethodSource("booleans2")
    void or(boolean firstEmpty, boolean secondEmpty) {
        Long[] id = transactionExecute(() -> IntStream.range(0, 6)
                .mapToObj(i -> newEntity().getId())
                .toArray(Long[]::new));
        List<Long> first = firstEmpty ? List.of() : List.of(id[1], id[2], id[3]);
        List<Long> second = secondEmpty ? List.of() : List.of(id[2], id[3], id[5]);
        Long[] expected = Stream.of(id)
                .filter(i -> first.contains(i) || second.contains(i))
                .toArray(Long[]::new);
        Specification<Employee> specification = subj.or(asList(findByIds(first), null, findByIds(null), findByIds(second)));

        List<Employee> actual = repository.findAll(specification);

        assertThat(actual)
                .extracting(Employee::getId)
                .containsExactlyInAnyOrder(expected);
    }

    Specification<Employee> findByIds(List<Long> ids) {
        return (root, query, cb) ->
                ids == null ? null : root.get(Employee_.id).in(ids);
    }

    @MethodSource
    static Stream<Arguments> booleans2() {
        return Stream.of(false, true)
                .flatMap(a -> Stream.of(false, true)
                        .map(b -> Arguments.of(a, b)));
    }

    Employee newEntity() {
        return repository.save(new Employee());
    }

    <T> T transactionExecute(Supplier<T> callback) {
        return transactionOperations.execute(status -> callback.get());
    }
}