package com.example.usuario.infrastructure.repository.repository;

import com.example.demo.infrastructure.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// repositório da entidade Telefone
@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

    // não precisa de métodos extras por enquanto
    // JpaRepository já fornece save, findById, findAll, delete, etc.
}


//        Esse repository só existe para:
//
//        salvar telefone
//
//        buscar telefone
//
//        deletar telefone
//
//        Tudo isso já vem pronto do JpaRepositor