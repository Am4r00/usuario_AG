package com.amaro.usuario.business;

import com.amaro.usuario.business.dto.UsuarioDTO;
import com.amaro.usuario.infrastructure.entity.Usuario;
import com.amaro.usuario.infrastructure.exceptions.ConflictException;
import com.amaro.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.amaro.usuario.infrastructure.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioConverter usuarioConverter, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioConverter = usuarioConverter;
        this.passwordEncoder = passwordEncoder;
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
