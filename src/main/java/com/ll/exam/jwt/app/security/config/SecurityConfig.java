package com.ll.exam.jwt.app.security.config;

import com.ll.exam.jwt.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /* 인가 구분을 위한 url path 지정 */
/*
    private static final String[] AUTH_WHITELIST_STATIC = {
            "/css/**",
            "/fonts/**",
            "/image/**",
            "/images/**",
            "/img/**",
            "/js/**",
            "/scss",
            "/assets/**",
            "/error/**",
            "/new/**",
            "/manuals/**",
            "/sitemap.xml/**",
            "/robots.txt/**"
    }; // 정적 파일 인가 없이 모두 허용
    private static final String[] AUTH_ALL_LIST = {
            "/members/findPwd/**",
            "/members/changePwd/**",
            "/chat/**",
            "/singup/**",
            "/login/**",
            "/main/**",
            "/docs/**"
    }; // 모두 허용
    private static final String[] AUTH_ADMIN_LIST = {
            "/admin/**"
    }; // admin 롤 만 허용
    private static final String[] AUTH_AUTHENTICATED_LIST = {
            "/members/**",
            "/shadow/**",
            "/contact/**",
            "/admin/**",
            "/count/**"
    }; // 인가 필요
*/

/*
    private final MemberSecurityService customUserDetailsService;
    private final AuthenticationFailureHandler customFailureHandler;
*/

/*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
*/

/*
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return daoAuthenticationProvider;
    }
*/

/*
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new CustomSuccessHandler("/main");
    }
*/

    /*  스프링에서 보안상의 이슈로 ignoring() 을 권장하지 않음.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> web.ignoring().antMatchers(AUTH_WHITELIST_STATIC);
    }
    */
/*    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        http
                .requestMatchers((matchers) -> matchers.antMatchers(AUTH_WHITELIST_STATIC))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .requestCache().disable()
                .securityContext().disable()
                .sessionManagement().disable()
        ;
        return http.build();
    }*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
/*        http
                .cors().configurationSource(corsConfigurationSource());*/
        http
                .cors().disable();
        http
                .authorizeRequests().antMatchers("/**").permitAll();
//                .antMatchers(AUTH_ADMIN_LIST).hasRole("ADMIN") // 403 페이지 대신 alert 로 변경
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                .antMatchers(AUTH_ALL_LIST).permitAll()
//                .antMatchers(AUTH_AUTHENTICATED_LIST).authenticated();
        http
                .csrf()
                .disable();
        //.ignoringAntMatchers("/h2-console/**");
        http
                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .formLogin()
                .disable();
//                .successHandler(customSuccessHandler())
//                .failureHandler(customFailureHandler);
/*        http
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);*/
        http
                .exceptionHandling()
                .accessDeniedPage("/restrict");


        return http.build();
    }

/*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(CorsConfiguration.ALL));
        configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
        configuration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
*/

}