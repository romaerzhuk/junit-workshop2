package com.example.junitworkshop2.service;

import static com.example.junitworkshop2.test.extension.UidExtension.uid;
import static com.example.junitworkshop2.test.extension.UidExtension.uidS;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.junitworkshop2.test.annotation.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * EmployeeSpecificationsIntegrationTest.
 *
 * @author Roman_Erzhukov
 */
@IntegrationTest
class EmployeeSpecificationsIntegrationTest {
    @Autowired
    EmployeeSpecifications subj;

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
    @MethodSource("booleansWithNull")
    void getByIdIn(Boolean empty) {
        Long[] id = transactionExecute(() -> Stream.of(
                        newEmployee(),
                        newEmployee(),
                        newEmployee(),
                        newEmployee()
                ).map(Employee::getId)
                .toArray(Long[]::new));
        int[] index = {uid(id.length), uid(id.length)};
        List<Long> expected = Boolean.FALSE.equals(empty) ? List.of(id[index[0]], id[index[1]]) : List.of(id);
        Set<Long> ids;
        if (empty == null) {
            ids = null;
        } else {
            ids = empty ? Set.of() : Set.of(id[0] - uid(), id[index[0]], id[index[1]]);
        }

        List<Employee> actual = repository.findAll(subj.getByIdIn(ids));

        assertThat(actual)
                .extracting(Employee::getId)
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    static Stream<Boolean> booleansWithNull() {
        return Stream.of(null, false, true);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void getByName(boolean hasName) {
        String[] name = {uidS(), uidS(), uidS()};
        Long[] id = transactionExecute(() -> Stream.of(
                        newEmployee(it -> it.setName(name[0])),
                        newEmployee(it -> it.setName(name[1])),
                        newEmployee(it -> it.setName(name[2])),
                        newEmployee(it -> it.setName(null))
                ).map(Employee::getId)
                .toArray(Long[]::new));
        int index = uid(name.length);
        List<Long> expected = hasName ? List.of(id[index]) : List.of(id);

        List<Employee> actual = repository.findAll(subj.getByName(hasName ? name[index] : null));

        assertThat(actual)
                .extracting(Employee::getId)
                .containsExactlyElementsOf(expected);
    }

    Employee newEmployee() {
        return newEmployee(it -> it.setName(uidS()));
    }

    Employee newEmployee(Consumer<Employee> consumer) {
        var entity = new Employee();
        consumer.accept(entity);
        return repository.save(entity);
    }

    <T> T transactionExecute(Supplier<T> callback) {
        return transactionOperations.execute(status -> callback.get());
    }
}
