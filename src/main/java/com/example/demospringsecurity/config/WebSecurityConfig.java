package com.example.demospringsecurity.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

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
        PreAuthHeaderTokenFilter filter = new PreAuthHeaderTokenFilter("User-ID");
        filter.setAuthenticationManager(new AuthenticationManager()
        {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String principal = (String) authentication.getPrincipal();
                if (!"User-ID".equals(principal)) {
                    throw new BadCredentialsException("The API key was not found or not the expected value.");
                }
                authentication.setAuthenticated(true);
                return authentication;
            }
        });

        http.authorizeRequests()
                .antMatchers("/actuator/*")
                .permitAll()
                .and()
                .addFilter(filter)
            .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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

        UserDetails userDetails = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }


    //Beans for pre-authentication

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManager() {
//        return new ProviderManager(preauthProvider());
//    }
//
//    @Bean
//    PreAuthenticatedAuthenticationProvider preauthProvider() {
//        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
//        authenticationProvider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
//        return authenticationProvider;
//    }
//
//    private UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {
//        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetails = new UserDetailsByNameServiceWrapper();
//        userDetails.setUserDetailsService(userDetailsService());
//        return userDetails;
//    }


}
