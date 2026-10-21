# Arquitetura efetiva do ServiceHub — Entrega 7

## Escopo

A arquitetura abaixo reflete o código encontrado no ZIP `entrega-7`: SPA React/TypeScript, API Spring Boot, JPA e H2 em memória. As referências a PostgreSQL e Asaas são decisões planejadas, não integrações efetivas nesta entrega.

## Visão de contexto

Pendente

## Visão de contêineres 

Pendente

## Responsabilidades

| Componente | Responsabilidade |
|---|---|
| `ServiceHub/src/paginas` | Home, cadastro, dashboards e interação |
| `ServiceHub/src/servicos/api.ts` | HTTP e conversão de respostas |
| `SecurityConfig` | Sessão, BCrypt, CORS, CSRF e perfis |
| `controller` | Endpoints HTTP |
| `SolicitacaoServicoService` | Propostas, aceite, status e persistência |
| `model`/`repository` | Entidades, relacionamentos e consultas |
| GitHub Actions | Testes backend, build/lint frontend e Compose em PR |

## Apoio ao MVP

A separação SPA/API evita misturar UI e regras de negócio. A API centraliza autenticação, autorização e transições do ciclo da solicitação. `SolicitacaoServico`–`PropostaServico` suporta o fluxo de contratação.

## Diferenças entre planejado e efetivo

1. O README/ADR-0001 citam PostgreSQL, mas o código efetivo usa H2 em memória e o Compose não declara PostgreSQL.
2. O ADR-0002 define Asaas, mas nenhum cliente/serviço de pagamento foi localizado.
3. O workflow existe, mas o ZIP não contém evidência de execução do GitHub.
4. A entrega possui entidade de proposta, unicidade e operação transacional, ausentes na `main` comparada.
5. `ddl-auto=update` e H2 indicam ambiente de desenvolvimento/teste, não staging.

## Pendências

Decidir H2/PostgreSQL em ADR, isolar Asaas em adapter/serviço, adicionar testes `MockMvc` para propriedade/CSRF e corrigir o Compose para refletir a stack real.