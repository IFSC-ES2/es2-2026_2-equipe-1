package com.es.servicehub.dto;

import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRespostaDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private TipoPerfil tipoPerfil;
    private List<Especialidades> especialidades;

    public static UsuarioRespostaDTO fromEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioRespostaDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getTipoPerfil(),
                usuario.getEspecialidades() == null ? List.of() : usuario.getEspecialidades()
        );
    }
}
