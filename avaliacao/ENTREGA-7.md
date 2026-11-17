# Avaliação da Entrega 7 - Sprint 3

## Identificação

- Equipe: es2-2026_2-equipe-1
- Projeto: ServiceHub — plataforma de contratação de serviços autônomos
- Entrega: 7 - Sprint 3
- Repositório confirmado pela configuração: `IFSC-ES2/es2-2026_2-equipe-1`
- Pull request avaliado: [#29](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/pull/29), `entrega-7` → `main`.
- Versão avaliada: `7b7307a`, merge do PR #29 em 22/10/2026 às 22h37 (UTC−3).
- Release esperada: `v0.2.0`

## Documentos Consultados

Somente documentos e artefatos do repositório avaliado, consultados na `main` e, quando relevante, comparados com a tag:

- `README.md` e `USO-IA.md`.
- `.github/PULL_REQUEST_TEMPLATE.md`, `.github/ISSUE_TEMPLATE/task_template.md` e `.github/workflows/ci.yml`.
- `docs/inception.md`, `docs/dod.md`, `docs/BASELINE.md` e `docs/ESTIMATIVAS.md`.
- `docs/entregas/sprint-3-planejamento.md`.
- `docs/ARQUITETURA.md`.
- `docs/diagramas/Diagrama-Contexto-Sistema.md`, `docs/diagramas/Diagrama-Contêineres.md` e `docs/diagramas/Diagrama-de-Classes.md`.
- `docs/adrs/ADR-0001-stack-tecnologica.md`, `ADR-0002-integracao-pagamentos-asaas.md`, `ADR-0003-autenticacao.md`, `ADR-0004-padroes-de-projeto.md` e `ADR-0005-evolucao-arquitetura-negociacao.md`.
- `docs/PADROES-DE-PROJETO.md` e `docs/REENGENHARIA.md`.
- `docs/METRICAS.md` e `docs/metricas/M-01.md`, `M-02.md` e `M-03.md`.
- `docs/COBERTURA-JACOCO.md`, `docs/riscos.md`, `docs/qualidade.md`, `docs/ci.md` e `docs/fluxo-de-trabalho.md`.
- `docs/contribuições/CONTRIBUICOES-SPRINT-3.md`.
- Código, testes, manifests, Dockerfiles, Compose e configurações em `back-end/`, `ServiceHub/` e `docker-compose.yml`.
- Issues #9, #13, #14 e #24, PR #29, revisões, checks, histórico de commits, rulesets e [release `v0.3.0`](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/releases/tag/v0.3.0).
- [Project 32 — equipe 1](https://github.com/orgs/IFSC-ES2/projects/32): campos, sete itens e eventos de status (`ProjectV2ItemStatusChangedEvent`/`AddedToProjectV2Event`) consultados pela API GraphQL em 06/11/2026.

## 1. Descrição — Resumo da Entrega

O README, linhas 66–80, declara o MVP do ServiceHub. Geolocalização em tempo real, chat WebSocket, avaliações, cupons e aplicativo nativo estão fora do escopo desta versão e não foram cobrados. A Sprint 3 seleciona #9 e #14 e replaneja Asaas, PostgreSQL/staging e CD para a Sprint 4; esse adiamento é explícito e não foi tratado como ausência de implementação obrigatória nesta sprint.

O incremento é real: prestador envia ou atualiza proposta; cliente proprietário escolhe uma proposta; o backend registra prestador e valor e altera o status para `EM_ANDAMENTO`. Há entidade de proposta, unicidade por solicitação/prestador, transações e bloqueio pessimista. Logout explícito, expansão de testes, diagramas, nova ADR, reengenharia identificável no PR e CI com testes executados em PR complementam a entrega.

Entretanto, a consulta passiva de chamados não demonstra notificação ativa nem filtro por especialidade, e não existe recusa explícita registrada — embora a recusa integre o item #9 selecionado e fechado nesta entrega. O ciclo completo de conclusão/cancelamento permanece incompleto, mas não é cobrado como se todo o MVP futuro devesse estar pronto na Sprint 3. Há riscos de identidade/autorização na API, arquitetura com imprecisões, revisão insuficiente dos ADRs anteriores, comprovação limitada da reengenharia, métricas inconsistentes e uma tag que não corresponde ao fechamento integrado.

## 2. Itens Obrigatórios da Entrega

### 2.1 Inspeção e replanejamento da Sprint 3

- **Atendido:** meta, estado da Sprint 2, capacidade, itens selecionados e adiamentos justificados (Asaas, PostgreSQL/staging e CD para a Sprint 4) registrados em `docs/entregas/sprint-3-planejamento.md`.
- **Parcialmente atendido:** fechamento planejado × realizado e mudança da semântica da recusa não consolidados nas demais fontes.

#### A1 — Replanejamento funcional e fechamento da issue divergentes

- **Categoria:** requisitos e coerência do MVP. 
- **Relevância:** média.
- **Evidência:** `README.md:72` mantém aceite/recusa pelo prestador; a [issue #9](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/issues/9), fechada pelo PR #29, exige recusa registrada pelo prestador. `docs/entregas/sprint-3-planejamento.md:35–39` muda o fluxo para proposta pelo prestador/aceite pelo cliente e define recusa como ignorar propostas. `docs/METRICAS.md:97,107` reconhece recusa pendente; `docs/riscos.md:113` apresenta #9 como concluído. O controller, linhas 82–103, oferece envio/aceite, não recusa.
- **Impacto:** a mudança para negociação é coerente e tem mérito, mas ignorar proposta pelo cliente não equivale a recusa registrada pelo prestador. Afeta principalmente o incremento; no planejamento, a ressalva é a consolidação insuficiente do escopo, sem desconto integral repetido.
- **Recomendação:** formalizar os critérios revisados ou manter #9 parcial; alinhar README, issue, métricas e riscos ao comportamento implementado.

### 2.2 Incremento funcional da Sprint 3

- **Atendido:** negociação integrada e persistida — envio/atualização de proposta, aceite pelo cliente proprietário, registro de prestador/valor e mudança de status, com unicidade, transações e bloqueio pessimista; logout explícito; issue #9 e PR #29 vinculados.
- **Parcialmente atendido:** recusa ausente, fragilidade na concordância sobre valor e riscos de autorização/entrada impedem considerar o núcleo plenamente consolidado. Outras autorizações foram corrigidas: PUT/DELETE de solicitação exigem ADMIN e a consulta individual verifica participante.
- **Não atendido:** recusa registrada do item #9.
- **Não atendido:** board atualizado indicando o estado real do trabalho — o Project 32 não acompanhou a Sprint 3 (A14).

#### A2 — Criação recebe identidade persistente controlada pelo consumidor

- **Categoria:** implementação e autorização. 
- **Severidade:** alta; cenário identificado por análise estática, sem exploração HTTP.
- **Evidência:** `J/controller/SolicitacaoServicoController.java:71–79` recebe diretamente `SolicitacaoServico`, redefine cliente, prestador, propostas, estado e data, mas não rejeita/limpa `id`. `J/service/SolicitacaoServicoService.java:156–163` chama `repository.save`. O modelo possui identidade persistente e não há verificação de propriedade de um ID eventualmente recebido nesse caminho.
- **Impacto:** um POST com identidade de recurso existente pode alcançar o caminho de merge da persistência, em vez de assegurar criação nova, permitindo potencial sobrescrita de proprietário/estado/associações. Não é reportado como ataque reproduzido. A restrição administrativa de PUT/DELETE não resolve esse caminho alternativo. Compõe a avaliação da robustez do incremento, não um critério extra de segurança.
- **Recomendação:** contrato próprio de criação sem identidade fornecida pelo cliente, rejeição de IDs indevidos e teste de integração de tentativa de reutilização de ID alheio.

#### A3 — Endereços continuam globais, apesar de risco marcado como mitigado

- **Categoria:** privacidade e fidelidade dos riscos. 
- **Severidade:** alta.
- **Evidência:** `J/controller/EnderecoController.java:25–32` e `J/service/EnderecoService.java:17–23` listam endereços sem filtro de proprietário; `J/config/SecurityConfig.java:87–90` exige cliente para POST, mas GET recai na autenticação genérica. `ServiceHub/src/paginas/DashboardCliente/index.tsx:73–79` consome essa lista. O POST de solicitação não valida propriedade do endereço. `docs/riscos.md:100,120` identifica essa exposição e depois marca R08 como mitigado.
- **Impacto:** o isolamento de dados pessoais não está consolidado. Não se repete a alegação antiga de que toda mutação continua liberada.
- **Recomendação:** associar e consultar endereços por proprietário; validar seu uso na solicitação; revisar R08 por subproblema. Na pontuação documental considera-se a inexatidão da reclassificação, não novo desconto integral pela mesma falha técnica.

#### A4 — Aceite não identifica a versão ou o valor visualizado da proposta

- **Categoria:** regra de negócio. 
- **Severidade:** média/alta; cenário derivado estaticamente.
- **Evidência:** `J/service/SolicitacaoServicoService.java:98–107` atualiza o valor da proposta existente; `:129–136` aceita o valor corrente. `ServiceHub/src/servicos/api.ts:182–185` envia apenas solicitação e prestador.
- **Impacto:** se o prestador atualizar R$ 100 para R$ 200 depois da listagem e antes do clique do cliente, o aceite pode aplicar R$ 200 embora a interface ainda mostre R$ 100. O bloqueio pessimista serializa operações, mas não assegura concordância com a versão visualizada.
- **Recomendação:** identificar/versionar a oferta aceita e detectar alteração de valor antes de contratar; cobrir esse cenário com testes.

#### A5 — Publicação não valida invariantes suficientes na API

- **Categoria:** implementação e testes. 
- **Severidade:** média.
- **Evidência:** o controller (`:71–79`) recebe entidade sem DTO validado; o modelo `SolicitacaoServico.java:22–30` e o serviço (`:156–163`) não asseguram os campos mínimos. Os dashboards acessam endereço/valor presumindo preenchimento (`DashboardCliente/index.tsx:210–214`, `DashboardPrestador/index.tsx:111–115`).
- **Impacto:** chamadas diretas com dados incompletos podem gerar recursos que a interface não trata adequadamente. Não foi feita reprodução interativa. Compõe a ressalva de robustez do incremento junto aos achados anteriores, sem descontos adicionais fora da rubrica.
- **Recomendação:** validar invariantes no backend e testar entradas incompletas e inválidas, não apenas o formulário e os casos felizes.

### 2.3 Documentação da arquitetura

- **Atendido:** visões de contexto e de contêineres, explicação de responsabilidades e registro de diferenças planejado × real em `docs/ARQUITETURA.md` e `docs/diagramas/`.
- **Parcialmente atendido:** filtro, contato, endpoints e infraestrutura descritos com imprecisões em relação ao sistema real.

#### A6 — Arquitetura representa o núcleo, mas inclui comportamentos e infraestrutura não entregues

- **Categoria:** arquitetura e coerência documental. 
- **Relevância:** média.
- **Evidência:** `docs/ARQUITETURA.md:38` afirma filtro por especialidade e contato do cliente após aceite; o serviço (`:32–38`) filtra somente estado e o DTO do cliente possui ID/nome, não contato. `ARQUITETURA.md:75` cita `/api/propostas`, enquanto os endpoints estão sob `/api/solicitacoes/{id}/propostas`. Os diagramas (`:32–59`) indicam HTTPS e “H2 / PostgreSQL”, embora a execução entregue seja HTTP local e `application.properties:8–16` use H2 em memória.
- **Impacto:** as visões de contexto e contêineres são aproveitáveis, mas a arquitetura não é integralmente fiel. H2 também é a configuração padrão da aplicação, não somente dos testes; dados desaparecem no reinício. PostgreSQL postergado é decisão aceita nesta sprint, não falha autônoma por ausência de uma tecnologia específica.
- **Recomendação:** distinguir arquitetura atual, hipótese de implantação e evolução futura; documentar persistência efêmera e usar endpoints e comportamentos reais.

### 2.4 ADRs consolidados

- **Atendido:** ADR-0005 substancial, relacionada ao código, com contexto, alternativas e consequências da negociação.
- **Parcialmente atendido:** decisões anteriores e pendências de testes não foram efetivamente revisadas.

#### A7 — Nova ADR pertinente, revisão insuficiente das decisões anteriores

- **Categoria:** decisões técnicas. 
- **Relevância:** média.
- **Evidência:** ADR-0001 permanece sem registrar a solução provisória H2; ADR-0003:22 e ADR-0004:27–28 ainda tratam testes como pendência. `docs/PADROES-DE-PROJETO.md:30` diz não haver framework de testes frontend, embora Vitest exista já na tag. ADR-0005 foi criada após a tag.
- **Impacto:** a criação da nova decisão não substitui a consolidação do histórico. A condição “aceita” da ADR Asaas, por si só, não foi tratada como alegação de implementação.
- **Recomendação:** revisar status, consequências e ações pendentes dos ADRs, preservando a evolução histórica e distinguindo decisão adotada de funcionalidade implementada.

### 2.5 Manutenção/reengenharia

- **Atendido:** problema e alteração estrutural reais de integridade da negociação: `J/model/PropostaServico.java:12` define unicidade; `J/repository/SolicitacaoServiceRepository.java:22–24` define bloqueio; o serviço (`:80–144`) aplica transações e validações. O PR #29 identifica a intervenção, atendendo à alternativa de PR claramente identificável, sem exigir PR exclusivo.
- **Parcialmente atendido:** validação descrita além do que os testes demonstram; a mudança também altera a regra de contratação, não sendo apenas refatoração comportamentalmente neutra.

### 2.6 Comparação de métrica antes/depois da reengenharia

- **Atendido:** `docs/REENGENHARIA.md` apresenta indicadores antes/depois.
- **Parcialmente atendido:** indicadores simplificados, sem protocolo de medição nem hashes comparados, e referências de validação incorretas.

#### A8 — Reengenharia concreta, mas comprovação antes/depois e validação exageradas

- **Categoria:** design, métricas e testes. 
- **Relevância:** média.
- **Evidência:** `docs/REENGENHARIA.md:59–63` compara presença/ausência de mecanismos e usa “0% → 100%” para sessão sem protocolo de medição. `:28` atribui data de envio à proposta, inexistente na entidade. `:47` atribui logout/401 a `SecurityIntegrationTest`; o teste real está em `UsuarioSessionIntegrationTest` e espera 403. `:49` descreve validação de formulários, mas `ServiceHub/tests/servicos.test.ts` testa funções de serviço com `fetch` simulado. O relatório termina truncado em `:70`.
- **Impacto:** indicadores estruturais 0→1 são evidência válida, ainda que limitada; não demonstram isoladamente redução de acoplamento, preservação de todo comportamento ou concorrência real. Não se exige ferramenta específica de complexidade nem teste de carga como nova obrigação.
- **Recomendação:** fixar hashes antes/depois, explicar método/unidade e limites dos indicadores, associar testes reais às alegações e corrigir referências de validação.

### 2.7 Testes automatizados integrados ao pipeline

- **Atendido:** testes versionados executam automaticamente em PR, falhas propagam ao check e há execução verde verificável no PR #29 (run 39884680856, quatro jobs verdes, testes backend com JaCoCo e build/testes/lint frontend).
- **Observação:** a ausência de checks obrigatórios nas regras de proteção é descontada somente no critério de fluxo (A12), evitando duplicação.

### 2.8 Atualização de métricas, riscos e documentação

- **Atendido:** atualizações de métricas, riscos, cobertura, contribuições e registro de IA existem no conjunto integrado.
- **Parcialmente atendido:** progresso inclui item parcial; riscos e registros de IA têm contradições; README e métricas apontam para caminhos inexistentes.
- **Não atendido:** coleta da M-02.

#### A9 — Progresso do MVP superestimado e métrica de processo não coletada

- **Categoria:** acompanhamento e métricas. 
- **Relevância:** média.
- **Evidência:** `docs/METRICAS.md:93–107` apresenta #9 parcial, mas soma suas 8h integralmente em 48/88h = 54,5%. Os itens #7, #8 e #14 integralmente declarados concluídos somam 40/88h = 45,5%. M-02 continua “a confirmar” (`:66,88,111`), embora abertura/merge do PR #29 sejam consultáveis, com aproximadamente 7min34s entre eles. Esse valor caracteriza o PR final, não uma média automaticamente representativa de toda a sprint.
- **Impacto:** a atualização não permite confiar integralmente no progresso e na avaliação do processo. Reconhece-se corretamente que horas estimadas de itens não são horas efetivamente trabalhadas.
- **Recomendação:** aplicar o DoD ao cálculo ou definir regra explícita de crédito parcial; coletar M-02 com fonte/recorte; atualizar as fichas e preservar comparações temporais.

#### A10 — Rastreabilidade documental e instruções ainda incompletas

- **Categoria:** documentação e execução. 
- **Relevância:** baixa/média.
- **Evidência:** `README.md:143,158–159` aponta para `sprint-2.md`, `docs/RISCOS.md` e `docs/USO-IA.md`, inexistentes nesses caminhos. `docs/METRICAS.md:124–125` aponta para a pasta `docs/evidencias/`, ausente; o resumo real é `docs/COBERTURA-JACOCO.md`. O artefato HTML JaCoCo foi publicado no CI final, mas o percentual declarado de 77,35% não foi recalculado nesta avaliação. `docs/riscos.md:117` ainda propõe configurar JaCoCo já configurado e `:92` mistura resultado da Sprint 3 com seção da Sprint 2. README e CI concordam em JDK 25/Node 24, mas `ServiceHub/Dockerfile:1` usa Node 18. O Compose define `VITE_API_URL`, enquanto `api.ts:5` usa URL fixa; conserva também variáveis Node no serviço Java. O check `docker compose config` confirma sintaxe, não execução de toda a solução.
- **Impacto:** dificulta reprodução e localização; não invalida o CI de build/testes nem comprova que todos os contêineres falham.
- **Recomendação:** corrigir caminhos/capitalização, relacionar cobertura ao commit/run, separar instruções locais e Docker e validar o ambiente efetivamente suportado.

#### A11 — Declaração e validação humana de IA parcialmente consolidadas

- **Categoria:** uso de IA e documentação. 
- **Relevância:** média.
- **Evidência:** `USO-IA.md:140,150,189` mantém confirmação/revisão pendente, enquanto `:169,179` afirma revisão dos documentos e `docs/riscos.md:122` declara R10 mitigado. Há numeração repetida e referências “conforme o commit” sem hash. A atuação de `copilot-swe-agent[bot]` em `f6eda74`, coassinada por João, é evidência direta de apoio automatizado no CI, não identificada de modo específico nos registros; as declarações amplas de Copilot/atualização de pipeline não permitem concluir ocultação deliberada.
- **Impacto:** compatibilidade parcial do registro com a entrega (detalhada em “Observações sobre Uso de IA”), com impacto localizado no critério documental.
- **Recomendação:** vincular registros a hashes/PRs, corrigir caminhos, identificar agente e revisão humana correspondente, indicar quem validou e o resultado, removendo estados futuros somente após a verificação efetiva.

### 2.9 Fluxo de trabalho evidenciado no repositório

- **Atendido:** ramificação `entrega-7`, PR #29 com aprovação no último head após CI verde e integração por merge commit.
- **Parcialmente atendido:** checks não obrigatórios na configuração efetiva consultada; PR único reúne código, testes, refatoração e documentação, com revisão concentrada no fechamento.

#### A12 — Proteção existente, sem checks obrigatórios

- **Categoria:** processo GitHub. 
- **Relevância:** alta no processo.
- **Evidência:** a API clássica retorna 404, mas os rulesets ativos `20533295` e `20533407`, confirmados também por `gh api repos/IFSC-ES2/es2-2026_2-equipe-1/rules/branches/main`, exigem PR/uma aprovação e vedam exclusão/non-fast-forward. Nenhum contém regra `required_status_checks`. Permitem merge, squash e rebase; nesta entrega foi efetivamente usado merge commit.
- **Impacto:** não se conclui branch desprotegida. Há testes que tornam o check vermelho em caso de falha, mas as regras atuais não impõem check verde para integrar. A consulta atual não reconstitui sozinha toda a configuração histórica. O PR #29 teve quatro checks verdes antes da aprovação/merge; não há integração com teste quebrado nesta entrega.
- **Recomendação:** exigir os checks relevantes nas regras aplicáveis.

### 2.10 Release/tag do marco

- **Atendido:** tag `v0.3.0` e release publicadas no prazo, com descrição do incremento.
- **Parcialmente atendido:** a tag não corresponde ao fechamento integrado e a descrição cita artefato fora da tag e recomendações de CI desatualizadas.

#### A13 — Tag/release anterior ao fechamento e revisão concentrada

- **Categoria:** publicação e colaboração. 
- **Relevância:** média/alta.
- **Evidência:** tag em `b5c42e1`; integração em `7b7307a`. A [release](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/releases/tag/v0.3.0) cita ADR-0005, inexistente na tag, e recomenda integrar testes frontend ao workflow, embora essa integração já esteja na tag. O registro de contribuições na tag tem três descrições vazias. A aprovação formal é válida, sobre `601e0a8`, mas sem discussão técnica de código rastreável.
- **Impacto:** o marco não reproduz o fechamento anunciado. Publicar antes da revisão final enfraquece o significado da release, sem configurar por si só atraso no prazo. Não se presume revisão profunda a partir do simples clique de aprovação, nem se invalida a aprovação existente.
- **Recomendação:** publicar marcos coerentes com o commit aprovado/integrado, identificar complementos posteriores sem mover silenciosamente tag publicada e revisar incrementos menores ao longo da sprint.

### 2.11 Registro de contribuição individual

- **Atendido:** `docs/contribuições/CONTRIBUICOES-SPRINT-3.md` registra atividades por integrante no conjunto integrado.
- **Parcialmente atendido:** o quadro individual sugere comando até 21/10, omitindo o dia 22, e não fornece hashes por pessoa; na tag, três descrições estão vazias. O board não registra responsáveis nem progresso da Sprint 3 (A14).

#### A14 — Project 32 consultado: board não acompanhou a Sprint 3

- **Categoria:** processo GitHub / backlog, board e contribuições.
- **Relevância:** média.
- **Evidência:** o [Project 32](https://github.com/orgs/IFSC-ES2/projects/32) continua com os mesmos sete itens criados em agosto (#1, #2, #4, #7, #8, #9 e #10), com campo `Status` (`Todo`, `In Progress`, `Done`) e sem campo de sprint/iteração. Nenhum item foi incluído na janela da Sprint 3: **#14**, um dos dois itens selecionados para a sprint, e #13 e #24 não estão no quadro. Na janela 02/10–22/10 não há nenhuma mudança de status manual. A única movimentação é de `github-project-automation` em **#9**: `Todo → In Progress` às **22h29min32s de 22/10** (UTC−3), quando o PR #29 foi vinculado, e `In Progress → Done` às **22h37min07s**, no fechamento automático pelo merge. **#7**, encerrada em 01/10, continua `In Progress` — a inconsistência já apontada na Entrega 6 não foi reconciliada. **#10** (Asaas), replanejada para a Sprint 4, permanece `Todo`, o que é coerente.
- **Impacto:** durante três semanas o quadro mostrou o item central da sprint como não iniciado, apesar dos incrementos de Leonardo em 16 e 20/10, e não representou a frente de documentação (#14) nem o defeito #24, fechado no mesmo PR. O board existe e a automação funciona, mas não foi usado para indicar o estado real do trabalho, como pede o item 2 da definição. Os estados finais de #9 resultam do vínculo/merge automático, não de acompanhamento pela equipe. Desconto de 0,2 no critério de incremento funcional, onde a definição cobra o board. Não se atribui a omissão a um integrante específico: as atribuições de 01/10, feitas por Davi, não foram seguidas de movimentações por nenhum membro.
- **Recomendação:** incluir no quadro todos os itens selecionados para a sprint, defeitos e tarefas de apoio; movimentar os cartões conforme o trabalho avança e reconciliar estados com issues fechadas, como #7.

#### Análise das Contribuições Individuais

Notas de contribuição são distintas da nota coletiva. Foram consideradas relevância, oportunidade, periodicidade, responsabilidades, integração, revisões e comunicação rastreável. Ausência de evidência não equivale a ausência absoluta de trabalho; alegações informais não foram presumidas.

- **Ana Clara Teixeira Ronzani:** 16 commits não merge até o fechamento, em 21–22/10; 9 até a tag. `a4a9927` amplia testes backend/frontend e configuração Vitest; `362ad92` adiciona testes de sessão/validação e JaCoCo; `fd8ebb9` incorpora cobertura ao CI; `a2f2300` atualiza métricas; `4793f47` participa da correção final do workflow. Há documentação, diagramas e aprovação do PR #29 no último head após o CI verde. Trabalho substancial e coerente com Qualidade/Scrum Master; absorve parte da automação prevista para João e auxilia a arquitetura, habilitando validação do incremento de Leonardo. A atividade rastreável está concentrada nos dois últimos dias, com complementos fora da tag; não se localizou comunicação de progresso anterior nem revisão técnica detalhada, e não há prova de prejuízo coletivo específico causado por essa concentração.

- **Davi Israel Quirino:** 5 commits não merge em 20 e 22/10; 2 até a tag. `5eb7bcb` corrige CSS/carrossel; `295347b` cria ADR-0005 após a tag; há complementos de contribuições e IA. É o autor da publicação da release. O planejamento (`:44`) lhe atribui dashboards e integração de modais/propostas; o núcleo dessas alterações é rastreável principalmente a Leonardo. O registro final, “CSS + auxílio na documentação”, é mais compatível com o conteúdo efetivamente observado. Participação real em dois dias, sem revisão formal de código localizada nem comunicação rastreável explicando a redistribuição. Não se atribui a Davi toda a implementação frontend nem se presume ausência de apoio informal.

- **João Vitor Schmitt:** um commit autoral não merge, `68199b7`, em 22/10 às 22h26, com publicação de relatório e checks documentais, além de coautoria explícita em `f6eda74`, produzido por agente às 22h32. Não há commit autoral próprio no recorte até a tag. O planejamento lhe atribui CI, tarefas/issues e riscos; não se confirmou execução equivalente em todas essas frentes. A integração de testes frontend e JaCoCo recebeu contribuição substancial de Ana; a issue #9 está atribuída a João/Davi, mas o núcleo funcional é de Leonardo. Atuação concentrada nos minutos finais, depois da publicação da release. `68199b7` introduz verificação de `sprint-3.md`, caminho inexistente; o run `39884286653` falha e o caminho é corrigido antes do run verde — retrabalho localizado, não atraso coletivo prolongado.

- **Leonardo Victor Muller De Morais:** 10 commits não merge em 16, 20 e 22/10; 4 até a tag. `2b9d091` integra sessão/logout, dashboards, modais, backend e testes; `5ada842` implementa negociação/entidade/aceite e testes; `522d51f` amplia arquitetura. Participa de métricas/riscos/reengenharia e é autor do PR #29. Principal contribuição ao domínio e ao incremento ponta a ponta, além da arquitetura; absorve implementação frontend atribuída a Davi e parte do trabalho de #9 atribuído a João/Davi. Os incrementos de 16 e 20/10 precedem o fechamento e permitem os testes posteriores. Melhor distribuição temporal observada; ainda há commits excessivamente abrangentes, documentação concentrada no final e ausência de revisão formal de código de colegas nas fontes consultadas. Os riscos técnicos identificados limitam a maturidade do incremento; a nota individual reconhece contribuição e integração, não ausência de defeitos.

**Colaboração e limites:** consultas às issues #9, #13, #14 e #24 não localizaram comentários de progresso/impedimentos; a aprovação formal observada no PR final é de Ana. O Project 32 também não contém movimentação de nenhum integrante na janela da sprint (A14), o que impede usar o board como evidência de acompanhamento individual. Há absorção de responsabilidades por Leonardo e Ana, mas não evidência suficiente para atribuir intenção, recusa de colaboração ou causa pessoal. A distribuição de commits e a atividade estreita no fechamento são fatos; prejuízo amplo a dependências não foi presumido. As notas podem ser revistas mediante evidências contemporâneas verificáveis de autoria, revisão e acompanhamento.

## 3. Critérios de Avaliação (10 pts)

### Nota da Entrega

| Critério | Máximo | Nota |
|---|---:|---:|
| Inspeção e replanejamento | 1,0 | 0,8 |
| Incremento funcional da Sprint 3 | 1,5 | 0,7 |
| Arquitetura real documentada | 1,5 | 1,1 |
| ADRs revisados e consolidados | 1,0 | 0,6 |
| Reengenharia, PR e métrica antes/depois | 1,5 | 0,9 |
| Testes automatizados integrados ao CI | 1,0 | 1,0 |
| Métricas, riscos, documentação, README e IA | 1,0 | 0,5 |
| Fluxo de trabalho, checks, merge e release | 1,5 | 1,0 |
| **Total** | **10,0** | **6,6** |

### Justificativa da Nota

**Nota coletiva: 6,6/10,0.** O incremento de negociação é real e integrado, a arquitetura tem as visões mínimas, há nova ADR pertinente, reengenharia identificável e o CI executa testes com sucesso em PR, com falhas propagadas ao check. A nota não é reduzida por ausência de chat/mapa/avaliações, nem por adiar Asaas/PostgreSQL/CD de forma explícita.

As perdas decorrem de recusa não consolidada (A1), riscos concretos no fluxo/API (A2–A5), imprecisões arquiteturais (A6), revisão incompleta de ADRs (A7), comprovação limitada de reengenharia (A8), métricas, documentação e registro de IA inconsistentes (A9–A11), checks não obrigatórios (A12), publicação divergente do fechamento (A13) e board que não acompanhou a sprint (A14). Após a consulta ao Project 32, o critério de incremento passou de 0,9 para 0,7, porque o item da definição que exige board atualizado, indicando o estado real do trabalho, não foi atendido.

A mesma insuficiência não foi usada para aplicar descontos integrais em vários blocos: a ausência de checks obrigatórios fica em fluxo Git e o sucesso dos testes é reconhecido em CI; artefatos pós-tag recebem crédito no conjunto integrado e a divergência do marco é tratada na publicação. As ressalvas de segurança compõem a robustez do incremento e a fidelidade das alegações de mitigação, sem criação de rubrica extra.

### Notas Individuais

Escala 0–10, considerando conteúdo, relevância, aderência às responsabilidades, periodicidade/oportunidade, integração, revisão, comunicação e apoio aos colegas; não são proporcionais à contagem de commits.

| Integrante | Nota individual | Justificativa resumida |
|---|---:|---|
| Ana Clara Teixeira Ronzani | **8,5** | Testes, JaCoCo, CI, métricas, documentação e aprovação final, amplos e integrados; atividade concentrada nos dois últimos dias e sem revisão técnica detalhada rastreável. |
| Davi Israel Quirino | **5,5** | Interface, ADR-0005 e publicação da release; descompasso entre responsabilidade assumida e execução central comprovada e fechamento documental incompleto da release. |
| João Vitor Schmitt | **4,5** | Contribuição útil em CI e coautoria reconhecida; abrangência e acompanhamento temporal significativamente inferiores ao conjunto de responsabilidades. Não é nota de “nenhuma contribuição”. |
| Leonardo Victor Muller De Morais | **9,0** | Contribuição técnica central, relevante e mais incremental no incremento ponta a ponta e arquitetura; sem base para nota máxima em rastreabilidade/revisão distribuída. |

### Notas Compostas por Integrante

A nota composta da Entrega 7 combina 30% da nota coletiva e 70% da contribuição individual, com peso 10 para a entrega. Os critérios técnicos continuam exclusivamente os da rubrica normativa. A média parcial está registrada em `NOTAS.md`.

`nota composta = (nota coletiva × 0,30) + (nota individual × 0,70)`

| Integrante | Coletiva | Individual | Cálculo | Nota composta |
|---|---:|---:|---:|---:|
| Ana Clara Teixeira Ronzani | 6,6 | 8,5 | `(6,6 × 0,30) + (8,5 × 0,70) = 7,93` | **7,9** |
| Davi Israel Quirino | 6,6 | 5,5 | `(6,6 × 0,30) + (5,5 × 0,70) = 5,83` | **5,8** |
| João Vitor Schmitt | 6,6 | 4,5 | `(6,6 × 0,30) + (4,5 × 0,70) = 5,13` | **5,1** |
| Leonardo Victor Muller De Morais | 6,6 | 9,0 | `(6,6 × 0,30) + (9,0 × 0,70) = 8,28` | **8,3** |

## 4. Observações

### Observações sobre Uso de IA

O uso declarado não foi penalizado por si só. Foram lidos todos os registros de `USO-IA.md`, confrontando finalidade, caminhos e resultados com os artefatos.

| Declaração | Confronto com a entrega |
|---|---|
| README, templates e documentação das Entregas 1–4 | Artefatos correspondentes existem; `.docs` é referência imprecisa à pasta `docs/`. |
| Home, seleção de perfil, imagens e componentes de imagem | Há páginas/componentes compatíveis com o apoio descrito. Não se infere autoria pelo estilo. |
| Testes de serviços e integração frontend/backend das Entregas 5–6 | Suítes e módulos de API existem. O relato `docs/entregas/sprint-2.md` citado continua ausente, embora exista planejamento com outro nome. |
| Sprint 3: CSS/carrossel | Commit `5eb7bcb` e alterações compatíveis; relato admite tentativa de correção parcial. |
| Entidade `PropostaServico`, DTOs, dashboards e Vitest | Existem; o tipo de apoio do registro 12 é compatível com os artefatos. O CI confirma compilação/testes, não correção de todas as regras. |
| ADR-0005, arquitetura, reengenharia, métricas e riscos | Existem no conjunto integrado; a ADR e parte das revisões são posteriores à tag. Persistem as divergências A6–A10. |
| Registro 14: testes de sessão/validação, Gradle, Vitest, JaCoCo e CI | Existem na `main`; parte não existe na tag. Contribuições estão em `docs/contribuições/`, não no caminho declarado; `docs/evidencias/` não existe. |
| Registro 15: documentação da release | A release existe, mas cita artefato fora da tag e mantém recomendações de CI desatualizadas. |

As divergências de validação humana e a atuação não especificada de `copilot-swe-agent[bot]` estão descritas em A11.

**Conclusão explícita:** `USO-IA.md` é **parcialmente compatível com a entrega integrada**, com correspondência ampla entre os tipos de apoio e os artefatos existentes, mas divergências de caminhos, recorte de publicação e validação ainda não encerrada. Na tag, a declaração da Sprint 3 é substancialmente menos abrangente que na `main`. Não há base para acusar uso oculto generalizado ou atribuir autoria por aparência textual. O achado decorre de evidências concretas e da falta de fechamento da validação, com impacto localizado no critério documental. A compreensão técnica deverá ser verificada na defesa.

### Recomendações para a Equipe

- Fechar o caminho de criação com ID controlável, isolar endereços por proprietário e testar entradas inválidas e operações entre usuários distintos.
- Identificar a versão/valor aceito; implementar ou formalmente replanejar a recusa, atualizando a issue #9 e todos os documentos relacionados.
- Fixar commits comparados na reengenharia, tornar indicadores reproduzíveis e limitar alegações às validações realizadas.
- Revisar arquitetura, ADRs antigos, riscos, caminhos e resultados de métricas; não contar integralmente histórias parciais.
- Exigir checks verdes, revisar incrementalmente, registrar redistribuição/progresso e publicar release correspondente ao commit aprovado.
- Completar a validação humana do uso de IA com responsável e evidência e distinguir claramente artefatos da tag dos complementos da `main`.
- Corrigir o registro de contribuições com o dia 22/10 e hashes por integrante.
- Usar o Project 32 durante a sprint: incluir os itens selecionados, defeitos e tarefas de apoio e movimentá-los conforme o trabalho real.
