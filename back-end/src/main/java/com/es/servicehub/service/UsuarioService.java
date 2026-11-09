package com.es.servicehub.service;

import com.es.servicehub.dto.CadastroUsuarioRequest;
import com.es.servicehub.enums.TipoPerfil;
import com.es.servicehub.model.Usuario;
import com.es.servicehub.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }

    @Transactional
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        if (usuario.getEmail() != null) {
            String email = usuario.getEmail().trim().toLowerCase();
            usuario.setEmail(email);
        }

        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        if (usuario.getId() == null && usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Já existe um usuário cadastrado com este e-mail.");
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario cadastrar(CadastroUsuarioRequest request) {
        if (request == null) {
            throw new RuntimeException("Dados do cadastro obrigatórios.");
        }

        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new RuntimeException("Nome é obrigatório.");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("E-mail é obrigatório.");
        }

        if (request.getSenha() == null || request.getSenha().isBlank()) {
            throw new RuntimeException("Senha é obrigatória.");
        }

        if (request.getTipoPerfil() == null) {
            throw new RuntimeException("Selecione um perfil válido.");
        }

        if (request.getTipoPerfil() == TipoPerfil.ADMIN) {
            throw new RuntimeException("Não é permitido criar contas administrativas pelo cadastro público.");
        }

        String cpf = request.getCpf() == null ? "" : request.getCpf().trim();
        String cnpj = request.getCnpj() == null ? "" : request.getCnpj().trim();
        if (request.getTipoPerfil() == TipoPerfil.CLIENTE && !cpfValido(cpf)) {
            throw new RuntimeException("Informe um CPF válido.");
        }
        if (request.getTipoPerfil() == TipoPerfil.CLIENTE && !cnpj.isEmpty()) {
            throw new RuntimeException("Clientes devem informar CPF, não CNPJ.");
        }
        if (request.getTipoPerfil() == TipoPerfil.PRESTADOR) {
            if (cpf.isEmpty() == cnpj.isEmpty()) {
                throw new RuntimeException(cpf.isEmpty()
                        ? "Informe CPF ou CNPJ para continuar."
                        : "Informe somente CPF ou CNPJ.");
            }
            if (!cpf.isEmpty() && !cpfValido(cpf)) {
                throw new RuntimeException("Informe um CPF válido.");
            }
            if (!cnpj.isEmpty() && !cnpjValido(cnpj)) {
                throw new RuntimeException("Informe um CNPJ válido.");
            }
        }

        if (request.getSenha().length() < 10) {
            throw new RuntimeException("A senha deve ter pelo menos 10 caracteres.");
        }

        if (request.getTipoPerfil() == TipoPerfil.PRESTADOR &&
                (request.getEspecialidades() == null || request.getEspecialidades().isEmpty())) {
            throw new RuntimeException("Prestadores devem informar pelo menos uma especialidade.");
        }

        String email = request.getEmail().trim().toLowerCase();
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Já existe um usuário cadastrado com este e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome().trim());
        usuario.setEmail(email);
        usuario.setCpf(cpf.isEmpty() ? null : cpf.replaceAll("\\D", ""));
        usuario.setCnpj(cnpj.isEmpty() ? null : cnpj.replaceAll("\\D", ""));
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTelefone(request.getTelefone() == null ? null : request.getTelefone().trim());
        usuario.setTipoPerfil(request.getTipoPerfil());
        usuario.setEspecialidades(request.getEspecialidades() == null ? new ArrayList<>() : request.getEspecialidades());

        return usuarioRepository.save(usuario);
    }

    private boolean cpfValido(String cpf) {
        if (!cpf.matches("(?:\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})")) {
            return false;
        }

        String digitos = cpf.replaceAll("\\D", "");
        if (digitos.matches("(\\d)\\1{10}")) {
            return false;
        }

        int primeiroDigito = calcularDigitoCpf(digitos, 9);
        int segundoDigito = calcularDigitoCpf(digitos, 10);
        return digitos.charAt(9) - '0' == primeiroDigito
                && digitos.charAt(10) - '0' == segundoDigito;
    }

    private int calcularDigitoCpf(String cpf, int quantidade) {
        int soma = 0;
        for (int indice = 0; indice < quantidade; indice++) {
            soma += (cpf.charAt(indice) - '0') * (quantidade + 1 - indice);
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private boolean cnpjValido(String cnpj) {
        if (!cnpj.matches("(?:\\d{14}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2})")) {
            return false;
        }

        String digitos = cnpj.replaceAll("\\D", "");
        if (digitos.matches("(\\d)\\1{13}")) {
            return false;
        }

        int primeiroDigito = calcularDigitoCnpj(digitos, 12);
        int segundoDigito = calcularDigitoCnpj(digitos, 13);
        return digitos.charAt(12) - '0' == primeiroDigito
                && digitos.charAt(13) - '0' == segundoDigito;
    }

    private int calcularDigitoCnpj(String cnpj, int quantidade) {
        int[] pesos = quantidade == 12
                ? new int[] { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 }
                : new int[] { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
        int soma = 0;
        for (int indice = 0; indice < quantidade; indice++) {
            soma += (cnpj.charAt(indice) - '0') * pesos[indice];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    @Transactional
    public Usuario autenticar(String email, String senha) {
        String emailNormalizado = email == null ? "" : email.trim().toLowerCase();

        if (emailNormalizado.isBlank() || senha == null || senha.isBlank()) {
            throw new RuntimeException("Informe e-mail e senha para continuar.");
        }

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas. Verifique e-mail e senha."));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas. Verifique e-mail e senha.");
        }

        return usuario;
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = buscarPorId(id);

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }
        usuarioExistente.setCpf(usuarioAtualizado.getCpf());
        usuarioExistente.setCnpj(usuarioAtualizado.getCnpj());
        usuarioExistente.setTelefone(usuarioAtualizado.getTelefone());
        usuarioExistente.setTipoPerfil(usuarioAtualizado.getTipoPerfil());
        usuarioExistente.setEspecialidades(usuarioAtualizado.getEspecialidades() == null ? new ArrayList<>() : usuarioAtualizado.getEspecialidades());
        usuarioExistente.setEndereco(usuarioAtualizado.getEndereco());

        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}
