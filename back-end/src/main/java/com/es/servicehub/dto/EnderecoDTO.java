package com.es.servicehub.dto;

public record EnderecoDTO(
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado) {
}
