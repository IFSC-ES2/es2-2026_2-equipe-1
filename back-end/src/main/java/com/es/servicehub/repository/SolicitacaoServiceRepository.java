package com.es.servicehub.repository;

import com.es.servicehub.model.SolicitacaoServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoServiceRepository extends JpaRepository<SolicitacaoServico, Long> {
}
