# Sprint 2 - Entrega 6

**Período:** até 01/10/2026  
**Branch:** `entrega-6`

## Meta

Implementar cadastro e autenticação segura para clientes e prestadores, identificando o perfil de cada usuário. Também foram feitos ajustes visuais na página inicial, no cadastro/login e nos dashboards.

## Escopo por Sprint

| Sprint | Trabalho principal |
|:---|:---|
| Sprint 1 / Entrega 5 | Fluxo de pedidos e serviços: criação/publicação, endereços e trabalho de aceite pelo prestador. |
| Sprint 2 / Entrega 6 | Cadastro, login e autenticação de clientes e prestadores. Ajustes visuais foram complementares. |

O fluxo de pedidos pertence à Sprint 1 e não faz parte da meta de autenticação da Sprint 2. No código atual, o botão de aceite ainda não grava a decisão no backend.

## O que foi implementado

- Cadastro e login conectados ao backend.
- Senhas protegidas com BCrypt, sessão, proteção CSRF e regras de acesso por perfil.
- Ajustes visuais na Home, no cadastro/login e nos dashboards de cliente e prestador.
- Dockerfiles e Docker Compose foram adicionados como trabalho complementar. A configuração foi validada, mas os containers não foram iniciados.

## Testes executados localmente

- Testes do backend: passaram. Foram adicionados casos para cadastro de cliente e prestador, senha protegida, dados inválidos, e-mail duplicado e login válido/inválido.
- Build e lint do frontend: passaram.
- Validação da configuração do Docker Compose: passou com aviso de `version` obsoleto.

Os testes ainda não cobrem o fluxo HTTP completo de login, sessão e autorização. O `npm ci` também reportou uma vulnerabilidade alta em dependência, ainda sem triagem.

## Métricas

As métricas da Sprint 1 não foram registradas. Nesta Sprint também não há apontamento de horas nem relatório de cobertura. Por isso, não dá para comparar numericamente a produtividade entre Sprints. O histórico mostra trabalho funcional relevante, mas commits não permitem calcular horas ou produtividade individual. Ver [METRICAS.md](../METRICAS.md).

## Padrões e decisão técnica

Os padrões Strategy e Adapter estão resumidos em [PADROES-DE-PROJETO.md](../PADROES-DE-PROJETO.md). A decisão sobre sessão, CSRF e perfis está em [ADR-0003](../adrs/ADR-0003-autenticacao.md).

## PR e release

O PR da Entrega 6 ainda será aberto. Aprovação, checks remotos, merge na `main` e release/tag `v0.2.0` ainda vão ocorrer.
