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

## 5. Evidência

Na validação da Entrega 7, a suíte backend passou com 57 testes e a suíte frontend passou com 13 testes. O build e o lint frontend também passaram. A execução correspondente no GitHub ainda deve ser vinculada ao PR da entrega.

## 6. Evolução

O workflow executa `./gradlew test jacocoTestReport --no-daemon` e gera o relatório JaCoCo. Na validação local, a cobertura de linhas foi de 77,35%, acima da meta de 70%. A execução correspondente no GitHub ainda deve ser vinculada ao PR da entrega.