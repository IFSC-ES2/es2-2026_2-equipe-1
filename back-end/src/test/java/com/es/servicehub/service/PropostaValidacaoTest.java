package com.es.servicehub.service;

import com.es.servicehub.repository.SolicitacaoServiceRepository;
import com.es.servicehub.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class PropostaValidacaoTest {
    @Mock
    private SolicitacaoServiceRepository solicitacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SolicitacaoServicoService service;

    @Test
    void enviarProposta_valorNulo_deveRetornar400SemConsultarBanco() {
        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.enviarProposta(1L, 2L, null));

        assertEquals(400, erro.getStatusCode().value());
        verifyNoInteractions(solicitacaoRepository, usuarioRepository);
    }

    @Test
    void enviarProposta_valorNegativo_deveRetornar400SemConsultarBanco() {
        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.enviarProposta(1L, 2L, new BigDecimal("-0.01")));

        assertEquals(400, erro.getStatusCode().value());
        verifyNoInteractions(solicitacaoRepository, usuarioRepository);
    }
}
