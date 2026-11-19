# CI - ServiceHub

## 1. Objetivo

Executar verificações automáticas em todo Pull Request destinado à `main`, evitando regressões simples: código que não compila, testes quebrados, configuração inválida e ausência de documentos obrigatórios.

## 2. Ferramenta

Foi utilizado o **GitHub Actions** para executar o pipeline.

## 3. Funcionamento

O workflow é executado quando um Pull Request para `main` é aberto, atualizado ou reaberto. Ele roda os testes do backend, os testes automatizados do frontend, o build e o lint do frontend, e valida a configuração do Docker Compose.

## 4. Arquivo

O workflow está localizado em:

`.github/workflows/ci.yml`

## 5. Evidência local e do pull request

Na validação local da Entrega 9, a suíte backend passou com 131 testes e a suíte frontend passou
com 29 testes. O build e o lint frontend também passaram. O workflow publica o relatório HTML do
JaCoCo como artefato `jacoco-backend`; ele não executa Playwright nem cobertura do frontend.

## 6. Evolução

O workflow executa `./gradlew test jacocoTestReport --no-daemon` e gera o relatório JaCoCo. Na
validação local registrada em `docs/COBERTURA-JACOCO.md`, a cobertura de linhas foi de 95,38%,
acima da meta de 70%.