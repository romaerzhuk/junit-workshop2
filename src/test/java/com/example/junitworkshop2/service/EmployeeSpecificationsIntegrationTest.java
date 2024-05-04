package com.example.junitworkshop2.service;

import static com.example.junitworkshop2.test.extension.UidExtension.newLocalDate;
import static com.example.junitworkshop2.test.extension.UidExtension.uid;
import static com.example.junitworkshop2.test.extension.UidExtension.uidS;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.junitworkshop2.test.annotation.IntegrationTest;
import com.example.junitworkshop2.test.junit.MethodSourceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
class EmployeeSpecificationsIntegrationTest implements MethodSourceHelper {
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

    @ParameterizedTest
    @MethodSource("booleans")
    void getByName(boolean hasName) {
        String[] name = {uidS(), uidS(), uidS()};
        int index = uid(name.length);
        Long[] id = transactionExecute(() -> Stream.of(
                        newEmployee(it -> it.setName(name[0])),
                        newEmployee(it -> it.setName(name[1])),
                        newEmployee(it -> it.setName(name[2])),
                        newEmployee(it -> it.setName(name[index])),
                        newEmployee(it -> it.setName(null))
                ).map(Employee::getId)
                .toArray(Long[]::new));
        List<Long> expected = hasName ? List.of(id[index], id[3]) : List.of(id);

        List<Employee> actual = repository.findAll(subj.getByName(hasName ? name[index] : null));

        assertThat(actual)
                .extracting(Employee::getId)
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @ParameterizedTest
    @MethodSource("booleans")
    void getByMinStartDate(boolean hasDate) {
        var date = newLocalDate();
        Long[] id = transactionExecute(() -> Stream.of(
                        newEmployee(it -> it.setStartDate(date.minusDays(uid()))), // 0
                        newEmployee(it -> it.setStartDate(date.minusDays(1))), // 1
                        newEmployee(it -> it.setStartDate(date)), // 2
                        newEmployee(it -> it.setStartDate(date.plusDays(1))), // 3
                        newEmployee(it -> it.setStartDate(date.plusDays(uid()))), // 4
                        newEmployee(it -> it.setStartDate(null)), // 5
                        newEmployee(it -> it.setStartDate(date)) // 6
                ).map(Employee::getId)
                .toArray(Long[]::new));
        List<Long> expected = hasDate ? List.of(id[2], id[3], id[4], id[6]) : List.of(id);

        List<Employee> actual = repository.findAll(subj.getByMinStartDate(hasDate ? date : null));

        assertThat(actual)
                .extracting(Employee::getId)
                .containsExactlyElementsOf(expected);
    }

    @ParameterizedTest
    @MethodSource("booleans")
    void getByMaxStartDate(boolean hasDate) {
        var date = newLocalDate();
        Long[] id = transactionExecute(() -> Stream.of(
                        newEmployee(it -> it.setStartDate(date.minusDays(uid()))), // 0
                        newEmployee(it -> it.setStartDate(date.minusDays(1))), // 1
                        newEmployee(it -> it.setStartDate(date)), // 2
                        newEmployee(it -> it.setStartDate(date.plusDays(1))), // 3
                        newEmployee(it -> it.setStartDate(date.plusDays(uid()))), // 4
                        newEmployee(it -> it.setStartDate(null)), // 5
                        newEmployee(it -> it.setStartDate(date)) // 6
                ).map(Employee::getId)
                .toArray(Long[]::new));
        List<Long> expected = hasDate ? List.of(id[0], id[1], id[2], id[6]) : List.of(id);

        List<Employee> actual = repository.findAll(subj.getByMaxStartDate(hasDate ? date : null));

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
