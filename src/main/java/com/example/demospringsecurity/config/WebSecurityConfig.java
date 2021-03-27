package com.example.demospringsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.function.Function;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/actuator/*")
                .permitAll()
                .antMatchers("/greeting/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/greeting/user/**").hasAuthority("ROLE_USER")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        Function<String, String> encoderFunction = str -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.encode(str);
        };

        UserDetails userUser = User.builder()
                .username("user")
                .password("secret")
                .authorities("ROLE_USER")
                .passwordEncoder(encoderFunction)
                .build();
        UserDetails userAdmin = User.builder()
                .username("admin")
                .password("secret")
                .authorities("ROLE_ADMIN")
                .passwordEncoder(encoderFunction)
                .build();
        return new InMemoryUserDetailsManager(userUser, userAdmin);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
