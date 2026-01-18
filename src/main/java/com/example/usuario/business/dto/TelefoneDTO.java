package com.example.usuario.business.dto;


import lombok.*;

@Builder


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TelefoneDTO {

    private Long id;
    private String numero;
    private String ddd;


}
