# Evidência de testes e cobertura JaCoCo — v1.0.0-rc.1

**Data da execução local:** 18/11/2026
**Comando executado:** `.\gradlew.bat test jacocoTestReport --no-daemon` em `back-end/`

## Resultado do backend

| Métrica | Resultado |
|:---|---:|
| Testes executados | 138 |
| Falhas / erros / ignorados | 0 / 0 / 0 |
| Cobertura de linhas | 95,24% |
| Cobertura de instruções | 94,27% |
| Cobertura de branches | 81,53% |
| Cobertura de métodos | 91,49% |
| Cobertura de classes | 100,00% |

O relatório foi gerado localmente pelo Gradle. O workflow de CI executa o mesmo
comando em `back-end/` e publica o relatório HTML como artefato. Os números
acima correspondem à execução local registrada para esta RC; não são uma
afirmação de execução do GitHub Actions.

## Testes de autenticação e fluxos atuais

A autenticação é stateless com JWT. As evidências atuais incluem:

- `UsuarioJwtIntegrationTest.loginEmiteJwtQueAutenticaRequisicoesStateless`:
  autentica pelo endpoint de login e usa o JWT em `Authorization: Bearer`.
- `SolicitacaoPropostaIntegrationTest.fluxoPropostaAceite_eConcluidoViaHttp`:
  percorre envio de proposta pelo prestador, aceite pelo cliente e conclusão,
  usando os tokens JWT emitidos pelo endpoint real.
- `SolicitacaoPropostaIntegrationTest.enderecoCadastrado_deveAparecerNaListagem`:
  valida cadastro e listagem HTTP de endereço.

As evidências de cobertura e execução do frontend e do Playwright estão
separadas no [Plano de Testes](./PLANO-DE-TESTES.md) e na
[Matriz de Aceitação](./ACEITACAO.md). Playwright é executado localmente e não
faz parte do CI.

## Resultado do frontend

Medição local separada do backend, com `npm run test:coverage` em `ServiceHub/` (Vitest + `@vitest/coverage-v8`):

| Métrica | Resultado |
|:---|---:|
| Testes executados | 35 |
| Falhas | 0 |
| Cobertura de linhas | 20,81% |
| Cobertura de statements | 20,81% |
| Cobertura de branches | 70,94% |
| Cobertura de funções | 61,29% |

A cobertura é concentrada nos módulos de serviço e nos componentes cobertos por teste
(`src/servicos/cep.ts`, `src/servicos/api.ts`, `src/servicos/autenticacao.ts`,
`src/componentes/InputSenha`, `src/componentes/modal/ModalPerfilUsuario`). As páginas
(`DashboardCliente`, `DashboardPrestador`, `Cadastro`, `Home`, `SelecaoUser`) e os modais de criação
não têm teste de componente e respondem pela maior parte das linhas descobertas. O CI **não** executa
`npm run test:coverage`; este percentual não é produzido pelo workflow.

## Relatórios gerados

```text
back-end/build/reports/jacoco/test/html/index.html
back-end/build/reports/jacoco/test/jacocoTestReport.xml
```

O job de backend do CI publica o relatório HTML. O frontend usa Vitest, mas o
workflow executa `npm test -- --run`, sem a opção de cobertura; `npm run
test:coverage` não foi executado nesta validação da RC. Por isso, este documento
não atribui um percentual atual de cobertura ao frontend.
