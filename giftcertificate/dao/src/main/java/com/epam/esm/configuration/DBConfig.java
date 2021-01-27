package com.epam.esm.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * DBConfig class for configure data source and jdbcTemplate as beans of Spring
 */
@Configuration
@ComponentScan(basePackages = "com.epam.esm")
@PropertySource("classpath:db.properties")
@EnableWebMvc
public class DBConfig {

    /**
     * String field for driver class of DB
     */
    @Value("${db.driver}")
    private String DRIVER_CLASS;

    /**
     * String field for url DB
     */
    @Value("${db.url}")
    private String URL;

    /**
     * String field for user name of DB
     */
    @Value("${db.user}")
    private String USER_NAME;

    /**
     * String field for password for DB
     */
    @Value("${db.password}")
    private String PASSWORD;

    /**
     * Configure DataSource bean
     *
     * @return DataSource
     */
    @Bean
    @Profile("prod")
    public DataSource dataSourceProd() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(DRIVER_CLASS);
            dataSource.setJdbcUrl(URL);
            dataSource.setUser(USER_NAME);
            dataSource.setPassword(PASSWORD);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    /**
     * Configure JdbcTemplate bean
     *
     * @return
     */
    @Bean
    @Profile("prod")
    public JdbcTemplate jdbcTemplateProd() {
        return new JdbcTemplate(dataSourceProd());
    }

    @Bean
    @Profile("dev")
    public DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("test-schema.sql")
                .addScript("test-data.sql")
                .build();
    }

    @Bean
    @Profile("dev")
    public JdbcTemplate testJdbcTemplate() {
        return new JdbcTemplate(testDataSource());
    }
}
