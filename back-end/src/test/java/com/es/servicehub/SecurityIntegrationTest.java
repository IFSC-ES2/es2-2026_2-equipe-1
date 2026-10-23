package com.es.servicehub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void endpointCsrf_deveSerPublico() throws Exception {
        mockMvc.perform(get("/api/usuarios/csrf"))
                .andExpect(status().isOk());
    }

    @Test
    void endpointUsuarioAtual_deveExigirAutenticacao() throws Exception {
        mockMvc.perform(get("/api/usuarios/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void prestadorPodeListarSolicitacoesDisponiveis() throws Exception {
        mockMvc.perform(get("/api/solicitacoes/disponiveis")
                        .with(user("prestador@example.com").roles("PRESTADOR")))
                .andExpect(status().isOk());
    }

    @Test
    void prestadorNaoPodeCriarSolicitacaoDeCliente() throws Exception {
        mockMvc.perform(post("/api/solicitacao")
                        .with(user("prestador@example.com").roles("PRESTADOR"))
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"titulo\":\"Serviço\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void clienteNaoPodeListarTodasAsSolicitacoesAdministrativas() throws Exception {
        mockMvc.perform(get("/api/solicitacoes")
                        .with(user("cliente@example.com").roles("CLIENTE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void requisicaoMutavelSemCsrf_deveSerRejeitadaAntesDoController() throws Exception {
        mockMvc.perform(post("/api/solicitacao")
                        .with(user("cliente@example.com").roles("CLIENTE"))
                        .contentType("application/json")
                        .content("{\"titulo\":\"Serviço\"}"))
                .andExpect(status().isForbidden());
    }
}
