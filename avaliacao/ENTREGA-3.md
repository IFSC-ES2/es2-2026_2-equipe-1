# Avaliação da Entrega 3 - Estimativas e Métricas (Baseline)

## Identificação

- Equipe: es2-2026_2-equipe-1
- Projeto: ServiceHub
- Entrega: 3 - Estimativas e Métricas (Baseline)
- Data prevista da entrega: 20/08/2026
- Pull request avaliado: PR #16, branch `entrega-3` para `main`
- Versão considerada: estado atual de `origin/main` após integração da Entrega 3

## Documentos Consultados

- `README.md` da equipe.
- `USO-IA.md` da equipe.
- `docs/BASELINE.md`.
- `docs/ESTIMATIVAS.md`.
- `docs/METRICAS.md`.
- `docs/metricas/M-01.md`.
- `docs/metricas/M-02.md`.
- `docs/metricas/M-03.md`.
- Issues GitHub #7, #8, #9, #10, #13, #14 e #15.
- Pull request GitHub #16.
- Histórico Git local e remoto.

## Resumo da Entrega

A equipe entregou os principais artefatos esperados para a Entrega 3: baseline de planejamento, documento de estimativas, definição geral de métricas, fichas individuais de métricas e atualização do README com links para os documentos da etapa. O planejamento apresenta recorte do backlog, priorização, estimativas em horas, técnica adotada, hipóteses, capacidade planejada e previsão incremental de conclusão do MVP.

A entrega é consistente do ponto de vista documental e atende à maior parte dos itens de planejamento, estimativas, capacidade e métricas. As principais perdas estão nas evidências de processo: a entrega foi integrada em 28/08/2026, após a data prevista de 20/08/2026, o PR #16 não possui aprovação formal registrada e não há checks executados no pull request.

## Critérios Atendidos

- O baseline foi registrado em `docs/BASELINE.md` com data de registro, versão, recorte do backlog, priorização e estimativas dos itens do MVP.
- O planejamento apresenta capacidade da equipe com quatro integrantes ativos, papéis e disponibilidade estimada.
- O baseline inclui previsão de conclusão distribuída em sprints e hipóteses assumidas.
- A abordagem de estimativa foi registrada em `docs/ESTIMATIVAS.md`, com técnica, unidade, participantes, critérios de dimensionamento e limitações/incertezas.
- As métricas foram organizadas em produto, processo e projeto em `docs/METRICAS.md`.
- Há fichas individuais para três métricas em `docs/metricas/`, com objetivo, classificação, fórmula, fonte, frequência, responsável, interpretação e histórico de coletas.
- O README foi atualizado com links para baseline, estimativas, métricas e fichas individuais.
- O registro de uso de IA foi atualizado para a Entrega 3, indicando ferramenta, finalidade, artefatos impactados e validação.
- Há issue organizadora da Entrega 3 com critérios de aceitação para os documentos e atualização do README.
- A entrega foi desenvolvida na branch `entrega-3` e integrada à `main` por merge commit do PR #16.

## Critérios Parcialmente Atendidos

- O backlog priorizado está registrado no baseline, mas as issues usadas como base não possuem labels de prioridade, milestone da entrega ou outro campo explícito de prioridade na consulta da API.
- O README contém link para o GitHub Project #32, mas a verificação direta do board não foi possível porque o token atual não possui o escopo `read:project`.
- A métrica M-02 usa baseline inicial de `0h`, embora o repositório já tivesse histórico anterior de PRs; como baseline inicial da métrica, é compreensível, mas perde força como referência histórica do processo já observado.
- As issues #13 e #14, usadas no planejamento, foram criadas apenas em 28/08/2026 e permanecem abertas, reduzindo a evidência de planejamento concluído até o marco da entrega.
- A issue #15 organiza a entrega e possui critérios de aceitação, mas também foi criada em 28/08/2026, após a data prevista da Entrega 3.

## Critérios Não Atendidos

- A entrega não foi integrada até a data prevista de 20/08/2026; o PR #16 foi aberto e mesclado em 28/08/2026.
- Não há aprovação formal registrada no PR #16 via mecanismo de review do GitHub; a consulta retornou `reviews: []`.
- Não há checks obrigatórios executados ou registrados no PR #16; a consulta retornou `statusCheckRollup: []`.
- Não foi possível confirmar proteção de branch/checks obrigatórios pela API; a consulta de proteção da `main` retornou indisponibilidade do recurso para o repositório.

## Achados com Evidências

- README atualizado com links da Entrega 3: `README.md`, linhas 96-105.
- Registro de uso de IA da Entrega 3: `USO-IA.md`, linhas 37-45.
- Baseline com data e versão: `docs/BASELINE.md`, linhas 1-5.
- Recorte e priorização do backlog do MVP: `docs/BASELINE.md`, linhas 8-21.
- Capacidade planejada da equipe: `docs/BASELINE.md`, linhas 25-33.
- Previsão incremental por sprint: `docs/BASELINE.md`, linhas 37-44.
- Hipóteses assumidas: `docs/BASELINE.md`, linhas 48-53.
- Técnica, unidade e item de referência das estimativas: `docs/ESTIMATIVAS.md`, linhas 5-13.
- Participantes da estimativa: `docs/ESTIMATIVAS.md`, linhas 16-22.
- Critérios de dimensionamento: `docs/ESTIMATIVAS.md`, linhas 26-32.
- Limitações e incertezas: `docs/ESTIMATIVAS.md`, linhas 36-40.
- Definição das métricas por categoria: `docs/METRICAS.md`, linhas 7-19.
- Tabela de acompanhamento das métricas: `docs/METRICAS.md`, linhas 23-29.
- Ficha M-01 com classificação, objetivo, fórmula, fonte, frequência, responsável, interpretação e histórico: `docs/metricas/M-01.md`, linhas 1-24.
- Ficha M-02 com classificação, objetivo, fórmula, fonte, frequência, responsável, interpretação e histórico: `docs/metricas/M-02.md`, linhas 1-24.
- Ficha M-03 com classificação, objetivo, fórmula, fonte, frequência, responsável, interpretação e histórico: `docs/metricas/M-03.md`, linhas 1-23.
- Issues do backlog com critérios de aceitação: #7, #8, #9 e #10.
- Issues #13 e #14 criadas para infraestrutura e documentação viva, ambas abertas e criadas em 28/08/2026.
- Issue organizadora da Entrega 3: issue #15, fechada, criada em 28/08/2026, com checklist dos artefatos esperados.
- PR de entrega existente e mesclado: PR #16, branch `entrega-3` para `main`, estado `MERGED`, URL `https://github.com/IFSC-ES2/es2-2026_2-equipe-1/pull/16`.
- PR #16 criado em 28/08/2026 e mesclado em 28/08/2026, após a data prevista de 20/08/2026.
- PR #16 sem review formal registrado: consulta do PR retornou `reviews: []`.
- PR #16 sem checks registrados: consulta do PR retornou `statusCheckRollup: []`.
- Integração por merge commit: commit `a944c6f`, mensagem `Merge pull request #16 from IFSC-ES2/entrega-3`.
- Branch de entrega preservada no remoto: `origin/entrega-3` aponta para o commit `605702f`.

## Recomendações para a Equipe

- Registrar e integrar as próximas entregas até a data prevista ou documentar autorização de prazo quando houver exceção.
- Garantir que os próximos PRs tenham aprovação formal registrada pelo mecanismo de review do GitHub.
- Configurar checks obrigatórios nos pull requests ou documentar claramente quais validações manuais foram executadas enquanto o pipeline ainda não existir.
- Tornar a prioridade relativa do backlog explícita nas issues, usando labels, milestones, campos do board ou ordenação documentada.
- Manter issues de planejamento criadas antes ou durante a execução da entrega, não apenas no momento de fechar a entrega.
- Atualizar a métrica de lead time considerando os PRs já existentes, caso a equipe queira usar histórico real do processo como baseline.
- Garantir que issues planejadas para a entrega sejam fechadas ou justificadas conforme o estado real do trabalho.

## Nota da Entrega

Nota: 3,6 / 5,0

## Justificativa da Nota

- Planejamento inicial e baseline coerentes com o MVP e o backlog priorizado: 0,8 / 1,0. O baseline apresenta recorte, priorização, estimativas, capacidade, previsão e hipóteses, mas parte das issues relacionadas foi criada tardiamente e permanece aberta.
- Estimativas registradas com técnica, unidade, participantes, hipóteses e limitações: 0,9 / 1,0. O documento de estimativas é claro e completo; a pequena perda ocorre pela ausência de evidência detalhada da dinâmica de votação além do relato textual.
- Capacidade planejada da equipe declarada de forma realista e justificada: 0,9 / 1,0. A capacidade está clara em horas por integrante e por sprint, com papéis e restrições, mas poderia explicitar melhor disponibilidade individual e eventuais indisponibilidades concretas.
- Métricas de produto, processo e projeto definidas com objetivo, fórmula, fonte, frequência e interpretação: 0,8 / 1,0. As métricas cobrem as categorias exigidas e possuem fichas, mas a métrica de lead time usa baseline inicial zero apesar de já haver histórico de PRs.
- Evidências no repositório: README atualizado, PR revisado, checks obrigatórios e ramificação `entrega-3` integrada por commit de mesclagem: 0,2 / 1,0. O README foi atualizado e a branch foi integrada por merge commit, mas a entrega foi integrada após o prazo, sem review formal e sem checks registrados.

## Observações sobre Uso de IA

A equipe declarou uso do Gemini para auxiliar na estruturação e geração dos documentos de planejamento da Entrega 3, incluindo baseline, estimativas, definição de métricas e fichas individuais. A declaração é compatível com os artefatos entregues, pois esses documentos foram criados no repositório. O registro também informa validação humana, citando revisão das estimativas, ajuste de capacidade semanal e validação das fórmulas e métricas em relação ao escopo do MVP.
