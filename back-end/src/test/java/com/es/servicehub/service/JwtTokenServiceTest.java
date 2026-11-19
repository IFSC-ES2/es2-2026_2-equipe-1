package com.es.servicehub.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários do serviço que emite o JWT stateless.
 *
 * <p>Validam o contrato descrito em {@code docs/adrs/ADR-0003-autenticacao.md}: assinatura HS256,
 * emissor {@code servicehub}, expiração de uma hora, papéis sem o prefixo {@code ROLE_} e ausência
 * de revogação server-side (o token continua válido após o logout).</p>
 */
class JwtTokenServiceTest {

    private static final String SEGREDO = "segredo-de-teste-com-pelo-menos-32-bytes";

    private JwtTokenService jwtTokenService;
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        SecretKey chave = new SecretKeySpec(SEGREDO.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        JwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(chave));
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(chave)
                .macAlgorithm(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256)
                .build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("servicehub"));
        this.jwtDecoder = decoder;
        this.jwtTokenService = new JwtTokenService(encoder);
    }

    private Authentication autenticacao(String email, String... papeis) {
        List<SimpleGrantedAuthority> autoridades = java.util.Arrays.stream(papeis)
                .map(SimpleGrantedAuthority::new)
                .toList();
        return UsernamePasswordAuthenticationToken.authenticated(email, null, autoridades);
    }

    @Test
    @DisplayName("token emitido carrega emissor, assunto e papéis sem prefixo ROLE_")
    void emitir_deveIncluirEmissorAssuntoEPapeis() {
        String token = jwtTokenService.emitir(autenticacao("cliente@example.com", "ROLE_CLIENTE"));

        Jwt jwt = jwtDecoder.decode(token);

        assertEquals("servicehub", jwt.getClaimAsString("iss"));
        assertEquals("cliente@example.com", jwt.getSubject());
        assertEquals(List.of("CLIENTE"), jwt.getClaimAsStringList("roles"));
    }

    @Test
    @DisplayName("token emitido expira em uma hora")
    void emitir_deveDefinirExpiracaoDeUmaHora() {
        Instant antes = Instant.now();
        String token = jwtTokenService.emitir(autenticacao("prestador@example.com", "ROLE_PRESTADOR"));

        Jwt jwt = jwtDecoder.decode(token);

        assertEquals(Duration.ofHours(1).toSeconds(),
                Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()).toSeconds());
        assertTrue(jwt.getExpiresAt().isAfter(antes.plus(Duration.ofMinutes(59))));
        assertTrue(jwt.getExpiresAt().isBefore(Instant.now().plus(Duration.ofMinutes(61))));
    }

    @Test
    @DisplayName("tokens de usuários diferentes não são idênticos e autenticam apenas o próprio assunto")
    void emitir_deveGerarTokensDistintosPorUsuario() {
        String tokenCliente = jwtTokenService.emitir(autenticacao("a@example.com", "ROLE_CLIENTE"));
        String tokenPrestador = jwtTokenService.emitir(autenticacao("b@example.com", "ROLE_PRESTADOR"));

        assertEquals("a@example.com", jwtDecoder.decode(tokenCliente).getSubject());
        assertEquals("b@example.com", jwtDecoder.decode(tokenPrestador).getSubject());
        assertTrue(!tokenCliente.equals(tokenPrestador), "Cada emissão deve produzir um token próprio.");
    }

    @Test
    @DisplayName("token assinado com outro segredo é recusado na validação")
    void decoder_deveRecusarTokenAssinadoComOutroSegredo() {
        SecretKey outraChave = new SecretKeySpec(
                "outro-segredo-de-teste-com-32-bytes".getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        JwtTokenService outroServico = new JwtTokenService(new NimbusJwtEncoder(new ImmutableSecret<>(outraChave)));

        String tokenAlheio = outroServico.emitir(autenticacao("intruso@example.com", "ROLE_ADMIN"));

        assertThrows(Exception.class, () -> jwtDecoder.decode(tokenAlheio));
    }
}
