package com.amaro.usuario.business;

import com.amaro.usuario.business.dto.UsuarioDTO;
import com.amaro.usuario.infrastructure.entity.Usuario;
import com.amaro.usuario.infrastructure.exceptions.ConflictException;
import com.amaro.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.amaro.usuario.infrastructure.repository.UsuarioRepository;
import com.amaro.usuario.infrastructure.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioConverter usuarioConverter, PasswordEncoder passwordEncoder,JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioConverter = usuarioConverter;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioDTO){
        checarEmail(usuarioDTO.getEmail());

        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario.setSenha(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);

        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email){
        Usuario usuario =  usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email Não encontrado !" + email));
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void deletarPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizarUser (String token, UsuarioDTO dto){
        String email = jwtUtil.extractUsername(token.substring(7));
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null );

        Usuario usuarioEntity = usuarioRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("email não encontrado"));

        Usuario usuario = usuarioConverter.checkUser(dto, usuarioEntity);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    private void checarEmail(String email) {
        try {
            boolean existe = verificarEmailExiste(email);
            if (existe)
                throw new ConflictException("Email já cadastrado");
        } catch (Exception e) {
            throw new ConflictException("Email já cadastrado", e.getCause());
        }
    }

    private boolean verificarEmailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
