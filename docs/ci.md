# CI - ServiceHub

## 1. Objetivo

Executar verificações automáticas em todo Pull Request destinado à `main`, evitando regressões simples: código que não compila, testes quebrados, configuração inválida e ausência de documentos obrigatórios.

## 2. Ferramenta

Foi utilizado o **GitHub Actions** para executar o pipeline.

## 3. Funcionamento

O workflow é executado quando um Pull Request para `main` é aberto, atualizado ou reaberto **e
também a cada push em `main`**, como exige a Entrega 9 (pipeline em PRs e no ramo principal).

Quatro jobs rodam em paralelo:

| Job | O que faz |
|:---|:---|
| `check_files` | Confere a presença dos documentos obrigatórios (README, USO-IA, ARQUITETURA, METRICAS, riscos, qualidade, ADR-0005 e o planejamento da Sprint 3). |
| `backend` | `./gradlew test jacocoTestReport --no-daemon` e publica o relatório HTML do JaCoCo como artefato `jacoco-backend`. |
| `frontend` | `npm ci`, `npm run build`, `npm test -- --run` (Vitest) e `npm run lint`. |
| `configuration` | `docker compose -f docker-compose.yml config --quiet` (validação sintática). |

O workflow **não** executa Playwright/E2E, **não** roda `npm run test:coverage` (cobertura do
frontend) e **não** faz varredura de segredos. Essas verificações são locais e estão descritas em
[PLANO-DE-TESTES.md](./PLANO-DE-TESTES.md).

## 4. Arquivo

O workflow está localizado em:

`.github/workflows/ci.yml`

## 5. Evidência local e do pull request

Na validação local da Entrega 9, a suíte backend passou com 138 testes e a suíte frontend passou
com 35 testes. O build e o lint frontend também passaram. O workflow publica o relatório HTML do
JaCoCo como artefato `jacoco-backend`; ele não executa Playwright nem cobertura do frontend.

## 6. Evolução

O workflow executa `./gradlew test jacocoTestReport --no-daemon` e gera o relatório JaCoCo. Na
validação local registrada em `docs/COBERTURA-JACOCO.md`, a cobertura de linhas foi de 95,24%,
acima da meta de 70%.