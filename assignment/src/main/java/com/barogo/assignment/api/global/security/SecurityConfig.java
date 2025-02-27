package com.barogo.assignment.api.global.security;

import com.barogo.assignment.api.global.exception.CommonException;
import com.barogo.assignment.api.global.security.jwt.AuthorityRoleType;
import com.barogo.assignment.api.global.security.jwt.JwtTokenAuthenticationFilter;
import com.barogo.assignment.api.global.security.jwt.JwtTokenProvider;
import com.barogo.assignment.api.global.security.provider.CustomAuthenticationProvider;
import com.barogo.assignment.api.global.security.service.CustomUserDetailsService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtTokenProvider jwtTokenProvider;

    @Resource
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter() {
        return new JwtTokenAuthenticationFilter(jwtTokenProvider, handlerExceptionResolver);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(customUserDetailsService, getPasswordEncoder());
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())  // CORS 설정
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()  // 로그인, 회원가입 등의 엔드포인트 허용
                        .requestMatchers("/api/order/**").hasAuthority(AuthorityRoleType.ORDER_USER.getAuthority())
                        .anyRequest().authenticated()  // 나머지는 JWT 필요
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((r, s, e) -> {throw new CommonException(e.getMessage(), HttpStatus.UNAUTHORIZED);}) // 401 JSON 응답 설정
                        .accessDeniedHandler((r, s, e) -> {throw new CommonException(e.getMessage(), HttpStatus.UNAUTHORIZED);}) // 403 JSON 응답 설정
                )
                .addFilterBefore(jwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //h2-console은 Security filter X
        return web -> web.ignoring().requestMatchers(
                "/h2-console/**"
        );
    }

}
