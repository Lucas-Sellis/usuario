package com.example.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

// Diz para o JPA que essa classe vira uma tabela no banco
@Entity

// Nome da tabela no banco de dados
@Table(name = "telefone")
public class Telefone {

    // Chave primária da tabela
    @Id
    // ID gerado automaticamente pelo banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     Número do telefone.
     Usei String porque telefone não é usado para cálculo.
    */
    @Column(name = "numero", length = 10)
    private String numero;

    /*
     DDD do telefone.
     Também é String para manter zeros à esquerda (ex: 011).
    */
    @Column(name = "ddd", length = 3)
    private String ddd;
}
