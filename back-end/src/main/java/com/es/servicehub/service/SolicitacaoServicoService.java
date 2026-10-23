package com.es.servicehub.service;

import com.es.servicehub.dto.SolicitacaoServicoRespostaDTO;
import com.es.servicehub.enums.StatusSolicitacao;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.PropostaServico;
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
                .map(solicitacao -> SolicitacaoServicoRespostaDTO.fromEntity(solicitacao, false))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoServicoRespostaDTO> listarDoCliente(Long clienteId) {
        return solicitacaoServiceRepository.findByCliente_IdOrderByDataCriacaoDesc(clienteId)
                .stream()
                .map(solicitacao -> SolicitacaoServicoRespostaDTO.fromEntity(solicitacao, true))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoServicoRespostaDTO> listarDoPrestador(Long prestadorId) {
        return solicitacaoServiceRepository.findByPrestador_IdAndStatusSolicitacaoInOrderByDataCriacaoDesc(
                        prestadorId, List.of(StatusSolicitacao.ACEITO, StatusSolicitacao.EM_ANDAMENTO))
                .stream()
        .map(solicitacao -> SolicitacaoServicoRespostaDTO.fromEntity(solicitacao, false))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoServicoRespostaDTO> listarTodosComoResposta() {
        return solicitacaoServiceRepository.findAll().stream()
                .map(solicitacao -> SolicitacaoServicoRespostaDTO.fromEntity(solicitacao, true))
                .toList();
    }

    @Transactional(readOnly = true)
    public SolicitacaoServicoRespostaDTO buscarComoResposta(Long id) {
        return SolicitacaoServicoRespostaDTO.fromEntity(buscarPorId(id), true);
    }

    @Transactional
    public SolicitacaoServicoRespostaDTO criarComoResposta(SolicitacaoServico solicitacao) {
        return SolicitacaoServicoRespostaDTO.fromEntity(criar(solicitacao), true);
    }

    @Transactional
    public SolicitacaoServicoRespostaDTO atualizarComoResposta(Long id, SolicitacaoServico solicitacao) {
        return SolicitacaoServicoRespostaDTO.fromEntity(atualizar(id, solicitacao), true);
    }

    @Transactional
    public SolicitacaoServico enviarProposta(Long solicitacaoId, Long prestadorId, BigDecimal valorProposto) {
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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas prestadores podem enviar propostas.");
        }

        PropostaServico proposta = solicitacao.getPropostas().stream()
                .filter(item -> item.getPrestador().getId().equals(prestadorId))
                .findFirst()
                .orElseGet(() -> {
                    PropostaServico novaProposta = new PropostaServico();
                    novaProposta.setPrestador(prestador);
                    solicitacao.adicionarProposta(novaProposta);
                    return novaProposta;
                });
        proposta.setValorProposto(valorProposto);
        return solicitacaoServiceRepository.save(solicitacao);
    }

    @Transactional
    public SolicitacaoServicoRespostaDTO enviarPropostaComoResposta(
            Long solicitacaoId, Long prestadorId, BigDecimal valorProposto) {
        return SolicitacaoServicoRespostaDTO.fromEntity(
                enviarProposta(solicitacaoId, prestadorId, valorProposto), false);
    }

    @Transactional
    public SolicitacaoServico clienteAceitarProposta(Long solicitacaoId, Long clienteId, Long prestadorId) {
        SolicitacaoServico solicitacao = solicitacaoServiceRepository.findByIdForUpdate(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação não encontrada."));
        if (solicitacao.getCliente() == null || !solicitacao.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o cliente da solicitação pode aceitar propostas.");
        }
        if (solicitacao.getStatusSolicitacao() != StatusSolicitacao.SOLICITADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta solicitação já foi aceita ou não está disponível.");
        }

        PropostaServico proposta = solicitacao.getPropostas().stream()
                .filter(item -> item.getPrestador().getId().equals(prestadorId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proposta não encontrada."));

        solicitacao.setPrestador(proposta.getPrestador());
        solicitacao.setValorEstimado(proposta.getValorProposto());
        solicitacao.setStatusSolicitacao(StatusSolicitacao.EM_ANDAMENTO);
        return solicitacaoServiceRepository.save(solicitacao);
    }

    @Transactional
    public SolicitacaoServicoRespostaDTO clienteAceitarPropostaComoResposta(
            Long solicitacaoId, Long clienteId, Long prestadorId) {
        return SolicitacaoServicoRespostaDTO.fromEntity(
                clienteAceitarProposta(solicitacaoId, clienteId, prestadorId), true);
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
