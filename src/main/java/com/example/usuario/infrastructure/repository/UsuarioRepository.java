package com.example.usuario.infrastructure.repository;


import com.example.usuario.infrastructure.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// indica que essa interface acessa o banco
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // verifica se existe usuário com esse email
    boolean existsByEmail(String email);

    // busca usuário pelo email
    Optional<Usuario> findByEmail(String email);

    // deleta usuário pelo email
    @Transactional
    void deleteByEmail(String email);
}

//Ideia principal (bem curta)
//
//        Repository fala com o banco
//
//        não tem regra de negócio
//
//        não recebe HTTP
//
//        Spring cria tudo automaticamente