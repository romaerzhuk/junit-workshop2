package com.example.junitworkshop2.service;

import static com.example.junitworkshop2.test.extension.UidExtension.newLocalDate;
import static com.example.junitworkshop2.test.extension.UidExtension.uid;
import static com.example.junitworkshop2.test.extension.UidExtension.uidL;
import static com.example.junitworkshop2.test.extension.UidExtension.uidS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.example.junitworkshop2.controller.EmployeeFilter;
import com.example.junitworkshop2.test.extension.UidExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

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
    @InjectMocks
    EmployeeSpecifications subj;

    @Mock
    SpecificationHelper helper;

    @Test
    void getByFilter() {
        Set<Long> ids = Set.of(uidL());
        String name = uidS();
        var minStartDate = newLocalDate();
        var maxStartDate = newLocalDate();
        var filter = EmployeeFilter.builder()
                .ids(ids)
                .name(name)
                .minStartDate(minStartDate)
                .maxStartDate(maxStartDate)
                .build();
        List<Specification<Employee>> specs = IntStream.range(0, 5)
                .mapToObj(i -> newSpecification())
                .toList();
        doReturn(specs.get(1)).when(subj).getByIdIn(ids);
        doReturn(specs.get(2)).when(subj).getByName(name);
        doReturn(specs.get(3)).when(subj).getByMinStartDate(minStartDate);
        doReturn(specs.get(4)).when(subj).getByMaxStartDate(maxStartDate);
        doReturn(specs.get(0)).when(helper).and(specs.subList(1, specs.size()));

        Specification<Employee> actual = subj.getByFilter(filter);

        assertThat(actual).isEqualTo(specs.get(0));
    }

    Specification<Employee> newSpecification() {
        return mock(Specification.class, "spec" + uid());
    }
}