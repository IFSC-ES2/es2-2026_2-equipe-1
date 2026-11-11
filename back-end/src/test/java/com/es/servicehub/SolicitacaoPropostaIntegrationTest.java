package com.es.servicehub;

import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.StatusSolicitacao;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.Endereco;
import com.es.servicehub.model.PropostaServico;
import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.EnderecoRepository;
import com.es.servicehub.repository.PropostaServicoRepository;
import com.es.servicehub.repository.SolicitacaoServiceRepository;
import com.es.servicehub.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração HTTP dos fluxos de proposta, aceite, conclusão e autorização por proprietário.
 *
 * <p>Diferentemente de {@code SecurityIntegrationTest}, que usa o pós-processador {@code user(...)}
 * e não exercita o login real, estes testes autenticam pelo endpoint {@code POST /api/usuarios/login},
 * carregam o cookie de sessão ({@code JSESSIONID}) e o token CSRF emitido pela API, e reutilizam a
 * mesma sessão nas requisições seguintes. Assim cobrem o caminho HTTP efetivamente usado pelo front-end.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class SolicitacaoPropostaIntegrationTest {

    private static final String SENHA = "senha-segura-123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SolicitacaoServiceRepository solicitacaoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PropostaServicoRepository propostaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario cliente;
    private Usuario outroCliente;
    private Usuario prestador;

    @BeforeEach
    void setUp() {
        long sufixo = System.nanoTime();
        cliente = criarUsuario("cliente-proposta-" + sufixo + "@example.com", TipoPerfil.CLIENTE);
        outroCliente = criarUsuario("outro-cliente-proposta-" + sufixo + "@example.com", TipoPerfil.CLIENTE);
        prestador = criarUsuario("prestador-proposta-" + sufixo + "@example.com", TipoPerfil.PRESTADOR);
        prestador.setEspecialidades(new ArrayList<>(List.of(Especialidades.ELETRICISTA)));
        prestador = usuarioRepository.save(prestador);
    }

    private Usuario criarUsuario(String email, TipoPerfil perfil) {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário " + email.substring(0, email.indexOf('@')));
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(SENHA));
        usuario.setTipoPerfil(perfil);
        return usuarioRepository.save(usuario);
    }

    private Endereco criarEndereco() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua de Teste");
        endereco.setNumero("100");
        endereco.setComplemento("");
        endereco.setBairro("Centro");
        endereco.setCidade("São José");
        endereco.setEstado("SC");
        endereco.setCep("88100000");
        return enderecoRepository.save(endereco);
    }

    private SolicitacaoServico criarSolicitacao(StatusSolicitacao status) {
        SolicitacaoServico solicitacao = new SolicitacaoServico();
        solicitacao.setTitulo("Troca de fiação");
        solicitacao.setDescricao("Fiação antiga na sala");
        solicitacao.setValorEstimado(new BigDecimal("100.00"));
        solicitacao.setStatusSolicitacao(status);
        solicitacao.setDataCriacao(LocalDateTime.now());
        solicitacao.setCliente(cliente);
        solicitacao.setEnderecoAtendimento(criarEndereco());
        solicitacao.setEspecialidadesRequiridas(new ArrayList<>(List.of(Especialidades.ELETRICISTA)));
        return solicitacaoRepository.save(solicitacao);
    }

    private int quantidadeDePropostas(Long solicitacaoId) {
        return (int) propostaRepository.countBySolicitacao_Id(solicitacaoId);
    }

    private BigDecimal valorDaProposta(Long solicitacaoId) {
        return propostaRepository.findBySolicitacao_Id(solicitacaoId).getFirst().getValorProposto();
    }

    private MockHttpSession autenticar(Usuario usuario) throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/usuarios/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + usuario.getEmail() + "\",\"senha\":\"" + SENHA + "\"}"))
                .andExpect(status().isOk())
                .andReturn();
        MockHttpSession sessao = (MockHttpSession) resultado.getRequest().getSession(false);
        assertNotNull(sessao, "O login deve criar uma sessão HTTP");
        return sessao;
    }

    @Test
    @DisplayName("prestador autenticado por sessão envia proposta e cliente proprietário aceita")
    void fluxoPropostaAceite_eConcluidoViaHttp() throws Exception {
        SolicitacaoServico solicitacao = criarSolicitacao(StatusSolicitacao.SOLICITADO);
        MockHttpSession sessaoPrestador = autenticar(prestador);
        MockHttpSession sessaoCliente = autenticar(cliente);

        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas")
                        .session(sessaoPrestador)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valorProposto\":140.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusSolicitacao").value("SOLICITADO"));

        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas/"
                        + prestador.getId() + "/aceitar")
                        .session(sessaoCliente)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusSolicitacao").value("EM_ANDAMENTO"))
                .andExpect(jsonPath("$.valorEstimado").value(140.00))
                .andExpect(jsonPath("$.prestador.id").value(prestador.getId()));

        mockMvc.perform(put("/api/solicitacoes/" + solicitacao.getId() + "/concluir")
                        .session(sessaoCliente)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusSolicitacao").value("CONCLUIDO"));

        SolicitacaoServico persistida = solicitacaoRepository.findById(solicitacao.getId()).orElseThrow();
        assertEquals(StatusSolicitacao.CONCLUIDO, persistida.getStatusSolicitacao());
        assertEquals(0, new BigDecimal("140.00").compareTo(persistida.getValorEstimado()));
        assertEquals(prestador.getId(), persistida.getPrestador().getId());
        assertEquals(1, quantidadeDePropostas(solicitacao.getId()));
    }

    @Test
    @DisplayName("cliente que não é proprietário não pode aceitar proposta nem excluir solicitação")
    void clienteNaoProprietario_naoPodeAlterarSolicitacao() throws Exception {
        SolicitacaoServico solicitacao = criarSolicitacao(StatusSolicitacao.SOLICITADO);
        MockHttpSession sessaoPrestador = autenticar(prestador);
        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas")
                        .session(sessaoPrestador)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valorProposto\":120.00}"))
                .andExpect(status().isOk());

        MockHttpSession sessaoOutro = autenticar(outroCliente);

        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas/"
                        + prestador.getId() + "/aceitar")
                        .session(sessaoOutro)
                        .with(csrf()))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/solicitacoes/" + solicitacao.getId() + "/concluir")
                        .session(sessaoOutro)
                        .with(csrf()))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/solicitacoes/" + solicitacao.getId())
                        .session(sessaoOutro))
                .andExpect(status().isForbidden());

        assertEquals(StatusSolicitacao.SOLICITADO,
                solicitacaoRepository.findById(solicitacao.getId()).orElseThrow().getStatusSolicitacao());
    }

    @Test
    @DisplayName("proposta com valor zero ou nulo retorna 400 e não persiste proposta")
    void propostaValorInvalido_deveRetornar400() throws Exception {
        SolicitacaoServico solicitacao = criarSolicitacao(StatusSolicitacao.SOLICITADO);
        MockHttpSession sessaoPrestador = autenticar(prestador);

        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas")
                        .session(sessaoPrestador)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valorProposto\":0.00}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas")
                        .session(sessaoPrestador)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valorProposto\":-10.00}"))
                .andExpect(status().isBadRequest());

        assertEquals(0, quantidadeDePropostas(solicitacao.getId()));
    }

    @Test
    @DisplayName("proposta em solicitação já aceita retorna 409")
    void propostaEmSolicitacaoAceita_deveRetornar409() throws Exception {
        SolicitacaoServico solicitacao = criarSolicitacao(StatusSolicitacao.EM_ANDAMENTO);
        MockHttpSession sessaoPrestador = autenticar(prestador);

        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas")
                        .session(sessaoPrestador)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valorProposto\":50.00}"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("reenvio de proposta pelo mesmo prestador atualiza o valor sem duplicar o registro")
    void reenvioDeProposta_naoDeveDuplicar() throws Exception {
        SolicitacaoServico solicitacao = criarSolicitacao(StatusSolicitacao.SOLICITADO);
        MockHttpSession sessaoPrestador = autenticar(prestador);

        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas")
                        .session(sessaoPrestador)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valorProposto\":90.00}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas")
                        .session(sessaoPrestador)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valorProposto\":95.00}"))
                .andExpect(status().isOk());

        assertEquals(1, quantidadeDePropostas(solicitacao.getId()));
        assertEquals(0, new BigDecimal("95.00").compareTo(valorDaProposta(solicitacao.getId())));
    }

    @Test
    @DisplayName("cliente não pode enviar proposta e prestador não pode aceitar proposta")
    void perfisTrocados_devemReceber403() throws Exception {
        SolicitacaoServico solicitacao = criarSolicitacao(StatusSolicitacao.SOLICITADO);
        MockHttpSession sessaoCliente = autenticar(cliente);
        MockHttpSession sessaoPrestador = autenticar(prestador);

        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas")
                        .session(sessaoCliente)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valorProposto\":80.00}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/solicitacoes/" + solicitacao.getId() + "/propostas/"
                        + prestador.getId() + "/aceitar")
                        .session(sessaoPrestador)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("listagens por perfil devolvem apenas as solicitações do usuário autenticado")
    void listagensPorPerfil_isolamDadosDoUsuario() throws Exception {
        SolicitacaoServico doCliente = criarSolicitacao(StatusSolicitacao.SOLICITADO);
        SolicitacaoServico deOutro = new SolicitacaoServico();
        deOutro.setTitulo("Solicitação de outro cliente");
        deOutro.setStatusSolicitacao(StatusSolicitacao.SOLICITADO);
        deOutro.setDataCriacao(LocalDateTime.now());
        deOutro.setCliente(outroCliente);
        deOutro.setEspecialidadesRequiridas(new ArrayList<>(List.of(Especialidades.PINTOR)));
        solicitacaoRepository.save(deOutro);

        MockHttpSession sessaoCliente = autenticar(cliente);
        MockHttpSession sessaoPrestador = autenticar(prestador);

        mockMvc.perform(get("/api/solicitacoes/minhas").session(sessaoCliente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(doCliente.getId()));

        mockMvc.perform(get("/api/solicitacoes/disponiveis").session(sessaoPrestador))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Troca de fiação"));

        mockMvc.perform(get("/api/solicitacoes/disponiveis")
                        .param("especialidade", "PINTOR")
                        .session(sessaoPrestador))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Solicitação de outro cliente"));
    }

    @Test
    @DisplayName("cadastro HTTP com dados inválidos devolve 400 com mensagem e não cria usuário")
    void cadastroInvalidoViaHttp_deveRetornar400() throws Exception {
        String email = "cadastro-invalido-" + System.nanoTime() + "@example.com";

        mockMvc.perform(post("/api/usuarios/cadastro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Sem Senha","email":"%s","cpf":"52998224725",
                                 "senha":"123","telefone":"48999990000","tipoPerfil":"CLIENTE"}
                                """.formatted(email)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A senha deve ter pelo menos 10 caracteres."));

        assertNull(usuarioRepository.findByEmail(email).orElse(null));
    }

    @Test
    @DisplayName("cadastro HTTP de administrador é recusado")
    void cadastroAdminViaHttp_deveRetornar400() throws Exception {
        mockMvc.perform(post("/api/usuarios/cadastro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Admin","email":"admin-%d@example.com","cpf":"52998224725",
                                 "senha":"senha-segura-123","telefone":"48999990000","tipoPerfil":"ADMIN"}
                                """.formatted(System.nanoTime())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("requisição mutável sem token CSRF é rejeitada mesmo com sessão autenticada")
    void requisicaoSemCsrf_comSessaoAutenticada_deveRetornar403() throws Exception {
        MockHttpSession sessaoCliente = autenticar(cliente);

        mockMvc.perform(post("/api/solicitacao")
                        .session(sessaoCliente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Sem CSRF\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("endereço cadastrado por um cliente fica visível na listagem usada pelo modal")
    void enderecoCadastrado_deveAparecerNaListagem() throws Exception {
        MockHttpSession sessaoCliente = autenticar(cliente);

        mockMvc.perform(post("/api/enderecos")
                        .session(sessaoCliente)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"logradouro":"Rua Nova","numero":"77","complemento":"",
                                 "bairro":"Kobrasol","cidade":"São José","estado":"SC","cep":"88102000"}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/enderecos").session(sessaoCliente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.logradouro == 'Rua Nova')]").isNotEmpty());
    }
}
