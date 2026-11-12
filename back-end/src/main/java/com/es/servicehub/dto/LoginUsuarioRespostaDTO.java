package com.es.servicehub.dto;

import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.Endereco;
import com.es.servicehub.model.Usuario;

import java.util.List;

public record LoginUsuarioRespostaDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        TipoPerfil tipoPerfil,
        List<Especialidades> especialidades,
        Endereco endereco,
        String token) {

    public static LoginUsuarioRespostaDTO fromEntity(Usuario usuario, String token) {
        return new LoginUsuarioRespostaDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getTipoPerfil(),
                usuario.getEspecialidades() == null ? List.of() : usuario.getEspecialidades(),
                usuario.getEndereco(),
                token);
    }
}
