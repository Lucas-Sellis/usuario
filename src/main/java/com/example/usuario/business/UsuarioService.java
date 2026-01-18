package com.example.usuario.business;

import com.example.usuario.business.converter.UsuarioConverter;
import com.example.usuario.business.dto.EnderecoDTO;
import com.example.usuario.business.dto.TelefoneDTO;
import com.example.usuario.business.dto.UsuarioDTO;
import com.example.usuario.infrastructure.entity.Endereco;
import com.example.usuario.infrastructure.entity.Telefone;
import com.example.usuario.infrastructure.entity.Usuario;
import com.example.usuario.infrastructure.exceptions.ConflictException;
import com.example.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.example.usuario.infrastructure.repository.repository.EnderecoRepository;
import com.example.usuario.infrastructure.repository.repository.TelefoneRepository;
import com.example.usuario.infrastructure.repository.repository.UsuarioRepository;
import com.example.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    // Dependências que o service precisa
    private final UsuarioRepository usuarioRepository;   // Acesso ao banco de dados
    private final UsuarioConverter usuarioConverter;     // Converte DTO ↔ Entity
    private final PasswordEncoder passwordEncoder;       // Criptografa senha
    private final JwtUtil jwtUtil;                       // Lê informações do token JWT
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;


    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){

        // Verifica se já existe alguém com esse email
        emailExiste(usuarioDTO.getEmail());

        // Criptografa a senha antes de salvar no banco
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        // Converte o DTO (que veio da requisição) para Entity (que vai pro banco)
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);

        // Salva no banco
        usuario = usuarioRepository.save(usuario);

        // Converte de volta para DTO para retornar na resposta
        return usuarioConverter.paraUsuarioDTO(usuario);
    }


    public void emailExiste(String email) {

        // Se já existe → lança erro de conflito (email duplicado)
        if (verificaEmailExistente(email)) {
            throw new ConflictException("Email já cadastrado: " + email);
        }
    }

    // Consulta no banco se o email existe
    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }


    public UsuarioDTO buscarUsuarioPorEmail(String email) {  // foi alterado isso aqui
        try {
            // Busca no banco: se não achar, lança a exceção de "Não Encontrado"
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                            .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado: " + email))
            );
        } catch (ResourceNotFoundException e) {
            // Captura o erro específico e relança como erro de execução (Runtime)
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }
    }


    // Deleta um usuário usando o email
    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }


    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){

//        ➡️ É um método que recebe:
//
//        um token JWT
//
//        um DTO com os dados que o usuário quer atualizar

        // O token chega assim: "Bearer xxxxxx"
        // substring(7) remove o "Bearer "
        String email = jwtUtil.extractUsername(token.substring(7));

        // Se veio senha nova no DTO → criptografa
        // Se vier null → não altera a senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        // Busca os dados atuais do usuário no banco
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email nao localizado")
        );

        // Mescla os dados novos que vieram na requisição com os dados antigos do banco
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        // Salva as alterações e retorna em formato DTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }


    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {

        // 1. Busca o endereço no banco pelo ID.
        //    Se não existir, lança uma exceção informando que o ID não foi encontrado.
        Endereco entity = enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado " + idEndereco));

        // 2. Atualiza a entidade existente com os novos dados vindos do DTO.
        //    Esse update é feito no converter para não sujar o service.
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);

        // 3. Salva o endereço atualizado no banco e converte de volta para DTO
        //    antes de retornar para o controller.
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }


    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto) {

        // 1. Busca o telefone no banco pelo ID.
        //    Se o ID não existir, lança exceção.
        Telefone entity = telefoneRepository.findById(idTelefone)
                .orElseThrow(() -> new ResourceNotFoundException("id nao encontrado " + idTelefone));

        // 2. Atualiza a entidade existente com os novos dados do DTO.
        //    A lógica fica dentro do converter para evitar repetição.
        Telefone telefone = usuarioConverter.updateTelefone(dto, entity);

        // 3. Salva no banco e devolve o retorno já convertido para DTO.
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto){
        // método público que qualquer um pode chamar
        // ele devolve um endereço e serve para cadastrar um endereço
        // ele recebe um token e um endereço novo

        String email = jwtUtil.extractUsername(token.substring(7));
        // aqui ele puxa o email de dentro do token

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("email não localizado " + email));
        // aqui ele vai no banco e procura o usuário pelo email para pegar o id
        // se não encontrar o usuário, ele dá um erro dizendo que o email não existe

        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        // converte o DTO (modelo que vem de fora) para um Endereco (modelo que o banco entende)
        // e coloca o id do usuário junto

        Endereco enderecoEntity = enderecoRepository.save(endereco);
        // salva o endereço convertido no banco

        return usuarioConverter.paraEnderecoDTO(enderecoEntity);
        // converte de novo para DTO e devolve para a controller
    }



    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto) {
        // 1. Extração do e-mail (lembre-se de tratar o "Bearer " se necessário)
        String email = jwtUtil.extractUsername(token.substring(7));

        // 2. Busca do usuário
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não localizado: " + email));

        // 3. Conversão (Ajustado para o nome correto do método no Converter)
        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());

        // 4. Persistência
        Telefone telefoneEntity = telefoneRepository.save(telefone);

        // 5. Retorno convertido para DTO
        return usuarioConverter.paraTelefoneDTO(telefoneEntity);
    }




}
