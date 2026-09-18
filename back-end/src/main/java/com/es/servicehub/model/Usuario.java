package com.es.servicehub.model;

import com.es.servicehub.enums.TipoPerfil;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String cnpj;
    private String telefone;

    private TipoPerfil tipoPerfil;

    @OneToOne
    private Endereco endereco;
}
