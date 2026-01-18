package com.example.usuario.business.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UsuarioDTO {

//    Explicação simples
//
//    API é uma forma de um sistema conversar com outro sistema.


//    DTO serve para transportar dados
//
//    Pensa assim:
//
//            ➡ O cliente (Postman, Front, Mobile) envia um JSON com dados.
//            ➡ A API precisa receber isso em algum objeto.
//            ➡ Esse objeto é o DTO.
//
//    E quando a API responde, ela também pode devolver DTO.



    // nome que chega da requisição (Postman, Front, etc)
    private String nome;

    // email que o usuário vai usar para login
    private String email;

    // senha normal (texto puro) — depois eu vou criptografar no service
    private String senha;

    // lista de endereços enviados na entrada (cada endereço também é um DTO)
    private List<EnderecoDTO> enderecos;

    // lista de telefones enviados na entrada (também DTO)
    private List<TelefoneDTO> telefones;
}


//Analogia simples
//
//Imagine um restaurante:
//
//        🍽 Cliente → faz o pedido
//📋 Garçom (DTO) → leva o pedido (dados) para a cozinha
//👨‍🍳 Cozinha (entity / banco) → processa
//🍽 Garçom (DTO) → leva o prato de volta para o cliente
//
//        Sem DTO, o cliente teria que entrar na cozinha.
//Seria uma bagunça 😅