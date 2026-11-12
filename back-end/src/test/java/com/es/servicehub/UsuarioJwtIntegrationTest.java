package com.es.servicehub;

import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioJwtIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void loginEmiteJwtQueAutenticaRequisicoesStateless() throws Exception {
        String email = "qa-jwt-" + System.nanoTime() + "@example.com";
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário QA");
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode("senha-segura"));
        usuario.setTipoPerfil(TipoPerfil.CLIENTE);
        usuarioRepository.save(usuario);

        String respostaLogin = mockMvc.perform(post("/api/usuarios/login")
                        .contentType("application/json")
                        .content("{\"email\":\"" + email + "\",\"senha\":\"senha-segura\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();
        java.util.regex.Matcher tokenMatcher = java.util.regex.Pattern
                .compile("\"token\"\\s*:\\s*\"([^\"]+)\"")
                .matcher(respostaLogin);
        org.junit.jupiter.api.Assertions.assertTrue(tokenMatcher.find(), "Login deve retornar um JWT.");
        String token = tokenMatcher.group(1);

        mockMvc.perform(get("/api/usuarios/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));

        mockMvc.perform(post("/api/usuarios/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void rotaProtegidaSemToken_deveRecusarAcesso() throws Exception {
        mockMvc.perform(get("/api/usuarios/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rotaProtegidaComTokenInvalido_deveRecusarAcesso() throws Exception {
        mockMvc.perform(get("/api/usuarios/me").header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized());
    }
}
