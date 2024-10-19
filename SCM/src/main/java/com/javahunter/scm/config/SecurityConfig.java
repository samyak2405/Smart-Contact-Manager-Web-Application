package com.javahunter.scm.config;

import com.javahunter.scm.services.impl.SecurityUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
class SecurityConfig{
    String[] authenticate = new String[]{
            "/user/**"
    };
    private final AuthFailureHandler authFailureHandler;
    private final SecurityUserDetailsServiceImpl securityUserDetailsService;
    private final OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(securityUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //configuration for public and protected url
        http.authorizeHttpRequests(authorize->{
            authorize.requestMatchers("/auth/verify-email").permitAll();
            authorize.requestMatchers(authenticate).authenticated();
            authorize.anyRequest().permitAll();
        });

        //If anything we want to change related to form login
        http.formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/dashboard");
            formLogin.failureUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

//                    .failureHandler(new AuthenticationFailureHandler() {
//                        @Override
//                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//
//                        }
//                    })
//                    .successHandler(new AuthenticationSuccessHandler() {
//                        @Override
//                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//
//                        }
//                    });
//            formLogin.failureUrl(authFailureHandler);

            //OAUTH Configurations
            formLogin.failureHandler(authFailureHandler);
        });
        http.oauth2Login(oauth2Login -> {
            oauth2Login.loginPage("/login")
                    .successHandler(oAuthAuthenticationSuccessHandler);
        });
        http.csrf(AbstractHttpConfigurer::disable);
        http.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/login?logout=true");
        });
        return http.build();
        }
}
