package ru.netcracker.bikepackerserver.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.netcracker.bikepackerserver.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;
    @Autowired
    private final ObjectMapper objectMapper;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        BikepackerAuthenticationFilter authFilter = new BikepackerAuthenticationFilter(objectMapper);
        authFilter.setAuthenticationManager(authenticationManager());

        http
                .csrf().disable()
                .addFilterAt(
                        authFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/login", "/signup").permitAll()
                .antMatchers("/logout", "/user").hasAnyRole(ADMIN, USER)
                .antMatchers("/points", "/points/*", "/points/**", "/points/*/**").hasAnyRole(ADMIN, USER)
                .antMatchers("/logout", "/user","/friends","/users","/tracks","/favoritetracks","/image").hasAnyRole(ADMIN, USER)
                .antMatchers("/admin", "/users").hasRole(ADMIN)
                .anyRequest().authenticated()
                .and()
                .logout()
                .deleteCookies("JSESSIONID");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/swagger-ui/",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger/*");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
