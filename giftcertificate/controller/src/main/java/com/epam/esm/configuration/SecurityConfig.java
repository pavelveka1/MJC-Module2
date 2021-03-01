package com.epam.esm.configuration;

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
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@EnableConfigurationProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT = "/api/**";
    private static final String LOGIN_ENDPOINT = "/api/auth/**";
    private static final String ORDER_ENDPOINT = "/api/orders";
    private static final String READ_ENDPOINT = "/api/**";
    private static final String READ_TAGS_ENDPOINT = "/api/tags";
    private static final String READ_TAG_ENDPOINT = "/api/tags/*";
    private static final String READ_TOP_TAG_USER="/api/tags/toptag/**";
    private static final String READ_USERS="/api/users";
    private static final String READ_USER="/api/users/*";
    private static final String READ_CERTIFICATES_ENDPOINT = "/api/certificates";
    private static final String READ_CERTIFICATE_ENDPOINT = "/api/certificates/**";
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";

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
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET, READ_TAG_ENDPOINT, READ_TAGS_ENDPOINT, READ_CERTIFICATE_ENDPOINT,
                        READ_CERTIFICATES_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET, READ_ENDPOINT, READ_TOP_TAG_USER).hasRole(USER_ROLE)
                .antMatchers(HttpMethod.GET, READ_USER, READ_USERS).hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.POST, ORDER_ENDPOINT).hasRole(USER_ROLE)
                .antMatchers(HttpMethod.POST, ADMIN_ENDPOINT).hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.DELETE, ADMIN_ENDPOINT).hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.PATCH, ADMIN_ENDPOINT).hasRole(ADMIN_ROLE)
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
