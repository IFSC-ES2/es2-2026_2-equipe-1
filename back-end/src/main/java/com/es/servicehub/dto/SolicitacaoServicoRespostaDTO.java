package com.es.servicehub.dto;

import com.es.servicehub.enums.Especialidades;
import com.es.servicehub.enums.StatusSolicitacao;
import com.es.servicehub.model.Endereco;
import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SolicitacaoServicoRespostaDTO(
        Long id,
        String titulo,
        String descricao,
        BigDecimal valorEstimado,
        StatusSolicitacao statusSolicitacao,
        LocalDateTime dataCriacao,
        EnderecoRespostaDTO enderecoAtendimento,
        List<Especialidades> especialidadesRequiridas,
        UsuarioResumoDTO cliente,
        UsuarioRespostaDTO prestador) {

    public static SolicitacaoServicoRespostaDTO fromEntity(SolicitacaoServico solicitacao) {
        return new SolicitacaoServicoRespostaDTO(
                solicitacao.getId(),
                solicitacao.getTitulo(),
                solicitacao.getDescricao(),
                solicitacao.getValorEstimado(),
                solicitacao.getStatusSolicitacao(),
                solicitacao.getDataCriacao(),
                EnderecoRespostaDTO.fromEntity(solicitacao.getEnderecoAtendimento()),
                solicitacao.getEspecialidadesRequiridas() == null
                        ? List.of()
                        : List.copyOf(solicitacao.getEspecialidadesRequiridas()),
                UsuarioResumoDTO.fromEntity(solicitacao.getCliente()),
                UsuarioRespostaDTO.fromEntity(solicitacao.getPrestador()));
    }

    public record UsuarioResumoDTO(Long id, String nome) {
        private static UsuarioResumoDTO fromEntity(Usuario usuario) {
            if (usuario == null) {
                return null;
            }
            return new UsuarioResumoDTO(usuario.getId(), usuario.getNome());
        }
    }

    public record EnderecoRespostaDTO(
            Long id,
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado,
            String cep) {

        private static EnderecoRespostaDTO fromEntity(Endereco endereco) {
            if (endereco == null) {
                return null;
            }
            return new EnderecoRespostaDTO(
                    endereco.getId(),
                    endereco.getLogradouro(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getBairro(),
                    endereco.getCidade(),
                    endereco.getEstado(),
                    endereco.getCep());
        }
    }
}
