package com.es.servicehub.repository;

import com.es.servicehub.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    List<Endereco> findByProprietario_Id(Long proprietarioId);
}