package com.es.servicehub.controller;

import com.es.servicehub.model.SolicitacaoServico;
import com.es.servicehub.service.SolicitacaoServicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacao")
@CrossOrigin(origins = "http://localhost:5173")
public class SolicitacaoServicoController {

    private final SolicitacaoServicoService solicitacaoServicoService;

    public SolicitacaoServicoController(SolicitacaoServicoService solicitacaoServicoService) {
        this.solicitacaoServicoService = solicitacaoServicoService;
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoServico>> listarTodos() {
        return ResponseEntity.ok(solicitacaoServicoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoServico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitacaoServicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<SolicitacaoServico> criar(
            @RequestBody SolicitacaoServico solicitacao) {

        return ResponseEntity.ok(solicitacaoServicoService.criar(solicitacao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoServico> atualizar(
            @PathVariable Long id,
            @RequestBody SolicitacaoServico solicitacao) {

        return ResponseEntity.ok(solicitacaoServicoService.atualizar(id, solicitacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        solicitacaoServicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
