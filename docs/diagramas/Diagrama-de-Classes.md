# Diagrama de Classes do Backend

O diagrama representa as entidades, enums, serviços e repositórios implementados
em `back-end/src/main/java/com/es/servicehub`. Os métodos exibidos são os
principais pontos de entrada dos serviços; getters e setters gerados pelo Lombok
foram omitidos.

```mermaid
classDiagram
    direction TB

    class Usuario {
        -Long id
        -String nome
        -String email
        -String senha
        -String cpf
        -String cnpj
        -String telefone
        -TipoPerfil tipoPerfil
        -List~Especialidades~ especialidades
        -Endereco endereco
    }

    class Endereco {
        -Long id
        -String logradouro
        -String numero
        -String complemento
        -String bairro
        -String cidade
        -String estado
        -String cep
    }

    class SolicitacaoServico {
        -Long id
        -String titulo
        -String descricao
        -BigDecimal valorEstimado
        -StatusSolicitacao statusSolicitacao
        -LocalDateTime dataCriacao
        -Endereco enderecoAtendimento
        -Usuario cliente
        -Usuario prestador
        -List~PropostaServico~ propostas
        -List~Especialidades~ especialidadesRequiridas
        +adicionarProposta(proposta: PropostaServico) void
    }

    class PropostaServico {
        -Long id
        -SolicitacaoServico solicitacao
        -Usuario prestador
        -BigDecimal valorProposto
    }

    class Especialidades {
        <<enumeration>>
        ELETRICISTA
        ENCANADOR
        PEDREIRO
        TECNICO_REDES
        PINTOR
        DIARISTA
        MARCENEIRO
        JARDINAGEM
        CHAVEIRO
        MONTADOR_MOVEIS
    }

    class TipoPerfil {
        <<enumeration>>
        CLIENTE
        PRESTADOR
        ADMIN
    }

    class StatusSolicitacao {
        <<enumeration>>
        SOLICITADO
        ACEITO
        RECUSADO
        EM_ANDAMENTO
        CONCLUIDO
        CANCELADO
    }

    class UsuarioService {
        -UsuarioRepository usuarioRepository
        +cadastrar(request: CadastroUsuarioRequest) Usuario
        +autenticar(email: String, senha: String) Usuario
        +listarTodos() List~Usuario~
        +buscarPorId(id: Long) Usuario
        +buscarPorEmail(email: String) Usuario
        +salvar(usuario: Usuario) Usuario
        +atualizar(id: Long, usuario: Usuario) Usuario
        +deletar(id: Long) void
    }

    class SolicitacaoServicoService {
        -SolicitacaoServiceRepository solicitacaoServiceRepository
        -UsuarioRepository usuarioRepository
        +listarDisponiveis() List~SolicitacaoServicoRespostaDTO~
        +listarDoCliente(clienteId: Long) List~SolicitacaoServicoRespostaDTO~
        +listarDoPrestador(prestadorId: Long) List~SolicitacaoServicoRespostaDTO~
        +criar(solicitacao: SolicitacaoServico) SolicitacaoServico
        +atualizar(id: Long, solicitacao: SolicitacaoServico) SolicitacaoServico
        +enviarProposta(solicitacaoId: Long, prestadorId: Long, valorProposto: BigDecimal) SolicitacaoServico
        +clienteAceitarProposta(solicitacaoId: Long, clienteId: Long, prestadorId: Long) SolicitacaoServico
        +deletar(id: Long) void
    }

    class EnderecoService {
        -EnderecoRepository enderecoRepository
        +listarTodos() List~Endereco~
        +criar(endereco: Endereco) Endereco
    }

    class CadastroUsuarioRequest {
        <<DTO>>
        -String nome
        -String email
        -String senha
        -String telefone
        -TipoPerfil tipoPerfil
        -List~Especialidades~ especialidades
    }

    class SolicitacaoServicoRespostaDTO {
        <<DTO>>
    }

    class UsuarioRepository {
        <<interface>>
    }

    class SolicitacaoServiceRepository {
        <<interface>>
    }

    class EnderecoRepository {
        <<interface>>
    }

    Usuario "0..1" -- "0..1" Endereco : endereco
    Usuario "1" --> "0..*" Especialidades : especialidades
    Usuario --> TipoPerfil : tipoPerfil

    SolicitacaoServico "0..*" --> "0..1" Endereco : enderecoAtendimento
    SolicitacaoServico "0..*" --> "0..1" Usuario : cliente
    SolicitacaoServico "0..*" --> "0..1" Usuario : prestador
    SolicitacaoServico "1" *-- "0..*" PropostaServico : propostas
    PropostaServico "0..*" --> "1" Usuario : prestador
    SolicitacaoServico "1" --> "0..*" Especialidades : especialidadesRequiridas
    SolicitacaoServico --> StatusSolicitacao : statusSolicitacao

    UsuarioService ..> UsuarioRepository : usa
    UsuarioService ..> CadastroUsuarioRequest : recebe
    UsuarioService ..> Usuario : gerencia
    SolicitacaoServicoService ..> SolicitacaoServiceRepository : usa
    SolicitacaoServicoService ..> UsuarioRepository : usa
    SolicitacaoServicoService ..> SolicitacaoServicoRespostaDTO : retorna
    SolicitacaoServicoService ..> SolicitacaoServico : gerencia
    EnderecoService ..> EnderecoRepository : usa
    EnderecoService ..> Endereco : gerencia
```

Cada `PropostaServico` pertence a uma solicitação e a um prestador. O backend
também restringe a uma proposta por combinação de solicitação e prestador. Ao
aceitar uma proposta, o serviço associa o prestador escolhido à solicitação e
atualiza seu valor estimado e status.
