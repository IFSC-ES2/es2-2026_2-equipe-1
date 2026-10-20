package com.es.servicehub.model;

import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @ManyToOne
    private Endereco enderecoAtendimento;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario prestador;

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PropostaServico> propostas = new ArrayList<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Especialidades> especialidadesRequiridas;

    public void adicionarProposta(PropostaServico proposta) {
        propostas.add(proposta);
        proposta.setSolicitacao(this);
    }
}
