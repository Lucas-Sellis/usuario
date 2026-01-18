package com.example.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder //@Builder é para criar objetos complexos de forma clara, sem precisar construtor gigante ou muitos setters.

// Diz para o JPA que essa classe vira uma tabela no banco
@Entity

// Nome da tabela no banco de dados
@Table(name = "usuario")
public class Usuario implements UserDetails {

    // Chave primária da tabela
    @Id
    // ID gerado automaticamente pelo banco (auto increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome do usuário
    @Column(name = "nome", length = 100)
    private String nome;

    // Email do usuário (usado como login)
    @Column(name = "email", length = 100)
    private String email;

    // Senha do usuário (vai ser salva criptografada)
    @Column(name = "senha")
    private String senha;

    /*
     Um usuário pode ter vários endereços.
     cascade ALL:
     - salva o endereço junto com o usuário
     - deleta o endereço junto com o usuário
    */
    @OneToMany(cascade = CascadeType.ALL)
    // Cria a coluna usuario_id na tabela endereco
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private List<Endereco> enderecos;

    /*
     Um usuário pode ter vários telefones.
     Mesma lógica do endereço.
    */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private List<Telefone> telefones;

    /*
     Métodos obrigatórios do UserDetails (Spring Security)
    */

    // Aqui seriam os perfis (roles), mas não estamos usando
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    // Retorna a senha para o Spring Security
    @Override
    public @Nullable String getPassword() {
        return senha;
    }

    // Retorna o login do usuário (email)
    @Override
    public String getUsername() {
        return email;
    }
}
