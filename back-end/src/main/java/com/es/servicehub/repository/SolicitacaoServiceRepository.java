package com.es.servicehub.repository;

import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.enums.StatusSolicitacao;
import com.es.servicehub.enums.Especialidades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface SolicitacaoServiceRepository extends JpaRepository<SolicitacaoServico, Long> {
    List<SolicitacaoServico> findByStatusSolicitacaoOrderByDataCriacaoDesc(StatusSolicitacao status);

    @Query("""
            select distinct s from SolicitacaoServico s
            join s.especialidadesRequiridas especialidade
            where s.statusSolicitacao = :status
              and especialidade in :especialidades
            order by s.dataCriacao desc
            """)
    List<SolicitacaoServico> findDisponiveisPorEspecialidades(
            @Param("status") StatusSolicitacao status,
            @Param("especialidades") List<Especialidades> especialidades);

    List<SolicitacaoServico> findByCliente_IdOrderByDataCriacaoDesc(Long clienteId);

    List<SolicitacaoServico> findByPrestador_IdAndStatusSolicitacaoInOrderByDataCriacaoDesc(
            Long prestadorId, List<StatusSolicitacao> status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SolicitacaoServico s where s.id = :id")
    Optional<SolicitacaoServico> findByIdForUpdate(@Param("id") Long id);
}
