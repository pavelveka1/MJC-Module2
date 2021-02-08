package com.epam.esm.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;


/**
 * DBConfig class for configure data source and jdbcTemplate as beans of Spring
 */
@Configuration
@ComponentScan(basePackages = "com.epam.esm")
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@EnableWebMvc
public class DBConfig {

    private Logger logger = Logger.getLogger(DBConfig.class);
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
            logger.error("Error while setting fields for dataSource");
        }
        return dataSource;
    }

    /**
     * Configure JdbcTemplate bean for 'prod' profile
     *
     * @return JdbcTemplate
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

    /**
     * Configure JdbcTemplate bean for 'dev' profile
     *
     * @return JdbcTemplate
     */
    @Bean
    @Profile("dev")
    public JdbcTemplate testJdbcTemplate() {
        return new JdbcTemplate(testDataSource());
    }

    @Bean
    @Profile("prod")
    public PlatformTransactionManager transactionManagerProd() throws IOException {
        return new HibernateTransactionManager(sessionFactory());
    }

    @Bean
    public SessionFactory sessionFactory() throws IOException {
        LocalSessionFactoryBean sessionFactoryBean=new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSourceProd());
        sessionFactoryBean.setPackagesToScan("com.epam.esm.entity");
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        sessionFactoryBean.afterPropertiesSet();
        return sessionFactoryBean.getObject();
    }

    private Properties hibernateProperties() {
        Properties hibernateProp = new Properties();
        hibernateProp.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        hibernateProp.put("hibernate.format_sql", true);
        hibernateProp.put("hibernate.use_sql_comments", true);
        hibernateProp.put("hibernate.show_sql", true);
        hibernateProp.put("hibernate.max_fetch_depth", 3);
        hibernateProp.put("hibernate.jdbc.batch_size", 10);
        hibernateProp.put("hibernate.jdbc.fetch_size", 50);
        return hibernateProp;

    }

    /**
     * Method is used for getting bean ModelMapper.
     *
     * @return ModelMapper what is used for convert dto class to entity class
     */
    @Bean
    @Profile("prod")
    public ModelMapper modelMapperProd() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }

}
