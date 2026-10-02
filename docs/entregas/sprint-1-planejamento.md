# Planejamento Inicial - Sprint 1 (Entrega 5)

**Projeto:** ServiceHub
**Data da reunião:** 10/09/2026
**Participantes:** Ana Clara Teixeira Ronzani, Davi Israel Quirino, João Vitor Schmitt, Leonardo Victor Muller De Morais

---

## 1. Meta da Sprint 1

Entregar o primeiro incremento funcional do MVP como um **vertical slice** que atravesse interface, lógica de aplicação e persistência, cobrindo o **setup de infraestrutura** e a **criação e publicação de solicitação de serviço**, conforme replanejado pela equipe nesta reunião.

## 2. Itens do backlog selecionados para a sprint

| ID | Item do Backlog | Prioridade | Estimativa |
|:---|:---|:---:|:---:|
| #13 | Configuração de infraestrutura (Docker, PostgreSQL, CI/GitHub Actions) | Alta | 16h |
| #8 | Criação e publicação de solicitação de serviço | Alta | 16h |

**Capacidade planejada para a sprint:** 48h úteis (conforme `docs/BASELINE.md`).
**Esforço total selecionado:** 32h, deixando margem para revisão, testes e ajustes.

## 3. Justificativa da escolha

- Ambos os itens são classificados como **prioridade Alta** no backlog priorizado desde a Entrega 3 (`docs/BASELINE.md`).
- A infraestrutura (#13) é pré-requisito técnico para qualquer funcionalidade que exija persistência ou pipeline de CI, incluindo as demais funcionalidades do MVP.
- **Replanejamento de escopo:** o `docs/BASELINE.md` previa originalmente cadastro/autenticação (#7) para a Sprint 1 e a publicação de solicitações (#8) para a Sprint 2. Em orientação com o professor, foi apontado que a autenticação não deveria ser o ponto de partida deste projeto. A equipe optou então por adiantar o item **#8 (Criação e publicação de solicitação de serviço)**, por representar mais diretamente o valor central do produto — o registro do pedido de serviço em si —, permitindo demonstrar o núcleo de negócio do ServiceHub já na Sprint 1, sem depender de um mecanismo de autenticação completo.
- O item #7 (cadastro/autenticação) fica replanejado para a Sprint 2; nesta sprint, o solicitante poderá ser identificado de forma simplificada (ex.: usuário fixo/mock), apenas o suficiente para demonstrar o fluxo de publicação de solicitação.

## 4. Decisões tomadas e ajustes de escopo

- **Ajuste de escopo em relação ao `BASELINE.md`:** troca do item #7 (cadastro/autenticação) pelo item #8 (criação e publicação de solicitação de serviço) no vertical slice da Sprint 1, por orientação do professor.

- **Divisão de tarefas entre os integrantes:**
  - **João Vitor Schmitt** — Back-end (Spring Boot): implementação da API e da persistência da solicitação de serviço.
  - **Davi Quirino** — Front-end (React + Vite + TypeScript): interface de criação e publicação da solicitação de serviço.
  - **Leonardo Victor Muller** — Arquitetura: diagrama UML, decisão de arquitetura cliente-servidor em camadas para o incremento e suporte na cobertura de testes.
  - **Ana Clara Teixeira Ronzani** — Engenharia de Qualidade: documentação da sprint, suporte na correção de erros e avaliação da qualidade dos processos (incluindo os testes automatizados entregues).

- O item #7 (cadastro/autenticação) permanece no backlog, replanejado para a Sprint 2 junto com os itens originalmente previstos.

## 5. Definição de pronto para a sprint

Um item é considerado concluído quando:
- o código está integrado à `main` via Pull Request revisado e aprovado;
- os checks obrigatórios do CI passaram;
- há testes de unidade automatizados cobrindo o comportamento principal;
- a funcionalidade é demonstrável (via interface, chamada de API ou script).
