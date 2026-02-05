package com.amaro.usuario.business;

import com.amaro.usuario.business.dto.EnderecoDTO;
import com.amaro.usuario.business.dto.TelefoneDTO;
import com.amaro.usuario.business.dto.UsuarioDTO;
import com.amaro.usuario.infrastructure.entity.Endereco;
import com.amaro.usuario.infrastructure.entity.Telefone;
import com.amaro.usuario.infrastructure.entity.Usuario;
import com.amaro.usuario.infrastructure.exceptions.ConflictException;
import com.amaro.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.amaro.usuario.infrastructure.repository.EnderecoRepository;
import com.amaro.usuario.infrastructure.repository.TelefoneRepository;
import com.amaro.usuario.infrastructure.repository.UsuarioRepository;
import com.amaro.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

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

    public EnderecoDTO atualizarEndereco(Long id, EnderecoDTO dto){
        Endereco enderecoEntity = enderecoRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Endereço não encontrado !"));
        Endereco endereco = usuarioConverter.checkEndereco(dto, enderecoEntity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizarTelefone(Long id, TelefoneDTO dto){
        Telefone telefoneEntity = telefoneRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Telefone não encontrado !"));
        Telefone telefone = usuarioConverter.checkTelefone(dto, telefoneEntity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
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
