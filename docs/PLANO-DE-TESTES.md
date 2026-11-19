# Plano de testes — Release Candidate v1.0.0-rc.1

**Projeto:** ServiceHub
**Versão de referência:** `v1.0.0-rc.1`
**Data da validação local registrada:** 18/11/2026

Este plano separa as verificações executadas pelo workflow de CI das verificações
que dependem do ambiente local. A matriz de aceitação do MVP está em
[ACEITACAO.md](./ACEITACAO.md).

## 1. Estratégia e escopo

| Camada | Ferramenta | O que verifica | Onde é executada |
|:---|:---|:---|:---|
| Backend | JUnit 5, Spring Boot Test, MockMvc e JaCoCo | Regras de negócio, integração HTTP, autenticação JWT, autorização, solicitações, propostas e endereços | CI e localmente |
| Frontend | Vitest e Testing Library | Serviços de API/autenticação e componentes React | CI e localmente |
| Build e lint do frontend | TypeScript, Vite e ESLint | Compilação de produção, tipos e padrões de código | CI e localmente |
| End-to-End | Playwright | Fluxos completos no navegador, incluindo cadastro/login, perfil e ciclo da solicitação | **Localmente; não executado pelo CI** |
| Configuração | Docker Compose | Validade sintática do `docker-compose.yml` | CI |

### O que o CI executa

O workflow `.github/workflows/ci.yml` é acionado em pull requests direcionados à
branch `main`. Ele executa:

- Backend: `./gradlew test jacocoTestReport --no-daemon` em `back-end/` e publica
  o relatório HTML JaCoCo como artefato.
- Frontend: `npm ci`, `npm run build`, `npm test -- --run` (Vitest) e
  `npm run lint` em `ServiceHub/`.
- Validação de configuração: `docker compose -f docker-compose.yml config --quiet`.

O workflow **não executa Playwright**. Portanto, os resultados E2E desta RC são
execuções locais e não devem ser apresentados como resultados do GitHub Actions.
O CI também não executa `npm run test:coverage`; a cobertura JaCoCo é gerada
para o backend, enquanto a cobertura do frontend é uma medição local separada.

## 2. Execução local

Comandos equivalentes para Windows e para Linux/macOS:

```powershell
# Backend — Windows
cd back-end
.\gradlew.bat test jacocoTestReport --no-daemon

# Frontend — Windows
cd ..\ServiceHub
npm ci
npm run build
npm test -- --run
npm run lint
npm run test:e2e
```

```bash
# Backend — Linux/macOS
cd back-end
./gradlew test jacocoTestReport --no-daemon

# Frontend — Linux/macOS
cd ../ServiceHub
npm ci
npm run build
npm test -- --run
npm run lint
npm run test:e2e
```

O Playwright precisa do Chromium instalado e de `JWT_SECRET` configurado para
iniciar o backend local. Consulte [DEPLOY.md](./DEPLOY.md) para a configuração
local da variável. O comando E2E inicia o ambiente definido em
`ServiceHub/playwright.config.ts`; não é um job do CI.

## 3. Evidências da validação da RC

Execução local registrada em 18/11/2026:

| Verificação | Resultado |
|:---|:---|
| Backend — `gradlew test jacocoTestReport --no-daemon` | 131 testes; 0 falhas, 0 erros, 0 ignorados; `BUILD SUCCESSFUL` |
| Backend — cobertura JaCoCo de linhas | 95,38% |
| Frontend — `npm test -- --run` | 29 testes aprovados em 3 arquivos |
| Frontend — `npm run build` | Aprovado |
| Frontend — `npm run lint` | Aprovado |
| Playwright — `npm run test:e2e` | 3 cenários aprovados localmente |

No PowerShell desta execução, os comandos npm foram invocados como `npm.cmd`
devido à política local que bloqueia `npm.ps1`; isso não altera o conteúdo nem
o resultado dos scripts do projeto.

Os três cenários Playwright aprovados foram:

- `login apresenta erro para credenciais inválidas`
- `cliente edita perfil e os dados persistem após recarregar`
- `cliente publica solicitação e prestador envia proposta`

O último cenário percorre publicação, envio e aceite de proposta e conclusão do
serviço. O preenchimento do endereço nele é manual; não comprova a consulta
externa de CEP. As evidências e ressalvas por caso estão detalhadas em
[ACEITACAO.md](./ACEITACAO.md).

## 4. Limitações e itens não medidos

- Playwright é executado localmente, fora do workflow de CI.
- Não há, nesta validação, teste automatizado que comprove a consulta real à API
  externa ViaCEP; há testes de cadastro/listagem do endereço e preenchimento
  manual no E2E.
- `npm run test:coverage` não foi executado nesta RC; não há percentual atual de
  cobertura do frontend reportado aqui.
- Os testes aprovam o fluxo de proposta, aceite e conclusão; recusa de proposta
  e notificações ativas permanecem limitações, conforme a matriz de aceitação.
- A integração de pagamentos Asaas não tem evidência de teste nesta validação.
