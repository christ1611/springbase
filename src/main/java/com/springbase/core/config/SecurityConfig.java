package com.springbase.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbase.core.context.RequestWrapperFilter;
import com.springbase.core.security.filter.AuthenticationLoginFilter;
import com.springbase.core.security.filter.AuthenticationTokenFilter;
import com.springbase.core.security.handler.AuthEntryPointHandler;
import com.springbase.core.security.matcher.SkipPathRequestMatcher;
import com.springbase.core.security.provider.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final String[] PERMIT_ALL_URL = new String[]{"/", "/favicon.ico", "/html/**", "/actuator/**"
            , "/swagger/**", "/swagger-ui/**", "/h2-console/**", "/v2/api-docs", "/webjars/**", "/v3/**", "/api/**", "/api/core/resetPass"};
    private final String FORM_BASED_LOGIN_ENTRY_POINT   = "/api/core/login";
    private final String FORM_BASED_LOGOUT_END_POINT    = "/HANABANK/V1/LOGOUT.SVC";
    private final String TOKEN_BASED_AUTH_ENTRY_POINT   = "/api/**/**";

    private final AuthEntryPointHandler authEntryPointHandler;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final ObjectMapper objectMapper;

    private final AuthProvider authProvider;
    private final UserDetailsService userDetailsService;
    private final ObjectPostProcessor<Object> objectPostProcessor;
    private final RequestWrapperFilter requestWrapperFilter;


    /**
     *  @MethodName: filterChain
     *  @Author : handabin
     *  @Date : 2024-09-06
     *  @Param : httpSecurity
     *  @Description : 사용하고자 하는 filter를 필터체인에 등록
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(exceptionHandler -> exceptionHandler.authenticationEntryPoint(authEntryPointHandler));
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(request -> request.requestMatchers(PERMIT_ALL_URL).permitAll().anyRequest().authenticated());
        http.addFilterBefore(buildAuthenticationLoginFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(buildAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(requestWrapperFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    protected AuthenticationLoginFilter buildAuthenticationLoginFilter() throws Exception {
        List<String> path = Collections.singletonList(FORM_BASED_LOGIN_ENTRY_POINT);
        List<RequestMatcher> m = path.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
        OrRequestMatcher matchers = new OrRequestMatcher(m);
        AuthenticationLoginFilter filter = new AuthenticationLoginFilter(matchers, successHandler, failureHandler, objectMapper);
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
        filter.setAuthenticationManager(authenticationManager(builder));
        return filter;
    }


    protected AuthenticationTokenFilter buildAuthenticationTokenFilter() throws Exception {
        List<String> pathsToSkip = Arrays.asList(FORM_BASED_LOGIN_ENTRY_POINT, "/api/core/resetPass");
        List<String> path = Arrays.asList(TOKEN_BASED_AUTH_ENTRY_POINT, FORM_BASED_LOGOUT_END_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(HttpMethod.POST, pathsToSkip, path);
        AuthenticationTokenFilter filter = new AuthenticationTokenFilter(matcher, failureHandler);
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
        filter.setAuthenticationManager(authenticationManager(builder));
        return filter;
    }

    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        return auth.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     *  @MethodName: corsConfigurationSource
     *  @Author : handabin
     *  @Date : 2024-09-06
     *  @Description : CORS 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // 쿠키/세션 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}


