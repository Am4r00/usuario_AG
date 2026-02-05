package com.amaro.usuario.controller;

import com.amaro.usuario.business.UsuarioService;
import com.amaro.usuario.business.dto.UsuarioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(usuarioDTO));
    }
}
