package com.es.servicehub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.StatusSolicitacao;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.SolicitacaoServiceRepository;
import com.es.servicehub.repository.UsuarioRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SolicitacaoServiceRepository solicitacaoRepository;

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
        Usuario prestador = new Usuario();
        prestador.setNome("Prestador Teste");
        prestador.setEmail("prestador@example.com");
        prestador.setSenha("hash");
        prestador.setTipoPerfil(TipoPerfil.PRESTADOR);
        prestador.setEspecialidades(List.of(Especialidades.ELETRICISTA));
        usuarioRepository.save(prestador);

        SolicitacaoServico solicitacao = new SolicitacaoServico();
        solicitacao.setTitulo("Instalação elétrica");
        solicitacao.setDescricao("Instalar luminária");
        solicitacao.setStatusSolicitacao(StatusSolicitacao.SOLICITADO);
        solicitacao.setDataCriacao(LocalDateTime.now());
        solicitacao.setEspecialidadesRequiridas(List.of(Especialidades.ELETRICISTA));
        solicitacaoRepository.save(solicitacao);

        mockMvc.perform(get("/api/solicitacoes/disponiveis")
                        .with(user("prestador@example.com").roles("PRESTADOR")))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].titulo")
                        .value("Instalação elétrica"));

        mockMvc.perform(get("/api/solicitacoes/disponiveis")
                        .param("especialidade", "ELETRICISTA")
                        .with(user("prestador@example.com").roles("PRESTADOR")))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].titulo")
                        .value("Instalação elétrica"));
    }

    @Test
    void clienteNaoPodeConsultarSolicitacoesDisponiveisDePrestador() throws Exception {
        mockMvc.perform(get("/api/solicitacoes/disponiveis")
                        .with(user("cliente@example.com").roles("CLIENTE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void prestadorNaoPodeConcluirServico() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .put("/api/solicitacoes/1/concluir")
                        .with(user("prestador@example.com").roles("PRESTADOR"))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void prestadorNaoPodeExcluirSolicitacaoDeCliente() throws Exception {
        mockMvc.perform(delete("/api/solicitacoes/1")
                        .with(user("prestador@example.com").roles("PRESTADOR"))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void administradorNaoPodeExcluirSolicitacaoComoSeFosseCliente() throws Exception {
        mockMvc.perform(delete("/api/solicitacoes/1")
                        .with(user("admin@example.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void clienteProprietarioPodeConcluirServicoEmAndamento() throws Exception {
        Usuario cliente = novoUsuario("cliente-conclusao@example.com", TipoPerfil.CLIENTE);
        SolicitacaoServico solicitacao = new SolicitacaoServico();
        solicitacao.setTitulo("Pintura");
        solicitacao.setDescricao("Pintura da sala");
        solicitacao.setStatusSolicitacao(StatusSolicitacao.EM_ANDAMENTO);
        solicitacao.setDataCriacao(LocalDateTime.now());
        solicitacao.setCliente(cliente);
        solicitacaoRepository.save(solicitacao);

        mockMvc.perform(put("/api/solicitacoes/" + solicitacao.getId() + "/concluir")
                        .with(user(cliente.getEmail()).roles("CLIENTE"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$.statusSolicitacao").value("CONCLUIDO"));
    }

    @Test
    void usuarioPodeAtualizarSomenteCamposDoProprioPerfil() throws Exception {
        Usuario cliente = novoUsuario("perfil-proprio@example.com", TipoPerfil.CLIENTE);

        mockMvc.perform(put("/api/usuarios/" + cliente.getId())
                        .with(user(cliente.getEmail()).roles("CLIENTE"))
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "nome": "Nome atualizado",
                                  "telefone": "(48) 99999-1111",
                                  "tipoPerfil": "ADMIN",
                                  "senha": "senha-injetada"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$.nome").value("Nome atualizado"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$.tipoPerfil").value("CLIENTE"));
    }

    @Test
    void clienteProprietarioPodeExcluirSolicitacao() throws Exception {
        Usuario cliente = novoUsuario("cliente-exclusao@example.com", TipoPerfil.CLIENTE);
        SolicitacaoServico solicitacao = new SolicitacaoServico();
        solicitacao.setTitulo("Montagem");
        solicitacao.setDescricao("Montar mesa");
        solicitacao.setStatusSolicitacao(StatusSolicitacao.SOLICITADO);
        solicitacao.setDataCriacao(LocalDateTime.now());
        solicitacao.setCliente(cliente);
        solicitacao = solicitacaoRepository.save(solicitacao);

        mockMvc.perform(delete("/api/solicitacoes/" + solicitacao.getId())
                        .with(user(cliente.getEmail()).roles("CLIENTE"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
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

    private Usuario novoUsuario(String email, TipoPerfil perfil) {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário de teste");
        usuario.setEmail(email);
        usuario.setSenha("hash");
        usuario.setTipoPerfil(perfil);
        return usuarioRepository.save(usuario);
    }
}
