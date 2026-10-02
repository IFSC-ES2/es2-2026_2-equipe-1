# Planejamento Inicial - Sprint 2 (Entrega 6)

**Projeto:** ServiceHub  
**Data da reunião:** 01/10/2026  
**Participantes:** Ana Clara Teixeira Ronzani, Davi Israel Quirino, João Vitor Schmitt, Leonardo Victor Muller De Morais

---

## 1. Meta da Sprint 2

Concluir o item de maior prioridade restante no MVP: o **cadastro e a autenticação segura de clientes e prestadores**, com identificação do perfil de cada usuário e controle de acesso por tipo de conta. A funcionalidade de criação e publicação de solicitações já foi priorizada na Sprint 1, e a Sprint 2 consolida a parte de segurança e autenticação necessária para o uso real do sistema. Além disso, foi feito também as alterações de melhoria no Front-End

## 2. Itens do backlog selecionados para a sprint

| ID | Item do Backlog | Prioridade | Estimativa |
|:---|:---|:---:|:---:|
| #7 | Cadastro e autenticação de usuários (Cliente e Prestador) | Alta | 16h |

**Capacidade planejada para a sprint:** 48h úteis (conforme `docs/BASELINE.md`).  
**Esforço total selecionado:** 16h, deixando margem para revisão, testes e ajustes.

## 3. Justificativa da escolha

- O item #7 é classificado como **prioridade Alta** no backlog priorizado do MVP (`docs/BASELINE.md`).
- O replanejamento realizado após a Sprint 1 manteve a autenticação como foco central da Sprint 2, pois o item #8 (criação e publicação de solicitação) foi adiantado e entregue na etapa anterior.
- O cadastro e a autenticação são fundamentais para distinguir clientes e prestadores, respeitar regras de acesso e habilitar o fluxo real de uso do ServiceHub de forma segura.
- A funcionalidade é um pré-requisito para o avanço do MVP: sem autenticação e controle de perfil, não é possível validar corretamente o uso do sistema em cenários de negócio reais e nem garantir a separação de papéis.

## 4. Decisões tomadas e ajustes de escopo

- **Ajuste de escopo em relação ao `BASELINE.md`:** o item #7 permanece no foco principal da Sprint 2 junto com as melhorias pedidas pelo PO no Front-End, enquanto os itens de criação/publicação de solicitação (#8) e demais fluxos de negócio foram concluídos ou priorizados em etapas anteriores conforme replanejamento da equipe.
- **Decisão de arquitetura e segurança:** a autenticação foi tratada com foco em segurança, perfil de usuário, sessão e controle de acesso por papel, conforme decisão registrada no ADR da autenticação (`docs/adrs/ADR-0003-autenticacao.md`).
- **Divisão de tarefas entre os integrantes:**
  - **João Vitor Schmitt** — DevOps / Infra: suporte de CI, Docker, configuração e ajuste de ambiente de execução.
  - **Davi Quirino** — Front-end: Documentação, ajustes nas Issues e revisão das documentações das sprints anteriores.
  - **Leonardo Victor Muller** — Arquitetura: implementação das telas de cadastro, login, seleção de perfil e dashboards de cliente/prestador + Auxilio na Documentação.
  - **Ana Clara Teixeira Ronzani** — Engenharia de Qualidade: testes automatizados, validação de regras de negócio, revisão e documentação da sprint.

- **Escopo complementar observado:** ajustes visuais na página inicial, no cadastro/login e nos dashboards foram tratados como complementares ao item principal.

## 5. Definição de pronto para a sprint

Um item é considerado concluído quando:
- o código está integrado à `main` via Pull Request revisado e aprovado;
- os checks obrigatórios do CI passaram;
- há testes automatizados cobrindo os cenários principais de cadastro, login e validação de dados;
- a funcionalidade é demonstrável por meio da interface, da API ou de scripts de validação;
- o perfil do usuário e as regras de acesso ficam consistentes com a arquitetura definida para o sistema.
