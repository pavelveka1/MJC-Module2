package com.epam.esm.configuration;

import com.epam.esm.constant.ControllerConstant;
import com.epam.esm.service.security.JwtConfigurer;
import com.epam.esm.service.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    private UserDetailsService userService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(ControllerConstant.LOGIN_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET, ControllerConstant.READ_TAG_ENDPOINT, ControllerConstant.READ_TAGS_ENDPOINT,
                        ControllerConstant.READ_CERTIFICATE_ENDPOINT, ControllerConstant.READ_CERTIFICATES_ENDPOINT)
                .permitAll()
                .antMatchers(HttpMethod.GET, ControllerConstant.READ_ENDPOINT, ControllerConstant.READ_USER)
                .hasRole(ControllerConstant.USER_ROLE)
                .antMatchers(HttpMethod.GET, ControllerConstant.READ_USERS, ControllerConstant.READ_TOP_TAG_USER,
                        ControllerConstant.ACTUATOR_ENDPOINT).hasRole(ControllerConstant.ADMIN_ROLE)
                .antMatchers(HttpMethod.POST, ControllerConstant.ORDER_ENDPOINT).hasRole(ControllerConstant.USER_ROLE)
                .antMatchers(HttpMethod.POST, ControllerConstant.ADMIN_ENDPOINT).hasRole(ControllerConstant.ADMIN_ROLE)
                .antMatchers(HttpMethod.DELETE, ControllerConstant.ADMIN_ENDPOINT).hasRole(ControllerConstant.ADMIN_ROLE)
                .antMatchers(HttpMethod.PATCH, ControllerConstant.ADMIN_ENDPOINT).hasRole(ControllerConstant.ADMIN_ROLE)
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder)
            throws Exception {
        builder.userDetailsService(userService);
    }

}
