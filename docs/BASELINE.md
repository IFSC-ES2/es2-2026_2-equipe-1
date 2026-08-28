# Linha de Base (Baseline) do Planejamento - ServiceHub

**Data de Registro:** 20 de Agosto de 2026  
**Versão:** 1.0  

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

## 3. Previsão de Conclusão e Cronograma Incremental

Considerando o esforço total estimado para o MVP (88 horas) e a capacidade quinzenal ajustada da equipe (48 horas/Sprint), o desenvolvimento foi planejado de forma incremental ao longo do calendário acadêmico:

* **Sprint 1 (Entrega 5 - 17/09):** Conclusão do *Vertical Slice* (Setup de infraestrutura, cadastro/autenticação e interface inicial - ~40h).
* **Sprint 2 (Entrega 6 - 01/10):** Conclusão do *Core* funcional (Publicação de solicitações, aceite/recusa por prestadores - ~32h).
* **Sprint 3 (Entrega 7 - 22/10):** Integração com Gateway de Pagamentos Asaas, aplicação de padrões OO e testes automatizados (~16h restantes + refinamento de testes).
* **Sprint 4 (Entrega 8 - 05/11):** Implantação em ambiente de staging, pipeline CD e correções finais (*hardening*).

---

## 4. Hipóteses Assumidas

1. **Dedicação Regular:** Cada integrante dedicará em média 6 horas semanais ao projeto, respeitando os limites da rotina acadêmica e profissional.
2. **Estabilidade de Escopo:** O escopo do MVP permanecerá fixo nas 88 horas estimadas, sem adição de novas funcionalidades fora do escopo (ex.: geolocalização e chat em tempo real).
3. **Estabilidade Tecnológica:** A stack definida (Spring Boot, React, PostgreSQL, Docker) e as chaves de teste (sandbox) da API Asaas funcionarão sem impedimentos técnicos externos.
4. **Governança Git:** O fluxo de ramificação (`entrega-X`) e integração via Pull Requests revisados será mantido por todos os integrantes.
