package com.es.servicehub.model;

import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class SolicitacaoServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private BigDecimal valorEstimado;
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusSolicitacao;
    private LocalDateTime dataCriacao;

    @OneToOne
    private Endereco enderecoAtendimento;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Especialidades> especialidadesRequiridas;
}
