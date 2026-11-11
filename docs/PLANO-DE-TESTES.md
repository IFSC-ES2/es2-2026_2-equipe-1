# Plano de testes da Sprint 4 (Entrega 8)

**Projeto:** ServiceHub  
**Papel responsável:** Ana Clara Teixeira Ronzani (Scrum Master & Engenheira de Qualidade)  
**Versão de referência:** `v0.4.0` (branch `entrega-8`)  
**Data da execução registrada:** 11/11/2026

Este documento complementa `docs/qualidade.md`, `docs/METRICAS.md` e `docs/COBERTURA-JACOCO.md`.
Ele registra **o que já existia**, **o que faltava** e **o que foi acrescentado** na suíte automatizada
da Sprint 4, com os comandos e os resultados observados.

---

## 1. Situação encontrada (antes dos testes faltantes)

A suíte da Entrega 7 estava boa em volume, mas com lacunas de cobertura nos fluxos que a Sprint 4
colocou em produção no staging. Verificado por leitura do código e por execução local:

| Lacuna identificada | Evidência | Risco |
|:---|:---|:---|
| Proposta, aceite e conclusão não eram exercitados pelo caminho HTTP real (login por sessão + cookie CSRF) | `SecurityIntegrationTest` usa o pós-processador `user(...)` do Spring Security Test, que injeta o usuário sem passar por `POST /api/usuarios/login` | Regressões em sessão/CSRF só apareceriam no E2E |
| `UsuarioService` sem testes para CPF/CNPJ inválidos, perfil `ADMIN`, documento duplicado, senha curta e especialidade obrigatória | `UsuarioServiceTest`/`UsuarioServiceAdditionalTest` cobriam apenas parte das validações | Regras de cadastro público sem proteção contra regressão |
| `atualizarPerfil` (criação/reaproveitamento de endereço, especialidades de cliente, autorização por dono) sem testes | nenhum teste chamava `atualizarPerfil` | Fluxo corrigido na Sprint 4 sem rede de segurança |
| `listarDisponiveis` para cliente, prestador sem especialidade e prestador inexistente sem testes | apenas caminho feliz testado | Filtro por especialidade poderia expor dados indevidos |
| `InputSenha` (componente criado na Sprint 4) sem teste | `ServiceHub/src/componentes/InputSenha/index.tsx` sem arquivo em `tests/` | Alternância mostrar/ocultar senha sem verificação |
| Cobertura do frontend não era medida | `vitest.config.ts` sem bloco `coverage` | Métrica M-01 existia apenas para o backend |
| `npm run lint` falhava no CI | `InputSenha/index.tsx`: `@typescript-eslint/no-empty-object-type` | O job de frontend do pipeline não podia servir de critério de merge |
| E2E não era executado pelo pipeline | `ci.yml` não tinha job de Playwright | O fluxo principal só era validado manualmente |
| `.env.example` citado no README e no `DEPLOY.md` não existia | ausência no repositório | Configuração de ambiente dependia de conhecimento informal |

---

## 2. Testes acrescentados

### 2.1 Back-end — `UsuarioServiceRegrasAdicionaisTest` (31 testes)

Arquivo: `back-end/src/test/java/com/es/servicehub/service/UsuarioServiceRegrasAdicionaisTest.java`

| Grupo | Casos cobertos |
|:---|:---|
| Cadastro — campos obrigatórios | nome em branco, e-mail nulo, senha em branco, perfil nulo |
| Cadastro — perfil | recusa de `tipoPerfil = ADMIN` no cadastro público |
| Cadastro — documentos | cliente com CNPJ, cliente com CPF inválido, prestador sem documento, prestador com CPF e CNPJ, CPF inválido, CNPJ inválido |
| Cadastro — senha e especialidades | senha com 9 caracteres, prestador sem especialidade |
| Cadastro — duplicidade | e-mail já cadastrado não chama `save` |
| Cadastro — sucesso | prestador com CNPJ formatado persiste somente dígitos, senha em hash BCrypt e especialidades; cliente sem telefone persiste nulo |
| `salvar` | duplicidade apenas na criação; usuário com `id` não verifica e-mail |
| `atualizarPerfil` | 403 para outro usuário, 400 sem dados, 400 especialidades em cliente, criação de endereço quando não existe, reaproveitamento do endereço existente, preservação de telefone não enviado, 400 para UF inválida |
| `listarDisponiveis` | prestador sem especialidade retorna vazio sem consultar o repositório, cliente recebe 403, prestador inexistente recebe 404 |
| Aceite | aceite escolhe a proposta do prestador indicado e mantém as demais propostas |
| Exclusão | `deletar` de usuário inexistente |

### 2.2 Back-end — `SolicitacaoPropostaIntegrationTest` (11 testes)

Arquivo: `back-end/src/test/java/com/es/servicehub/SolicitacaoPropostaIntegrationTest.java`

Autentica pelo endpoint real (`POST /api/usuarios/login`), reutiliza a sessão HTTP
(`JSESSIONID`) e o token CSRF do Spring Security — o mesmo caminho usado pelo front-end.

| Caso | Resultado esperado |
|:---|:---|
| Fluxo completo: proposta → aceite → conclusão | `SOLICITADO` → `EM_ANDAMENTO` (valor 140,00 e prestador associados) → `CONCLUIDO` |
| Cliente não proprietário aceitar, concluir ou ler a solicitação | 403 nas três operações e status inalterado |
| Proposta com valor `0` e valor negativo | 400 e nenhuma proposta persistida |
| Proposta em solicitação já aceita | 409 |
| Reenvio de proposta pelo mesmo prestador | 1 registro, valor atualizado para 95,00 |
| Cliente enviando proposta e prestador aceitando | 403 (perfis invertidos) |
| `GET /api/solicitacoes/minhas` e `/disponiveis` | isolamento por usuário e filtro por especialidade |
| Cadastro HTTP inválido (senha curta) e cadastro de `ADMIN` | 400 com mensagem e sem usuário criado |
| Requisição mutável sem CSRF com sessão válida | 403 |
| Endereço cadastrado aparece em `GET /api/enderecos` | 201 na criação e presença na listagem |

Apoio: `back-end/src/test/java/com/es/servicehub/repository/PropostaServicoRepository.java`
(repositório de teste usado apenas para verificar a persistência das propostas).

### 2.3 Front-end — `InputSenha.test.tsx` (5 testes)

Arquivo: `ServiceHub/tests/InputSenha.test.tsx`

- inicia mascarado, com botão “Exibir senha”;
- alterna para texto e volta para senha, com rótulos de acessibilidade corretos;
- o botão tem `type="button"` e não dispara submit do formulário;
- repassa `placeholder`, `disabled` e o evento `onChange` ao `input`;
- campo desabilitado continua desabilitado.

### 2.4 Correção necessária para o pipeline passar

`ServiceHub/src/componentes/InputSenha/index.tsx`: a interface vazia foi trocada por um alias de tipo
(`type InputSenhaProps = React.InputHTMLAttributes<HTMLInputElement>`), eliminando o erro de lint que
impedia `npm run lint` de passar.

### 2.5 Cobertura do frontend

`ServiceHub/vitest.config.ts` passou a configurar cobertura (`v8`, relatórios `text`, `html` e
`json-summary` em `ServiceHub/coverage/`) e o script `npm run test:coverage` foi adicionado ao
`package.json`. A dependência `@vitest/coverage-v8` foi adicionada às `devDependencies`.

---

## 3. Como executar

```bash
# Back-end (Java 25)
cd back-end
./gradlew test jacocoTestReport --no-daemon

# Front-end — testes unitários e de componente (Node 24)
cd ServiceHub
npm ci
npm test

# Front-end — cobertura
npm run test:coverage

# Front-end — build e lint
npm run build
npm run lint

# E2E (requer backend em http://localhost:8081 e Chromium do Playwright)
npx playwright install chromium
npm run test:e2e
```

No CI, `.github/workflows/ci.yml` executa: verificação de arquivos obrigatórios, testes do backend com
JaCoCo, build + testes com cobertura + lint do frontend, **job E2E com Playwright** (novo) e validação
do Docker Compose + varredura de segredos versionados.

---

## 4. Resultados observados (11/11/2026)

| Suíte | Antes (Entrega 7) | Depois (Entrega 8) |
|:---|---:|---:|
| Backend — testes executados | 87 | **129** |
| Backend — falhas / erros / ignorados | 0 / 0 / 0 | **0 / 0 / 0** |
| Backend — cobertura de linhas | 77,35% | **93,37%** |
| Backend — cobertura de instruções | 76,48% | **93,03%** |
| Backend — cobertura de branches | 63,89% | **80,80%** |
| Backend — cobertura de métodos | 67,26% | **87,22%** |
| Frontend — testes | 24 | **29** |
| Frontend — cobertura de linhas | não medida | **25,22%** |
| E2E Playwright | 3 cenários (manual) | **3 cenários aprovados** |

Comandos usados para os números do backend:

```bash
cd back-end && ./gradlew test jacocoTestReport --no-daemon
# relatório: back-end/build/reports/jacoco/test/html/index.html
```

O aumento da cobertura de backend vem dos dois novos arquivos de teste (31 + 11 casos) e não da
alteração de testes existentes. A cobertura do frontend é baixa porque as páginas
(`DashboardCliente`, `DashboardPrestador`, `Cadastro`, `Home`) só são exercitadas pelo E2E; ela é
registrada como limitação conhecida e oportunidade de melhoria em `docs/METRICAS.md`.

---

## 5. Lacunas que permanecem (registradas como limitação)

1. `VITE_API_URL` é lida com `import.meta.env.VITE_API_URL` em `ServiceHub/src/servicos/api.ts`;
   a variável precisa estar disponível no momento do build do frontend (Vite embute o valor).
2. As páginas do frontend não têm testes de componente; a garantia vem do E2E.
3. Não há teste de concorrência real para `findByIdForUpdate` (o bloqueio é verificado apenas por
   comportamento sequencial).
4. Integração Asaas não possui testes porque está fora do escopo desta entrega.
