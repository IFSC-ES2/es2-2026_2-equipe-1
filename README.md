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

### Execução Local do Backend

O backend requer `JWT_SECRET` no ambiente antes de iniciar. Os comandos abaixo geram 32 bytes aleatórios e os exportam em Base64. Execute-os a partir da raiz do repositório.

**PowerShell (Windows):**

```powershell
cd back-end
$bytes = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
.\gradlew.bat bootRun
```

**Bash (Linux/macOS):**

```bash
cd back-end
export JWT_SECRET=$(openssl rand -base64 32)
./gradlew bootRun
```

O backend fica disponível em <http://localhost:8081>.

### Frontend

```bash
cd ServiceHub
npm ci
npm run dev
```

## Como testar

```bash
cd back-end && ./gradlew test jacocoTestReport --no-daemon
cd ServiceHub && npm ci && npm test && npm run build && npm run lint
```

### Teste E2E do fluxo de solicitação e proposta

Com o backend em execução em `http://localhost:8081`, execute:

```bash
cd ServiceHub
npm ci
npx playwright install chromium
npm run test:e2e
```

O teste inicia o frontend Vite em `http://localhost:5173`. Ele cria as contas fixas de teste apenas na primeira execução; depois, faz login e reutiliza as mesmas contas e o endereço já cadastrado. Os dados são exclusivos para teste e não devem ser usados em produção. Para assistir ao fluxo no Chromium, com as ações desaceleradas, execute `npm run test:e2e:visivel`. Para usar outra URL do frontend, defina `PLAYWRIGHT_BASE_URL`.

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
- **Planejamento da Sprint 2:** [docs/entregas/sprint-2-planejamento.md](docs/entregas/sprint-2-planejamento.md)
- **Métricas e lacunas de coleta:** [docs/METRICAS.md](docs/METRICAS.md)
- **Revisão dos riscos:** [docs/riscos.md](docs/riscos.md)
- **Padrões de projeto OO:** [docs/PADROES-DE-PROJETO.md](docs/PADROES-DE-PROJETO.md)
- **ADR-0003: Autenticação:** [docs/adrs/ADR-0003-autenticacao.md](docs/adrs/ADR-0003-autenticacao.md)
O planejamento documenta o escopo definido para a Sprint 2.
- **Registro de uso de IA:** [USO-IA.md](USO-IA.md)

---
## Entrega 7 — Sprint 3

- **Inspeção e Replanejamento:** [docs/entregas/sprint-3-planejamento.md](docs/entregas/sprint-3-planejamento.md)
- **Arquitetura Efetiva (C4 Níveis 1 e 2):** [docs/ARQUITETURA.md](docs/ARQUITETURA.md)
- **Relatório de Reengenharia:** [docs/REENGENHARIA.md](docs/REENGENHARIA.md)
- **Acompanhamento de Métricas:** [docs/METRICAS.md](docs/METRICAS.md)
- **Registro e Matriz de Riscos:** [docs/riscos.md](docs/riscos.md)
- **Registro de Uso de IA:** [USO-IA.md](USO-IA.md)
- **Contribuições Individuais:** [docs/contribuições/CONTRIBUICOES-SPRINT-3.md](docs/contribuições/CONTRIBUICOES-SPRINT-3.md)

---
## Entrega 8 — Sprint 4 (Concluída)

- **Planejamento da Sprint 4:** [docs/entregas/sprint-4-planejamento.md](docs/entregas/sprint-4-planejamento.md)
- **Guia de Deploy e Staging:** [docs/DEPLOY.md](docs/DEPLOY.md)
- **Execução local e variáveis de ambiente:** [docs/DEPLOY.md — Execução Local do Backend](docs/DEPLOY.md#execu%C3%A7%C3%A3o-local-do-backend)
- **Plano de Testes e Resultados da Sprint 4:** [docs/PLANO-DE-TESTES.md](docs/PLANO-DE-TESTES.md)
- **Evidência de Cobertura (backend e frontend):** [docs/COBERTURA-JACOCO.md](docs/COBERTURA-JACOCO.md)
- **Métricas Finais do Projeto:** [docs/METRICAS.md](docs/METRICAS.md)
- **Matriz de Riscos Atualizada:** [docs/riscos.md](docs/riscos.md)
- **Registro de Uso de IA:** [USO-IA.md](USO-IA.md)
- **Contribuições Individuais:** [docs/contribuições/CONTRIBUICOES-SPRINT-4.md](docs/contribuições/CONTRIBUICOES-SPRINT-4.md)
- **Ambiente de Validação Online:** <https://servicehub.jvs.dev.br>

---

## Entrega 9 — Release Candidate v1.0.0-rc.1

- **Release Candidate:** [docs/entregas/release-candidate.md](docs/entregas/release-candidate.md)
- **Matriz de Aceitação:** [docs/ACEITACAO.md](docs/ACEITACAO.md)
- **Retrospectiva da RC:** [docs/RETROSPECTIVA.md](docs/RETROSPECTIVA.md)
- **Staging do frontend:** <https://frontservicehub.jvs.dev.br>
- **API de staging:** <https://servicehub.jvs.dev.br>