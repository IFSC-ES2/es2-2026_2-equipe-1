package com.es.servicehub.service;

import com.es.servicehub.enums.StatusSolicitacao;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.PropostaServico;
import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.SolicitacaoServiceRepository;
import com.es.servicehub.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoServicoServiceAdditionalTest {
    @Mock
    private SolicitacaoServiceRepository solicitacaoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private SolicitacaoServicoService service;

    private SolicitacaoServico solicitacao;
    private Usuario cliente;
    private Usuario prestador;

    @BeforeEach
    void setUp() {
        solicitacao = new SolicitacaoServico();
        solicitacao.setId(1L);
        solicitacao.setStatusSolicitacao(StatusSolicitacao.SOLICITADO);
        cliente = new Usuario();
        cliente.setId(10L);
        cliente.setTipoPerfil(TipoPerfil.CLIENTE);
        prestador = new Usuario();
        prestador.setId(20L);
        prestador.setTipoPerfil(TipoPerfil.PRESTADOR);
    }

    @Test
    void criar_deveDefinirStatusEDataQuandoAusentes() {
        solicitacao.setStatusSolicitacao(null);
        solicitacao.setDataCriacao(null);
        when(solicitacaoRepository.save(solicitacao)).thenAnswer(invocacao -> invocacao.getArgument(0));

        SolicitacaoServico resultado = service.criar(solicitacao);

        assertEquals(StatusSolicitacao.SOLICITADO, resultado.getStatusSolicitacao());
        assertNotNull(resultado.getDataCriacao());
    }

    @Test
    void enviarProposta_solicitacaoInexistente_deveRetornar404() {
        when(solicitacaoRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.enviarProposta(1L, 20L, BigDecimal.TEN));

        assertEquals(404, erro.getStatusCode().value());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void enviarProposta_statusIndisponivel_deveRetornar409() {
        solicitacao.setStatusSolicitacao(StatusSolicitacao.EM_ANDAMENTO);
        when(solicitacaoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(solicitacao));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.enviarProposta(1L, 20L, BigDecimal.TEN));

        assertEquals(409, erro.getStatusCode().value());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void enviarProposta_prestadorInexistente_deveRetornar404() {
        when(solicitacaoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(solicitacao));
        when(usuarioRepository.findById(20L)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.enviarProposta(1L, 20L, BigDecimal.TEN));

        assertEquals(404, erro.getStatusCode().value());
    }

    @Test
    void enviarProposta_usuarioCliente_deveRetornar403() {
        cliente.setId(20L);
        when(solicitacaoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(solicitacao));
        when(usuarioRepository.findById(20L)).thenReturn(Optional.of(cliente));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.enviarProposta(1L, 20L, BigDecimal.TEN));

        assertEquals(403, erro.getStatusCode().value());
    }

    @Test
    void enviarProposta_mesmoPrestador_deveAtualizarPropostaSemDuplicar() {
        PropostaServico existente = new PropostaServico();
        existente.setPrestador(prestador);
        existente.setValorProposto(new BigDecimal("80.00"));
        solicitacao.adicionarProposta(existente);
        when(solicitacaoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(solicitacao));
        when(usuarioRepository.findById(20L)).thenReturn(Optional.of(prestador));
        when(solicitacaoRepository.save(any())).thenAnswer(invocacao -> invocacao.getArgument(0));

        SolicitacaoServico resultado = service.enviarProposta(1L, 20L, new BigDecimal("90.00"));

        assertEquals(1, resultado.getPropostas().size());
        assertEquals(new BigDecimal("90.00"), resultado.getPropostas().getFirst().getValorProposto());
    }

    @Test
    void aceitarProposta_semPropostaDoPrestador_deveRetornar404() {
        solicitacao.setCliente(cliente);
        when(solicitacaoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(solicitacao));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.clienteAceitarProposta(1L, 10L, 20L));

        assertEquals(404, erro.getStatusCode().value());
        verify(solicitacaoRepository, never()).save(any());
    }

    @Test
    void aceitarProposta_clienteAusente_deveRetornar403() {
        when(solicitacaoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(solicitacao));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.clienteAceitarProposta(1L, 10L, 20L));

        assertEquals(403, erro.getStatusCode().value());
    }

    @Test
    void listarConsultas_deveUsarFiltrosDoRepositorio() {
        when(solicitacaoRepository.findByStatusSolicitacaoOrderByDataCriacaoDesc(StatusSolicitacao.SOLICITADO))
                .thenReturn(List.of(solicitacao));
        when(solicitacaoRepository.findByCliente_IdOrderByDataCriacaoDesc(10L)).thenReturn(List.of(solicitacao));
        when(solicitacaoRepository.findByPrestador_IdAndStatusSolicitacaoInOrderByDataCriacaoDesc(eq(20L), anyList()))
                .thenReturn(List.of(solicitacao));

        assertEquals(1, service.listarDisponiveis().size());
        assertEquals(1, service.listarDoCliente(10L).size());
        assertEquals(1, service.listarDoPrestador(20L).size());
        verify(solicitacaoRepository).findByStatusSolicitacaoOrderByDataCriacaoDesc(StatusSolicitacao.SOLICITADO);
        verify(solicitacaoRepository).findByCliente_IdOrderByDataCriacaoDesc(10L);
        verify(solicitacaoRepository).findByPrestador_IdAndStatusSolicitacaoInOrderByDataCriacaoDesc(eq(20L), anyList());
    }
}
