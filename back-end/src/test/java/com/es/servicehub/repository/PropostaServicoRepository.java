package com.es.servicehub.repository;

import com.es.servicehub.model.PropostaServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostaServicoRepository extends JpaRepository<PropostaServico, Long> {
    long countBySolicitacao_Id(Long solicitacaoId);

    List<PropostaServico> findBySolicitacao_Id(Long solicitacaoId);
}
