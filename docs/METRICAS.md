# Definição e Acompanhamento de Métricas - ServiceHub

Para garantir a qualidade do software, a previsibilidade do processo de desenvolvimento e a visibilidade do avanço do projeto, a equipe definiu um conjunto de métricas distribuído nas três categorias essenciais.

---

## 1. Categorias das Métricas Escolhidas

### 1.1 Métrica de Produto

- **M-01: Cobertura de Testes Automatizados (Code Coverage %)**
- **Objetivo:** avaliar a porcentagem do código backend (Spring Boot) coberta por testes unitários e de integração, reduzindo o risco de regressões.

### 1.2 Métrica de Processo

- **M-02: Lead Time de Pull Requests (Tempo Médio de Review/Merge)**
- **Objetivo:** acompanhar a eficiência do fluxo de trabalho e evitar gargalos nas revisões de código entre os integrantes.

### 1.3 Métrica de Projeto

- **M-03: Progresso do MVP (Horas Estimadas dos Itens Concluídos)**
- **Objetivo:** acompanhar o progresso dos itens do MVP em relação às 88 horas estimadas no Planning Poker.

---

## 2. Tabela de Acompanhamento das Métricas

| Código | Métrica | Categoria | Baseline Inicial | Meta | Frequência | Responsável |
|:---:|:---|:---:|:---:|:---:|:---:|:---|
| **M-01** | Cobertura de testes | Produto | 0% | ≥ 70% | A cada PR / Sprint | Ana Clara Teixeira Ronzani (QA) |
| **M-02** | Lead time de PRs | Processo | 0h | < 24h | Semanal | Davi Israel Quirino |
| **M-03** | Progresso do MVP | Projeto | 0h / 88h | 88h / 88h | A cada entrega | Leonardo Victor Muller De Morais |

---

## 3. Fichas detalhadas das métricas

### 3.1 Ficha da Métrica M-01

- **Nome:** Cobertura de Testes Automatizados (Code Coverage)
- **Classificação:** Produto
- **Objetivo:** garantir a confiabilidade e a facilidade de manutenção das regras de negócio no backend Spring Boot.
- **Fórmula:**

  ```text
  Cobertura (%) = (Linhas de código executadas por testes / Total de linhas elegíveis) × 100
  ```

- **Fonte dos dados:** relatório JaCoCo gerado pelo Gradle no pipeline do GitHub Actions.
- **Frequência de atualização:** a cada Pull Request e ao final de cada Sprint.
- **Responsável:** Ana Clara Teixeira Ronzani, Engenheira de Qualidade.

#### Forma de interpretação

- **Abaixo de 50%:** crítico; indica baixa confiabilidade e exige expansão da suíte antes da aprovação.
- **Entre 50% e 69%:** aceitável para uma fase inicial, mas requer ampliação dos testes.
- **70% ou superior:** meta atingida para a maturidade pretendida no MVP do ServiceHub.

### 3.2 Ficha da Métrica M-02

- **Nome:** Lead Time de Pull Requests.
- **Classificação:** Processo.
- **Fórmula:** tempo entre a abertura e o merge do Pull Request.
- **Meta:** menor que 24 horas.
- **Fonte dos dados:** histórico de Pull Requests do GitHub.
- **Status:** aguardando o registro dos horários reais de abertura, aprovação e merge do PR da Entrega 7.

### 3.3 Ficha da Métrica M-03

- **Nome:** Progresso do MVP.
- **Classificação:** Projeto.
- **Fórmula:**

  ```text
  Progresso (%) = (Horas estimadas dos itens concluídos / 88 horas estimadas do MVP) × 100
  ```

- **Fonte dos dados:** backlog, Planning Poker e itens concluídos nas Sprints.
- **Observação:** o cálculo utiliza as estimativas dos itens concluídos, não representa horas efetivamente trabalhadas.

---

## 4. Resultado das Sprints

| Métrica | Baseline (20/08) | Sprint 1 (17/09) | Sprint 2 (01/10) | Sprint 3 (22/10) | Meta |
|:---|:---:|:---:|:---:|:---:|:---:|
| M-01 Cobertura de testes | 0% | Não medida | Não medida | **77,35% de linhas** | ≥ 70% |
| M-02 Lead time de PR | 0h | Não registrado | A medir | A confirmar no GitHub | < 24h |
| M-03 Progresso do MVP | 0/88h (0%) | 16/88h (18,2%) | 32/88h (36,4%) | **48/88h (54,5%)** | 88/88h |

### 4.1 Itens considerados no progresso do MVP

| Item | Estimativa | Contado na Sprint 3? | Motivo |
|:---|---:|:---:|:---|
| #7 Cadastro e autenticação | 16h | Sim | Funcional e validado por testes unitários e de integração |
| #8 Criar/publicar solicitação | 16h | Sim | Funcional e integrado ao backend |
| #9 Aceite/recusa | 8h | **Parcial** | O aceite de proposta está implementado e persiste no backend; a recusa permanece pendente |
| #10 Pagamento Asaas | 24h | Não | Não iniciado; replanejado para a Sprint 4 |
| #13 Infraestrutura | 16h | Não | Parcial; CI implementado, mas PostgreSQL e staging permanecem pendentes |
| #14 Documentação viva | 8h | Sim | Documentação de arquitetura, reengenharia, métricas, riscos e qualidade atualizada |
| **Total contado** | **48h / 88h** |  | **54,5%** |

### 4.2 Análise da evolução

Na Sprint 1, foram considerados 16h de itens concluídos. Na Sprint 2, o acumulado chegou a 32h, correspondendo a 36,4% do MVP. Na Sprint 3, foram acrescentados o aceite de propostas e a documentação viva, totalizando 48h estimadas de itens concluídos, ou 54,5% do MVP.

O item #9 foi concluído parcialmente: o aceite de propostas está implementado, com persistência do prestador, do valor proposto e do status `EM_ANDAMENTO`; a operação explícita de recusa ainda precisa ser implementada ou formalmente replanejada.

A métrica M-01 evoluiu de uma situação sem medição para 77,35% de cobertura de linhas. Foram executados 57 testes backend, sem falhas, erros ou testes ignorados. A cobertura de branches foi de 63,89% e permanece como oportunidade de melhoria.

A métrica M-02 ainda depende das evidências reais do GitHub para registrar o lead time do Pull Request. A execução local do pipeline não substitui os horários de abertura, aprovação e merge registrados no GitHub.

---

## 5. Histórico de Coletas da M-01

| Data | Marco / Sprint | Valor coletado | Observações / Ações tomadas |
|:---:|:---:|:---:|:---|
| 20/08/2026 | Entrega 3 — Baseline | 0% | Registro inicial durante a fase de planejamento |
| 22/10/2026 | Entrega 7 — Sprint 3 | **77,35% de linhas** | 57 testes backend passaram; relatório gerado por `./gradlew test jacocoTestReport --no-daemon`; meta de 70% atingida |

## 6. Evidências

- Relatório resumido: [`docs/evidencias/COBERTURA-JACOCO.md`](evidencias/COBERTURA-JACOCO.md)
- Relatório XML: [`docs/evidencias/jacocoTestReport.xml`](evidencias/jacocoTestReport.xml)
- Workflow de CI: [`.github/workflows/ci.yml`](../.github/workflows/ci.yml)
- Testes backend: `back-end/src/test/`
- Testes frontend: `ServiceHub/tests/`