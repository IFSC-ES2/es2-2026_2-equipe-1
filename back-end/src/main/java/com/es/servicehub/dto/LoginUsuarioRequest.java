package com.es.servicehub.dto;

import lombok.Data;

@Data
public class LoginUsuarioRequest {
    private String email;
    private String senha;
}
