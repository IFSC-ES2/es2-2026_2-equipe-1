# Padrões de Projeto OO - Sprint 2

Esta sprint aplica dois padrões: **Adapter** (frontend) e **Strategy** (backend). Decisão registrada em [ADR-0004](adrs/ADR-0004-padroes-de-projeto.md).

---

## 1. Adapter - conversão entre contrato da API e modelo da interface

### Problema identificado
O contrato JSON do backend não coincide com os modelos usados pelas telas. Exemplos reais: a API usa `tipoPerfil` (incluindo `ADMIN`), a tela usa `perfil` (apenas `CLIENTE`/`PRESTADOR`); a API usa `logradouro` e `id` numérico, a tela usa `rua` e `id` textual; `telefone` e `especialidades` podem vir ausentes. Sem uma camada de conversão, cada página teria de conhecer e tratar esses detalhes.

### Padrão adotado e por que é adequado
**Adapter**: funções que recebem o tipo da API e devolvem o tipo esperado pela interface, isolando a diferença de interfaces em um único ponto. É adequado porque o contrato do backend ainda muda (a Sprint 3 vai alterar solicitações e aceite), e o impacto deve ficar contido.

### Classes/módulos afetados
| Adapter | Arquivo | Converte |
|:---|:---|:---|
| `mapearUsuario` | `ServiceHub/src/servicos/autenticacao.ts` | `UsuarioApi` -> `Usuario` |
| `converterEndereco` (e o corpo de `criarEndereco`, no sentido inverso) | `ServiceHub/src/servicos/api.ts` | `EnderecoApi` <-> `EnderecoSelecionavel`/`Endereco` |
| `formatarSolicitacao` | `ServiceHub/src/paginas/DashboardCliente/index.tsx` | `SolicitacaoApi` -> `Solicitacao` (modelo da tela) |

### Benefícios
- Páginas não dependem dos nomes de campos do HTTP.
- Mudança de contrato altera um conversor, não várias telas.
- Valores padrão (`telefone ?? ""`, `especialidades ?? []`) ficam centralizados.

### Trade-offs
- Mais código de conversão para manter em sincronia com o backend.
- `formatarSolicitacao` está declarada dentro do componente `DashboardCliente`; o dashboard do prestador não a reutiliza. **Melhoria prevista:** mover para `servicos/api.ts`.
- Não há teste automatizado dos conversores (o frontend não tem framework de testes).

---

## 2. Strategy - escolha do tratamento do token CSRF

### Problema identificado
O frontend (SPA) lê o token CSRF do cookie `XSRF-TOKEN` e o devolve no cabeçalho `X-XSRF-TOKEN`. O Spring Security, por padrão, espera o token codificado (proteção contra o ataque BREACH). Um único tratamento não serve aos dois cenários: quando o token chega no **cabeçalho** (valor puro lido do cookie) é preciso resolvê-lo de um jeito; quando chega em **parâmetro de formulário** (valor codificado), de outro.

### Padrão adotado e por que é adequado
**Strategy**: `SpaCsrfTokenRequestHandler` implementa a interface `CsrfTokenRequestHandler` e escolhe, em tempo de execução, entre duas estratégias intercambiáveis do mesmo contrato - `CsrfTokenRequestAttributeHandler` (valor puro) e `XorCsrfTokenRequestAttributeHandler` (valor codificado) - conforme a presença do cabeçalho. A decisão fica encapsulada, sem `if` espalhado pela configuração de segurança, e novas estratégias podem ser acrescentadas sem alterar o chamador.

### Classes/módulos afetados
- `back-end/.../config/SecurityConfig.java`: classe interna `SpaCsrfTokenRequestHandler` e sua configuração em `securityFilterChain`.
- `ServiceHub/src/servicos/api.ts` (`obterTokenCsrf`): cliente do mecanismo.

### Benefícios
Mantém a proteção contra BREACH sem quebrar o fluxo da SPA; isola a regra de resolução do token.

### Trade-offs
- Há uma camada de delegação a mais para manter consistente com versões futuras do Spring Security.
- A classe é um detalhe privado de `SecurityConfig`; não há teste automatizado dela (ver ADR-0003, "testes de integração ainda são necessários").