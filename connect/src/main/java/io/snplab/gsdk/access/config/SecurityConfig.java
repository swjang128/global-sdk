package io.snplab.gsdk.access.config;

import io.snplab.gsdk.access.handler.AccountAccessDeniedHandler;
import io.snplab.gsdk.access.handler.AccountAuthenticationProvider;
import io.snplab.gsdk.access.handler.AccountAuthenticationFailureHandler;
import io.snplab.gsdk.access.handler.AccountAuthenticationSuccessHandler;
import io.snplab.gsdk.access.service.AccountUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AccountAuthenticationProvider accountAuthenticationProvider;
    private final AccountAuthenticationSuccessHandler accountAuthenticationSuccessHandler;
    private final AccountAuthenticationFailureHandler accountAuthenticationFailureHandler;
    private final AccountUserDetailsService accountUserDetailsService;
    private final AccountAccessDeniedHandler accountAccessDeniedHandler;
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/pages/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(accountAuthenticationProvider)
                .userDetailsService(accountUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/login/**", "/password/**", "/api/dev/**").permitAll()
                    .antMatchers("/admin/**").hasRole("NORMAL")
                    .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("enc-username")
                    .passwordParameter("enc-password")
                    .loginProcessingUrl("/login")
                    .successHandler(accountAuthenticationSuccessHandler)
                    .failureHandler(accountAuthenticationFailureHandler)
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(accountAccessDeniedHandler)
                    .and()
                .sessionManagement()
                    .invalidSessionUrl("/Login?invalidSession")
                    .maximumSessions(1)
                    .expiredUrl("/Login?expired")
                        .and()
                    .and()
                .rememberMe()
                    .userDetailsService(accountUserDetailsService)
                    .tokenRepository(this.getTokenRepository())
                    .rememberMeParameter("auto-login")
                    .tokenValiditySeconds(60 * 60 * 24 * 7)
                    .and()
                .csrf()
                    .disable()
                .cors()
                    .and()
                .headers()
                    .frameOptions()
                        .sameOrigin();
    }

    private PersistentTokenRepository getTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        return jdbcTokenRepository;
    }
}
