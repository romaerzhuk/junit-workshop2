package com.example.junitworkshop2.service;

import static com.example.junitworkshop2.test.extension.UidExtension.newLocalDate;
import static com.example.junitworkshop2.test.extension.UidExtension.uid;
import static com.example.junitworkshop2.test.extension.UidExtension.uidDec;
import static com.example.junitworkshop2.test.extension.UidExtension.uidL;
import static com.example.junitworkshop2.test.extension.UidExtension.uidS;
import static com.example.junitworkshop2.test.hamcrest.PropertiesMatcher.matching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.example.junitworkshop2.controller.EmployeeDto;
import com.example.junitworkshop2.controller.EmployeeFilter;
import com.example.junitworkshop2.controller.GenericPage;
import com.example.junitworkshop2.test.extension.UidExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Stream;

/**
 * EmployeeServiceTest.
 *
 * @author Roman_Erzhukov
 */
@ExtendWith({
        MockitoExtension.class,
        UidExtension.class
})
@SuppressWarnings("unchecked")
class EmployeeServiceTest {
    @Spy
    @InjectMocks
    EmployeeService subj;

    @Mock
    EmployeeRepository repository;

    @Mock
    EmployeeSpecifications specifications;

    @Test
    void find() {
        int number = uid();
        int size = uid();
        var pageable = PageRequest.of(number, size);
        var filter = newEmployeeFilter();
        Specification<Employee> spec = mock(Specification.class);
        doReturn(spec).when(specifications).findByFilter(filter);
        Employee[] employee = {newValueEntity(), newValueEntity()};
        long total = uid();
        doReturn(new PageImpl<>(List.of(employee), pageable, total)).when(repository).findAll(spec, pageable);
        EmployeeDto[] dto = {newEmployeeDto(), newEmployeeDto()};
        doReturn(dto[0]).when(subj).mapToDto(employee[0]);
        doReturn(dto[1]).when(subj).mapToDto(employee[1]);

        GenericPage<EmployeeDto> actual = subj.find(pageable, filter);

        // AssertJ assertSoftly выполняет множество assert-ов, даже если один из них упадёт.
        // AssertJ assertThat интуитивен. Пишем слева на право: пусть актуальное значение равно ожидаемому.
        assertSoftly(s -> {
            s.assertThat(actual.total()).describedAs("total").isEqualTo(total);
            s.assertThat(actual.data()).describedAs("data").isEqualTo(List.of(dto));
        });

        // JUnit5 assertAll: выполняет множество assert-ов, даже если один из них упадёт.
        assertAll(
                () -> assertThat(actual.total()).describedAs("total").isEqualTo(total),
                () -> assertThat(actual.data()).describedAs("data").isEqualTo(List.of(dto)));

        // Неудачный вариант:
        // 1. достаточно упасть первому assert-у, чтоб второй вовсе не отработал. Придётся запускать тест ещё раз.
        // 2. JUnit assertEquals(expected, actual) неинтуитивен. Пишем слева на право: пусть актуальное значение равно ожидаемому, тут наоборот.
        assertEquals(total, actual.total());
        assertEquals(List.of(dto), actual.data());
    }

    @ParameterizedTest
    @MethodSource("mapToDtoArguments")
    void mapToDto(Employee entity) {
        EmployeeDto actual = subj.mapToDto(entity);

        // Собственный PropertiesMatcher. Специально был разработан для сравнения множества полей JavaBean-ов.
        // Накапливает в List расхождения между сравниваемыми значениями.
        // Левый аргумент: "id", "name", "startDate", "endDate", "salary" - всего лишь подсказка.
        assertThat(actual).is(matching(matcher -> matcher
                .add("id", actual.id(), entity.getId())
                .add("name", actual.name(), "anyName") // тут специально ошибка, чтоб показать, как ведёт себя PropertiesMatcher при падении.
                .add("startDate", actual.startDate(), newLocalDate()) // тут специально ошибка.
                .add("endDate", actual.endDate(), entity.getEndDate())
                .add("salary", actual.salary(), entity.getSalary())
        ));
    }

    static Employee newEmployee() {
        return new Employee()
                .setId(uidL())
                .setName(uidS())
                .setStartDate(newLocalDate())
                .setEndDate(newLocalDate())
                .setSalary(uidDec());
    }

    static Stream<Employee> mapToDtoArguments() {
        return Stream.of(new Employee(), newEmployee());
    }

    EmployeeFilter newEmployeeFilter() {
        return new EmployeeFilter(uidL(), uidS());
    }

    Employee newValueEntity() {
        return new Employee().setId(uidL());
    }

    EmployeeDto newEmployeeDto() {
        return EmployeeDto.builder().id(uidL()).build();
    }
}