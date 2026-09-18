package com.es.servicehub.service;

import com.es.servicehub.enums.StatusSolicitacao;
import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.repository.SolicitacaoServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoServicoServiceTest {

    @Mock
    private SolicitacaoServiceRepository solicitacaoServiceRepository;

    @InjectMocks
    private SolicitacaoServicoService solicitacaoServicoService;

    private SolicitacaoServico solicitacao;

    @BeforeEach
    void setUp() {
        solicitacao = new SolicitacaoServico();
        solicitacao.setId(1L);
        solicitacao.setTitulo("Conserto de torneira");
        solicitacao.setDescricao("Vazamento na cozinha");
        solicitacao.setValorEstimado(new BigDecimal("100.00"));
        solicitacao.setStatusSolicitacao(StatusSolicitacao.SOLICITADO);
    }

    @Test
    void listarTodos_deveRetornarListaDeSolicitacoes() {
        SolicitacaoServico outra = new SolicitacaoServico();
        outra.setId(2L);
        when(solicitacaoServiceRepository.findAll()).thenReturn(Arrays.asList(solicitacao, outra));

        List<SolicitacaoServico> resultado = solicitacaoServicoService.listarTodos();

        assertEquals(2, resultado.size());
        verify(solicitacaoServiceRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_quandoExiste_deveRetornarSolicitacao() {
        when(solicitacaoServiceRepository.findById(1L)).thenReturn(Optional.of(solicitacao));

        SolicitacaoServico resultado = solicitacaoServicoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Conserto de torneira", resultado.getTitulo());
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarExcecao() {
        when(solicitacaoServiceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> solicitacaoServicoService.buscarPorId(99L));
    }

    @Test
    void criar_deveSalvarNovaSolicitacao() {
        when(solicitacaoServiceRepository.save(solicitacao)).thenReturn(solicitacao);

        SolicitacaoServico resultado = solicitacaoServicoService.criar(solicitacao);

        assertNotNull(resultado);
        assertEquals("Conserto de torneira", resultado.getTitulo());
        verify(solicitacaoServiceRepository, times(1)).save(solicitacao);
    }

    @Test
    void atualizar_deveAtualizarDadosDaSolicitacaoExistente() {
        SolicitacaoServico dadosNovos = new SolicitacaoServico();
        dadosNovos.setTitulo("Titulo atualizado");
        dadosNovos.setDescricao("Descricao atualizada");
        dadosNovos.setValorEstimado(new BigDecimal("200.00"));
        dadosNovos.setStatusSolicitacao(StatusSolicitacao.EM_ANDAMENTO);

        when(solicitacaoServiceRepository.findById(1L)).thenReturn(Optional.of(solicitacao));
        when(solicitacaoServiceRepository.save(any(SolicitacaoServico.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        SolicitacaoServico resultado = solicitacaoServicoService.atualizar(1L, dadosNovos);

        assertEquals("Titulo atualizado", resultado.getTitulo());
        assertEquals(StatusSolicitacao.EM_ANDAMENTO, resultado.getStatusSolicitacao());
        assertEquals(new BigDecimal("200.00"), resultado.getValorEstimado());
    }

    @Test
    void atualizar_quandoSolicitacaoNaoExiste_deveLancarExcecao() {
        when(solicitacaoServiceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> solicitacaoServicoService.atualizar(1L, solicitacao));
    }

    @Test
    void deletar_deveRemoverSolicitacaoExistente() {
        when(solicitacaoServiceRepository.findById(1L)).thenReturn(Optional.of(solicitacao));
        doNothing().when(solicitacaoServiceRepository).delete(solicitacao);

        solicitacaoServicoService.deletar(1L);

        verify(solicitacaoServiceRepository, times(1)).delete(solicitacao);
    }

    @Test
    void deletar_quandoSolicitacaoNaoExiste_deveLancarExcecao() {
        when(solicitacaoServiceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> solicitacaoServicoService.deletar(1L));
        verify(solicitacaoServiceRepository, never()).delete(any());
    }
}
