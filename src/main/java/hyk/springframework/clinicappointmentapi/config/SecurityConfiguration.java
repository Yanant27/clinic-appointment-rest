package hyk.springframework.clinicappointmentapi.config;

import hyk.springframework.clinicappointmentapi.security.filter.JWTTokenGeneratorFilter;
import hyk.springframework.clinicappointmentapi.security.filter.JWTTokenValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final Environment environment;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(new JWTTokenGeneratorFilter(environment), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(environment), BasicAuthenticationFilter.class)
//                .addFilterBefore(new ExceptionHandlerFilter(), JWTTokenValidatorFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize.antMatchers("/h2-console/**", "/register", "/authenticate").permitAll()
                            .antMatchers(HttpMethod.POST, "/api/v1/patients").permitAll();
                })
                .authorizeHttpRequests().anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // prevent to generate JSESSIONID
                .and().formLogin()
                .and().httpBasic();
        // h2-console config, h2 use frame and spring security blocks frame
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }
}
