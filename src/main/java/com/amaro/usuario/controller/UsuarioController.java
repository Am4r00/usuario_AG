package com.amaro.usuario.controller;

import com.amaro.usuario.business.UsuarioService;
import com.amaro.usuario.business.dto.EnderecoDTO;
import com.amaro.usuario.business.dto.LoginDTO;
import com.amaro.usuario.business.dto.TelefoneDTO;
import com.amaro.usuario.business.dto.UsuarioDTO;
import com.amaro.usuario.infrastructure.entity.Telefone;
import com.amaro.usuario.infrastructure.repository.EnderecoRepository;
import com.amaro.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO){
        Authentication authentication  = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
                        loginDTO.getSenha())
        );
        return jwtUtil.generateToken(authentication.getName());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(usuarioDTO));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaPorEmail(@PathVariable String email){
        usuarioService.deletarPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizarUser(@RequestBody UsuarioDTO usuarioDTO, @RequestHeader String token ){
        return ResponseEntity.ok(usuarioService.atualizarUser(token,usuarioDTO));
    }

    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizarEndereco (@RequestBody EnderecoDTO enderecoDTO, @RequestParam("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizarEndereco(id,enderecoDTO));
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizarTelefone(@RequestBody TelefoneDTO telefoneDTO, @RequestParam("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizarTelefone(id,telefoneDTO));
    }
}
