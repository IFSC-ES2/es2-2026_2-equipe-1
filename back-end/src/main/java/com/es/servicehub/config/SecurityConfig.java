package com.es.servicehub.config;

import com.es.servicehub.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return email -> usuarioRepository.findByEmail(email)
                .map(usuario -> User.withUsername(usuario.getEmail())
                        .password(usuario.getSenha())
                        .roles(usuario.getTipoPerfil().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas."));
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository) throws Exception {
        http
                .cors(Customizer.withDefaults())
                // A REST API authenticated through request headers does not use browser CSRF state.
                .csrf(csrf -> csrf.disable())
                .securityContext(context -> context.securityContextRepository(securityContextRepository))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/cadastro", "/api/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/logout").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/*").authenticated()
                        .requestMatchers("/api/usuarios", "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/solicitacao", "/api/solicitacoes").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/solicitacao/minhas", "/api/solicitacoes/minhas").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/solicitacao/disponiveis", "/api/solicitacoes/disponiveis",
                                "/api/solicitacao/meus-chamados", "/api/solicitacoes/meus-chamados").hasRole("PRESTADOR")
                        .requestMatchers(HttpMethod.POST, "/api/solicitacao/*/propostas", "/api/solicitacoes/*/propostas").hasRole("PRESTADOR")
                        .requestMatchers(HttpMethod.POST, "/api/solicitacao/*/propostas/*/aceitar",
                                "/api/solicitacoes/*/propostas/*/aceitar").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/solicitacao/*/concluir",
                                "/api/solicitacoes/*/concluir").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/solicitacao/*", "/api/solicitacoes/*")
                                .hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/solicitacao", "/api/solicitacoes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/enderecos").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/solicitacao/**", "/api/solicitacoes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/solicitacao/**", "/api/solicitacoes/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated())
                // BasicAuthenticationFilter is the header-based authentication filter available
                // in this application; it must run before username/password authentication.
                .addFilterBefore(new BasicAuthenticationFilter(authenticationManager),
                        UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.deny()));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://frontservicehub.jvs.dev.br"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-XSRF-TOKEN"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}