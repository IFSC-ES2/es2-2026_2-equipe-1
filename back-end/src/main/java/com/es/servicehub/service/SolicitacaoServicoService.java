package com.es.servicehub.service;

import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.repository.SolicitacaoServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitacaoServicoService {
    private final SolicitacaoServiceRepository solicitacaoServiceRepository;

    public SolicitacaoServicoService(SolicitacaoServiceRepository solicitacaoServiceRepository) {
        this.solicitacaoServiceRepository = solicitacaoServiceRepository;
    }

    public List<SolicitacaoServico> listarTodos() {
        return solicitacaoServiceRepository.findAll();
    }

    public SolicitacaoServico buscarPorId(Long id) {
        return solicitacaoServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
    }

    public SolicitacaoServico criar(SolicitacaoServico solicitacao) {
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
