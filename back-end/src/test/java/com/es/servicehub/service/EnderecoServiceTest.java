package com.es.servicehub.service;

import com.es.servicehub.model.Endereco;
import com.es.servicehub.repository.EnderecoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {
    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    @Test
    void listarTodos_deveDelegarAoRepositorio() {
        Endereco endereco = new Endereco();
        when(enderecoRepository.findAll()).thenReturn(List.of(endereco));

        List<Endereco> resultado = enderecoService.listarTodos();

        assertEquals(1, resultado.size());
        assertSame(endereco, resultado.getFirst());
        verify(enderecoRepository).findAll();
    }

    @Test
    void criar_deveIgnorarIdRecebidoParaEvitarSobrescrita() {
        Endereco endereco = new Endereco();
        endereco.setId(99L);
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Endereco resultado = enderecoService.criar(endereco);

        assertSame(endereco, resultado);
        assertEquals(null, endereco.getId());
        verify(enderecoRepository).save(endereco);
    }
}
