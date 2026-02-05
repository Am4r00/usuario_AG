package com.amaro.usuario.business;

import com.amaro.usuario.business.dto.EnderecoDTO;
import com.amaro.usuario.business.dto.TelefoneDTO;
import com.amaro.usuario.business.dto.UsuarioDTO;
import com.amaro.usuario.infrastructure.entity.Endereco;
import com.amaro.usuario.infrastructure.entity.Telefone;
import com.amaro.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioDTO usuarioDTO){
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEnderecos(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefone(usuarioDTO.getTelefones()))
                .build();
    }

    private List<Endereco> paraListaEnderecos(List<EnderecoDTO> enderecoDTO){
        return enderecoDTO.stream().map(this::paraEndereco).toList();
    }

    private Endereco paraEndereco(EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    private List<Telefone> paraListaTelefone(List<TelefoneDTO> telefoneDTOS){
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }


    private Telefone paraTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    public  UsuarioDTO paraUsuarioDTO (Usuario usuario){
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(paraListaEnderecosDTO(usuario.getEnderecos()))
                .telefones(paraListaTelefoneDTO(usuario.getTelefones()))
                .build();
    }

    private  List<EnderecoDTO> paraListaEnderecosDTO(List<Endereco> endereco){
        return endereco.stream().map(this::paraEnderecoDTO).toList();
    }

    public  EnderecoDTO paraEnderecoDTO(Endereco endereco){
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .estado(endereco.getEstado())
                .build();
    }

    private  List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefones){
        return telefones.stream().map(this::paraTelefoneDTO).toList();
    }


    public  TelefoneDTO paraTelefoneDTO(Telefone telefone){
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    public Usuario checkUser(UsuarioDTO dto, Usuario usuario){
        return Usuario.builder()
                .nome(dto.getNome() != null ? dto.getNome() : usuario.getNome())
                .id(usuario.getId())
                .senha(dto.getSenha() != null ? dto.getSenha() : usuario.getSenha())
                .email(dto.getEmail() != null ? dto.getEmail() : usuario.getEmail())
                .enderecos(usuario.getEnderecos())
                .telefones(usuario.getTelefones())
                .build();
    }

    public Endereco checkEndereco(EnderecoDTO dto, Endereco endereco){
        return Endereco.builder()
                .id(endereco.getId())
                .rua(dto.getRua() != null ? dto.getRua(): endereco.getRua())
                .numero(dto.getNumero() != null ? dto.getNumero() : endereco.getNumero())
                .cidade(dto.getCidade() != null ? dto.getCidade() : endereco.getCidade())
                .cep(dto.getCep() != null ? dto.getCep() : endereco.getCep())
                .complemento(dto.getComplemento() != null ? dto.getComplemento() : endereco.getComplemento())
                .estado(dto.getEstado() != null ? dto.getEstado() : endereco.getEstado())
                .build();
    }

    public Telefone checkTelefone(TelefoneDTO dto, Telefone telefone){
        return Telefone.builder()
                .id(telefone.getId())
                .numero(dto.getNumero() != null ? dto.getNumero() : telefone.getNumero())
                .ddd(dto.getDdd() != null ? dto.getDdd() : telefone.getDdd())
                .build();
    }
}
