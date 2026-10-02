# Contribuições Individuais - Sprint 2

> **Atenção:** o pacote analisado não inclui o histórico do Git (`.git`), então não é possível extrair autoria de commits, PRs ou reviews. Este documento traz a estrutura e os papéis; as colunas `<PREENCHER>` devem ser completadas por cada integrante com evidências reais (hash, número de issue/PR). Não preencher com informações que não possam ser comprovadas no repositório.

## Como levantar as evidências

```bash
git log --author="Nome" --since=2026-09-17 --until=2026-10-02 --pretty="%h %ad %s" --date=short entrega-6
git shortlog -sn --since=2026-09-17 entrega-6
gh pr list --state all --search "author:USUARIO" --json number,title,mergedAt
gh pr list --state all --search "reviewed-by:USUARIO" --json number,title
```

## Papéis (conforme README e planejamento)

| Integrante | Papel | Área de responsabilidade registrada |
|:---|:---|:---|
| Ana Clara Teixeira Ronzani | Engenheira de Qualidade | Testes, qualidade, documentação de sprint |
| Davi Israel Quirino | Scrum Master & Front-end | Interface React, acompanhamento do board |
| João Vitor Schmitt | DevOps / Infra | CI, Docker, back-end (Sprint 1) |
| Leonardo Victor Muller De Morais | Arquiteto de Software | Arquitetura, ADRs, segurança/back-end |

## Registro por integrante

### Ana Clara Teixeira Ronzani
| Tipo | Descrição | Evidência |
|:---|:---|:---|
| Testado | Testes de `UsuarioService` e `SolicitacaoServicoService` (USO-IA nº 8 cita o aperfeiçoamento dos testes) | `<PREENCHER: issue/PR/commit>` |
| Documentado | `<PREENCHER>` | `<PREENCHER>` |
| Revisado | `<PREENCHER: PRs revisados>` | `<PREENCHER>` |

### Davi Israel Quirino
| Tipo | Descrição | Evidência |
|:---|:---|:---|
| Implementado | Telas de Home, seleção de perfil, cadastro, login e dashboards (USO-IA nº 6 e 7) | `<PREENCHER>` |
| Organizado | Board, issues e critérios de aceitação da Sprint 2 | `<PREENCHER: link do projeto>` |
| Revisado | `<PREENCHER>` | `<PREENCHER>` |

### João Vitor Schmitt
| Tipo | Descrição | Evidência |
|:---|:---|:---|
| Implementado | Workflow de CI, Dockerfiles e compose | `<PREENCHER>` |
| Revisado | `<PREENCHER>` | `<PREENCHER>` |

### Leonardo Victor Muller De Morais
| Tipo | Descrição | Evidência |
|:---|:---|:---|
| Implementado | Autenticação: `SecurityConfig`, `UsuarioController`, `UsuarioService` (confirmar autoria) | `<PREENCHER>` |
| Documentado | ADR-0003, ADR-0004, padrões de projeto | `<PREENCHER>` |
| Revisado | `<PREENCHER>` | `<PREENCHER>` |

## Regra de coerência
Cada linha deve apontar para algo verificável no GitHub e ser compatível com o papel assumido. Pelo menos um PR relevante deve ter aprovação de integrante diferente do autor.
