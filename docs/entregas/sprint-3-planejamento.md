# Planejamento Inicial - Sprint 3 (Entrega 7)

**Projeto:** ServiceHub  
**Data da reunião:** 20/10/2026  
**Participantes:** Ana Clara Teixeira Ronzani, Davi Israel Quirino, João Vitor Schmitt, Leonardo Victor Muller De Morais  

---

## 1. Meta da Sprint 3

Consolidar o fluxo de atendimento das solicitações de serviço do MVP, permitindo que prestadores enviem propostas e que clientes analisem e aceitem uma proposta, com persistência do prestador associado e atualização do status da solicitação. A Sprint 3 também contempla a revisão da arquitetura efetivamente implementada, a consolidação das ADRs, a manutenção orientada a design, a evolução dos testes automatizados e a atualização das métricas, riscos e documentação do projeto.

O fluxo de pagamento via Asaas, a preparação completa do ambiente de staging e o pipeline de CD permanecem planejados para a Sprint 4, conforme o replanejamento registrado em `docs/BASELINE.md`.

## 2. Itens do backlog selecionados para a sprint

| ID | Item do Backlog | Prioridade | Estimativa |
|:---|:---|:---:|:---:|
| #9 | Aceite ou recusa de solicitação pelo prestador | Alta | 8h |
| #14 | Documentação viva, ADRs, métricas, arquitetura e riscos | Alta | 8h |

**Capacidade planejada para a sprint:** 48h úteis (conforme `docs/BASELINE.md`).  
**Esforço total selecionado:** 16h, deixando margem para revisão, testes, manutenção e ajustes de documentação.

## 3. Justificativa da escolha

- O item #9 é o principal item funcional restante do fluxo de contratação do MVP e havia sido identificado no final da Sprint 2 como não concluído.
- A evolução da entrega implementa a entidade `PropostaServico`, o envio de propostas pelo método `enviarProposta`, o aceite pelo cliente via `clienteAceitarProposta`, a associação do prestador e a mudança da solicitação para o status `EM_ANDAMENTO`.
- A restrição única entre solicitação e prestador, juntamente com a busca com bloqueio no serviço, contribui para evitar propostas duplicadas e inconsistências em operações concorrentes.
- O item #14 é necessário para registrar a arquitetura real, as decisões tomadas, as métricas observadas, os riscos atualizados e as evidências de qualidade exigidas pela Entrega 7.
- O pagamento Asaas (#10) possui estimativa de 24h e depende de integração externa, tratamento de estados e webhooks. Por isso, permanece priorizado para a Sprint 4, evitando ampliar o escopo desta sprint além da capacidade disponível.

## 4. Decisões tomadas e ajustes de escopo

- **Ajuste de escopo em relação ao `BASELINE.md`:** a Sprint 3 foi replanejada para concluir o fluxo de propostas e aceite antes da integração de pagamentos. A decisão considera o estado real do MVP ao final da Sprint 2.
- **Decisão funcional:** o prestador envia uma proposta (`enviarProposta`) para uma solicitação disponível e o cliente proprietário aceita uma das propostas (`clienteAceitarProposta`). O aceite associa o prestador, aplica o valor proposto ao `valorEstimado` e altera o status para `EM_ANDAMENTO`.
- **Regra de integridade:** cada prestador pode possuir no máximo uma proposta para a mesma solicitação, utilizando a restrição única de `PropostaServico`.
- **Regra de estado:** uma solicitação só pode receber ou aceitar propostas enquanto estiver no estado `SOLICITADO`. A operação usa transação e busca com bloqueio para reduzir o risco de aceite duplicado.
- **Definição de recusa no MVP:** no fluxo simplificado da Sprint 3, a recusa é operada de forma implícita pelo cliente ao ignorar propostas indesejadas ou selecionar a proposta de outro prestador.
- **Ajuste arquitetural:** a documentação deve refletir o sistema efetivo: frontend React/TypeScript, API Spring Boot, JPA e H2 em memória. PostgreSQL e Asaas continuam decisões planejadas, não integrações concluídas nesta entrega.
- **Manutenção/reengenharia:** registrar a evolução do modelo de propostas, a justificativa técnica, os testes de preservação de comportamento e a métrica antes/depois em `docs/REENGENHARIA.md`.
- **Divisão de tarefas entre os integrantes:**
  - **João Vitor Schmitt (DevOps/Infra):** Gestão das tarefas/issues da sprint, evolução e manutenção dos workflows do GitHub Actions (`ci.yml`) integrando os testes de backend (Gradle) e frontend (Vitest), e revisão do `RISCOS.md`.
  - **Davi Israel Quirino (Front-end):** Refatoração da interface dos Dashboards de Cliente e Prestador alinhando ao design da Landing Page, e integração dos modais/formulários de envio de proposta e aceite de chamados.
  - **Leonardo Victor Muller De Morais (Arquiteto de Software):** Modelagem e refatoração da entidade `PropostaServico`, implementação do bloqueio transacional (`findByIdForUpdate`), resolução do bug de sessão (#24) e elaboração do `ARQUITETURA.md` (C4 Contexto e Contêineres).
  - **Ana Clara Teixeira Ronzani (Scrum Master & Engenheira de Qualidade):** Elaboração da suíte de testes de frontend com Vitest (`servicos.test.ts`), testes de integração de segurança (`SecurityIntegrationTest`), e atualização das métricas do projeto em `METRICAS.md`.
- **Escopo complementar replanejado:** pagamento Asaas, PostgreSQL/staging, CD e correções finais de hardening permanecem para a Sprint 4.

## 5. Definição de pronto para a sprint

Um item é considerado concluído quando:

- o código está integrado à `main` via Pull Request revisado e aprovado;
- os checks obrigatórios do CI passaram;
- há testes automatizados cobrindo as regras principais de proposta, aceite, estado e duplicidade;
- a funcionalidade é demonstrável por meio da interface, da API ou de scripts de validação;
- somente o cliente proprietário consegue aceitar uma proposta;
- o aceite registra o prestador, o valor proposto e o status correto da solicitação;
- a arquitetura efetiva está documentada com visão de contexto e de contêineres;
- as ADRs, métricas, riscos, qualidade e uso de IA foram revisados;
- a reengenharia possui justificativa, validação objetiva e comparação de métrica;
- o registro de contribuições individuais está associado a evidências do repositório;
- a release/tag `v0.3.0` e as evidências do fluxo Git são confirmadas no GitHub antes da entrega.