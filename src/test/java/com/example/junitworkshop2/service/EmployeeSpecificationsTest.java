package com.example.junitworkshop2.service;

import static com.example.junitworkshop2.test.extension.UidExtension.uid;
import static com.example.junitworkshop2.test.extension.UidExtension.uidS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.example.junitworkshop2.controller.EmployeeFilter;
import com.example.junitworkshop2.test.extension.UidExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * EmployeeSpecificationsTest.
 *
 * @author Roman_Erzhukov
 */
@ExtendWith({
        MockitoExtension.class,
        UidExtension.class
})
@SuppressWarnings("unchecked")
class EmployeeSpecificationsTest {
    @Spy
    EmployeeSpecifications subj;

    @Test
    void getByFilter() {
        long id = uid();
        String name = uidS();
        var filter = EmployeeFilter.builder()
                .id(id)
                .name(name)
                .build();
        List<Specification<Employee>> specs = List.of(newSpecification(), newSpecification(), newSpecification());
        doReturn(specs.get(1)).when(subj).getById(id);
        doReturn(specs.get(2)).when(subj).getByName(name);
        doReturn(specs.get(0)).when(specs.get(1)).and(specs.get(2));

        Specification<Employee> actual = subj.getByFilter(filter);

        assertThat(actual).isEqualTo(specs.get(0));
    }

    Specification<Employee> newSpecification() {
        return mock(Specification.class, "spec" + uid());
    }
}