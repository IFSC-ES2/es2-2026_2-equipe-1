# ServiceHub

Projeto desenvolvido para a disciplina de **Engenharia de Software II** do curso de **Análise e Desenvolvimento de Sistemas** do **IFSC - Campus São José**.

## Integrantes

* Ana Clara Teixeira Ronzani (202510703694) - Scrum Master & Engenheira de Qualidade
* Davi Israel Quirino (202510703620) - Front End
* João Vitor Schmitt (202510703638) - DevOps/Infra
* Leonardo Victor Muller De Morais (202510703650) - Arquiteto de Software

## Tema do Projeto

O **ServiceHub** é uma plataforma para conectar clientes e prestadores de serviços, como encanadores, eletricistas, pintores e diaristas.

### (a) Qual problema o sistema pretende resolver?

Facilitar a **busca e contratação de profissionais autônomos**, que muitas vezes ocorre de forma informal e desorganizada.

### (b) Em qual área de aplicação?

Na área de **contratação e prestação de serviços autônomos**.

### (c) Quem são os usuários?

* **Clientes:** solicitam serviços.
* **Prestadores:** recebem, aceitam ou recusam solicitações.
* **Administradores:** gerenciam a plataforma.

### (d) Onde o sistema pode ser aplicado?

Pode ser utilizado **em qualquer cidade ou comunidade** que tenha clientes procurando serviços e profissionais disponíveis para realizá-los.

### (e) Por que o tema é relevante?

Porque facilita a contratação de profissionais e ajuda prestadores autônomos a encontrar **novas oportunidades de trabalho**.

### (f) Qual é a proposta do sistema?

O ServiceHub permite que o cliente **solicite um serviço**, que prestadores possam **aceitar ou recusar o atendimento** e que o **pagamento seja realizado pela plataforma**, por meio da integração com o Asaas. Assim, o processo de contratação fica mais simples e organizado.


## Como executar

```bash
# Backend (JDK 25) - http://localhost:8080
cd back-end && ./gradlew bootRun

# Frontend (Node 24) - http://localhost:5173
cd ServiceHub && npm ci && npm run dev
```

## Como testar

```bash
cd back-end && ./gradlew test jacocoTestReport --no-daemon
cd ServiceHub && npm ci && npm test && npm run build && npm run lint
```

## Linha do Projeto

O projeto está relacionado à linha:

**2. Gestão de serviços e processos institucionais.**

## Escopo do MVP (Minimum Viable Product)

### 🟢 Dentro do Escopo
- Cadastro e login (perfis de Cliente e Prestador).
- Cadastro e publicação de solicitações de serviços.
- Notificação/envio para profissionais disponíveis.
- Aceite ou recusa da solicitação pelo prestador.
- Acompanhamento do ciclo/status do serviço.

### 🔴 Fora do Escopo (Nesta versão)
- Geolocalização em tempo real dos profissionais e clientes no mapa.
- Chat em tempo real via WebSockets.
- Sistema de avaliação e comentários.
- Sistema de cupons e promoções.
- Aplicativo mobile nativo.

---

## Tecnologias (previstas)

* Java
* Spring Boot
* React
* PostgreSQL
* WebSocket
* Asaas API
* Docker
* Git e GitHub

## Política de Branches e Pull Requests

* A branch `main` é **protegida** e não permite alterações diretas.
* Toda alteração destinada à `main` deve ser realizada por meio de um **Pull Request (PR)**.
* O PR deve ser revisado antes de ser integrado à `main`.
* Commits diretamente na `main` são bloqueados.
* Alterações na `main` devem passar pelo fluxo de Pull Request definido no GitHub.

## Artefatos da Inception (Entrega 2)

- **Documento Completo de Inception:** [docs/inception.md](docs/inception.md)
- **Definition of Done (DoD):** [docs/dod.md](docs/dod.md)
- **Decisões Arquiteturais (ADRs):**
  - [ADR-0001: Definição da Stack Tecnológica (Java Spring Boot, React, Postgres)](docs/adrs/ADR-0001-stack-tecnologica.md)
  - [ADR-0002: Escolha do Gateway de Pagamentos Asaas](docs/adrs/ADR-0002-integracao-pagamentos-asaas.md)
- **Uso de Inteligência Artificial:** [USO-IA.md](USO-IA.md)
- **Quadro de Acompanhamento (Board & Backlog):** [Acessar GitHub Projects da Equipe](https://github.com/orgs/IFSC-ES2/projects/32/views/2)

## Artefatos da Entrega 3 (Estimativas e Métricas)

- **Linha de Base do Planejamento:** [docs/BASELINE.md](docs/BASELINE.md)
- **Processo de Estimativas (Planning Poker):** [docs/ESTIMATIVAS.md](docs/ESTIMATIVAS.md)
- **Definição de Métricas:** [docs/METRICAS.md](docs/METRICAS.md)
- **Fichas de Métricas:**
  - [M-01: Cobertura de Testes Automatizados](docs/metricas/M-01.md)
  - [M-02: Lead Time de Pull Requests](docs/metricas/M-02.md)
  - [M-03: Progresso do MVP](docs/metricas/M-03.md)
---

## Artefatos da Entrega 4 - Riscos e Qualidade

* **Registro e análise de riscos:** [docs/riscos.md](docs/riscos.md)
* **Fluxo de trabalho:** [docs/fluxo-de-trabalho.md](docs/fluxo-de-trabalho.md)
* **CI mínimo:** [docs/ci.md](docs/ci.md)
* **Pipeline do CI:** [.github/workflows/ci.yml](.github/workflows/ci.yml)
* **Critérios iniciais de qualidade:** [docs/qualidade.md](docs/qualidade.md)
* **Template de Pull Request:** [.github/PULL_REQUEST_TEMPLATE.md](.github/PULL_REQUEST_TEMPLATE.md)

---

## Entrega 5 - Sprint 1

- **Planejamento da Sprint 1:** [docs/entregas/sprint-1-planejamento.md](docs/entregas/sprint-1-planejamento.md)
- **Incremento:** criação/publicação de pedidos e cadastro de endereços.

---

## Entrega 6 - Sprint 2
- **Relato da Sprint e situação do PR/release:** [docs/entregas/sprint-2.md](docs/entregas/sprint-2.md)
- **Métricas e lacunas de coleta:** [docs/METRICAS.md](docs/METRICAS.md)
- **Revisão dos riscos:** [docs/riscos.md](docs/riscos.md)
- **Padrões de projeto OO:** [docs/PADROES-DE-PROJETO.md](docs/PADROES-DE-PROJETO.md)
- **ADR-0003: Autenticação:** [docs/adrs/ADR-0003-autenticacao.md](docs/adrs/ADR-0003-autenticacao.md)
O relato documenta o escopo e as evidências da Sprint e está pronto para acompanhar o PR. A release `v0.2.0` ainda não foi publicada.
- **Registro de uso de IA:** [USO-IA.md](USO-IA.md)

---
## Entrega 7 — Sprint 3

- **Inspeção e Replanejamento:** [docs/entregas/sprint-3-planejamento.md](docs/entregas/sprint-3-planejamento.md)
- **Arquitetura Efetiva (C4 Níveis 1 e 2):** [docs/ARQUITETURA.md](docs/ARQUITETURA.md)
- **Relatório de Reengenharia:** [docs/REENGENHARIA.md](docs/REENGENHARIA.md)
- **Acompanhamento de Métricas:** [docs/METRICAS.md](docs/METRICAS.md)
- **Registro e Matriz de Riscos:** [docs/RISCOS.md](docs/RISCOS.md)
- **Registro de Uso de IA:** [docs/USO-IA.md](docs/USO-IA.md)
- **Contribuições Individuais:** [docs/CONTRIBUICOES-SPRINT-3.md](docs/contribuições/CONTRIBUICOES-SPRINT-3.md)