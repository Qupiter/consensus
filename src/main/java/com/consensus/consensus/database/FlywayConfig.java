package com.consensus.consensus.database;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Manually wires Flyway because Spring Boot 4.0 does not yet include
 * Flyway auto-configuration in spring-boot-autoconfigure.
 *
 * The {@link BeanFactoryPostProcessor} ensures the JPA EntityManagerFactory
 * (which validates the schema) always depends on the flyway bean, so
 * migrations run before Hibernate's schema validation.
 */
@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();
    }

    /**
     * Forces the JPA EntityManagerFactory to depend on the flyway bean,
     * guaranteeing migrations run before Hibernate validates the schema.
     */
    @Bean
    public static BeanFactoryPostProcessor flywayJpaDependencyPostProcessor() {
        return beanFactory -> {
            for (String beanName : beanFactory.getBeanDefinitionNames()) {
                if (beanName.contains("entityManagerFactory") || beanName.contains("EntityManagerFactory")) {
                    beanFactory.getBeanDefinition(beanName).setDependsOn("flyway");
                }
            }
        };
    }
}
