package com.es.servicehub.service;

import com.es.servicehub.model.Endereco;
import com.es.servicehub.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    public Endereco criar(Endereco endereco) {
        endereco.setId(null);
        return enderecoRepository.save(endereco);
    }
}