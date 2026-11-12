# Critérios de Qualidade - ServiceHub

## 1. Objetivo

Os critérios abaixo definem os principais atributos de qualidade que serão considerados durante o desenvolvimento do MVP do ServiceHub.

Como referência, será utilizado o modelo de qualidade da **ISO/IEC 25010**.

## 2. Atributos prioritários

### 2.1 Manutenibilidade

O sistema deve possuir código organizado e modular, facilitando alterações e correções.

**Por que é importante:** o projeto será desenvolvido em várias etapas e poderá sofrer alterações durante o semestre.

**Como avaliar futuramente:**

* organização dos módulos;
* facilidade para modificar funcionalidades;
* cobertura de testes.

### 2.2 Confiabilidade

O sistema deve executar corretamente as principais funcionalidades do MVP.

**Por que é importante:** erros no cadastro, solicitações ou pagamentos podem prejudicar diretamente usuários e prestadores.

**Como avaliar futuramente:**

* quantidade de falhas encontradas;
* testes automatizados;
* cobertura de testes.

### 2.3 Segurança

Os dados dos usuários e as operações realizadas pelo sistema devem ser protegidos.

**Por que é importante:** o sistema possui usuários autenticados e integração com pagamentos.

**Como avaliar futuramente:**

* funcionamento da autenticação;
* proteção das informações;
* ausência de dados sensíveis expostos.

### 2.4 Capacidade de interação

As telas devem permitir que os usuários realizem as principais operações do MVP de forma simples.

**Por que é importante:** clientes e prestadores precisam conseguir utilizar o fluxo de contratação sem dificuldade.

**Como avaliar futuramente:**

* facilidade de execução das tarefas;
* quantidade de erros cometidos pelo usuário;
* feedback dos usuários durante testes.

## 3. Relação entre riscos e qualidade

| Risco                     | Atributos afetados               | Mitigação                           |
| ------------------------- | -------------------------------- | ----------------------------------- |
| R01 - Atraso              | Manutenibilidade, Confiabilidade | Priorizar funcionalidades do MVP    |
| R02 - Aumento do escopo   | Manutenibilidade                 | Manter o escopo definido            |
| R03 - Problemas com Asaas | Confiabilidade, Segurança        | Testar a integração no sandbox      |
| R04 - Falhas no CI        | Confiabilidade, Manutenibilidade | Automatizar verificações            |
| R05 - Baixa cobertura     | Confiabilidade, Manutenibilidade | Criar testes para regras principais |
| R06 - Comunicação         | Manutenibilidade                 | Utilizar Issues e PRs               |

## 4. Avaliação futura

A avaliação da qualidade será realizada nas próximas etapas do projeto por meio de testes, inspeções e métricas.

Entre as evidências previstas estão:

* cobertura de testes automatizados;
* resultados dos testes;
* revisão de código;
* execução dos checks do CI;
* identificação de falhas;
* feedback sobre utilização das telas.

As métricas já definidas em `docs/metricas/` serão utilizadas como apoio à avaliação.

## 5. Evidência da Entrega 8 (Sprint 4 — final)

Foi executado em 11/11/2026:

```bash
cd back-end && ./gradlew test jacocoTestReport --no-daemon
cd ServiceHub && npm test && npm run test:coverage && npm run lint && npm run build
cd ServiceHub && npm run test:e2e
```

Resultado: **129 testes backend aprovados**, sem falhas, erros ou testes ignorados, com **93,37% de
cobertura de linhas**, **80,80% de branches**, **87,22% de métodos** e **100% de classes**. No
frontend, **29 testes aprovados** (3 arquivos) e cobertura de **25,22% de linhas**; `tsc -b`, `vite
build` e `eslint` sem erros; **3 cenários E2E aprovados**.

Foram acrescentados nesta entrega:

- `back-end/src/test/java/com/es/servicehub/service/UsuarioServiceRegrasAdicionaisTest.java` — 31 casos
  sobre cadastro (documentos, senha, perfil, especialidades, duplicidade), `atualizarPerfil`,
  `listarDisponiveis` e aceite de proposta;
- `back-end/src/test/java/com/es/servicehub/SolicitacaoPropostaIntegrationTest.java` — 11 casos de
  integração HTTP autenticados por sessão real (login + CSRF), cobrindo proposta, aceite, conclusão,
  autorização por proprietário, isolamento das listagens e cadastro inválido;
- `ServiceHub/tests/InputSenha.test.tsx` — 5 casos do componente `InputSenha`;
- cobertura de frontend configurada em `ServiceHub/vitest.config.ts` e script `npm run test:coverage`.

O detalhamento dos testes, comandos e resultados está em `docs/PLANO-DE-TESTES.md`.