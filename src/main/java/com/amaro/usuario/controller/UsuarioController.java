package com.amaro.usuario.controller;

import com.amaro.usuario.business.UsuarioService;
import com.amaro.usuario.business.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(usuarioDTO));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }


}
