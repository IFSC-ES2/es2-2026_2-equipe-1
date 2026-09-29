package com.es.servicehub.controller;

import com.es.servicehub.dto.CadastroUsuarioRequest;
import com.es.servicehub.dto.LoginUsuarioRequest;
import com.es.servicehub.dto.UsuarioRespostaDTO;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
    public ResponseEntity<UsuarioRespostaDTO> login(@RequestBody LoginUsuarioRequest request) {
        try {
            Usuario usuario = usuarioService.autenticar(request.getEmail(), request.getSenha());
            return ResponseEntity.ok(UsuarioRespostaDTO.fromEntity(usuario));
        } catch (RuntimeException erro) {
            throw new IllegalArgumentException(erro.getMessage());
        }
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