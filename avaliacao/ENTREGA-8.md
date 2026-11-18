# Avaliação da Entrega 8 - Sprint 4

## Identificação

- Equipe: es2-2026_2-equipe-1
- Projeto: ServiceHub — plataforma de contratação de serviços autônomos
- Entrega: 8 - Sprint 4
- Repositório confirmado pela configuração: `IFSC-ES2/es2-2026_2-equipe-1`
- Pull request avaliado: [#32](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/pull/32), `entrega-8` → `main`.
- Versão avaliada: `e629067`, merge do PR #32 em 12/11/2026 às 00h04 (UTC−3).
- Prazo: 11/11/2026.
- Release esperada: `v0.4.0`

## Documentos Consultados

- `README.md` e `USO-IA.md`.
- `.github/PULL_REQUEST_TEMPLATE.md`, `.github/ISSUE_TEMPLATE/task_template.md` e `.github/workflows/ci.yml`.
- `docs/entregas/sprint-4-planejamento.md`.
- `docs/DEPLOY.md`, `docs/PLANO-DE-TESTES.md`, `docs/COBERTURA-JACOCO.md`, `docs/METRICAS.md`, `docs/riscos.md` e `docs/qualidade.md`.
- `docs/contribuições/CONTRIBUICOES-SPRINT-4.md`.
- `docs/ARQUITETURA.md`, `docs/PADROES-DE-PROJETO.md`, `docs/REENGENHARIA.md`, diagramas e ADRs `ADR-0001` a `ADR-0005`.
- `docker-compose.yml`, `back-end/Dockerfile`, `ServiceHub/Dockerfile`, configurações de segurança, testes e demais arquivos de implementação consultados.
- PR #32, issues #13, #14 e #30, Project 32, checks do GitHub Actions e [release `v0.4.0`](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/releases/tag/v0.4.0).

## 1. Descrição — Resumo da Entrega

O MVP declarado no `README.md` inclui cadastro/login de cliente e prestador, publicação de solicitações, disponibilização para profissionais, aceite ou recusa e acompanhamento do status. A equipe retirou o pagamento Asaas do escopo revisado (`docs/entregas/sprint-4-planejamento.md:32–34`).

Há um frontend e uma API acessíveis publicamente, Dockerfiles, Compose, testes backend/frontend, cenários Playwright, documentação de deploy e a release `v0.4.0`. O fluxo funcional de cadastro, login, solicitação, proposta e aceite está implementado.

Porém, a entrega não demonstra de modo suficiente um deploy reprodutível do staging: o processo de atualização do ambiente publicado não está documentado nem versionado, o Compose usa H2 em memória, falta o `.env.example` anunciado e a documentação contém divergências sobre autenticação, testes e configuração. A integração ocorreu às 00h04 de 12/11, tolerada como dentro do prazo de 11/11; a release foi publicada às 13h16 de 12/11, cerca de 13 horas após o prazo.

## 2. Itens Obrigatórios da Entrega

### 2.1 Planejamento e incremento funcional da Sprint 4

- **Atendido:** planejamento da Sprint 4 registrado em `docs/entregas/sprint-4-planejamento.md`, com revisão de escopo (retirada do Asaas). Fluxo funcional de cadastro, login, solicitação, proposta e aceite implementado e integrado.
- **Parcialmente atendido:** o fluxo de negociação evoluiu em relação ao texto inicial, mas não há evidência de notificação ativa nem de recusa registrada pelo prestador. A documentação anuncia 100% do escopo revisado sem matriz final que diferencie itens entregues, substituídos e pendentes. As referências #25 e #26 no planejamento são PRs antigos, não os itens descritos.

### 2.2 Ambiente de staging ou equivalente acessível

- **Atendido:** consultas GET realizadas em 14/11/2026 encontraram o frontend em `https://frontservicehub.jvs.dev.br` com HTTP 200; a API respondeu, inclusive ao endpoint público de configuração CSRF. `docs/DEPLOY.md` registra URLs e contas sintéticas de teste.
- **Parcialmente atendido:** a acessibilidade básica não comprova o fluxo autenticado completo nem a correspondência do ambiente com a tag. A persistência do ambiente publicado não é reproduzível pelo repositório (A1).

#### A1 — Staging acessível, mas ambiente não reproduzível

- **Categoria:** qualidade de implementação e infraestrutura.
- **Relevância:** alta.
- **Evidência:** `docker-compose.yml:3–24` contém frontend/backend, sem PostgreSQL ou volume; `application.properties:9–17` usa `jdbc:h2:mem:testdb`; `docs/DEPLOY.md:15,74–85` reconhece a diferença. O Dockerfile frontend executa Vite em modo desenvolvimento (`ServiceHub/Dockerfile:12`), enquanto o CI apenas constrói o frontend.
- **Impacto:** reiniciar o backend pode perder dados e o clone não recria a topologia do staging. O staging respondeu HTTP 200 no frontend, mas isso não comprova persistência, login ou correspondência com a tag.
- **Recomendação:** alinhar Compose e staging ou declarar formalmente o ambiente suportado, com banco, volumes, proxy, segredos, procedimento de atualização e rollback.

### 2.3 Deploy automatizado ou reprodutível

- **Atendido:** existem `docker-compose.yml`, Dockerfiles e instruções de inicialização em `docs/DEPLOY.md`, como infraestrutura declarativa inicial.
- **Parcialmente atendido:** há execução local por Compose, mas o processo que leva a versão ao staging não é reproduzível a partir do repositório: o uso de Coolify é declarado na release, sem configuração, passos ou verificação pós-deploy documentados. A topologia descrita (proxy/PostgreSQL) não é recriada pelo Compose (A1).
- **Observação:** CD remoto não é exigido nesta entrega; o enunciado aceita deploy automatizado **ou** reprodutível. 

#### A2 — Processo de deploy do staging não é reproduzível a partir do repositório

- **Categoria:** aderência aos requisitos da entrega e documentação de implantação.
- **Relevância:** média.
- **Evidência:** a release declara uso de Coolify, mas o repositório não contém sua configuração nem descreve os passos para publicar uma versão no staging; `docs/DEPLOY.md` cobre a execução local, que difere do ambiente publicado (`:15`). Não há registro da versão implantada nem de verificação pós-deploy; a API de deployments retornou lista vazia.
- **Impacto:** outra pessoa não consegue reproduzir a implantação do staging, e não é possível confirmar que o ambiente publicado corresponde à versão entregue. CD não era exigido; a lacuna é de reprodutibilidade e rastreabilidade do processo.
- **Recomendação:** documentar em `docs/DEPLOY.md` o procedimento de publicação no staging (configuração do Coolify, variáveis, banco, comandos ou gatilhos), registrar o commit implantado e um roteiro de verificação pós-deploy.

### 2.4 Manutenção e atualização da integração contínua

- **Atendido:** o run [43144343063](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/actions/runs/43144343063) passou para backend, frontend, lint, build e validação sintática do Compose. Os testes estão versionados, o workflow executa em PR e falhas propagam para os jobs correspondentes.
- **Parcialmente atendido:** o CI não executa Playwright/E2E, varredura de segredos ou deploy, apesar de essas atividades serem declaradas em `docs/DEPLOY.md`, `docs/PLANO-DE-TESTES.md` e no planejamento.
- **Observação:** a ausência de checks obrigatórios nos rulesets é descontada somente no critério de fluxo (2.8), evitando duplicação.

#### A3 — CI e resultados documentados não correspondem

- **Categoria:** testes e verificação.
- **Relevância:** média.
- **Evidência:** `docs/PLANO-DE-TESTES.md:124–126` e `docs/DEPLOY.md:165–166` afirmam E2E no pipeline; o workflow não contém Playwright. O CI executa `npm test -- --run` (`ci.yml:77–79`), não cobertura frontend nem varredura de segredos. `docs/COBERTURA-JACOCO.md` cita `UsuarioSessionIntegrationTest`, enquanto o código entregue usa JWT, e apresenta percentuais diferentes do relatório do run, sem vínculo confiável ao commit final.
- **Impacto:** a aprovação do run é válida para os jobs executados, mas não sustenta as verificações declaradas para E2E, CD e cobertura documentada.
- **Recomendação:** separar resultados locais, CI e staging; registrar run/commit/artefato de cada medição e não declarar como executado o que não está no workflow.

### 2.5 Documentação de deploy

- **Atendido:** `docs/DEPLOY.md` registra URLs, pré-requisitos, comandos, limitações, contas sintéticas e checklist de validação.
- **Parcialmente atendido:** o guia contradiz a implementação em autenticação e configuração, e o README não explica o pré-requisito `JWT_SECRET` para `bootRun`.

#### A4 — A documentação central da implantação contradiz a implementação

- **Categoria:** documentação, arquitetura e implantação.
- **Relevância:** alta.
- **Evidência:** `docs/entregas/sprint-4-planejamento.md:60–64` promete `.env.example`, sessão/CSRF e CORS restrito; o arquivo de ambiente não existe, `SecurityConfig.java` usa JWT stateless, desabilita CSRF e o frontend envia `Authorization: Bearer` com token em `localStorage` (`ServiceHub/src/servicos/api.ts`). `docs/DEPLOY.md:136–148` simultaneamente afirma que JWT não é consumido e que `.env.example` está versionado, depois afirma que ele não foi localizado.
- **Impacto:** um clone limpo não reproduz o procedimento documentado e as decisões de segurança não são confiáveis para manutenção ou validação.
- **Recomendação:** atualizar ADR-0003, arquitetura, riscos, planejamento e DEPLOY para o mecanismo efetivo; versionar um exemplo sem segredos ou documentar explicitamente como gerar/injetar os valores obrigatórios.

### 2.6 Atualização das métricas e riscos do projeto

- **Atendido:** `docs/METRICAS.md`, `docs/riscos.md`, `docs/qualidade.md` e `docs/COBERTURA-JACOCO.md` foram atualizados na janela da entrega, e há artefato JaCoCo no CI.
- **Parcialmente atendido:** os números de cobertura não estão vinculados de forma confiável ao commit final (A3); riscos, ADRs e arquitetura não foram reconciliados com a migração para JWT (A4). `USO-IA.md` não registra a Sprint 4 (A5).

#### A5 — Uso de IA da Sprint 4 não rastreável

- **Categoria:** uso de IA.
- **Relevância:** alta.
- **Evidência:** `USO-IA.md:129–190` termina na Entrega 7; não registra uso, ferramenta, artefato e validação da Sprint 4. Os registros anteriores citam artefatos existentes, mas vários mantêm “necessário revisão” ou validação futura.
- **Impacto:** não há registro mínimo exigido pelo protocolo de uso de IA (ferramenta, finalidade, artefato, aproveitamento e validação) para nenhum artefato da Sprint 4, o que impede identificar o papel da IA em Docker/Compose, JWT, testes e documentação novos. O protocolo prevê impacto na avaliação por falta de rastreabilidade; a penalização incide no critério documental e na contribuição de Ana, a quem o registro de contribuições atribui a atualização de `USO-IA.md`. Não há evidência forte suficiente para concluir uso oculto, plágio ou falsificação apenas pelo estilo.
- **Recomendação:** registrar a Sprint 4 com ferramenta, finalidade, artefatos, aproveitamento e validação humana efetivamente realizada, incluindo o responsável pela revisão.

### 2.7 Qualidade, segurança e configuração do ambiente

- **Atendido:** o Compose exige `JWT_SECRET` como variável de ambiente, em vez de fixar o segredo na configuração.
- **Parcialmente atendido:** `docs/DEPLOY.md:34–40` e o README anunciam `.env.example`, mas o arquivo não está no checkout. A variável vazia `VITE_API_URL` pode prevalecer sobre o fallback da API. A correspondência entre ambiente implantado e versão entregue não é verificável. Rotas genéricas mantêm riscos de autorização (A6).

#### A6 — Rotas genéricas deixam riscos de autorização e logout

- **Categoria:** qualidade técnica observável.
- **Severidade:** alta; cenário identificado por análise estática, sem exploração destrutiva ou mutante.
- **Evidência:** operações genéricas de solicitação, usuário e endereço sem validação equivalente às rotas especializadas (`SolicitacaoServicoController.java:116–122`, `UsuarioController.java:104–123`, `EnderecoController.java:25–27`). O logout limpa o contexto (`UsuarioController.java:92–95`), mas o JWT dura uma hora (`JwtTokenService.java:17,30`) e não é revogado.
- **Impacto:** há risco de acesso ou alteração indevida por usuário autenticado e a documentação de logout não corresponde ao comportamento stateless.
- **Recomendação:** revisar a matriz de autorização, remover endpoints alternativos permissivos, isolar dados pessoais e definir/testar a semântica de logout e expiração.

### 2.8 Fluxo de trabalho evidenciado no repositório

- **Atendido:** o PR #32 veio de `entrega-8`, recebeu aprovação de Davi no HEAD final e foi integrado por merge commit. A API confirmou dois rulesets ativos aplicáveis à `main`, exigindo PR e uma aprovação.
- **Parcialmente atendido:** os rulesets não contêm checks obrigatórios; a revisão foi sucinta; a integração ocorreu no limite do prazo, com baixa incrementalidade; o board não acompanhou os itens centrais.

#### A7 — Rastreabilidade de processo insuficiente e integração concentrada no fechamento

- **Categoria:** processo GitHub.
- **Relevância:** alta no processo.
- **Evidência:** PR #32 foi aberto às 23h45 de 11/11 e integrado às 00h04 de 12/11 (UTC−3), 19 minutos depois, o que foi tolerado como dentro do prazo; 36 de 42 commits de desenvolvimento da janela ocorreram em 11/11. O Project 32 não contém #13, #14 ou #30, e a issue #7 permanece em estado incorreto. Os rulesets exigem PR/aprovação, mas não checks obrigatórios; o PR teve aprovação formal de Davi, sem discussão técnica detalhada rastreável. Há ainda o commit `d90ecb3` com autor/committer inválidos, não atribuído a integrante.
- **Impacto:** o fluxo formal foi seguido, mas a integração no limite do prazo, com revisão de minutos, e a baixa incrementalidade reduzem previsibilidade, revisão e confiança na entrega.
- **Recomendação:** trabalhar incrementalmente ao longo da sprint, atualizar o board e issues durante a sprint, exigir checks nas regras e corrigir autoria/configuração inválida antes da integração.

### 2.9 Release/tag do marco

- **Atendido:** a release `v0.4.0` existe e a API confirmou que aponta para `e629067`, commit de merge do PR #32.
- **Parcialmente atendido:** publicada às 13h16 de 12/11, cerca de 13 horas após o prazo; as notas da release estão desatualizadas em relação ao estado de configuração (por exemplo, `.env.example`) e declaram Coolify sem evidência versionada.

### 2.10 Registro de contribuição individual

- **Atendido:** `docs/contribuições/CONTRIBUICOES-SPRINT-4.md` registra atividades por integrante.
- **Parcialmente atendido:** o registro atribui a Ana a atualização de `USO-IA.md`, que não mudou na janela, e não documenta adequadamente a participação de Davi nas métricas. A issue #30 é o único vínculo explícito entre tarefa e commit (`3c540be`).

#### Análise das Contribuições Individuais

Notas de contribuição são distintas da nota coletiva. Foram consideradas relevância, oportunidade, periodicidade, responsabilidades, integração, revisões e comunicação rastreável; não são calculadas por contagem bruta de commits.

- **Ana Clara Teixeira Ronzani:** seis commits, todos em 11/11, incluindo testes backend/frontend, correções de lint/configuração e documentação de cobertura, testes, riscos, métricas e deploy. O trabalho é relevante e coerente com QA, mas foi inteiramente concentrado no último dia da janela; o documento atribui a ela atualização de `USO-IA.md`, que não mudou na janela, deixando sem rastreabilidade o uso de IA de toda a Sprint 4 (A5), e não há revisão técnica própria detalhada rastreável.

- **Davi Israel Quirino:** três commits (07/11 e dois em 11/11, cerca de 390 linhas): implementou `InputSenha`, a integração com a API dos Correios para consulta de CEP e preenchimento do endereço (`83f6169`), ajustes de perfil e listagens; a issue #30 vincula claramente a tarefa ao commit `3c540be`. Também aprovou e integrou o PR final. Há evidência de comunicação melhor que a dos demais, porém a implementação ficou concentrada no fechamento, a revisão foi sucinta e a documentação de métricas não registra adequadamente sua participação.

- **João Vitor Schmitt:** 13 commits concentrados em 11/11, relacionados a portas, Docker, Compose, CORS/XSRF, configuração e publicação da release `v0.4.0`. A atuação é central para infraestrutura, mas `.env.example` não existe, o procedimento de deploy do staging não está documentado e a release, publicada cerca de 13 horas após o prazo, tem notas desatualizadas. Não há review próprio ou comunicação rastreável de impedimentos.

- **Leonardo Victor Muller De Morais:** 19 commits distribuídos em 08, 09 e 11/11, incluindo Playwright, autorização/JWT, perfil, dashboards, rotas, testes, documentação e consolidação do PR #32. É a contribuição mais abrangente e integradora. É a distribuição temporal mais incremental da equipe dentro da janela. Ainda assim, há commits multitemáticos e não foram atualizados ADRs/arquitetura de forma coerente com a migração para JWT.

**Colaboração e limites:** o commit `d90ecb3`, com autor/committer inválidos, não foi atribuído a nenhum integrante. A concentração de atividade em 11/11 é fato rastreável para todos os integrantes; não foi presumido prejuízo específico a dependências além da redução do tempo de revisão e integração.

## 3. Critérios de Avaliação (10 pts)

### Nota da Entrega

| Critério                                                         |   Máximo |    Nota |
| ---------------------------------------------------------------- | -------: | ------: |
| Planejamento e incremento da Sprint 4                            |      1,0 |     0,7 |
| Staging ou equivalente acessível, validável e associado à versão |      2,0 |     1,2 |
| Deploy automatizado ou reprodutível e `docs/DEPLOY.md`           |      1,5 |     0,8 |
| CI mantido e atualizado, com testes e checks nos PRs             |      1,5 |     1,1 |
| Métricas, riscos, documentação, README e IA                      |      1,0 |     0,3 |
| Configuração e segurança                                         |      1,0 |     0,5 |
| Fluxo de trabalho, checks, merge e release `v0.4.0`              |      2,0 |     1,5 |
| **Total**                                                        | **10,0** | **6,1** |

### Justificativa da Nota

**Nota coletiva: 6,1/10,0.** A equipe entregou trabalho substancial: staging com acessibilidade básica, fluxo funcional relevante, infraestrutura conteinerizada, testes automatizados, CI verde, documentação de deploy e release válida. Esses elementos impedem uma nota baixa por ausência de produto ou de evidências.

Entretanto, o requisito central da Entrega 8 é operacional. O processo de deploy do staging não é reproduzível a partir do repositório (A2); o Compose não reproduz o ambiente descrito, omite configuração essencial e usa persistência efêmera (A1); o CI não executa as verificações anunciadas (A3); e a documentação técnica está contraditória com o código atual (A4). Rotas genéricas mantêm riscos de autorização (A6) e `USO-IA.md` não cobre a Sprint 4, deixando o uso de IA da sprint sem rastreabilidade, o que é penalizado conforme o protocolo de uso de IA (A5). A integração no limite do prazo, com baixa incrementalidade, a release publicada cerca de 13 horas após o prazo e a rastreabilidade insuficiente do board agravam o resultado (A7). 

A mesma insuficiência não foi descontada integralmente em vários blocos: a ausência de checks obrigatórios fica no critério de fluxo, e o sucesso dos testes é reconhecido no critério de CI.

### Notas Individuais

Escala 0–10, considerando conteúdo, relevância, aderência às responsabilidades, periodicidade/oportunidade, integração, revisão, comunicação e apoio aos colegas; não são proporcionais à contagem de commits.

| Integrante                       | Nota individual | Justificativa resumida                                                                                                                                                                                    |
| -------------------------------- | --------------: | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Ana Clara Teixeira Ronzani       |         **7,0** | Testes, lint, cobertura e documentação coerentes com QA; tudo no último dia da janela, sem a atualização de `USO-IA.md` que lhe foi atribuída (uso de IA da sprint não rastreável) nem revisão detalhada. |
| Davi Israel Quirino              |         **6,8** | Componentes de interface com vínculo issue/commit e aprovação do PR final; implementação concentrada no fechamento e revisão sucinta.                                                                     |
| João Vitor Schmitt               |         **7,2** | Infraestrutura Docker/Compose/CORS e release, centrais para a entrega; `.env.example` ausente, deploy do staging não documentado e release publicada após o prazo, com notas desatualizadas.              |
| Leonardo Victor Muller De Morais |         **8,4** | Contribuição mais abrangente, integradora e incremental (Playwright, JWT, autorização, dashboards, consolidação do PR); commits multitemáticos e sem reconciliar ADRs/arquitetura.                        |

### Notas Compostas por Integrante

A nota composta da Entrega 8 combina 30% da nota coletiva e 70% da contribuição individual, com peso 10 para a entrega, em continuidade ao registro das Entregas 5–7. A média parcial está registrada em `NOTAS.md`.

`nota composta = (nota coletiva × 0,30) + (nota individual × 0,70)`

| Integrante                       | Coletiva | Individual |                              Cálculo | Nota composta |
| -------------------------------- | -------: | ---------: | -----------------------------------: | ------------: |
| Ana Clara Teixeira Ronzani       |      6,1 |        7,0 | `(6,1 × 0,30) + (7,0 × 0,70) = 6,73` |       **6,7** |
| Davi Israel Quirino              |      6,1 |        6,8 | `(6,1 × 0,30) + (6,8 × 0,70) = 6,59` |       **6,6** |
| João Vitor Schmitt               |      6,1 |        7,2 | `(6,1 × 0,30) + (7,2 × 0,70) = 6,87` |       **6,9** |
| Leonardo Victor Muller De Morais |      6,1 |        8,4 | `(6,1 × 0,30) + (8,4 × 0,70) = 7,71` |       **7,7** |

## 4. Observações

### Observações sobre Uso de IA

O uso declarado não foi penalizado por si só; a falta de rastreabilidade, sim. A ausência de registro da Sprint 4 está descrita em A5.

**Conclusão explícita:** `USO-IA.md` é **parcialmente compatível** com os artefatos integrados, pois os registros anteriores correspondem materialmente a código, testes e documentação existentes. Contudo, há **divergência documental para a Entrega 8**: não existe registro específico da Sprint 4, várias validações permanecem pendentes e documentos declarados revisados ainda contradizem o código e o workflow. Não foi encontrada evidência forte suficiente para acusar uso de IA não declarado, plágio ou falsificação; a lacuna é de transparência e fechamento da validação, não uma conclusão de fraude.

### Recomendações para a Equipe

- Entregar uma configuração local realmente reproduzível, com `.env.example` seguro, `JWT_SECRET`, `VITE_API_URL`, banco e persistência claramente definidos.
- Documentar o procedimento de publicação no staging (Coolify, variáveis, banco), registrando o commit implantado e a verificação pós-deploy; automatizar quando viável.
- Atualizar ADRs, arquitetura, riscos, README e DEPLOY para JWT stateless, logout, CORS e URLs reais.
- Alinhar CI, plano de testes e métricas: E2E, cobertura e segredos só devem ser declarados quando houver execução identificável.
- Corrigir autorização das rotas genéricas, isolamento de dados e comportamento de logout; adicionar testes de acesso cruzado e expiração.
- Corrigir links quebrados e registrar a Entrega 8 em `USO-IA.md`, com validação humana concluída.
- Atualizar board/issues durante a sprint, exigir checks obrigatórios e publicar a release logo após a integração, dentro do prazo.
