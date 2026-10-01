package com.es.servicehub.controller;

import com.es.servicehub.dto.CadastroUsuarioRequest;
import com.es.servicehub.dto.LoginUsuarioRequest;
import com.es.servicehub.dto.UsuarioRespostaDTO;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public UsuarioController(UsuarioService usuarioService, AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @ExceptionHandler({ RuntimeException.class, IllegalArgumentException.class })
    public ResponseEntity<Map<String, String>> tratarErro(Exception erro) {
        return ResponseEntity.badRequest().body(Map.of("message", erro.getMessage()));
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
    public ResponseEntity<?> login(@RequestBody LoginUsuarioRequest request,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getSenha()));
                httpRequest.getSession(true);
                httpRequest.changeSessionId();
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, httpRequest, httpResponse);
            Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());
            return ResponseEntity.ok(UsuarioRespostaDTO.fromEntity(usuario));
        } catch (RuntimeException erro) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciais inválidas. Verifique e-mail e senha."));
        }
    }

    @GetMapping("/csrf")
    public ResponseEntity<Map<String, String>> csrf(CsrfToken csrfToken) {
        return ResponseEntity.ok(Map.of("token", csrfToken.getToken()));
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
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        Usuario usuario = usuarioService.atualizar(id, usuarioAtualizado);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}