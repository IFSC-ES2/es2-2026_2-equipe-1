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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioSessionIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void loginMeLogout_deveInvalidarSessaoEImpedirNovoAcesso() throws Exception {
        String email = "qa-session-" + System.nanoTime() + "@example.com";
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário QA");
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode("senha-segura"));
        usuario.setTipoPerfil(TipoPerfil.CLIENTE);
        usuarioRepository.save(usuario);

        MvcResult login = mockMvc.perform(post("/api/usuarios/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"email\":\"" + email + "\",\"senha\":\"senha-segura\"}"))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession sessao = (MockHttpSession) login.getRequest().getSession(false);
        mockMvc.perform(get("/api/usuarios/me").session(sessao))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/usuarios/logout")
                        .with(csrf())
                        .session(sessao))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/usuarios/me").session(sessao))
                .andExpect(status().isForbidden());
    }
}
