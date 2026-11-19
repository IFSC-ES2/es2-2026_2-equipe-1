package com.es.servicehub.controller;

import com.es.servicehub.model.Endereco;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.UsuarioRepository;
import com.es.servicehub.service.EnderecoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {
    private final EnderecoService enderecoService;
    private final UsuarioRepository usuarioRepository;

    public EnderecoController(EnderecoService enderecoService, UsuarioRepository usuarioRepository) {
        this.enderecoService = enderecoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> listarTodos(Authentication authentication) {
        Usuario usuario = usuarioAutenticado(authentication);
        return ResponseEntity.ok(enderecoService.listarDoUsuario(usuario.getId()));
    }

    @PostMapping
    public ResponseEntity<Endereco> criar(@RequestBody Endereco endereco, Authentication authentication) {
        Usuario usuario = usuarioAutenticado(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.criar(endereco, usuario));
    }

    private Usuario usuarioAutenticado(Authentication authentication) {
        return usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado."));
    }
}