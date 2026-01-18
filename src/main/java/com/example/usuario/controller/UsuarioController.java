package com.example.usuario.controller;

import com.example.usuario.business.UsuarioService;
import com.example.usuario.business.dto.EnderecoDTO;
import com.example.usuario.business.dto.TelefoneDTO;
import com.example.usuario.business.dto.UsuarioDTO;
import com.example.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/usuario")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario((usuarioDTO)));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {

        // valida email e senha
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuarioDTO.getEmail(),
                        usuarioDTO.getSenha()
                )
        );

        // gera e retorna o token JWT
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    // GET /usuario?email=...
    // busca usuário pelo email
    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(
            @RequestParam("email") String email) {

        return ResponseEntity.ok(
                usuarioService.buscarUsuarioPorEmail(email)
        );
    }


    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(
            @PathVariable String email) {

        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping // Endpoint HTTP do tipo PUT (usado para atualização de dados)
    public ResponseEntity<UsuarioDTO> atualizaDadoUsuario(
            @RequestBody UsuarioDTO dto, // Pega o JSON enviado no corpo da requisição e converte para DTO
            @RequestHeader("Authorization") String token) { // Pega o token JWT que veio no header "Authorization"

        // Chama o serviço para atualizar os dados do usuário usando o token + os dados enviados
        // e retorna a resposta HTTP 200 (OK) com o DTO do usuário atualizado
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }

    @PutMapping("/endereco") // feito em 14/01
    public ResponseEntity<EnderecoDTO> atualizaEndereco(@RequestBody EnderecoDTO dto,
                                                        @RequestParam("id") Long id) {
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto));
    }

    @PutMapping("/telefone") // feito em 14/01
    public ResponseEntity<TelefoneDTO> atualizaTelefone(@RequestBody TelefoneDTO dto,
                                                        @RequestParam("id") Long id) {
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto));
    }

    @PostMapping("/endereco")
    public ResponseEntity<EnderecoDTO> cadastraEndereco(@RequestBody EnderecoDTO dto,
                                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, dto));
    }

    @PostMapping("/telefone")
    public ResponseEntity<TelefoneDTO> cadastraTelefone(@RequestBody TelefoneDTO dto,
                                                        @RequestHeader("Authorization") String token) {
        // Corrigido para chamar o método de telefone do service
        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, dto));
    }

}

