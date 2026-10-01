package com.es.servicehub.service;

import com.es.servicehub.dto.CadastroUsuarioRequest;
import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("123456");
        usuario.setTipoPerfil(TipoPerfil.CLIENTE);
    }

    @Test
    void cadastrar_clienteValido_deveSalvarSenhaComHash() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Maria Silva");
        request.setEmail(" MARIA@EMAIL.COM ");
        request.setSenha("senha-segura-123");
        request.setTipoPerfil(TipoPerfil.CLIENTE);

        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.cadastrar(request);

        assertEquals("Maria Silva", resultado.getNome());
        assertEquals("maria@email.com", resultado.getEmail());
        assertNotEquals("senha-segura-123", resultado.getSenha());
        assertTrue(new BCryptPasswordEncoder().matches("senha-segura-123", resultado.getSenha()));
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void cadastrar_semNome_deveRejeitarEnaoSalvar() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setEmail("maria@email.com");
        request.setSenha("senha-segura-123");
        request.setTipoPerfil(TipoPerfil.CLIENTE);

        assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void cadastrar_prestadorSemEspecialidade_deveRejeitar() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Pedro Prestador");
        request.setEmail("pedro@email.com");
        request.setSenha("senha-segura-123");
        request.setTipoPerfil(TipoPerfil.PRESTADOR);

        assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void cadastrar_prestadorComEspecialidade_deveSalvarPerfilEEspecialidade() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Pedro Prestador");
        request.setEmail("pedro@email.com");
        request.setSenha("senha-segura-123");
        request.setTipoPerfil(TipoPerfil.PRESTADOR);
        request.setEspecialidades(List.of(Especialidades.ELETRICISTA));
        when(usuarioRepository.findByEmail("pedro@email.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.cadastrar(request);

        assertEquals(TipoPerfil.PRESTADOR, resultado.getTipoPerfil());
        assertEquals(List.of(Especialidades.ELETRICISTA), resultado.getEspecialidades());
    }

    @Test
    void cadastrar_emailDuplicado_deveRejeitar() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Maria Silva");
        request.setEmail("maria@email.com");
        request.setSenha("senha-segura-123");
        request.setTipoPerfil(TipoPerfil.CLIENTE);
        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));

        assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void cadastrar_admin_deveRejeitar() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Admin");
        request.setEmail("admin@email.com");
        request.setSenha("senha-segura-123");
        request.setTipoPerfil(TipoPerfil.ADMIN);

        assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void cadastrar_senhaCurta_deveRejeitar() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Maria Silva");
        request.setEmail("maria@email.com");
        request.setSenha("curta");
        request.setTipoPerfil(TipoPerfil.CLIENTE);

        assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void autenticar_credenciaisValidas_deveRetornarUsuario() {
        usuario.setSenha(new BCryptPasswordEncoder().encode("senha-segura-123"));
        when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.autenticar(" JOAO@EMAIL.COM ", "senha-segura-123");

        assertSame(usuario, resultado);
    }

    @Test
    void autenticar_senhaIncorreta_deveRejeitar() {
        usuario.setSenha(new BCryptPasswordEncoder().encode("senha-segura-123"));
        when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario));

        assertThrows(RuntimeException.class,
                () -> usuarioService.autenticar("joao@email.com", "senha-errada"));
    }

    @Test
    void autenticar_usuarioInexistente_deveRejeitar() {
        when(usuarioRepository.findByEmail("ausente@email.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> usuarioService.autenticar("ausente@email.com", "senha-segura-123"));
    }

    @Test
    void autenticar_credenciaisEmBranco_deveRejeitarAntesDeConsultarRepositorio() {
        assertThrows(RuntimeException.class, () -> usuarioService.autenticar(" ", " "));
        verify(usuarioRepository, never()).findByEmail(any());
    }

    @Test
    void listarTodos_deveRetornarListaDeUsuarios() {
        Usuario outro = new Usuario();
        outro.setId(2L);
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario, outro));

        List<Usuario> resultado = usuarioService.listarTodos();

        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_quandoExiste_deveRetornarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarExcecao() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class,
                () -> usuarioService.buscarPorId(99L));

        assertTrue(excecao.getMessage().contains("não encontrado"));
    }

    @Test
    void salvar_quandoEmailNaoExiste_deveSalvarUsuario() {
        usuario.setId(null);
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.salvar(usuario);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void salvar_quandoEmailJaExiste_deveLancarExcecao() {
        usuario.setId(null);
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(new Usuario()));

        assertThrows(RuntimeException.class, () -> usuarioService.salvar(usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void atualizar_deveAtualizarDadosDoUsuarioExistente() {
        Usuario dadosNovos = new Usuario();
        dadosNovos.setNome("João Atualizado");
        dadosNovos.setEmail("novo@email.com");
        dadosNovos.setSenha("nova-senha");
        dadosNovos.setTipoPerfil(TipoPerfil.PRESTADOR);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = usuarioService.atualizar(1L, dadosNovos);

        assertEquals("João Atualizado", resultado.getNome());
        assertEquals("novo@email.com", resultado.getEmail());
        assertEquals(TipoPerfil.PRESTADOR, resultado.getTipoPerfil());
    }

    @Test
    void deletar_deveRemoverUsuarioExistente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        usuarioService.deletar(1L);

        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void deletar_quandoUsuarioNaoExiste_deveLancarExcecao() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.deletar(1L));
        verify(usuarioRepository, never()).delete(any());
    }
}
