# Planejamento e encerramento — Sprint 4 (Entrega 8)

**Projeto:** ServiceHub  
**Data da reunião de planejamento:** 05/11/2026  
**Data de encerramento:** 11/11/2026  
**Participantes:** Ana Clara Teixeira Ronzani, Davi Israel Quirino, João Vitor Schmitt, Leonardo Victor Muller De Morais

---

## 1. Meta da Sprint 4

Colocar o MVP do ServiceHub em um ambiente de validação online acessível à equipe, executar o fluxo
principal de ponta a ponta nesse ambiente e estabilizar a suíte automatizada, de modo que a entrega
final possa ser demonstrada e verificada sem depender de execução manual em máquina local.

A Sprint 4 concentra o fechamento do escopo revisado: ambiente de staging, execução com Docker,
testes E2E com Playwright, correções no fluxo de edição de perfil e no hardening de sessão, CORS e
CSRF, e consolidação das evidências de qualidade.

## 2. Itens do backlog selecionados para a sprint

| ID | Item do Backlog | Prioridade | Estimativa |
|:---|:---|:---:|:---:|
| #13 | Infraestrutura, Docker e ambiente de staging | Alta | 16h |
| #14 | Documentação viva, ADRs, métricas, arquitetura e riscos | Alta | 8h |
| #25 | Estabilização da suíte de testes e evidências de cobertura | Alta | 8h |
| #26 | Correções de perfil, sessão, CORS e CSRF identificadas na validação | Média | 8h |

**Capacidade planejada para a sprint:** 48h úteis (conforme `docs/BASELINE.md`).  
**Esforço total selecionado:** 40h, deixando margem para revisão, testes e ajustes finais.

O item #10 (pagamento Asaas, 24h) foi **formalmente retirado do escopo desta entrega** por depender de
integração externa, tratamento de webhooks e homologação em sandbox. A decisão está registrada em
`docs/METRICAS.md` (M-03) e no `README.md`.

## 3. Justificativa da escolha

- O fluxo funcional do MVP (cadastro, login, publicação de solicitação, proposta, aceite e conclusão)
  já estava implementado ao final da Sprint 3. O maior risco restante era **operacional**: a aplicação
  só havia sido validada em execução local.
- O staging permite validar configuração de CORS, `allowedHosts`, cookies de sessão e CSRF em um
  domínio real, cenário em que os testes locais não reproduzem o comportamento do navegador.
- Os testes E2E com Playwright cobrem o caminho que une frontend, API e autenticação, reduzindo o risco
  de regressão entre camadas.
- A revisão da suíte automatizada identificou lacunas de cobertura em regras de cadastro,
  `atualizarPerfil`, listagem por especialidade e no caminho HTTP real de proposta/aceite. Fechar essas
  lacunas era necessário para sustentar a afirmação de "MVP concluído" com evidência.
- A correção do fluxo de edição de perfil e do hardening de segurança era pré-requisito para expor a
  aplicação publicamente.

## 4. Decisões tomadas e ajustes de escopo

- **Escopo revisado:** o MVP é considerado concluído em 100% do escopo revisado, sem o pagamento
  Asaas. Os 100% não significam que as 88h do escopo original foram entregues.
- **Ambiente de staging:** frontend e API publicados atrás de proxy reverso com HTTPS; o banco pode ser
  limpo periodicamente, então dados de demonstração são sintéticos e recriáveis.
- **Execução local:** `docker-compose.yml` com backend em `8081` e frontend em `5173`, usando H2 em
  memória. O Compose não reproduz o PostgreSQL de staging; a diferença está registrada em
  `docs/DEPLOY.md`.
- **Configuração de ambiente:** o `.env.example` passa a ser versionado, com valores de exemplo e
  campos sensíveis comentados. O `.env` real permanece fora do controle de versão.
- **Hardening:** sessão com cookie `HttpOnly`, CSRF habilitado para requisições mutáveis, CORS
  restrito a `http://localhost:5173` e ao domínio do frontend publicado, e bloqueio de cadastro de
  `ADMIN` pela rota pública.
- **Divisão de tarefas entre os integrantes:**
  - **João Vitor Schmitt (DevOps/Infra):** disponibilização do ambiente de staging, criação do
    `docker-compose.yml`, Dockerfiles, suporte a variáveis de ambiente (`.env.example`), CORS,
    `allowedHosts` e header `XSRF-TOKEN`.
  - **Davi Israel Quirino (Front-end):** correção do fluxo de edição de perfil, ajustes de interface
    dos dashboards e integração dos modais ao ambiente publicado.
  - **Leonardo Victor Muller De Morais (Arquiteto de Software):** correções de autorização e sessão,
    revisão do modelo de dados e atualização do `docs/ARQUITETURA.md` e das ADRs.
  - **Ana Clara Teixeira Ronzani (Scrum Master & Engenheira de Qualidade):** execução e ampliação da
    suíte de testes, testes E2E, evidências de cobertura (`docs/COBERTURA-JACOCO.md`,
    `docs/PLANO-DE-TESTES.md`) e atualização de `docs/METRICAS.md` e `docs/qualidade.md`.

## 5. Definição de pronto para a sprint

Um item é considerado concluído quando:

- o código está integrado à `main` via Pull Request revisado e aprovado;
- os checks obrigatórios do CI passaram, incluindo o job de E2E;
- há testes automatizados cobrindo as regras principais do fluxo de contratação;
- a aplicação é demonstrável no ambiente de staging, com o fluxo principal executado de ponta a ponta;
- o `.env.example` versionado permite reproduzir a execução local;
- as evidências de cobertura e os resultados da suíte estão registrados na documentação;
- métricas, riscos, qualidade, contribuições e uso de IA foram revisados;
- a release/tag `v0.4.0` e as evidências do fluxo Git estão confirmadas no GitHub antes da entrega.

## 6. Encerramento da Sprint 4

| Item | Situação | Evidência |
|:---|:---:|:---|
| #13 Infraestrutura, Docker e staging | Concluído no escopo revisado | `docker-compose.yml`, Dockerfiles, `docs/DEPLOY.md` |
| #14 Documentação viva | Concluído | `docs/ARQUITETURA.md`, `docs/METRICAS.md`, `docs/RISCOS.md`, `docs/qualidade.md` |
| #25 Estabilização da suíte e cobertura | Concluído | 129 testes backend, 29 frontend, 3 E2E; `docs/PLANO-DE-TESTES.md` |
| #26 Correções de perfil, sessão, CORS e CSRF | Concluído | `SecurityConfig`, `WebConfig`, testes de integração |
| #10 Pagamento Asaas | Fora do escopo | Registrado em `docs/METRICAS.md` (M-03) |

**Resultados da suíte na entrega:**

- Backend: **129 testes**, 0 falhas, 0 erros, 0 ignorados; **93,37%** de linhas e **80,80%** de branches.
- Frontend: **29 testes**, 0 falhas; cobertura de **25,22%** de linhas (medida pela primeira vez).
- E2E: **3 cenários** aprovados (login inválido, edição de perfil, publicação de solicitação + proposta).
- Pipeline: verificação de arquivos obrigatórios, testes com cobertura, lint, E2E e validação do Compose.

**Pendências registradas para a próxima etapa:** integração de pagamento (Asaas), migração para
PostgreSQL, pipeline de CD e testes de componente das páginas do frontend.
