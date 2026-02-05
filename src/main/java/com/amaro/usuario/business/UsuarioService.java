package com.amaro.usuario.business;

import com.amaro.usuario.business.dto.UsuarioDTO;
import com.amaro.usuario.infrastructure.entity.Usuario;
import com.amaro.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private  final UsuarioConverter usuarioConverter;


    public UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuarioRepository.save(usuario);

        return usuarioConverter.paraUsuarioDTO(usuario);
    }
}
