# Padrões e camadas efetivamente presentes no ServiceHub

Este documento distingue padrões de organização arquitetural dos padrões GoF. A implementação atual usa camadas no backend, composição de componentes no frontend e adaptadores para compatibilizar os contratos da API com os modelos de tela. A autenticação está detalhada em [ADR-0003](adrs/ADR-0003-autenticacao.md).

## 1. Repository — acesso a dados

As interfaces Spring Data JPA isolam os serviços da implementação de persistência e fornecem operações CRUD e consultas específicas:

| Repositório | Responsabilidade |
|:---|:---|
| `UsuarioRepository` | Consultas e persistência de usuários, incluindo busca por e-mail. |
| `EnderecoRepository` | Consultas e persistência de endereços. |
| `SolicitacaoServiceRepository` | Consultas e persistência de solicitações; inclui consulta com bloqueio para operações concorrentes. |

Esses repositórios estendem `JpaRepository`. O perfil atual do repositório configura H2 em memória; o padrão Repository não implica que PostgreSQL esteja integrado.

## 2. Service — regras de negócio

Os serviços encapsulam validações e operações de domínio para que os controladores não concentrem regras de negócio:

| Serviço | Responsabilidade |
|:---|:---|
| `UsuarioService` | Cadastro, consulta e atualização de usuários e perfil. |
| `EnderecoService` | Operações de endereço e associação aos usuários. |
| `SolicitacaoServicoService` | Fluxos de solicitações, propostas, autorização por proprietário e transações. |

Operações sujeitas a concorrência usam transação e consulta com bloqueio quando necessário, em vez de depender apenas da interface.

## 3. Controller e DTO — fronteira da API

`UsuarioController`, `EnderecoController` e `SolicitacaoServicoController` expõem rotas REST e delegam operações às camadas de serviço. DTOs como `LoginUsuarioRespostaDTO`, `UsuarioRespostaDTO`, `EnderecoDTO` e `SolicitacaoServicoRespostaDTO` definem os dados de entrada e saída, evitando expor diretamente todas as entidades JPA nos contratos da API.

Controller e DTO são padrões de organização da API e separação de responsabilidades, não padrões GoF.

## 4. Security Filter Chain — autenticação stateless

`SecurityConfig` declara a `SecurityFilterChain` do Spring Security com política stateless e validação JWT pelo OAuth2 Resource Server. O token Bearer é enviado no cabeçalho `Authorization`; o filtro do Resource Server que o processa é `BearerTokenAuthenticationFilter`, não o fluxo HTTP Basic de `BasicAuthenticationFilter`.

`JwtTokenService` emite tokens com expiração de uma hora. O frontend armazena o token de login na chave `token` do `localStorage` e o cliente REST adiciona o cabeçalho às requisições protegidas. No logout, a interface remove o token; a API não mantém blacklist nem revoga o token no servidor, que permanece válido até expirar.

## 5. Adapter — compatibilidade entre API e interface

Os conversores no frontend transformam os tipos HTTP em modelos adequados às telas:

| Adaptador | Arquivo | Conversão |
|:---|:---|:---|
| `mapearUsuario` | `ServiceHub/src/servicos/autenticacao.ts` | `UsuarioApi` para `Usuario`. |
| `converterEndereco` | `ServiceHub/src/servicos/api.ts` | `EnderecoApi` para o modelo de endereço da interface. |
| `formatarSolicitacao` | `ServiceHub/src/paginas/DashboardCliente/index.tsx` | `SolicitacaoApi` para o modelo usado pelo dashboard cliente. |

`formatarSolicitacao` permanece dentro do dashboard cliente e não é compartilhada pelo dashboard prestador. Os adaptadores precisam acompanhar mudanças dos DTOs e dos modelos de tela.

## 6. Componentização React — composição e callbacks

`ModalPerfilUsuario` é reutilizado nos dashboards de cliente e prestador. Recebe o usuário, estado e callbacks por props, controla a apresentação/edição dos campos e não chama diretamente a API. Cada dashboard fornece `onSalvar` e delega a persistência ao cliente REST. Isso mantém a interface do modal desacoplada do transporte HTTP e permite reutilizar a mesma interação em contextos diferentes.

## 7. Integrações e fluxos relacionados

- **Consulta de CEP:** `ServiceHub/src/servicos/cep.ts` consulta diretamente `https://viacep.com.br/ws/{cep}/json/`, normaliza a resposta para o modelo de endereço do frontend e alimenta o formulário de cadastro de endereço. Não há integração direta com a API dos Correios no código atual.
- **Edição de perfil:** os dashboards carregam e salvam os dados; `ModalPerfilUsuario` recebe os dados e callbacks, sem dependência do serviço de API.

## Referências

- [ADR-0003: Autenticação stateless com JWT](adrs/ADR-0003-autenticacao.md)
- [ADR-0004: Adaptadores e componentização do frontend](adrs/ADR-0004-padroes-de-projeto.md)
- [Arquitetura efetiva](ARQUITETURA.md)
