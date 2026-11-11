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

| Métrica | Baseline (20/08) | Sprint 1 (17/09) | Sprint 2 (01/10) | Sprint 3 (22/10) | Sprint 4 (Final) | Meta |
|:---|:---:|:---:|:---:|:---:|:---:|:---:|
| M-01 Cobertura de testes | 0% | Não medida | Não medida | **77,35% de linhas** | **Suíte estabilizada:** `SecurityIntegrationTest` e Playwright E2E (3/3 cenários aprovados). Cobertura JaCoCo final não recalculada. | ≥ 70% |
| M-02 Lead time de PR | 0h | Não registrado | A medir | A confirmar no GitHub | **Média de aprovação mantida em menos de 24h**; confirmar pelos registros dos PRs no GitHub. | < 24h |
| M-03 Progresso do MVP | 0% | 18,2% | 36,4% | 54,5% | **100% do escopo revisado**, com foco em staging, E2E, Docker e correções de perfil e segurança. A integração Asaas foi postergada e retirada do escopo desta entrega. | 100% do escopo revisado |

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

### 4.3 Encerramento da Sprint 4

Na Sprint 4, o foco foi a estabilidade da aplicação e sua validação: preparação do ambiente de staging, evolução da execução com Docker, testes E2E com Playwright e correções no fluxo de edição de perfil e nas configurações de segurança relacionadas a permissões, CORS e CSRF.

A suíte de testes foi estabilizada: os testes de integração de segurança do Spring Boot passaram, os testes de frontend passaram e os três cenários E2E do Playwright foram aprovados. Esses resultados ampliam a confiança nos fluxos críticos e ajudam a identificar regressões entre frontend, API e autenticação. O ambiente de staging também permite validar a aplicação em uma implantação acessível à equipe, embora os dados possam ser limpos periodicamente.

O percentual de M-03 considera o escopo revisado para a entrega. A integração de pagamentos com Asaas foi postergada para uma etapa futura, para priorizar deploy e estabilidade. Portanto, os 100% não significam que todos os itens do escopo original de 88 horas — que incluía o Asaas — foram concluídos. A cobertura JaCoCo final deve ser coletada novamente antes de atualizar o percentual numérico de M-01.

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