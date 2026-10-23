package com.es.servicehub.service;

import com.es.servicehub.dto.CadastroUsuarioRequest;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceAdditionalTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void cadastrar_requestNulo_deveRejeitar() {
        RuntimeException erro = assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(null));
        assertEquals("Dados do cadastro obrigatórios.", erro.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void cadastrar_emailComEspacosEMaiusculas_deveNormalizar() {
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("  Maria  ");
        request.setEmail(" MARIA@EMAIL.COM ");
        request.setSenha("senha-segura-123");
        request.setTipoPerfil(TipoPerfil.CLIENTE);
        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.cadastrar(request);

        assertEquals("Maria", resultado.getNome());
        assertEquals("maria@email.com", resultado.getEmail());
    }

    @Test
    void salvar_deveNormalizarEmailEHashDeSenha() {
        Usuario usuario = new Usuario();
        usuario.setEmail("  USER@EMAIL.COM ");
        usuario.setSenha("senha-secreta");
        when(usuarioRepository.findByEmail("user@email.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.salvar(usuario);

        assertEquals("user@email.com", resultado.getEmail());
        assertNotEquals("senha-secreta", resultado.getSenha());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void atualizar_semSenhaNova_devePreservarSenhaAnterior() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setSenha("hash-anterior");
        Usuario alteracao = new Usuario();
        alteracao.setNome("Nome novo");
        alteracao.setSenha(" ");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = usuarioService.atualizar(1L, alteracao);

        assertEquals("Nome novo", resultado.getNome());
        assertEquals("hash-anterior", resultado.getSenha());
    }
}
