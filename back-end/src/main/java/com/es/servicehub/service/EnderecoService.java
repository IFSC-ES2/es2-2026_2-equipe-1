package com.es.servicehub.service;

import com.es.servicehub.model.Endereco;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    public List<Endereco> listarDoUsuario(Long usuarioId) {
        return enderecoRepository.findByProprietario_Id(usuarioId);
    }

    public Optional<Endereco> buscarDoUsuario(Long enderecoId, Long usuarioId) {
        return enderecoRepository.findById(enderecoId)
                .filter(endereco -> endereco.getProprietario() != null
                        && endereco.getProprietario().getId().equals(usuarioId));
    }

    public Endereco criar(Endereco endereco, Usuario proprietario) {
        endereco.setId(null);
        endereco.setProprietario(proprietario);
        return enderecoRepository.save(endereco);
    }

    public Endereco criar(Endereco endereco) {
        endereco.setId(null);
        return enderecoRepository.save(endereco);
    }
}