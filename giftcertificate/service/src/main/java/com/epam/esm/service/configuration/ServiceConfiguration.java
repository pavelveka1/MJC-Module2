package com.epam.esm.service.configuration;

import com.epam.esm.dao.impl.GiftCertificateJDBCTemplate;
import com.epam.esm.dao.impl.TagJDBCTemplate;

import static org.mockito.Mockito.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;

import static org.mockito.Mockito.mock;

/**
 * @Class ServiceConfiguration is used for declare bean component MjdelMapper
 */
@Configuration
@ComponentScan("com.epam.esm")
public class ServiceConfiguration {

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
