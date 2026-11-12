package com.es.servicehub.service;

import com.es.servicehub.dto.CadastroUsuarioRequest;
import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.StatusSolicitacao;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.Endereco;
import com.es.servicehub.model.PropostaServico;
import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.EnderecoRepository;
import com.es.servicehub.repository.SolicitacaoServiceRepository;
import com.es.servicehub.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Testes unitários de regras de {@link UsuarioService} ainda não cobertas na suíte da Sprint 4.
 *
 * <p>Complementa {@code UsuarioServiceTest} e {@code UsuarioServiceAdditionalTest} sem substituí-los:
 * cobre validação de documentos (CPF/CNPJ), obrigatoriedade de perfil e senha, regra de não
 * sobrescrita de usuário existente e atualização de perfil (autorização, especialidades e endereço).</p>
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceRegrasAdicionaisTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private CadastroUsuarioRequest requestCliente;

    @BeforeEach
    void setUp() {
        requestCliente = new CadastroUsuarioRequest();
        requestCliente.setNome("Cliente Teste");
        requestCliente.setEmail("cliente@example.com");
        requestCliente.setCpf("52998224725");
        requestCliente.setSenha("senha-segura-123");
        requestCliente.setTelefone("(48) 99999-0000");
        requestCliente.setTipoPerfil(TipoPerfil.CLIENTE);
        requestCliente.setEspecialidades(new ArrayList<>());
    }

    private CadastroUsuarioRequest requestPrestador() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Prestador Teste");
        request.setEmail("prestador@example.com");
        request.setSenha("senha-segura-123");
        request.setTipoPerfil(TipoPerfil.PRESTADOR);
        request.setEspecialidades(new ArrayList<>(List.of(Especialidades.ELETRICISTA)));
        return request;
    }

    // ---------------------------------------------------------------- cadastro

    @Test
    @DisplayName("cadastro sem nome é rejeitado antes de consultar o repositório")
    void cadastrar_semNome_deveRejeitar() {
        requestCliente.setNome("   ");

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(requestCliente));

        assertEquals("Nome é obrigatório.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("cadastro sem e-mail é rejeitado")
    void cadastrar_semEmail_deveRejeitar() {
        requestCliente.setEmail(null);

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(requestCliente));

        assertEquals("E-mail é obrigatório.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("cadastro sem senha é rejeitado")
    void cadastrar_semSenha_deveRejeitar() {
        requestCliente.setSenha(" ");

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(requestCliente));

        assertEquals("Senha é obrigatória.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("cadastro sem perfil é rejeitado")
    void cadastrar_semPerfil_deveRejeitar() {
        requestCliente.setTipoPerfil(null);

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(requestCliente));

        assertEquals("Selecione um perfil válido.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("cadastro público não pode criar administrador")
    void cadastrar_perfilAdmin_deveRejeitar() {
        requestCliente.setTipoPerfil(TipoPerfil.ADMIN);

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(requestCliente));

        assertEquals("Não é permitido criar contas administrativas pelo cadastro público.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("cliente com CNPJ informado é rejeitado")
    void cadastrar_clienteComCnpj_deveRejeitar() {
        requestCliente.setCnpj("04252011000110");

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(requestCliente));

        assertEquals("Clientes devem informar CPF, não CNPJ.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("cliente sem CPF válido é rejeitado")
    void cadastrar_clienteSemCpfValido_deveRejeitar() {
        requestCliente.setCpf("111.111.111-11");

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(requestCliente));

        assertEquals("Informe um CPF válido.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("prestador sem documento é rejeitado")
    void cadastrar_prestadorSemDocumento_deveRejeitar() {
        RuntimeException erro = assertThrows(RuntimeException.class,
                () -> usuarioService.cadastrar(requestPrestador()));

        assertEquals("Informe CPF ou CNPJ para continuar.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("prestador com CPF e CNPJ simultâneos é rejeitado")
    void cadastrar_prestadorComCpfECnpj_deveRejeitar() {
        CadastroUsuarioRequest request = requestPrestador();
        request.setCpf("52998224725");
        request.setCnpj("04252011000110");

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(request));

        assertEquals("Informe somente CPF ou CNPJ.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("prestador com CNPJ inválido é rejeitado")
    void cadastrar_prestadorCnpjInvalido_deveRejeitar() {
        CadastroUsuarioRequest request = requestPrestador();
        request.setCnpj("04.252.011/0001-11");

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(request));

        assertEquals("Informe um CNPJ válido.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("prestador com CPF inválido é rejeitado")
    void cadastrar_prestadorCpfInvalido_deveRejeitar() {
        CadastroUsuarioRequest request = requestPrestador();
        request.setCpf("529.982.247-24");

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(request));

        assertEquals("Informe um CPF válido.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("senha com menos de 10 caracteres é rejeitada")
    void cadastrar_senhaCurta_deveRejeitar() {
        requestCliente.setSenha("123456789");

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(requestCliente));

        assertEquals("A senha deve ter pelo menos 10 caracteres.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("prestador sem especialidade é rejeitado")
    void cadastrar_prestadorSemEspecialidade_deveRejeitar() {
        CadastroUsuarioRequest request = requestPrestador();
        request.setCnpj("04252011000110");
        request.setEspecialidades(new ArrayList<>());

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(request));

        assertEquals("Prestadores devem informar pelo menos uma especialidade.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("e-mail já cadastrado é rejeitado")
    void cadastrar_emailDuplicado_deveRejeitar() {
        when(usuarioRepository.findByEmail("cliente@example.com")).thenReturn(Optional.of(new Usuario()));

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(requestCliente));

        assertEquals("Já existe um usuário cadastrado com este e-mail.", erro.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("cadastro de prestador com CNPJ válido persiste documento limpo e especialidades")
    void cadastrar_prestadorComCnpjValido_devePersistir() {
        CadastroUsuarioRequest request = requestPrestador();
        request.setCnpj("04.252.011/0001-10");
        when(usuarioRepository.findByEmail("prestador@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.cadastrar(request);

        assertEquals(TipoPerfil.PRESTADOR, resultado.getTipoPerfil());
        assertEquals("04252011000110", resultado.getCnpj());
        assertNull(resultado.getCpf());
        assertEquals(List.of(Especialidades.ELETRICISTA), resultado.getEspecialidades());
        assertNotNull(resultado.getSenha());
        assertTrue(resultado.getSenha().startsWith("$2"), "A senha deve ser persistida como hash BCrypt");
    }

    @Test
    @DisplayName("cadastro de cliente sem telefone persiste telefone nulo")
    void cadastrar_semTelefone_devePersistirNulo() {
        requestCliente.setTelefone(null);
        when(usuarioRepository.findByEmail("cliente@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.cadastrar(requestCliente);

        assertNull(resultado.getTelefone());
        assertEquals("52998224725", resultado.getCpf());
    }

    // ---------------------------------------------------------------- salvar

    @Test
    @DisplayName("salvar rejeita e-mail duplicado apenas na criação")
    void salvar_emailDuplicadoNaCriacao_deveRejeitar() {
        Usuario usuario = new Usuario();
        usuario.setEmail("duplicado@example.com");
        when(usuarioRepository.findByEmail("duplicado@example.com")).thenReturn(Optional.of(new Usuario()));

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.salvar(usuario));

        assertEquals("Já existe um usuário cadastrado com este e-mail.", erro.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("salvar não valida duplicidade quando o usuário já possui id")
    void salvar_usuarioExistente_naoVerificaDuplicidade() {
        Usuario usuario = new Usuario();
        usuario.setId(5L);
        usuario.setEmail("existente@example.com");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.salvar(usuario);

        assertEquals("existente@example.com", resultado.getEmail());
        verify(usuarioRepository, never()).findByEmail(any());
    }

    // ---------------------------------------------------------------- consultas

    @Test
    @DisplayName("buscarPorEmail inexistente lança exceção")
    void buscarPorEmail_inexistente_deveLancarExcecao() {
        when(usuarioRepository.findByEmail("nao@existe.com")).thenReturn(Optional.empty());

        RuntimeException erro = assertThrows(RuntimeException.class,
                () -> usuarioService.buscarPorEmail("nao@existe.com"));

        assertEquals("Usuário não encontrado.", erro.getMessage());
    }

    // ---------------------------------------------------------------- atualizar perfil

    private Usuario clientePersistido() {
        Usuario cliente = new Usuario();
        cliente.setId(1L);
        cliente.setNome("Cliente Original");
        cliente.setEmail("cliente@example.com");
        cliente.setTipoPerfil(TipoPerfil.CLIENTE);
        return cliente;
    }

    @Test
    @DisplayName("atualizarPerfil de outro usuário retorna 403")
    void atualizarPerfil_outroUsuario_deveRetornar403() {
        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> usuarioService.atualizarPerfil(1L, 2L,
                        new com.es.servicehub.dto.AtualizarUsuarioRequest("Nome", null, null, null)));

        assertEquals(403, erro.getStatusCode().value());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("atualizarPerfil sem dados retorna 400")
    void atualizarPerfil_semDados_deveRetornar400() {
        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> usuarioService.atualizarPerfil(1L, 1L, null));

        assertEquals(400, erro.getStatusCode().value());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("cliente não pode receber especialidades no perfil")
    void atualizarPerfil_clienteComEspecialidades_deveRetornar400() {
        when(usuarioRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(clientePersistido()));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> usuarioService.atualizarPerfil(1L, 1L, new com.es.servicehub.dto.AtualizarUsuarioRequest(
                        "Cliente", null, List.of(Especialidades.PINTOR), null)));

        assertEquals(400, erro.getStatusCode().value());
        assertEquals("Somente prestadores podem cadastrar especialidades.", erro.getReason());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("atualizarPerfil cria endereço quando o usuário ainda não possui um")
    void atualizarPerfil_semEnderecoAnterior_deveCriarEndereco() {
        Usuario cliente = clientePersistido();
        when(usuarioRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(cliente));
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(invocacao -> invocacao.getArgument(0));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.atualizarPerfil(1L, 1L, new com.es.servicehub.dto.AtualizarUsuarioRequest(
                "  Cliente Novo  ",
                "  (48) 98888-7777 ",
                null,
                new com.es.servicehub.dto.EnderecoDTO("88100-000", "Rua A", "10", null, "Centro", "São José", "sc")));

        assertEquals("Cliente Novo", resultado.getNome());
        assertEquals("(48) 98888-7777", resultado.getTelefone());
        assertNotNull(resultado.getEndereco());
        assertEquals("SC", resultado.getEndereco().getEstado());
        assertEquals("", resultado.getEndereco().getComplemento());
        verify(enderecoRepository).save(any(Endereco.class));
    }

    @Test
    @DisplayName("atualizarPerfil reutiliza o endereço existente do usuário")
    void atualizarPerfil_comEnderecoAnterior_deveAtualizarMesmoObjeto() {
        Usuario cliente = clientePersistido();
        Endereco existente = new Endereco();
        existente.setId(9L);
        existente.setCidade("Antiga");
        cliente.setEndereco(existente);
        when(usuarioRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(cliente));
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(invocacao -> invocacao.getArgument(0));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.atualizarPerfil(1L, 1L, new com.es.servicehub.dto.AtualizarUsuarioRequest(
                "Cliente", null, null,
                new com.es.servicehub.dto.EnderecoDTO("88100000", "Rua B", "20", "", "Centro", "Palhoça", "SC")));

        assertSame(existente, resultado.getEndereco());
        assertEquals("Palhoça", resultado.getEndereco().getCidade());
        assertEquals(9L, resultado.getEndereco().getId());
    }

    @Test
    @DisplayName("atualizarPerfil não altera telefone quando o campo não é enviado")
    void atualizarPerfil_semTelefone_devePreservarValorAtual() {
        Usuario cliente = clientePersistido();
        cliente.setTelefone("(48) 90000-0000");
        when(usuarioRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(cliente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.atualizarPerfil(1L, 1L,
                new com.es.servicehub.dto.AtualizarUsuarioRequest("Cliente", null, null, null));

        assertEquals("(48) 90000-0000", resultado.getTelefone());
        assertNull(resultado.getEndereco());
    }

    @Test
    @DisplayName("atualizarPerfil com UF em branco retorna 400")
    void atualizarPerfil_ufEmBranco_deveRetornar400() {
        when(usuarioRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(clientePersistido()));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> usuarioService.atualizarPerfil(1L, 1L, new com.es.servicehub.dto.AtualizarUsuarioRequest(
                        "Cliente", null, null,
                        new com.es.servicehub.dto.EnderecoDTO("88100000", "Rua", "1", "", "Centro", "São José", " "))));

        assertEquals(400, erro.getStatusCode().value());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ---------------------------------------------------------------- exclusão

    @Test
    @DisplayName("deletar usuário inexistente lança exceção")
    void deletar_inexistente_deveLancarExcecao() {
        when(usuarioRepository.findById(77L)).thenReturn(Optional.empty());

        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.deletar(77L));

        assertEquals("Usuário não encontrado com o ID: 77", erro.getMessage());
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }

    // ---------------------------------------------------------------- proposta / aceite

    @Test
    @DisplayName("aceite associa o prestador da proposta e mantém as demais propostas registradas")
    void aceitarProposta_comDuasPropostas_deveSelecionarPropostaEscolhida() {
        SolicitacaoServiceRepository repository = mock(SolicitacaoServiceRepository.class);
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        SolicitacaoServicoService service = new SolicitacaoServicoService(repository, usuarios);

        Usuario cliente = new Usuario();
        cliente.setId(1L);
        Usuario prestadorA = new Usuario();
        prestadorA.setId(2L);
        Usuario prestadorB = new Usuario();
        prestadorB.setId(3L);

        SolicitacaoServico solicitacao = new SolicitacaoServico();
        solicitacao.setId(10L);
        solicitacao.setCliente(cliente);
        solicitacao.setStatusSolicitacao(StatusSolicitacao.SOLICITADO);
        solicitacao.setDataCriacao(LocalDateTime.now());
        solicitacao.setEspecialidadesRequiridas(new ArrayList<>(List.of(Especialidades.ELETRICISTA)));
        PropostaServico primeira = new PropostaServico();
        primeira.setPrestador(prestadorA);
        primeira.setValorProposto(new BigDecimal("90.00"));
        PropostaServico segunda = new PropostaServico();
        segunda.setPrestador(prestadorB);
        segunda.setValorProposto(new BigDecimal("95.00"));
        solicitacao.adicionarProposta(primeira);
        solicitacao.adicionarProposta(segunda);

        when(repository.findByIdForUpdate(10L)).thenReturn(Optional.of(solicitacao));
        when(repository.save(any(SolicitacaoServico.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        SolicitacaoServico resultado = service.clienteAceitarProposta(10L, 1L, 3L);

        assertSame(prestadorB, resultado.getPrestador());
        assertEquals(new BigDecimal("95.00"), resultado.getValorEstimado());
        assertEquals(StatusSolicitacao.EM_ANDAMENTO, resultado.getStatusSolicitacao());
        assertEquals(2, resultado.getPropostas().size());
        verify(usuarios, never()).findById(any());
    }

    @Test
    @DisplayName("listarDisponiveis para prestador sem especialidades retorna lista vazia sem consultar o repositório")
    void listarDisponiveis_prestadorSemEspecialidades_deveRetornarVazio() {
        SolicitacaoServiceRepository repository = mock(SolicitacaoServiceRepository.class);
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        SolicitacaoServicoService service = new SolicitacaoServicoService(repository, usuarios);
        Usuario prestador = new Usuario();
        prestador.setId(5L);
        prestador.setTipoPerfil(TipoPerfil.PRESTADOR);
        prestador.setEspecialidades(new ArrayList<>());
        when(usuarios.findById(5L)).thenReturn(Optional.of(prestador));

        assertTrue(service.listarDisponiveis(5L, null).isEmpty());
        verify(repository, never()).findDisponiveisPorEspecialidades(any(), anyList());
    }

    @Test
    @DisplayName("listarDisponiveis para usuário que não é prestador retorna 403")
    void listarDisponiveis_cliente_deveRetornar403() {
        SolicitacaoServiceRepository repository = mock(SolicitacaoServiceRepository.class);
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        SolicitacaoServicoService service = new SolicitacaoServicoService(repository, usuarios);
        Usuario cliente = new Usuario();
        cliente.setId(6L);
        cliente.setTipoPerfil(TipoPerfil.CLIENTE);
        when(usuarios.findById(6L)).thenReturn(Optional.of(cliente));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.listarDisponiveis(6L, null));

        assertEquals(403, erro.getStatusCode().value());
        verify(repository, never()).findDisponiveisPorEspecialidades(any(), anyList());
    }

    @Test
    @DisplayName("listarDisponiveis para prestador inexistente retorna 404")
    void listarDisponiveis_prestadorInexistente_deveRetornar404() {
        SolicitacaoServiceRepository repository = mock(SolicitacaoServiceRepository.class);
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        SolicitacaoServicoService service = new SolicitacaoServicoService(repository, usuarios);
        when(usuarios.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.listarDisponiveis(99L, null));

        assertEquals(404, erro.getStatusCode().value());
    }
}
