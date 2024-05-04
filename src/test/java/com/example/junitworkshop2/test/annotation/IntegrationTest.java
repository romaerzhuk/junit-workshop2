package com.example.junitworkshop2.test.annotation;

import com.example.junitworkshop2.test.extension.UidExtension;
import com.example.junitworkshop2.test.init.PostgresTestContainer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Интеграционный тест, {@link SpringBootTest}.
 *
 * @author Roman_Erzhukov
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(UidExtension.class)
@ContextConfiguration(initializers = PostgresTestContainer.Initializer.class)
public @interface IntegrationTest {
}