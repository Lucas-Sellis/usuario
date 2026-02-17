package com.example.usuario.infrastructure.repository;

import com.example.usuario.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repositório da entidade Endereco
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    // métodos básicos já vêm do JpaRepository
    // save, findById, findAll, delete, etc.
}
