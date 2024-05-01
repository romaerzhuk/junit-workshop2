package com.example.junitworkshop2.service;

import static com.example.junitworkshop2.test.extension.UidExtension.uid;
import static com.example.junitworkshop2.test.extension.UidExtension.uidL;
import static com.example.junitworkshop2.test.extension.UidExtension.uidS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.example.junitworkshop2.controller.EmployeeDto;
import com.example.junitworkshop2.controller.EmployeeFilter;
import com.example.junitworkshop2.controller.GenericPage;
import com.example.junitworkshop2.test.extension.UidExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

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
        Employee[] value = {newValueEntity(), newValueEntity()};
        long total = uid();
        doReturn(new PageImpl<>(List.of(value), pageable, total)).when(repository).findAll(spec, pageable);
        EmployeeDto[] dto = {newEmployeeDto(), newEmployeeDto()};

        GenericPage<EmployeeDto> actual = subj.find(pageable, filter);

        assertEquals(total, actual.total());
        assertEquals(List.of(dto), actual.data());
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