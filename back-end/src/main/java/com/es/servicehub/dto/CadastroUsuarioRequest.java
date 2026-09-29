package com.es.servicehub.dto;

import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.TipoPerfil;
import lombok.Data;

import java.util.List;

@Data
public class CadastroUsuarioRequest {
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private TipoPerfil tipoPerfil;
    private List<Especialidades> especialidades;
}
