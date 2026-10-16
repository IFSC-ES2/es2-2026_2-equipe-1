package com.es.servicehub.service;

import com.es.servicehub.dto.SolicitacaoServicoRespostaDTO;
import com.es.servicehub.enums.StatusSolicitacao;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.SolicitacaoServiceRepository;
import com.es.servicehub.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitacaoServicoService {
    private final SolicitacaoServiceRepository solicitacaoServiceRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitacaoServicoService(
            SolicitacaoServiceRepository solicitacaoServiceRepository,
            UsuarioRepository usuarioRepository) {
        this.solicitacaoServiceRepository = solicitacaoServiceRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoServicoRespostaDTO> listarDisponiveis() {
        return solicitacaoServiceRepository
                .findByStatusSolicitacaoOrderByDataCriacaoDesc(StatusSolicitacao.SOLICITADO)
                .stream()
                .map(SolicitacaoServicoRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoServicoRespostaDTO> listarDoCliente(Long clienteId) {
        return solicitacaoServiceRepository.findByCliente_IdOrderByDataCriacaoDesc(clienteId)
                .stream()
                .map(SolicitacaoServicoRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoServicoRespostaDTO> listarDoPrestador(Long prestadorId) {
        return solicitacaoServiceRepository.findByPrestador_IdAndStatusSolicitacaoInOrderByDataCriacaoDesc(
                        prestadorId, List.of(StatusSolicitacao.ACEITO, StatusSolicitacao.EM_ANDAMENTO))
                .stream()
                .map(SolicitacaoServicoRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoServicoRespostaDTO> listarTodosComoResposta() {
        return solicitacaoServiceRepository.findAll().stream()
                .map(SolicitacaoServicoRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public SolicitacaoServicoRespostaDTO buscarComoResposta(Long id) {
        return SolicitacaoServicoRespostaDTO.fromEntity(buscarPorId(id));
    }

    @Transactional
    public SolicitacaoServicoRespostaDTO criarComoResposta(SolicitacaoServico solicitacao) {
        return SolicitacaoServicoRespostaDTO.fromEntity(criar(solicitacao));
    }

    @Transactional
    public SolicitacaoServicoRespostaDTO atualizarComoResposta(Long id, SolicitacaoServico solicitacao) {
        return SolicitacaoServicoRespostaDTO.fromEntity(atualizar(id, solicitacao));
    }

    @Transactional
    public SolicitacaoServico aceitarSolicitacao(Long solicitacaoId, Long prestadorId, BigDecimal valorProposto) {
        if (valorProposto == null || valorProposto.signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um valor proposto maior que zero.");
        }

        SolicitacaoServico solicitacao = solicitacaoServiceRepository.findByIdForUpdate(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação não encontrada."));
        if (solicitacao.getStatusSolicitacao() != StatusSolicitacao.SOLICITADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta solicitação já foi aceita ou não está disponível.");
        }

        Usuario prestador = usuarioRepository.findById(prestadorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestador não encontrado."));
        if (prestador.getTipoPerfil() != TipoPerfil.PRESTADOR) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas prestadores podem aceitar solicitações.");
        }

        solicitacao.setPrestador(prestador);
        solicitacao.setValorEstimado(valorProposto);
        solicitacao.setStatusSolicitacao(StatusSolicitacao.EM_ANDAMENTO);
        return solicitacaoServiceRepository.save(solicitacao);
    }

    @Transactional
    public SolicitacaoServicoRespostaDTO aceitarSolicitacaoComoResposta(
            Long solicitacaoId, Long prestadorId, BigDecimal valorProposto) {
        return SolicitacaoServicoRespostaDTO.fromEntity(
                aceitarSolicitacao(solicitacaoId, prestadorId, valorProposto));
    }

    public List<SolicitacaoServico> listarTodos() {
        return solicitacaoServiceRepository.findAll();
    }

    public SolicitacaoServico buscarPorId(Long id) {
        return solicitacaoServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
    }

    public SolicitacaoServico criar(SolicitacaoServico solicitacao) {
        if (solicitacao.getStatusSolicitacao() == null) {
            solicitacao.setStatusSolicitacao(StatusSolicitacao.SOLICITADO);
        }
        if (solicitacao.getDataCriacao() == null) {
            solicitacao.setDataCriacao(LocalDateTime.now());
        }
        return solicitacaoServiceRepository.save(solicitacao);
    }

    public SolicitacaoServico atualizar(Long id, SolicitacaoServico solicitacao) {
        SolicitacaoServico existente = buscarPorId(id);

        existente.setTitulo(solicitacao.getTitulo());
        existente.setDescricao(solicitacao.getDescricao());
        existente.setValorEstimado(solicitacao.getValorEstimado());
        existente.setStatusSolicitacao(solicitacao.getStatusSolicitacao());
        existente.setDataCriacao(solicitacao.getDataCriacao());
        existente.setEnderecoAtendimento(solicitacao.getEnderecoAtendimento());
        existente.setEspecialidadesRequiridas(solicitacao.getEspecialidadesRequiridas());

        return solicitacaoServiceRepository.save(existente);
    }

    public void deletar(Long id) {
        SolicitacaoServico solicitacao = buscarPorId(id);
        solicitacaoServiceRepository.delete(solicitacao);
    }
}
