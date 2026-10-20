package com.es.servicehub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"solicitacao_id", "prestador_id"}))
public class PropostaServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private SolicitacaoServico solicitacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Usuario prestador;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorProposto;
}
