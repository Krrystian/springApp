package com.dziekanat.springApp.config;


import com.dziekanat.springApp.filter.JwtAuthenticationFilter;
import com.dziekanat.springApp.service.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImp userDetailsServiceImp;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomLogoutHandler logoutHandler;

    public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomLogoutHandler logoutHandler
    ) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.logoutHandler = logoutHandler;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/login/**", "/register/**", "/refresh_token/**", "/user/getAll").permitAll()

                        .requestMatchers(HttpMethod.POST, "/student/**", "/group/**", "/classes/**").hasAnyAuthority("ADMIN","PRACOWNIK")
                        .requestMatchers(HttpMethod.POST, "/announcement/**", "/employee/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/grades/**").hasAuthority("PRACOWNIK")

                        .requestMatchers(HttpMethod.DELETE, "/student/**", "/announcement/**", "/employee/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/group/**", "/classes/**").hasAnyAuthority("ADMIN", "PRACOWNIK")
                        .requestMatchers(HttpMethod.DELETE, "/grades/**").hasAuthority("PRACOWNIK")

                        .requestMatchers(HttpMethod.PUT, "/announcement/**", "/employee/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/group/**", "/classes/**", "/grades/**").hasAnyAuthority("ADMIN", "PRACOWNIK")

                        .requestMatchers("/admin/**", "/grades", "/export/**", "/import/**").hasAuthority("ADMIN")
                        .requestMatchers("/grades/class/**", "/grades/student/**","/user/getAdmin").hasAnyAuthority("ADMIN", "PRACOWNIK")

                        .requestMatchers("/student/profile/grades").hasAuthority("STUDENT")
                        .requestMatchers("/student/**", "/announcement/**", "/group/**", "/classes/**", "/employee/**").hasAnyAuthority("STUDENT", "ADMIN", "PRACOWNIK")

                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsServiceImp)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(403))
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .logout(l -> l
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}