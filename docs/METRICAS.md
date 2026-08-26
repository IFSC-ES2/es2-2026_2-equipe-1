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
