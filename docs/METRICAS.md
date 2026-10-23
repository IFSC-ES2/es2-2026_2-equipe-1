# Definição e Acompanhamento de Métricas - ServiceHub

Para garantir a qualidade do software, a previsibilidade do processo de desenvolvimento e a visibilidade do avanço do projeto, a equipe definiu um conjunto de métricas distribuído nas três categorias essenciais.

---

## 1. Categorias das Métricas Escolhidas

### 1.1 Métrica de Produto
* **M-01: Cobertura de Testes Automatizados (Code Coverage %)**
  * *Objetivo:* Avaliar a porcentagem do código backend (Spring Boot) coberta por testes unitários e de integração, garantindo que as regras de negócio não sofram regressão.

### 1.2 Métrica de Processo
* **M-02: Lead Time de Pull Requests (Tempo Médio de Review/Merge)**
  * *Objetivo:* Acompanhar a eficiência do fluxo de trabalho e evitar gargalos nas revisões de código entre os integrantes.

### 1.3 Métrica de Projeto
* **M-03: Progresso do MVP (Horas Executadas vs. Planejadas)**
  * *Objetivo:* Acompanhar o consumo das 88 horas estimadas no Planning Poker em relação ao escopo entregue ao longo das Sprints.

---

## 2. Tabela de Acompanhamento das Métricas

| Código | Métrica | Categoria | Baseline Inicial | Meta | Frequência | Responsável |
|:---:|:---|:---:|:---:|:---:|:---:|:---|
| **M-01** | Cobertura de Testes | Produto | 0% | $\ge 70\%$ | A cada PR / Sprint | Ana Clara (QA) |
| **M-02** | Lead Time de PRs | Processo | 0h | $< 24\text{h}$ | Semanal | Davi Israel (SM) |
| **M-03** | Progresso do MVP | Projeto | 0h / 88h | 88h / 88h | A cada Entrega | Leonardo (Arquiteto) |

## 3. Resultado das Sprints

| Métrica | Baseline (20/08) | Sprint 1 (17/09) | Sprint 2 (01/10) | Meta |
|:---|:---:|:---:|:---:|:---:|
| M-01 Cobertura de testes | 0% | Não medida | **77,35% de linhas** (JaCoCo) | >= 70% |
| M-02 Lead time de PR | 0h | Não registrado | **A medir após o merge do PR da Entrega 6** | < 24h |
| M-03 Progresso do MVP (por itens concluídos) | 0/88h (0%) | 16/88h (18,2%) | **32/88h (36,4%)** | 88/88h |

### Como M-03 foi calculado
Não há apontamento de horas executadas. O valor usa a **estimativa do Planning Poker dos itens do backlog considerados concluídos**, conforme a fórmula da ficha M-03 (horas das tarefas Done / 88h). É uma aproximação, não horas trabalhadas.

| Item | Estimativa | Contado? | Motivo |
|:---|:---:|:---:|:---|
| #7 Cadastro e autenticação | 16h | Sim (Sprint 2) | Funcional e testado em unidade; falta teste HTTP |
| #8 Criar/publicar solicitação | 16h | Sim (Sprint 1) | Funcional, integrado ao backend |
| #9 Aceite/recusa | 8h | Parcial | Aceite de proposta implementado e persistido; recusa ainda pendente |
| #10 Pagamento Asaas | 24h | Não | Não iniciado |
| #13 Infraestrutura | 16h | Não | Parcial: CI ok; compose/PostgreSQL pendentes |
| #14 Documentação viva | 8h | Não | Parcial |
| **Total contado** | **32h / 88h** | | **36,4%** |

### Análise
- **Planejado x executado:** a capacidade acumulada das duas sprints é de 96h (2 x 48h), maior que as 88h do MVP. Foram concluídos 32h de itens (cerca de um terço da capacidade acumulada). O baseline original previa chegar à Sprint 2 com #7, #8 e #9 prontos; #9 ficou de fora.
- **O que influenciou:** (a) o replanejamento da Sprint 1 (troca de #7 por #8) deslocou a autenticação; (b) parte do esforço foi para ajustes visuais e Dockerfiles, que não são itens do backlog; (c) a ausência de apontamento de horas e de relatório de cobertura impede comparações numéricas reais.
- **Comparação com a Sprint 1:** +16h de itens concluídos (+18,2 pontos percentuais). O ganho vem de um único item (#7).
- **M-01:** 57 testes backend passaram e o JaCoCo registrou 77,35% de cobertura de linhas; a meta de 70% foi atingida. A cobertura de branches permanece em 63,89%.
- **M-02:** o PR da Entrega 6 ainda não foi mesclado; registrar abertura e merge ao final.