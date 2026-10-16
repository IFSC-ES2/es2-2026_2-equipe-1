package com.es.servicehub.controller;

import com.es.servicehub.dto.AceitarSolicitacaoRequest;
import com.es.servicehub.dto.SolicitacaoServicoRespostaDTO;
import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.UsuarioRepository;
import com.es.servicehub.service.SolicitacaoServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping({"/api/solicitacao", "/api/solicitacoes"})
public class SolicitacaoServicoController {

    private final SolicitacaoServicoService solicitacaoServicoService;
    private final UsuarioRepository usuarioRepository;

    public SolicitacaoServicoController(
            SolicitacaoServicoService solicitacaoServicoService,
            UsuarioRepository usuarioRepository) {
        this.solicitacaoServicoService = solicitacaoServicoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoServicoRespostaDTO>> listarTodos() {
        return ResponseEntity.ok(solicitacaoServicoService.listarTodosComoResposta());
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<SolicitacaoServicoRespostaDTO>> listarDisponiveis() {
        return ResponseEntity.ok(solicitacaoServicoService.listarDisponiveis());
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<SolicitacaoServicoRespostaDTO>> listarMinhas(Authentication authentication) {
        Usuario usuario = usuarioAutenticado(authentication);
        return ResponseEntity.ok(solicitacaoServicoService.listarDoCliente(usuario.getId()));
    }

    @GetMapping("/meus-chamados")
    public ResponseEntity<List<SolicitacaoServicoRespostaDTO>> listarMeusChamados(Authentication authentication) {
        Usuario usuario = usuarioAutenticado(authentication);
        return ResponseEntity.ok(solicitacaoServicoService.listarDoPrestador(usuario.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoServicoRespostaDTO> buscarPorId(
            @PathVariable Long id, Authentication authentication) {
        SolicitacaoServicoRespostaDTO solicitacao = solicitacaoServicoService.buscarComoResposta(id);
        boolean administrador = authentication.getAuthorities().stream()
                .anyMatch(autoridade -> autoridade.getAuthority().equals("ROLE_ADMIN"));
        Usuario usuario = usuarioAutenticado(authentication);
        boolean participante = (solicitacao.cliente() != null
                && solicitacao.cliente().id().equals(usuario.getId()))
                || (solicitacao.prestador() != null
                && solicitacao.prestador().getId().equals(usuario.getId()));
        if (!administrador && !participante) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem acesso a esta solicitação.");
        }
        return ResponseEntity.ok(solicitacao);
    }

    @PostMapping
    public ResponseEntity<SolicitacaoServicoRespostaDTO> criar(
            @RequestBody SolicitacaoServico solicitacao, Authentication authentication) {
        solicitacao.setCliente(usuarioAutenticado(authentication));
        solicitacao.setPrestador(null);
        solicitacao.setStatusSolicitacao(null);
        solicitacao.setDataCriacao(null);
        return ResponseEntity.ok(solicitacaoServicoService.criarComoResposta(solicitacao));
    }

    @PostMapping("/{id}/aceitar")
    public ResponseEntity<SolicitacaoServicoRespostaDTO> aceitar(
            @PathVariable Long id,
            @RequestBody AceitarSolicitacaoRequest request,
            Authentication authentication) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o valor proposto.");
        }
        Usuario prestador = usuarioAutenticado(authentication);
        return ResponseEntity.ok(solicitacaoServicoService.aceitarSolicitacaoComoResposta(
                id, prestador.getId(), request.valorProposto()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoServicoRespostaDTO> atualizar(
            @PathVariable Long id,
            @RequestBody SolicitacaoServico solicitacao) {

        return ResponseEntity.ok(solicitacaoServicoService.atualizarComoResposta(id, solicitacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        solicitacaoServicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private Usuario usuarioAutenticado(Authentication authentication) {
        return usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado."));
    }
}
