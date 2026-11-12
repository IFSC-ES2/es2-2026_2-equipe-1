package com.es.servicehub.dto;

import com.es.servicehub.enums.Especialidades;

import java.util.List;

public record AtualizarUsuarioRequest(
        String nome,
        String telefone,
        List<Especialidades> especialidades,
        EnderecoDTO endereco) {
}
