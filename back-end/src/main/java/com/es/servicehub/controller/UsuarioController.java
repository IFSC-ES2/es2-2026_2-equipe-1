package com.es.servicehub.controller;

import com.es.servicehub.dto.CadastroUsuarioRequest;
import com.es.servicehub.dto.AtualizarUsuarioRequest;
import com.es.servicehub.dto.LoginUsuarioRequest;
import com.es.servicehub.dto.LoginUsuarioRespostaDTO;
import com.es.servicehub.dto.UsuarioRespostaDTO;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.service.JwtTokenService;
import com.es.servicehub.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public UsuarioController(
            UsuarioService usuarioService,
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @ExceptionHandler({ RuntimeException.class, IllegalArgumentException.class })
    public ResponseEntity<Map<String, String>> tratarErro(Exception erro) {
        return ResponseEntity.badRequest().body(Map.of("message", erro.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> tratarErroDeStatus(ResponseStatusException erro) {
        return ResponseEntity.status(erro.getStatusCode()).body(Map.of("message", erro.getReason()));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioRespostaDTO> cadastrar(@RequestBody CadastroUsuarioRequest request) {
        try {
            Usuario novoUsuario = usuarioService.cadastrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioRespostaDTO.fromEntity(novoUsuario));
        } catch (RuntimeException erro) {
            throw new IllegalArgumentException(erro.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUsuarioRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getSenha()));
            Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());
            String token = jwtTokenService.emitir(authentication);
            return ResponseEntity.ok(LoginUsuarioRespostaDTO.fromEntity(usuario, token));
        } catch (RuntimeException erro) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciais inválidas. Verifique e-mail e senha."));
        }
    }

    @GetMapping("/csrf")
    public ResponseEntity<Map<String, Boolean>> csrf() {
        return ResponseEntity.ok(Map.of("enabled", false));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioRespostaDTO> usuarioAtual(Authentication authentication) {
        Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());
        return ResponseEntity.ok(UsuarioRespostaDTO.fromEntity(usuario));
    }

    @PostMapping
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.salvar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarUsuarioRequest request,
            Authentication authentication) {
        Usuario solicitante = usuarioService.buscarPorEmail(authentication.getName());
        Usuario usuario = usuarioService.atualizarPerfil(id, solicitante.getId(), request);
        return ResponseEntity.ok(UsuarioRespostaDTO.fromEntity(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}