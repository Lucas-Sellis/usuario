package com.example.usuario.controller;

import com.example.usuario.business.UsuarioService;
import com.example.usuario.business.dto.UsuarioDTO;
import com.example.usuario.infrastructure.entity.Usuario;
import com.example.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.salvaUsuario((usuarioDTO)));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO){

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
    public ResponseEntity<Usuario> buscaUsuarioPorEmail(
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
}


