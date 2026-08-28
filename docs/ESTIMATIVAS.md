# Processo de Estimativas - ServiceHub

---

## 1. Técnica e Unidade Utilizadas

A equipe adotou a técnica do **Planning Poker** para dimensionar as tarefas do backlog do MVP. As estimativas foram realizadas de forma colaborativa, utilizando **Horas Estimadas** como unidade de medida para permitir uma comparação direta e realista com a capacidade útil de dedicação dos integrantes.

* **Técnica:** Planning Poker
* **Unidade de Estimativa:** Horas de Trabalho (h)
* **Deck de Cartas Adotado:** Sequência adaptada de tempo `[ 2, 4, 8, 16, 24, 32 ]` horas.
* **Item de Referência (Baseline de 2h):** Criação de uma rota REST simples no Spring Boot (healthcheck) ou estilização de um componente de botão/formulário simples em React.

---

## 2. Participantes

A sessão de estimativa contou com a participação de todos os papéis do projeto:
* **Ana Clara Teixeira Ronzani** (Engenheira de Qualidade)
* **Davi Israel Quirino** (Scrum Master & Front-end)
* **João Vitor Schmitt** (DevOps / Infraestrutura)
* **Leonardo Victor Muller De Morais** (Arquiteto de Software & Back-end)

---

## 3. Critérios de Dimensionamento e Votação

Durante a dinâmica de votação do Planning Poker, os consensos foram alcançados avaliando três dimensões:

1. **Complexidade Técnica:** Mapeamento de integrações externas (ex.: API de pagamento do Asaas e webhooks) demandou a maior estimativa (24h) pela necessidade de tratamento de exceções e testes de concorrência.
2. **Esforço de Interface e Regra de Negócio:** Módulos com múltiplos perfis (Cliente vs. Prestador) foram dimensionados considerando a criação das telas em React e integração com o backend.
3. **Incerteza e Configuração:** Tarefas de infraestrutura (Docker, pipelines no GitHub Actions) foram estimadas considerando a necessidade de padronização entre os ambientes de desenvolvimento dos 4 integrantes.

---

## 4. Limitações e Incertezas Percebidas

* **Capacidade Adequada da Equipe:** A escolha por 6 horas semanais por integrante reflete a busca por sustentabilidade e previsibilidade, evitando estimativas irrealistas.
* **Inexperiência com Webhooks Asaas:** O tempo alocado para a integração financeira inclui uma margem de segurança para aprendizado prático da API durante a Sprint 3.
* **Ambiente de Execução:** Possíveis divergências entre ambientes OS (Windows/Linux) durante a execução de containers Docker locais.
