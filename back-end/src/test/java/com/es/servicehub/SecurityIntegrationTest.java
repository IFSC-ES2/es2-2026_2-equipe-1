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
import org.springframework.transaction.annotation.Transactional;

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
        prestador.setEspecialidades(new java.util.ArrayList<>(List.of(Especialidades.ELETRICISTA)));
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
    void prestadorRecebeHistoricoConcluidoDoProprioPerfil() throws Exception {
        Usuario prestador = novoUsuario("prestador-historico@example.com", TipoPerfil.PRESTADOR);
        prestador.setEspecialidades(new java.util.ArrayList<>(List.of(Especialidades.ELETRICISTA)));
        usuarioRepository.save(prestador);
        Usuario cliente = novoUsuario("cliente-historico@example.com", TipoPerfil.CLIENTE);

        SolicitacaoServico concluida = new SolicitacaoServico();
        concluida.setTitulo("Reparo elétrico concluído");
        concluida.setDescricao("Troca da fiação");
        concluida.setValorEstimado(new java.math.BigDecimal("235.50"));
        concluida.setStatusSolicitacao(StatusSolicitacao.CONCLUIDO);
        concluida.setDataCriacao(LocalDateTime.now());
        concluida.setCliente(cliente);
        concluida.setPrestador(prestador);
        concluida.setEspecialidadesRequiridas(List.of(Especialidades.ELETRICISTA));
        solicitacaoRepository.save(concluida);

        mockMvc.perform(get("/api/solicitacoes/meus-chamados")
                        .with(user(prestador.getEmail()).roles("PRESTADOR")))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$[0].titulo").value("Reparo elétrico concluído"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$[0].statusSolicitacao").value("CONCLUIDO"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$[0].valorEstimado").value(235.50));
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
        cliente.setCpf("52998224725");
        cliente.setSenha("senha-original");
        cliente = usuarioRepository.save(cliente);

        mockMvc.perform(put("/api/usuarios/" + cliente.getId())
                        .with(user(cliente.getEmail()).roles("CLIENTE"))
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "nome": "Nome atualizado",
                                  "telefone": "(48) 99999-1111",
                                  "endereco": {
                                    "cep": "88000000",
                                    "logradouro": "Rua Central",
                                    "numero": "10",
                                    "complemento": "Apto 2",
                                    "bairro": "Centro",
                                    "cidade": "Florianópolis",
                                    "estado": "sc"
                                  },
                                  "tipoPerfil": "ADMIN",
                                  "senha": "senha-injetada"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$.nome").value("Nome atualizado"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$.tipoPerfil").value("CLIENTE"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$.endereco.logradouro").value("Rua Central"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$.endereco.estado").value("SC"));

        Usuario persistido = usuarioRepository.findById(cliente.getId()).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals("perfil-proprio@example.com", persistido.getEmail());
        org.junit.jupiter.api.Assertions.assertEquals("52998224725", persistido.getCpf());
        org.junit.jupiter.api.Assertions.assertEquals("senha-original", persistido.getSenha());
        org.junit.jupiter.api.Assertions.assertEquals(TipoPerfil.CLIENTE, persistido.getTipoPerfil());
        org.junit.jupiter.api.Assertions.assertEquals("(48) 99999-1111", persistido.getTelefone());
    }

    @Test
    @Transactional
    void prestadorPodeAtualizarEspecialidades() throws Exception {
        Usuario prestador = novoUsuario("perfil-prestador@example.com", TipoPerfil.PRESTADOR);
        prestador.setEspecialidades(new java.util.ArrayList<>(List.of(Especialidades.ELETRICISTA)));
        prestador = usuarioRepository.save(prestador);

        mockMvc.perform(put("/api/usuarios/" + prestador.getId())
                        .with(user(prestador.getEmail()).roles("PRESTADOR"))
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "nome": "Prestador atualizado",
                                  "especialidades": ["PINTOR"]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .jsonPath("$.especialidades[0]").value("PINTOR"));

        org.junit.jupiter.api.Assertions.assertEquals(
                List.of(Especialidades.PINTOR),
                usuarioRepository.findById(prestador.getId()).orElseThrow().getEspecialidades());
    }

    @Test
    void usuarioNaoPodeEditarPerfilDeOutroUsuario() throws Exception {
        Usuario solicitante = novoUsuario("perfil-solicitante@example.com", TipoPerfil.CLIENTE);
        Usuario alvo = novoUsuario("perfil-alvo@example.com", TipoPerfil.CLIENTE);

        mockMvc.perform(put("/api/usuarios/" + alvo.getId())
                        .with(user(solicitante.getEmail()).roles("CLIENTE"))
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"nome\":\"Alteração não autorizada\"}"))
                .andExpect(status().isForbidden());

        org.junit.jupiter.api.Assertions.assertEquals(
                "Usuário de teste",
                usuarioRepository.findById(alvo.getId()).orElseThrow().getNome());
    }

    @Test
    void usuarioNaoPodeAtualizarEnderecoComUfInvalida() throws Exception {
        Usuario cliente = novoUsuario("perfil-uf-invalida@example.com", TipoPerfil.CLIENTE);

        mockMvc.perform(put("/api/usuarios/" + cliente.getId())
                        .with(user(cliente.getEmail()).roles("CLIENTE"))
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                  "nome": "Nome atualizado",
                                  "endereco": {
                                    "cep": "88000000",
                                    "logradouro": "Rua Central",
                                    "numero": "10",
                                    "bairro": "Centro",
                                    "cidade": "Florianópolis",
                                    "estado": "Santa Catarina"
                                  }
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void clienteNaoPodeEnviarEspecialidadesNoPerfil() throws Exception {
        Usuario cliente = novoUsuario("perfil-cliente-especialidade@example.com", TipoPerfil.CLIENTE);

        mockMvc.perform(put("/api/usuarios/" + cliente.getId())
                        .with(user(cliente.getEmail()).roles("CLIENTE"))
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"nome\":\"Cliente atualizado\",\"especialidades\":[]}"))
                .andExpect(status().isBadRequest());
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
