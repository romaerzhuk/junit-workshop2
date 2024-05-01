package com.example.junitworkshop2.test.init;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.utility.MountableFile;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * PostgresTestContainer.
 *
 * @author Roman_Erzhukov
 */
@Slf4j
public class PostgresTestContainer {
    private static final Consumer<OutputFrame> logConsumer = out ->
            LoggerFactory.getLogger(PostgresTestContainer.class).debug(StringUtils.stripEnd(out.getUtf8String(), "\n"));

    @SuppressWarnings("resource")
    public final static GenericContainer<?> postgres = new GenericContainer<>("postgres:15")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB", "postgres")
            .withEnv("POSTGRES_USER", "postgres")
            .withEnv("POSTGRES_PASSWORD", "admin")
            .withCopyFileToContainer(file("docker/dev/init/init.sql"), "/docker-entrypoint-initdb.d/init.sql")
            .withLogConsumer(logConsumer)
            .withTmpFs(Collections.singletonMap("/var/lib/postgresql/data", "rw"));

    private static boolean postgresEnabled() {
        return !Boolean.parseBoolean(System.getenv("POSTGRES_LOCAL"));
    }

    static {
        if (postgresEnabled()) {
            postgres.start();
            log.info("Postgres container started. Address: {}, port: {}", postgres.getHost(), postgres.getFirstMappedPort());
        }
    }

    /**
     * Programmatic initialization of the <code>application context</code>.
     */
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(springApplicationProperties()).applyTo(applicationContext);
        }

        private static String[] springApplicationProperties() {
            if (!postgresEnabled()) {
                return new String[0];
            }
            String jdbcUrl = String.format("jdbc:postgresql://%s:%d/postgres?currentSchema=junit",
                    postgres.getHost(), postgres.getFirstMappedPort());
            return new String[]{
                    "spring.datasource.url=" + jdbcUrl,
                    "spring.liquibase.url=" + jdbcUrl
            };
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static MountableFile file(String fileName) {
        ClassLoader classLoader = PostgresTestContainer.class.getClassLoader();
        URL resource = Objects.requireNonNull(classLoader.getResource("."));
        try {
            Path testClassesDir = Paths.get(resource.toURI());
            Path path = testClassesDir.resolve("../../").resolve(fileName);
            Files.createDirectories(testClassesDir.resolve(fileName).getParent());
            Files.copy(path, testClassesDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return MountableFile.forClasspathResource(fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
