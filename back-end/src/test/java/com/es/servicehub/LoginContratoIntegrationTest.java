package com.es.servicehub.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Contrato do login consumido pelo frontend e pela suíte Playwright.
 *
 * <p>As asserções reproduzem o que {@code ServiceHub/src/servicos/autenticacao.ts} e
 * {@code ServiceHub/e2e/servicehub.spec.ts} esperam do endpoint: a mensagem fixa de credenciais
 * inválidas, a ausência de detalhes internos na resposta de erro e a validação de campos.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class LoginContratoIntegrationTest {

    private static final String MENSAGEM_CREDENCIAIS_INVALIDAS =
            "Credenciais inválidas. Verifique e-mail e senha.";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("login com senha incorreta responde 401 com a mensagem usada pelo frontend")
    void loginComSenhaIncorreta_deveRetornarMensagemPadrao() throws Exception {
        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"inexistente-login@example.com","senha":"senha-incorreta"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(MENSAGEM_CREDENCIAIS_INVALIDAS))
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    @DisplayName("login com corpo vazio também responde a mensagem padrão, sem detalhe interno")
    void loginComCorpoVazio_deveRetornarMensagemPadraoSemDetalheInterno() throws Exception {
        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(MENSAGEM_CREDENCIAIS_INVALIDAS))
                .andExpect(jsonPath("$.trace").doesNotExist())
                .andExpect(jsonPath("$.exception").doesNotExist());
    }
}
