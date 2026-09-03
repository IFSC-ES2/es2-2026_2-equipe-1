# CI - ServiceHub

## 1. Objetivo

O CI tem como objetivo executar verificações automaticamente nos Pull Requests destinados à branch `main`.

## 2. Ferramenta

Foi utilizado o **GitHub Actions** para executar o pipeline.

## 3. Funcionamento

O pipeline é executado automaticamente quando um Pull Request é aberto ou atualizado para a branch `main`.

Atualmente são realizados checks básicos para verificar a estrutura mínima do projeto.

## 4. Arquivo

O workflow está localizado em:

`.github/workflows/ci.yml`

## 5. Evidência

A execução do pipeline deve ser verificada nos Pull Requests da Entrega 4, onde os checks devem aparecer como concluídos.

## 6. Evolução

Nas próximas etapas, o pipeline poderá ser ampliado para executar build, testes automatizados, lint e análise de cobertura.
