package com.example.usuario.business.converter;

import com.example.usuario.business.dto.EnderecoDTO;
import com.example.usuario.business.dto.TelefoneDTO;
import com.example.usuario.business.dto.UsuarioDTO;
import com.example.usuario.infrastructure.entity.Endereco;
import com.example.usuario.infrastructure.entity.Telefone;
import com.example.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // indica para o Spring que esta classe pode ser injetada (usada em outros lugares)
public class UsuarioConverter {



    // 🥗 CONVERTER = "Garçom" da arquitetura
//
// Entrada:
//   - Recebe DTO (pedido do cliente)
//
// Saída:
//   - Devolve Entity (receita/processamento) para a cozinha (banco)
//
// Depois do processamento:
//   - Converte Entity de volta para DTO
//   - Para o cliente receber algo simples, limpo e sem detalhes internos

    // ==================== DTO -> ENTITY ====================

    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {
        // Converte um UsuarioDTO (que vem da API) para Usuario (que vai para o banco)
        return Usuario.builder()
                .nome(usuarioDTO.getNome()) // copia o nome do DTO para a entidade
                .email(usuarioDTO.getEmail()) // copia o email
                .senha(usuarioDTO.getSenha()) // copia a senha
                .enderecos(paraListaEndereco(usuarioDTO.getEnderecos())) // converte lista de EnderecoDTO -> Endereco
                .telefones(paraListaTelefones(usuarioDTO.getTelefones())) // converte lista de TelefoneDTO -> Telefone
                .build();
    }

    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS) {
        // Converte lista de EnderecoDTO para lista de Endereco
        return enderecoDTOS.stream().map(this::paraEndereco).toList();
    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO) {
        // Converte um único EnderecoDTO para Endereco (entidade)
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    public List<Telefone> paraListaTelefones(List<TelefoneDTO> telefoneDTOS) {
        // Converte lista de TelefoneDTO para lista de Telefone
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO) {
        // Converte um TelefoneDTO para Telefone (entidade)
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }


    // ==================== ENTITY -> DTO ====================

    public UsuarioDTO paraUsuarioDTO(Usuario usuario) {
        // Converte uma ENTITY (vinda do banco) para DTO (que vai para a API)
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(paraListaEnderecoDTO(usuario.getEnderecos())) // lista entidade -> DTO
                .telefones(paraListaTelefonesDTO(usuario.getTelefones()))
                .build();
    }

    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecoList) {
        // Converte lista de Endereco (banco) para lista de EnderecoDTO (API)
        return enderecoList.stream().map(this::paraEnderecoDTO).toList();
    }

    public EnderecoDTO paraEnderecoDTO(Endereco endereco) {
        // Converte um único Endereco (banco) para EnderecoDTO (API)
        return EnderecoDTO.builder()
                .id(endereco.getId()) // agora aparece o ID porque já existe no banco
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .estado(endereco.getEstado())
                .build();
    }

    public List<TelefoneDTO> paraListaTelefonesDTO(List<Telefone> telefoneList) {
        // Lista entidade -> lista DTO
        return telefoneList.stream().map(this::paraTelefoneDTO).toList();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone telefone) {
        // Telefone entidade -> DTO
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    // ==================== UPDATE (MERGE) ====================

    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario entity){
        // Atualiza os dados do usuário MESCLANDO o que veio no DTO com o que já existe no banco
        return Usuario.builder()
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : entity.getNome())
                // se mandou nome no DTO -> usa o novo, senão -> mantém o antigo
                .id(entity.getId()) // ID nunca muda, sempre usa o existente
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : entity.getSenha())
                // mesma lógica: só troca se veio senha nova
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : entity.getEmail())
                .enderecos(entity.getEnderecos()) // não muda endereços aqui
                .telefones(entity.getTelefones()) // não muda telefones aqui
                .build();
    }

    public Endereco updateEndereco(EnderecoDTO dto, Endereco entity){
        // Mescla dados do endereço
        return Endereco.builder()
                .id(entity.getId()) // ID permanece o mesmo
                .rua(dto.getRua() != null ? dto.getRua() : entity.getRua())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .cidade(dto.getCidade() != null ? dto.getCidade() : entity.getCidade())
                .cep(dto.getCep() != null ? dto.getCep() : entity.getCep())
                .complemento(dto.getComplemento() != null ? dto.getComplemento() : entity.getComplemento())
                .estado(dto.getEstado() != null ? dto.getEstado() : entity.getEstado())
                .build();
    }

    public Telefone updateTelefone(TelefoneDTO dto, Telefone entity){
        // Mescla dados do telefone
        return Telefone.builder()
                .id(entity.getId()) // ID existente
                .ddd(dto.getDdd() != null ? dto.getDdd() : entity.getDdd())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .build();
    }

    // Corrigido para receber EnderecoDTO em vez de TelefoneDTO
    public Endereco paraEnderecoEntity(EnderecoDTO dto, Long idusuario){
        return Endereco.builder()
                .rua(dto.getRua())
                .cidade(dto.getCidade())
                .cep(dto.getCep())
                .complemento(dto.getComplemento())
                .numero(dto.getNumero())
                .usuario_id(idusuario)
                .build();
    }

    public Telefone paraTelefoneEntity(TelefoneDTO dto, Long idUsuario){
        return Telefone.builder()
                .numero(dto.getNumero())
                .ddd(dto.getDdd())
                .usuario_id(idUsuario)
                .build();
    }


}
