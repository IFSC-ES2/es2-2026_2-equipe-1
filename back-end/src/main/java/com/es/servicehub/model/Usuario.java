package com.es.servicehub.model;

import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.TipoPerfil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;

    @JsonIgnore
    private String senha;

    private String cpf;
    private String cnpj;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private TipoPerfil tipoPerfil;

    @ElementCollection(targetClass = Especialidades.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "usuario_especialidades", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "especialidade")
    private List<Especialidades> especialidades = new ArrayList<>();

    @OneToOne
    private Endereco endereco;
}
