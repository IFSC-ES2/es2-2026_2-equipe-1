# Avaliação da Entrega 6 - Sprint 2

## Identificação

- Equipe: es2-2026_2-equipe-1
- Projeto: ServiceHub
- Entrega: 6 - Sprint 2
- Repositório confirmado pela configuração: `IFSC-ES2/es2-2026_2-equipe-1`
- Pull request avaliado: PR #25, de `entrega-6` para `main`
- Versão considerada: commit `8969a44`, merge do PR #25 em 01/10/2026 às 23:58:10 (UTC-3)
- Release esperada: `v0.2.0`

## Documentos Consultados

Somente documentos e artefatos do repositório da equipe:

- `README.md`, `ServiceHub/README.md` e `USO-IA.md`.
- `.github/PULL_REQUEST_TEMPLATE.md`, `.github/ISSUE_TEMPLATE/task_template.md` e `.github/workflows/ci.yml`.
- `docs/inception.md`, `docs/dod.md`, `docs/BASELINE.md` e `docs/ESTIMATIVAS.md`.
- `docs/METRICAS.md` e `docs/metricas/M-01.md`, `M-02.md` e `M-03.md`.
- `docs/riscos.md`, `docs/qualidade.md`, `docs/ci.md` e `docs/fluxo-de-trabalho.md`.
- `docs/PADROES-DE-PROJETO.md` e `docs/CONTRIBUICOES-SPRINT-2.md`.
- `docs/entregas/sprint-1-planejamento.md` e `docs/entregas/sprint-2-planejamento.md`.
- `docs/adrs/ADR-0001-stack-tecnologica.md`, `ADR-0002-integracao-pagamentos-asaas.md`, `ADR-0003-autenticacao.md` e `ADR-0004-padroes-de-projeto.md`.
- `docs/DiagramaUML_ServiceHub.drawio.png`.
- Código, testes, arquivos de build e Docker em `back-end/`, `ServiceHub/` e `docker-compose.yml`.
- Issues #7, #9, #13, #14 e #24, PR #25, revisões, checks, histórico de commits, regras de ramificação e release `0.2.0` no GitHub.
- [Project 32 — equipe 1](https://github.com/orgs/IFSC-ES2/projects/32/views/2): campos, visualizações, sete itens (#1, #2, #4, #7, #8, #9 e #10), datas e eventos de acompanhamento consultados pela API.

## 1. Descrição — Resumo da Entrega

O MVP declarado no README abrange cadastro/login de clientes e prestadores, publicação de solicitações, notificação de profissionais disponíveis, aceite/recusa e acompanhamento do ciclo/status. Geolocalização em tempo real, chat, avaliações, cupons e aplicativo móvel estão fora do escopo. Há ainda pagamento Asaas na proposta e no backlog (#10), embora ele não apareça na lista resumida de itens dentro do MVP: convém reconciliar essas fontes.

A Sprint 2 selecionou **#7, cadastro e autenticação**, estimado em 16h, em uma capacidade de 48h. A inversão com a publicação de solicitações, adiantada na Sprint 1, foi documentada. Não se exige aqui conclusão integral do MVP, nem se contabiliza novamente como incremento a publicação já entregue na Sprint 1.

O incremento é real: cadastro de dois perfis, validações, hash BCrypt, login por sessão, consulta do usuário autenticado, CSRF e integração React/API Spring. Há Dockerfiles, evolução do CI e ampliação dos testes de usuário. Persistência efetiva em H2 em memória, autorização incompleta, aceite visual e ausência de testes HTTP limitam o resultado. A documentação contém análise específica e reconhece várias pendências, mas não foi fechada de modo consistente após a integração.

## 2. Itens Obrigatórios da Entrega

### 2.1 Planejamento da Sprint 2

- **Atendido:** Meta e seleção de uma funcionalidade central, com estimativa, capacidade e justificativa do replanejamento (`sprint-2-planejamento.md:9–27`). A issue #7 possui critérios de aceitação.
- **Parcialmente atendido:** Planejamento coerente com o MVP, mas registrado no fechamento, sem fundamentação explícita suficiente nas métricas e nos riscos anteriores e sem consolidação final dos estados dos itens.

#### A1 — Planejamento e fechamento da sprint

- **Categoria:** aderência aos requisitos / planejamento.
- **Evidência:** reunião datada de 01/10 em `sprint-2-planejamento.md:4`, documento registrado em `1ac585d` às 22h14; há implementação desde 29/09. As linhas 22–27 não explicitam uso das métricas/riscos na escolha. O arquivo não consolida concluídos, parciais e pendências ao final.
- **Impacto:** atendimento parcial do critério 1. Isso prova registro tardio, não inexistência de discussão anterior.
- **Recomendação:** versionar planejamento no início, explicitar os dados usados na decisão e fechar o planejado versus entregue.

### 2.2 Incremento funcional do MVP

- **Atendido:** Cadastro/login implementados e integrados: `UsuarioService.java:59–106`, `UsuarioController.java:57–96` e `ServiceHub/src/servicos/autenticacao.ts:108–205`.
- **Parcialmente atendido:** Incremento funcional, porém com autorização por recurso e validações públicas insuficientes.

#### A2 — Autenticação não garante autorização por recurso

- **Categoria:** implementação / segurança.
- **Evidência:** `SecurityConfig.java:78–79` restringe POSTs a clientes e exige apenas autenticação para outras operações da API. `SolicitacaoServicoController.java:37–48` e `SolicitacaoServicoService.java:38–54` atualizam/excluem por ID sem verificar proprietário. A entidade de solicitação não relaciona cliente e prestador. Endereços e solicitações são listados globalmente, embora o dashboard diga “Minhas solicitações”. O risco R08 reconhece a lacuna.
- **Impacto:** redução no critério de incremento: uma sessão autenticada com CSRF válido pode atingir alterações de recursos alheios.
- **Recomendação:** implementar propriedade, autorização por papel/recurso e testes negativos de isolamento entre usuários.

#### A3 — Fluxos visuais e status não equivalem ao ciclo do serviço

- **Categoria:** coerência do MVP / comportamento.
- **Evidência:** `DashboardPrestador/index.tsx:59–63` remove o item apenas do estado local e informa que o cliente foi notificado, sem chamada à API. Disponibilidade também é local (`:20,92–98`). `DashboardCliente/index.tsx:111` apresenta todo status diferente de `CONCLUIDO` como “Em andamento”. O backend permite substituição genérica do status sem validar transições.
- **Impacto:** aceite/notificação não são contabilizados como funcionalidades entregues. Como #9 foi replanejado e a pendência é reconhecida nas métricas, não se aplica desconto por exigir MVP integral; permanece a divergência entre mensagem da interface e efeito real.
- **Recomendação:** identificar simulações e integrar persistência, vínculo do prestador, transições e notificação antes de declarar o fluxo completo.

#### A4 — Validação e persistência diferem das garantias documentadas

- **Categoria:** implementação / arquitetura.
- **Evidência:** a UI valida formato de e-mail e telefone (`autenticacao.ts:123–129`), mas `UsuarioService.java:68–70,102` aceita e-mail apenas não vazio e telefone nulo. `application.properties:8–16` usa H2 em memória, sem PostgreSQL no Compose, ao contrário da decisão ADR-0001. H2 é reconhecido como limitação na release e nos riscos.
- **Impacto:** validação do critério de aceitação #7 incompleta e persistência ainda de demonstração.
- **Recomendação:** validar os contratos também na API e atualizar a decisão arquitetural para refletir o ambiente efetivo e a migração planejada.

### 2.3 Testes automatizados relacionados às funcionalidades implementadas

- **Atendido:** Testes unitários versionados, comandos de execução e execução backend bem-sucedida no CI.
- **Parcialmente atendido:** Testes relevantes, mas sem integração funcional de login, sessão, CSRF, autorização ou persistência; build/lint não substituem testes frontend.
- **Não atendido:** Testes de integração funcionais das interações novas de autenticação/segurança.

#### A5 — Testes reais, mas caminho de login efetivo não exercitado

- **Categoria:** testes e verificação.
- **Evidência:** 28 métodos de teste no backend: 19 de usuário, oito de solicitação e um `contextLoads`. A Sprint 2 acrescenta 11 testes de usuário. `UsuarioServiceTest.java:143–173` testa `UsuarioService.autenticar`, mas `UsuarioController.java:71–79` autentica pelo `AuthenticationManager`. O teste de contexto não realiza login HTTP nem valida persistência/segurança. Não há framework de testes frontend ou cobertura percentual configurada.
- **Impacto:** crédito pelos unitários e pelo CI executável; atendimento parcial em integração e representatividade.
- **Recomendação:** testar cadastro/login reais, cookies, sessão, logout, CSRF e acesso indevido entre usuários; automatizar conversores frontend e medição da cobertura.

### 2.4 Integração contínua em uso

- **Não atendido:** Obrigatoriedade de status checks nas regras aplicáveis de `main`.

Achado relacionado: A8 (item 2.5).

### 2.5 Fluxo de trabalho evidenciado no repositório

- **Atendido:** Branch `entrega-6`, PR e integração por merge commit; quatro checks verdes antes do merge.
- **Parcialmente atendido:** Fluxo com branch, PR, merge e CI, mas sem aprovação formal e sem checks obrigatórios nas regras consultadas.
- **Não atendido:** Aprovação de outro integrante no PR #25: API de reviews retornou lista vazia.

#### A8 — CI passou; aprovação formal e checks bloqueantes não comprovados

- **Categoria:** processo GitHub.
- **Evidência:** [execução 36957860974](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/actions/runs/36957860974), evento `pull_request`, com quatro jobs `success`: arquivos básicos, backend tests, frontend build/lint e validação de configuração. A API `pulls/25/reviews` retornou `[]`, apesar do checklist afirmar revisão/aprovação. O corpo diz “Nenhuma issue informada”, embora a timeline vincule #7 ao PR.
- **Proteção verificada por API/CLI:** a proteção clássica retorna 404, mas os rulesets ativos **20533295 e 20533407** e `rules/branches/main` confirmam PR e uma aprovação exigida, proteção de exclusão e non-fast-forward. As regras retornadas não incluem `required_status_checks` e permitem merge/squash/rebase; o método efetivamente usado nesta entrega foi merge commit, correto. A consulta atual não demonstra a configuração histórica no instante do merge. Não se atribui bypass ou fraude para explicar o PR sem reviews.
- **Impacto:** crédito por branch/PR/merge e execução verde; redução por aprovação não rastreável e checks não obrigatórios.
- **Recomendação:** registrar aprovação de outro integrante antes da integração, tornar checks bloqueantes e restringir métodos à política normativa. Apresentar evidência histórica adicional caso tenha existido revisão formal não retornada pela API.

### 2.6 Aplicação justificada de padrões OO

- **Atendido:** Strategy OO identificável em `SecurityConfig.java:102–119`, com justificativa, consequências e ADR.
- **Parcialmente atendido:** Dois padrões documentados: Strategy OO comprovada e adaptação de contratos funcional, cuja realização não demonstra plenamente um segundo padrão OO.

#### A6 — Segundo padrão não demonstrado plenamente como OO

- **Categoria:** padrões e decisões técnicas.
- **Evidência:** `PADROES-DE-PROJETO.md:7–30` chama de Adapter funções como `mapearUsuario` e `converterEndereco`, que existem e resolvem adaptação real de dados. Contudo, são conversões funcionais, sem colaboração de objetos/contrato-alvo que evidencie Adapter OO. Já `SecurityConfig.java:102–119` implementa contrato comum e delegação entre estratégias CSRF. ADR-0004 registra alternativas e trade-offs.
- **Impacto:** crédito substancial pela justificativa e pelo design, sem equiparar automaticamente adaptação funcional ao requisito de dois padrões OO. Não se exige artificialmente criar classes só para aumentar a pontuação.
- **Recomendação:** demonstrar a estrutura OO e seus benefícios reais ou selecionar outro problema que justifique um segundo padrão OO.

### 2.7 Atualização das métricas

- **Atendido:** Riscos revisitados e progresso M-03 analisado, inclusive distinção entre estimativa dos itens e horas realmente trabalhadas.
- **Parcialmente atendido:** Métricas, contribuições e documentação atualizadas apenas parcialmente.
- **Não atendido:** Coleta final de M-01 e M-02.

#### A7 — Acompanhamento incompleto, apesar da revisão relevante de riscos

- **Categoria:** métricas / riscos / contribuições.
- **Evidência:** `METRICAS.md:33–57` mantém M-01 “Não medida” e M-02 “A medir após o merge”, embora o PR já esteja integrado. M-03 tem 32/88h, 36,4%, com explicação correta de que são estimativas de itens concluídos. O lead time observável do PR #25 é 2min40s, não registrado no fechamento. `CONTRIBUICOES-SPRINT-2.md:12–17` lista áreas sem vínculos individuais a hashes/issues/reviews. `riscos.md:80–102` revê riscos, mas R09 ainda menciona porta/volumes já corrigidos no Compose final.
- **Impacto:** critério 5 parcial. A análise do board, detalhada em A11, confirma acompanhamento existente, mas não resolve as métricas pendentes nem a falta de vínculos individuais no portfólio.
- **Recomendação:** registrar valores e comparação com baseline/Sprint 1, revisar riscos após os últimos commits e manter portfólio individual com evidências contemporâneas.

### 2.8 Atualização dos riscos

Achado relacionado: A7 (item 2.7).

### 2.9 Documentação atualizada

- **Atendido:** Documentos de padrões, ADRs de autenticação/padrões, registro de IA e release existentes.
- **Não atendido:** Relato `docs/entregas/sprint-2.md`, citado no README e no registro de IA, ausente da árvore entregue.

#### A9 — Documentação e release não sincronizadas com a entrega

- **Categoria:** documentação / release.
- **Evidência:** `README.md:142–148` diz “em andamento”, informa release não publicada e aponta o relato inexistente `docs/entregas/sprint-2.md`. `docs/ci.md:23` ainda aguarda PR/checks. Existe [release 0.2.0](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/releases/tag/0.2.0), apontando ao merge, mas não a tag exigida `v0.2.0`; a descrição ainda pede confirmação dos checks já aprovados. `ServiceHub/README.md` é o texto padrão Vite; README principal não esclarece terminais separados/retorno à raiz entre comandos. Docker usa Node 18, enquanto README/CI usam Node 24, e `api.ts:5` fixa a URL ignorando `VITE_API_URL` do Compose. O diagrama contém operações/relações ainda não realizadas, sem identificar claramente visão futura.
- **Impacto:** documentação parcialmente útil, porém não confiável como retrato completo do fechamento. Não se afirma falha de build Docker sem executá-lo.
- **Recomendação:** revisar links, comandos, estado entregue, configuração, diagrama e release conjuntamente; distinguir arquitetura prevista de arquitetura implementada.

#### A10 — IA declarada, mas validação humana permanece pendente

- **Categoria:** uso de IA.
- **Evidência:** `USO-IA.md:98–127`, registros 10–12, termina com “Necessario a revisão e confirmação final pela equipe”; registro 10 cita relato ausente. Não há declaração geral equivalente de revisão, compreensão e responsabilidade integral. Os efeitos são detalhados na seção específica abaixo.
- **Impacto:** redução localizada no critério documental/IA; não configura, por si, prova de fraude ou uso oculto.
- **Recomendação:** concluir revisão técnica humana e registrar responsáveis, artefatos efetivos, procedimentos e resultados da validação.

### 2.10 Release do marco

- **Parcialmente atendido:** Release substancial, porém com tag `0.2.0`, em vez de `v0.2.0`, e documentação ainda anterior ao fechamento.
- **Não atendido:** Tag exata `v0.2.0` e registro de validação humana concluída nos usos de IA da Entrega 6.

Achado relacionado: A9 (item 2.9).

### 2.11 Registro das contribuições individuais

- **Atendido:** Board existente e acessível, com estados, responsáveis e vínculos a PRs; #7 está representada e possui movimentação rastreável no fechamento da sprint. Isso não implica atualização integral e coerente do quadro.
- **Parcialmente atendido:** Acompanhamento pelo board comprovado, mas com divergência de estado em #7 e cobertura atual incompleta das tarefas de apoio; o histórico recuperado não constitui um snapshot integral da Sprint 2.

#### A11 — Project 32 consultado: organização comprovada e fechamento inconsistente

- **Categoria:** processo GitHub / backlog, board e contribuições.
- **Evidência:** o [Project 32](https://github.com/orgs/IFSC-ES2/projects/32), consultado em 28/10/2026, tem título “equipe 1”, foi criado em 07/08/2026 e contém sete itens, todos não arquivados, com paginação integralmente consultada. A visualização indicada no README é um board agrupado por `Status`, sem filtro. Há campos de responsáveis, status, labels, PRs vinculados, milestone, revisores e datas; não há campo específico de sprint/iteração ou estimativa. A norma não obriga esses campos específicos: sua ausência não gera desconto autônomo. O estado atual e os eventos disponíveis não equivalem a um snapshot completo em 01/10; a análise distingue os fatos recuperáveis desse marco das alterações posteriores.

  | Issue                                               | Estado atual no Project | Evidência e recorte temporal                                                                                                                                                                                                             |
  | --------------------------------------------------- | ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
  | #1 — README, #2 — labels/milestone e #4 — Inception | Done                    | Itens de etapas anteriores, criados e atualizados em agosto; não são novas contribuições da Sprint 2.                                                                                                                                    |
  | #7 — Cadastro e autenticação                        | **In Progress**         | Issue encerrada em 01/10 às 21h37min48s (UTC−3); a timeline registra `Todo → Done` às 21h37min50s. O valor atual `In Progress` tem última atualização em **01/10 às 23h55min33s**, antes do merge às 23h58min10s.                        |
  | #8 — Criação/publicação de solicitação              | Done                    | Campo atualizado em 18/09 às 00h02min33s (UTC−3), correspondente à Sprint 1; não deve ser novamente creditado à Sprint 2.                                                                                                                |
  | #9 — Aceite/recusa                                  | Done                    | A timeline mantém `Todo` até a movimentação vinculada ao PR #29, em **22/10 às 22h29min32s (UTC−3)**. O fechamento e a passagem a `Done` ocorreram após o merge desse PR, já na etapa posterior. Na Sprint 2, estava aberta e em `Todo`. |
  | #10 — Conclusão/pagamento Asaas                     | Todo                    | O campo de status não recebeu nova atualização desde agosto; permanece pendente.                                                                                                                                                         |

- **Divergência histórica de #7:** a API retorna o valor atual `In Progress` com `updatedAt: 2026-10-02T02:55:33Z`, anterior ao merge `2026-10-02T02:58:10Z` — ambos ainda em 01/10 no fuso UTC−3. Isso sustenta uma divergência do acompanhamento já antes da integração, apesar do fechamento da issue e da contagem de #7 como concluída em `METRICAS.md`. Entretanto, a timeline não retornou o evento da passagem posterior de `Done` para `In Progress`, nem seu autor. O evento anterior `Todo → Done` identifica `github-project-automation`; a conexão de #7 ao PR #25 ocorreu um segundo antes da atualização do campo, mas essa proximidade não prova a causa da mudança seguinte. Não se atribui essa alteração a um aluno nem se afirma que #7 nunca chegou a `Done` ou foi reaberta.
- **Cobertura do acompanhamento:** #13 (infraestrutura), #14 (documentação) e #24 (defeito de perfis) não aparecem entre os sete itens atuais, e suas timelines consultadas não retornaram inclusão/remoção no Project 32. Logo, essas frentes não estão cobertas no quadro consultado e não se recuperou comprovação de sua representação durante a sprint. Isso não prova que jamais estiveram no board. Os itens atuais foram criados em agosto; a movimentação de itens antigos comprova atividade mesmo sem inclusão de novos itens na Sprint 2. O quadro atual inclui mudanças de outubro posteriores ao prazo, que não são incorporadas como realização da Entrega 6.
- **Contribuições e responsabilidades datadas:** as timelines confirmam que Davi (`daviiq`) organizou atribuições em 01/10, UTC−3: #24 às 21h11 (Davi e Leonardo), #14 às 21h11 (Leonardo e Ana), #13 às 21h17 (Leonardo, João e Ana), #9 às 21h31 (Davi e João) e #7 às 21h36 (Davi, João e Leonardo). Ele também comentou/encerrou #7 às 21h37. Isso comprova organização de issues no fechamento, compatível com a responsabilidade documental/de acompanhamento, mas não distribuição antecipada nem execução/autoria do código por todos os responsáveis. Os dados do Project não identificam o autor de `108c02f` nem comprovam aprovação formal do PR #25: as consultas de reviews retornaram listas vazias.
- **Impacto:** severidade moderada e localizada no acompanhamento, avaliado em **0,5/1,0 no critério 5**. A existência do quadro e as ações de organização são reconhecidas, mas as inconsistências de fechamento, as métricas pendentes e o portfólio sem vínculos individuais sustentam o atendimento parcial. Essas lacunas são consideradas conjuntamente, sem descontos cumulativos pela mesma insuficiência e sem transformar o estado atual em prova integral do histórico. As atribuições corroboram a organização por Davi considerada em sua nota individual, mas não demonstram implementação ou acompanhamento contínuo.
- **Recomendação:** reconciliar o estado das issues/PRs com o board após a integração, incluir tarefas de apoio e defeitos no acompanhamento, registrar responsabilidades no início e preservar evidências datadas de progresso e impedimentos. Rever automações de status para evitar que itens encerrados permaneçam em andamento.

#### Análise das Contribuições Individuais

Janela analisada: fechamento da Sprint 1 até 01/10. Nos 21 commits não merge do incremento, três são de 29/09 e dezoito de 01/10. Não há novos commits desse incremento entre 18 e 28/09 nem em 30/09. Isso indica concentração do registro versionado, não prova ausência de trabalho fora do Git. O PR único foi aberto às 23h55 e integrado cerca de 2min40s depois: houve pouca oportunidade rastreável para revisão independente e reação a falhas.

- **Ana Clara Teixeira Ronzani:** 12 commits, todos em 01/10, 19h59–22h31: instruções (`464b0db`), métricas/CI (`b230fac`), riscos (`0c15aed`), padrões/ADR (`11e94c6`) e correção do workflow (`4cd313d`). Trabalho documental e de qualidade substancial, com apoio à integração final. Porém, os testes automatizados atribuídos no planejamento não têm alteração sob sua autoria nesta sprint; documentação de padrões não equivale à implementação. Sem review formal e com concentração no fechamento.
- **Davi Israel Quirino:** Dois commits em 01/10, 22h14–22h16: planejamento (`1ac585d`) e IA (`94d2896`), sem código React atribuível nesta janela. Em contrapartida, abriu #24 às 21h11 com imagens e descrição de defeito, organizou atribuições de #7/#9/#13/#14/#24 entre 21h11 e 21h36 e comentou/encerrou #7 às 21h37. As timelines confirmam organização/QA no fechamento, compatível com seu papel de acompanhamento, sem demonstrar atuação contínua durante a sprint. O papel de interface declarado não se confirma em código da sprint; validação sem roteiro detalhado. A nota reconhece a contribuição de processo e comunicação, com as limitações de execução e periodicidade observadas.
- **João Vitor Schmitt:** Dois commits em dias distintos: Docker/Compose (`8d5635b`, 29/09) e correções de ambiente/Gradle (`029a4b6`, 01/10). Abriu PR #25 e publicou a release. Infraestrutura e operacionalização relevantes, com primeira versão e correção posterior. CI foi também trabalhado por Leonardo e Ana e não é creditado integralmente a João. Pesam PR muito tardio, aprovação não comprovada e fechamento da release incompleto.
- **Leonardo Victor Muller De Morais:** Quatro commits em 29/09 e 01/10: Home (`8cccc3f`), segurança e integração (`0bf10e2`), testes/CI/ADR/documentos (`bd60f25`) e descrição de contribuição (`f588c9f`); fez o merge. Maior contribuição técnica nominal comprovada e apoio transversal. Não se lhe atribui automaticamente o cadastro/login inicial do autor genérico. Como Scrum Master/arquiteto, faltam acompanhamento incremental e revisão formal do fechamento; parte do trabalho foi agrupada em commit amplo.

**Autoria não identificada:** `108c02f`, de 29/09 às 20h02, usa `aluno <aluno@servicehub.local>`; a API retorna autor/committer de conta nulos. Altera 15 arquivos centrais de cadastro/login, backend e frontend. Não se atribui esse trabalho nominalmente sem evidência adicional. A equipe deve apresentar comprovação contemporânea da autoria, sem reescrever histórico ou criar atribuições retroativas apenas para obter nota.

**Colaboração e limites:** a issue #24 comprova comunicação de problema; os commits de CI e testes mostram apoio transversal. O Project confirma movimentação e atribuições no fechamento, inclusive responsabilidades de Ana e Leonardo em documentação, João/Leonardo/Ana em infraestrutura e Davi/João/Leonardo em #7. Atribuição não equivale a implementação, teste ou review. Não há reviews no PR nem comunicação contínua rastreável suficiente para afirmar previsibilidade plena. Não se conclui que Leonardo absorveu tarefas por omissão de outros ou que o board estava abandonado. As notas podem ser revistas mediante evidências históricas pertinentes de autoria, revisão e acompanhamento, não simples reatribuição retrospectiva.

## 3. Critérios de Avaliação (10 pts)

### Nota da Entrega

| Critério                                               |   Máximo |    Nota |
| ------------------------------------------------------ | -------: | ------: |
| Planejamento da Sprint 2                               |      1,0 |     0,7 |
| Incremento funcional central, integrado e demonstrável |      2,0 |     1,5 |
| Testes automatizados coerentes e executáveis           |      1,5 |     1,0 |
| Pelo menos dois padrões OO justificados                |      1,5 |     1,0 |
| Métricas, riscos, backlog, board e contribuições       |      1,0 |     0,5 |
| Fluxo de trabalho e CI                                 |      1,5 |     0,9 |
| README, documentação, IA e release/tag                 |      1,5 |     0,8 |
| **Total**                                              | **10,0** | **6,4** |

### Justificativa da Nota

**Nota coletiva: 6,4/10,0.** O cadastro/login é uma evolução central e integrada em relação à Sprint 1. Testes relevantes e execução real do CI elevam a qualidade comprovada; decisões de autenticação, riscos e padrões possuem conteúdo técnico específico.

Não há atendimento pleno porque o incremento seguro ainda carece de autorização por recurso e validação uniforme; os testes não atravessam o caminho HTTP realmente usado; apenas um padrão está plenamente demonstrado como OO; duas métricas continuam sem coleta; aprovação e obrigatoriedade de checks não foram comprovadas; e a documentação/IA/release não fecha coerentemente o marco. O aceite não concluído não é cobrado como se integrasse o escopo confirmado da Sprint 2. O board confirma organização existente, porém com fechamento inconsistente em #7 e cobertura atual incompleta de frentes de apoio, compondo a avaliação parcial do acompanhamento em 0,5/1,0. Os descontos distinguem produto, verificação e precisão documental, sem repetir integralmente a mesma falha em vários critérios.

### Notas Individuais

As notas abaixo avaliam contribuição observável em escala 0–10: conteúdo e relevância técnica, correspondência às responsabilidades, periodicidade, integração, comunicação e colaboração. Não são proporcionais à quantidade de commits. Não se presume atraso em compromisso interno, omissão de colega ou redistribuição sem evidência.

| Integrante                       | Nota individual |
| -------------------------------- | --------------: |
| Ana Clara Teixeira Ronzani       |         **6,5** |
| Davi Israel Quirino              |         **5,5** |
| João Vitor Schmitt               |         **6,5** |
| Leonardo Victor Muller De Morais |         **7,0** |

### Notas Compostas por Integrante

Conforme a padronização solicitada pelo professor para seguir o modelo das demais equipes, o registro em `NOTAS.md` combina **30% da nota coletiva e 70% da contribuição individual**. Com a coletiva de 6,4, as notas compostas são: Ana Clara, 6,47; Davi Israel, 5,77; João Vitor, 6,47; Leonardo Victor, 6,82. Na tabela de notas, são apresentadas com uma casa decimal.

| Integrante                       | Nota coletiva | Nota individual |                            Cálculo | Nota composta |
| -------------------------------- | ------------: | --------------: | ---------------------------------: | ------------: |
| Ana Clara Teixeira Ronzani       |           6,4 |             6,5 | `(6,4 × 0,3) + (6,5 × 0,7) = 6,47` |       **6,5** |
| Davi Israel Quirino              |           6,4 |             5,5 | `(6,4 × 0,3) + (5,5 × 0,7) = 5,77` |       **5,8** |
| João Vitor Schmitt               |           6,4 |             6,5 | `(6,4 × 0,3) + (6,5 × 0,7) = 6,47` |       **6,5** |
| Leonardo Victor Muller De Morais |           6,4 |             7,0 | `(6,4 × 0,3) + (7,0 × 0,7) = 6,82` |       **6,8** |

## 4. Observações

### Observações sobre Uso de IA

Na Entrega 6, os registros 10–12 declaram Copilot e DeepSeek para análise do enunciado e estruturação/revisão de planejamento, baseline, métricas, riscos, padrões, ADR e contribuições. Esses documentos existem e seu conteúdo é compatível com o tipo de apoio declarado, **exceto o relato `docs/entregas/sprint-2.md`, ausente**. O planejamento efetivo tem outro nome e não substitui integralmente o fechamento citado.

Também foram conferidos os artefatos anteriores referenciados: imagens/componentes/telas de seleção e Home existem; `SolicitacaoServicoServiceTest.java` e `UsuarioServiceTest.java` existem; a integração de solicitações e endereços é observável. Permanecem validações de código no futuro nos registros 6–7, descrição imprecisa de artefatos no registro 9 e menção à Entrega 4 na validação de testes do registro 8, situado na Entrega 5. Não se reatribui a IA, sem evidência, toda ampliação de código ou testes da Sprint 2.

**Conclusão explícita: compatibilidade parcial, com divergências verificáveis.** Ferramentas e finalidades declaradas correspondem a boa parte dos artefatos, mas o documento ausente e a revisão/validação humana ainda futura impedem considerar o protocolo plenamente cumprido. Os checks verdes comprovam execução automatizada de parte do produto, não compreensão ou revisão humana de todos os documentos e decisões. Não foram encontradas evidências concretas suficientes de uso oculto de IA, plágio ou falsificação; estilo de texto, isoladamente, não fundamenta essa imputação. Compreensão técnica deverá ser verificada na defesa.

### Recomendações para a Equipe

- Corrigir propriedade e autorização das solicitações/endereços e adicionar testes negativos entre perfis e usuários.
- Testar o fluxo HTTP real de autenticação, sessão, CSRF e logout, além de cadastro e persistência; medir cobertura.
- Registrar aprovação formal independente antes do merge e tornar os checks necessários obrigatórios.
- Planejar e integrar incrementalmente, registrar impedimentos e esclarecer a autoria genérica com evidências contemporâneas.
- Coletar métricas finais, revisar riscos após as últimas correções, reconciliar estados do board com issues/PRs, incluir frentes de apoio no acompanhamento e vincular contribuição individual a commits/issues/PRs/reviews.
- Reconciliar escopo, arquitetura prevista/real, comandos, ambientes, links e release; identificar explicitamente simulações e pendências.
- Demonstrar o segundo padrão OO por necessidade real e concluir/documentar a validação humana dos artefatos apoiados por IA.
