package com.es.servicehub.dto;

import com.es.servicehub.enums.Especialidades;

import java.util.List;

public record AtualizarPerfilUsuarioRequest(
        String nome,
        String telefone,
        List<Especialidades> especialidades) {
}
