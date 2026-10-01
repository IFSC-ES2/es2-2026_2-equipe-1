# Linha de Base (Baseline) do Planejamento - ServiceHub

**Data de Registro inicial:** 20 de Agosto de 2026
**Revisão:** 1.1 - 01 de Outubro de 2026

---

## 1. Recorte e Priorização do Backlog (MVP)

O escopo selecionado para o MVP da plataforma **ServiceHub** prioriza o fluxo essencial de contratação, gestão de chamados e liquidação financeira entre Clientes e Prestadores de Serviços:

| ID | Item do Backlog / User Story | Prioridade | Estimativa (Planning Poker - Horas) |
|:---|:---|:---:|:---:|
| **#7** | Cadastro e autenticação de usuários (Cliente e Prestador) | Alta | 16h |
| **#8** | Criação e publicação de solicitação de serviço | Alta | 16h |
| **#9** | Aceite ou recusa de solicitação pelo prestador | Alta | 8h |
| **#10**| Conclusão do serviço e pagamento via API Asaas | Média | 24h |
| **#13** | Configuração de infraestrutura (Docker, PostgreSQL, CI/GitHub Actions) | Alta | 16h |
| **#14** | Documentação viva (Inception, ADRs, Métricas, Baseline) | Alta | 8h |

**Total de Esforço Estimado do MVP:** **88 Horas**

---

## 2. Capacidade Planejada da Equipe

* **Integrantes Ativos:** 4
  * **Ana Clara Teixeira Ronzani:** Engenheira de Qualidade (QA)
  * **Davi Israel Quirino:** Scrum Master & Front-end
  * **João Vitor Schmitt:** DevOps / Infraestrutura
  * **Leonardo Victor Muller De Morais:** Arquiteto de Software & Back-end
* **Disponibilidade Estimada:** 6 horas semanais por integrante = **24 horas/semana totais da equipe**
* **Capacidade de Entrega por Sprint (Ciclo de 2 Semanas):** **48 horas úteis de trabalho por Sprint**

---

## 3. Planejamento Original

O cronograma abaixo registra a previsão aprovada junto com a baseline inicial, em 20/08/2026. Ele é mantido como histórico do planejamento e não representa, após o replanejamento, o escopo confirmado de cada Sprint:

* **Sprint 1 (Entrega 5 - 17/09):** Conclusão do *Vertical Slice* (Setup de infraestrutura, cadastro/autenticação e interface inicial - ~40h).
* **Sprint 2 (Entrega 6 - 01/10):** Conclusão do *Core* funcional (Publicação de solicitações, aceite/recusa por prestadores - ~32h).
* **Sprint 3 (Entrega 7 - 22/10):** Integração com Gateway de Pagamentos Asaas, aplicação de padrões OO e testes automatizados (~16h restantes + refinamento de testes).
* **Sprint 4 (Entrega 8 - 05/11):** Implantação em ambiente de staging, pipeline CD e correções finais (*hardening*).

---

## 4. Replanejamento após a Entrega 5

Na revisão de 01/10/2026, a equipe reordenou o trabalho conforme o progresso observado e as prioridades da Sprint. O escopo de cada Sprint pode ser ajustado no planejamento, mantendo o backlog do MVP como referência:

| Sprint | Entrega | Trabalho realizado ou priorizado |
|:---|:---|:---|
| **Sprint 1** | Entrega 5 - 17/09 | Interface inicial e integração da criação de solicitações e endereços com o backend, além da exibição de solicitações no painel do prestador. O aceite apresentado na interface ainda não conclui a transição de estado no backend. |
| **Sprint 2** | Entrega 6 - 01/10 | Cadastro, login e autenticação segura de usuários, incluindo sessão, proteção CSRF e controle de acesso por perfil. |

### Proposta para as próximas Sprints

Com base no backlog restante e no cronograma original, a proposta é priorizar:

* **Sprint 3 (Entrega 7 - 22/10):** concluir o fluxo de atendimento da solicitação: implementar aceite e recusa persistidos no backend; associar a solicitação ao prestador que a aceitou; impedir aceite duplicado; e permitir que o cliente acompanhe o prestador associado. A equipe também deve definir como cliente e prestador trocarão informações necessárias para combinar o serviço. Como chat em tempo real está fora do escopo do MVP, a equipe deve comparar alternativas simples, como liberar os dados de contato após o aceite ou orientar a combinação do serviço por um canal externo, considerando consentimento e privacidade. A decisão deve ser registrada; qualquer implementação entra no escopo apenas se couber na capacidade da Sprint. Completar os testes das regras de negócio e revisar os critérios de aceitação das histórias #8 e #9.
* **Sprint 4 (Entrega 8 - 05/11):** implementar a conclusão do serviço e a integração de pagamento com o Asaas, incluindo tratamento dos principais estados e falhas; preparar o ambiente de staging; completar o pipeline de CI/CD e realizar correções finais de estabilidade e segurança.

Essa sequência é uma proposta de replanejamento, não uma estimativa nova nem compromisso de escopo fechado. No próximo Sprint Planning, a equipe deve confirmar a prioridade, decompor as histórias em tarefas, estimar o esforço e ajustar o que não couber à capacidade disponível. A decisão sobre comunicação deve ser registrada antes de assumir uma solução ou ampliar o MVP.

## 5. Hipóteses Assumidas

1. **Dedicação Regular:** Cada integrante dedicará em média 6 horas semanais ao projeto, respeitando os limites da rotina acadêmica e profissional.
2. **Gestão de Escopo:** As funcionalidades do MVP e as estimativas iniciais de 88 horas permanecem como referência do planejamento. A ordem e a distribuição dos itens entre Sprints podem ser revistas pelo Scrum; mudanças de escopo ou estimativa devem ser registradas e reavaliadas pela equipe. Funcionalidades fora do MVP (ex.: geolocalização e chat em tempo real) continuam fora do escopo.
3. **Estabilidade Tecnológica:** A stack definida (Spring Boot, React, PostgreSQL, Docker) e as chaves de teste (sandbox) da API Asaas funcionarão sem impedimentos técnicos externos.
4. **Governança Git:** O fluxo de ramificação (`entrega-X`) e integração via Pull Requests revisados será mantido por todos os integrantes.
