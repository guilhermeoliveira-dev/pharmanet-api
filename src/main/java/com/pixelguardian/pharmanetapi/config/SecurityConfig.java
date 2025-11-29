package com.pixelguardian.pharmanetapi.config;

import com.pixelguardian.pharmanetapi.security.JwtAuthFilter;
import com.pixelguardian.pharmanetapi.security.JwtService;
import com.pixelguardian.pharmanetapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        // Permite o seu front-end (MUITO IMPORTANTE)
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // Métodos permitidos, incluindo OPTIONS (crucial para pre-flight)
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Cabeçalhos permitidos (incluindo o Authorization)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a todas as rotas

        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()

                .antMatchers("/api/v1/usuarios/auth/**")
                .permitAll()

                .antMatchers("/api/v1/cargos/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/categorias/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/clientes/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/enderecos/**")
                .hasAnyRole("ADMIN", "USER")

                .antMatchers("/api/v1/estoques/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/farmacias/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/feedbacks/**")
                .hasAnyRole("ADMIN", "USER")

                .antMatchers("/api/v1/fornecedores/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/funcionarios/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/itemsPedidos/**")
                .hasAnyRole("ADMIN", "USER")

                .antMatchers("/api/v1/notificacoes/**")
                .hasAnyRole("ADMIN", "USER")

                .antMatchers("/api/v1/pagamentos/**")
                .hasAnyRole("ADMIN", "USER")

                .antMatchers("/api/v1/pedidoCompras/**")
                .hasAnyRole("ADMIN", "USER")

                .antMatchers(HttpMethod.GET,"/api/v1/estoques/**")
                .permitAll()

                .antMatchers(HttpMethod.GET,"/api/v1/produtos/**")
                .permitAll()

                .antMatchers("/api/v1/produtos/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/receitas/**")
                .hasAnyRole("ADMIN", "USER")

                .antMatchers("/api/v1/tarjas/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/usuarios/**")
                .hasRole("ADMIN")

                .antMatchers("/api/v1/vendas/**")
                .hasAnyRole("ADMIN", "USER")

                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}
