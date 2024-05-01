package com.example.junitworkshop2;

import com.example.junitworkshop2.test.init.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = PostgresTestContainer.Initializer.class)
class JunitWorkshop2ApplicationIntegrationTest {
    @Test
    void contextLoads() {
    }
}
