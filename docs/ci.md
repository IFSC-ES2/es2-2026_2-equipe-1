# CI - ServiceHub

## 1. Objetivo

O CI tem como objetivo executar verificações automaticamente nos Pull Requests destinados à branch `main`.

## 2. Ferramenta

Foi utilizado o **GitHub Actions** para executar o pipeline.

## 3. Funcionamento

O workflow é executado quando um Pull Request para `main` é aberto, atualizado ou reaberto. Ele roda os testes do backend, o build e o lint do frontend, e valida a configuração do Docker Compose.

## 4. Arquivo

O workflow está localizado em:

`.github/workflows/ci.yml`

## 5. Evidência

Os resultados locais passaram em 01/10/2026. Os checks do GitHub ainda precisam aparecer no PR da Entrega 6, que será aberto ao final do dia.

## 6. Evolução

O workflow ainda não gera relatório de cobertura de testes.
