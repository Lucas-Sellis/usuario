package com.example.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


@Entity

@Table(name = "endereco")
public class Endereco {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     Rua do endereço.
     Não defini length, então o banco usa o padrão.
    */
    @Column(name = "rua")
    private String rua;

    /*
     Número da casa ou apartamento.
     Usei Long para permitir valores maiores se precisar.
    */
    @Column(name = "numero")
    private Long numero;

    /*
     Complemento do endereço (apto, bloco, etc).
     Limitei para 10 caracteres.
    */
    @Column(name = "complemento", length = 10)
    private String complemento;

    /*
     Cidade do endereço.
     Dei um tamanho maior para evitar problema depois.
    */
    @Column(name = "cidade", length = 150)
    private String cidade;

    /*
     Estado no formato UF (ex: SP, RJ).
     Por isso length = 2.
    */
    @Column(name = "estado", length = 2)
    private String estado;

    /*
     CEP do endereço.
     Pode ter hífen, por isso usei String.
    */
    @Column(name = "cep", length = 9)
    private String cep;

    @Column (name = "usuario_id")
    private long usuario_id;
}
