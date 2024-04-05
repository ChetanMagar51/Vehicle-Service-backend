package com.application.vehicleservicemanagement.configuration;

import com.application.vehicleservicemanagement.entity.Role;
import com.application.vehicleservicemanagement.util.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
              .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**", "/pdf/createInvoice", "/swagger-ui/**",
                                "/swagger-resources/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/service/**").hasAnyAuthority(Role.SERVICE_ADVISOR.name())
                        .requestMatchers("/user/**", "/item").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/vehicle/**").hasAnyAuthority(Role.ADMIN.name(), Role.SERVICE_ADVISOR.name())
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.loginPage("https://vehicle-service-management-theta.vercel.app/authentication/sign-in")
                        .loginProcessingUrl("https://vehicle-service-management.onrender.com/auth/login")
                        .successHandler(myAuthenticationSuccessHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
