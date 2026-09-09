# Avaliação da Entrega 4 - Riscos e Consolidação

## Identificação

- Equipe: es2-2026_2-equipe-1
- Projeto: ServiceHub
- Entrega: 4 - Riscos e Consolidação
- Data prevista considerada: 03/09/2026, conforme cronograma geral do projeto
- Pull request avaliado: PR #17, branch `entrega-4` para `main`
- Versão considerada: `origin/main` no commit `ec043fd`

## Documentos Consultados

- `README.md` da equipe.
- `USO-IA.md` da equipe.
- `.github/PULL_REQUEST_TEMPLATE.md`.
- `.github/ISSUE_TEMPLATE/task_template.md`.
- `.github/workflows/ci.yml`.
- `docs/inception.md`.
- `docs/dod.md`.
- `docs/riscos.md`.
- `docs/fluxo-de-trabalho.md`.
- `docs/ci.md`.
- `docs/qualidade.md`.
- `docs/BASELINE.md`.
- `docs/ESTIMATIVAS.md`.
- `docs/METRICAS.md`.
- `docs/metricas/M-01.md`.
- `docs/metricas/M-02.md`.
- `docs/metricas/M-03.md`.
- `docs/adrs/ADR-0001-stack-tecnologica.md`.
- `docs/adrs/ADR-0002-integracao-pagamentos-asaas.md`.
- Issues GitHub #7, #8, #9, #10, #13, #14, #15 e #18.
- Pull requests GitHub #16 e #17.
- Histórico Git local e remoto.

## Resumo da Entrega

A equipe entregou os artefatos principais da Entrega 4: registro de riscos, documentação do fluxo de trabalho, documentação do CI mínimo, critérios iniciais de qualidade, atualização do README, atualização do registro de uso de IA e workflow de CI no GitHub Actions. O MVP declarado do ServiceHub continua sendo uma plataforma web para conectar clientes e prestadores de serviços, com cadastro/login, publicação de solicitações, envio para profissionais disponíveis, aceite ou recusa pelo prestador e acompanhamento do status do serviço.

O registro de riscos lista seis riscos relevantes, com probabilidade, impacto, prioridade, mitigação e responsável. O documento de qualidade seleciona quatro atributos baseados na ISO/IEC 25010: manutenibilidade, confiabilidade, segurança e capacidade de interação. A entrega foi integrada à `main` pelo PR #17, dentro da data considerada pelo cronograma geral, com CI executado com sucesso e merge commit.

As principais perdas estão na ausência de aprovação formal registrada no PR #17, na impossibilidade de confirmar proteção de branch pela API, no CI ainda limitado a checks estruturais de arquivos e no nível relativamente superficial de algumas relações entre riscos, ações, métricas e critérios de qualidade.

## Critérios Atendidos

- O registro de riscos foi versionado em `docs/riscos.md`.
- Foram registrados seis riscos, contemplando prazo, escopo, tecnologia, qualidade/processo e comunicação/equipe.
- Cada risco possui causa, consequência, probabilidade, impacto, prioridade, mitigação e responsável.
- O documento de riscos define critério de classificação de probabilidade e impacto em escala de 1 a 3.
- A prioridade dos riscos é calculada por probabilidade multiplicada por impacto.
- O documento destaca os riscos mais críticos no momento: R01, R02 e R03.
- Há plano de resposta com ações preventivas e ações caso cada risco se concretize.
- Há indicação de acompanhamento periódico dos riscos a cada entrega ou sprint.
- O fluxo de trabalho foi documentado em `docs/fluxo-de-trabalho.md`.
- O fluxo define uso de branch de entrega, pull request para `main`, revisão por outro integrante, pelo menos uma aprovação, checks e merge.
- O template de pull request existe e possui checklist mínimo de revisão.
- Foi criado workflow de CI em `.github/workflows/ci.yml`.
- O PR #17 executou o check `check` com conclusão `SUCCESS`.
- Foram selecionados quatro atributos de qualidade prioritários: manutenibilidade, confiabilidade, segurança e capacidade de interação.
- O documento de qualidade referencia a ISO/IEC 25010.
- Há relação explícita entre riscos e atributos de qualidade em `docs/qualidade.md`.
- O README foi atualizado com links para riscos, fluxo de trabalho, CI, workflow, qualidade e template de PR.
- A entrega foi desenvolvida na branch `entrega-4` e integrada à `main` por merge commit.

## Critérios Parcialmente Atendidos

- O registro de riscos é adequado, mas a tabela usa um campo `Risco` mais próximo de título curto e não separa explicitamente uma descrição detalhada do risco.
- A análise dos riscos possui matriz/tabela de priorização e identifica os riscos críticos, mas as justificativas das prioridades são breves e concentradas apenas nos riscos altos.
- O plano de resposta cobre todos os riscos, mas as ações preventivas e contingenciais são genéricas em alguns casos, sem gatilhos, prazos ou evidências objetivas de acompanhamento.
- A consolidação do fluxo declara `main` protegida, mas a consulta via API retornou indisponibilidade do recurso para o repositório privado, impedindo confirmação externa da proteção.
- O CI mínimo existe e executou no PR #17, mas verifica apenas a existência de `README.md` e `.github/PULL_REQUEST_TEMPLATE.md`, sem validar os documentos específicos da Entrega 4, build, testes ou lint.
- Os atributos de qualidade são coerentes com o MVP, mas a indicação de como orientarão decisões futuras ainda é genérica.
- A avaliação futura da qualidade cita evidências e métricas, mas não associa cada atributo a métricas específicas já definidas em `docs/metricas/`.
- O PR #17 documenta a entrega e passou no CI, mas não possui aprovação formal via mecanismo de review do GitHub.

## Critérios Não Atendidos

- O PR #17 não possui review formal registrado; a consulta retornou `reviews: []`.
- O checklist do PR #17 manteve desmarcado o item “Pelo menos 1 integrante revisou e aprovou estas alterações”.
- O corpo do PR #17 manteve o placeholder `Closes # (insira o número da issue se tiver)`, sem vínculo claro com uma issue específica da Entrega 4.
- Não foi possível confirmar checks obrigatórios configurados na proteção da branch `main`; a API retornou mensagem de indisponibilidade do recurso para o repositório.

## Achados com Evidências

- Equipe e papéis registrados no README: `README.md`, linhas 5-10.
- Tema e proposta do ServiceHub descritos no README: `README.md`, linhas 12-40.
- MVP declarado no README: `README.md`, linhas 49-64.
- Política de branches e PRs descrita no README: `README.md`, linhas 78-84.
- Artefatos da Entrega 4 referenciados no README: `README.md`, linhas 107-114.
- Escopo detalhado do MVP em `docs/inception.md`: linhas 34-60.
- DoD com PR, revisão, CI, documentação e merge commit: `docs/dod.md`, linhas 13-34.
- Template de PR com campos de descrição, issue, tipo de alteração e checklist: `.github/PULL_REQUEST_TEMPLATE.md`, linhas 1-17.
- Template de issue existente: `.github/ISSUE_TEMPLATE/task_template.md`, linhas 1-15.
- Workflow de CI criado para pull requests para `main`: `.github/workflows/ci.yml`, linhas 1-22.
- CI verifica existência de `README.md` e `.github/PULL_REQUEST_TEMPLATE.md`: `.github/workflows/ci.yml`, linhas 16-22.
- Registro de riscos com critério de probabilidade, impacto e prioridade: `docs/riscos.md`, linhas 3-18.
- Tabela com seis riscos, probabilidade, impacto, prioridade, mitigação e responsável: `docs/riscos.md`, linhas 19-28.
- Riscos críticos destacados: `docs/riscos.md`, linhas 30-36.
- Plano de resposta aos riscos: `docs/riscos.md`, linhas 38-74.
- Acompanhamento dos riscos previsto a cada entrega ou sprint: `docs/riscos.md`, linhas 76-78.
- Fluxo de trabalho com branch `entrega-x`: `docs/fluxo-de-trabalho.md`, linhas 3-10.
- Fluxo de PR, revisão, aprovação, checks e merge: `docs/fluxo-de-trabalho.md`, linhas 11-23.
- Revisão e critérios do revisor documentados: `docs/fluxo-de-trabalho.md`, linhas 24-34.
- Integração apenas por PR e merge descrita: `docs/fluxo-de-trabalho.md`, linhas 36-52.
- Documento de CI descreve objetivo, ferramenta e funcionamento: `docs/ci.md`, linhas 1-29.
- Qualidade baseada na ISO/IEC 25010: `docs/qualidade.md`, linhas 1-8.
- Atributos de qualidade priorizados e formas futuras de avaliação: `docs/qualidade.md`, linhas 9-58.
- Relação entre riscos e atributos de qualidade: `docs/qualidade.md`, linhas 59-68.
- Avaliação futura da qualidade e evidências previstas: `docs/qualidade.md`, linhas 70-83.
- Registro de uso de IA da Entrega 4: `USO-IA.md`, linhas 47-55.
- PR #17 integrou `entrega-4` à `main`, estado `MERGED`, URL `https://github.com/IFSC-ES2/es2-2026_2-equipe-1/pull/17`.
- PR #17 foi criado em 03/09/2026 23:01:43 UTC e mesclado em 03/09/2026 23:19:04 UTC.
- PR #17 alterou `.github/workflows/ci.yml`, `README.md`, `USO-IA.md`, `docs/ci.md`, `docs/fluxo-de-trabalho.md`, `docs/qualidade.md` e `docs/riscos.md`.
- PR #17 executou o check `check` com conclusão `SUCCESS`.
- PR #17 não possui reviews formais registrados: `reviews: []`.
- PR #17 manteve o item de aprovação desmarcado no checklist do corpo do PR.
- Integração por merge commit: commit `ec043fd`, mensagem `Merge pull request #17 from IFSC-ES2/entrega-4`.
- Branch remota `origin/entrega-4` preservada no commit `9170ba7b0d30a769a3c86ebea8a7b50394bbeb2f`.
- Consulta à proteção da branch `main` retornou mensagem de indisponibilidade do recurso para o repositório privado.
- Consulta ao GitHub Project #32 não pôde ser realizada por falta do escopo `read:project` no token atual.

## Recomendações para a Equipe

- Garantir que todos os PRs de entrega tenham review formal registrado pelo mecanismo de aprovação do GitHub.
- Não deixar placeholders do template no corpo dos PRs; vincular cada PR à issue correspondente quando houver issue.
- Evoluir o CI para validar também os artefatos específicos da entrega, executar build, testes e lint assim que houver código funcional.
- Confirmar e manter a proteção da branch `main` com exigência de aprovação e checks obrigatórios antes do merge.
- Tornar o registro de riscos mais acionável, separando descrição, causa, impacto, gatilhos de monitoramento, plano preventivo e plano de contingência.
- Associar cada atributo de qualidade a métricas específicas, limiares mínimos e evidências observáveis nas próximas entregas.
- Atualizar o fluxo de trabalho para indicar explicitamente quem pode revisar PRs e como evitar autoaprovação.
- Manter riscos, qualidade e métricas como artefatos vivos, revisados a cada sprint.

## Nota da Entrega

Nota: 3,8 / 5,0

## Justificativa da Nota

- Registro inicial de riscos, análise, priorização e plano de resposta: 1,4 / 1,6. A equipe registrou seis riscos, critério de classificação, prioridade, responsáveis e respostas; a perda ocorre pela descrição curta dos riscos e ações ainda genéricas.
- Consolidação do fluxo de trabalho e CI mínimo: 0,9 / 1,2. O fluxo está documentado e há CI executando no PR, mas o CI é muito básico e a proteção da branch não pôde ser confirmada pela API.
- Critérios iniciais de qualidade e relação com riscos: 1,0 / 1,2. Há quatro atributos, referência à ISO/IEC 25010, justificativas e relação com riscos; a perda ocorre pela ausência de métricas/limiares específicos por atributo.
- Atualização do README e documentação da etapa: 0,5 / 0,5. O README aponta explicitamente os artefatos da Entrega 4.
- Evidências de processo no repositório: 0,0 / 0,5. Apesar de haver PR, CI e merge commit dentro do prazo considerado, o PR #17 não possui aprovação formal registrada, o que compromete um requisito mínimo de evidência da etapa.

## Observações sobre Uso de IA

A equipe declarou uso do ChatGPT em 03/09/2026 para apoio na estruturação da documentação de riscos, fluxo de trabalho, CI e critérios iniciais de qualidade, com impacto em `docs/riscos.md`, `docs/fluxo-de-trabalho.md`, `docs/ci.md`, `docs/qualidade.md` e `README.md`. A declaração é compatível com os artefatos observados no repositório, pois esses documentos foram criados ou atualizados na Entrega 4. O registro inclui validação humana, mas poderia detalhar melhor quais partes foram geradas, adaptadas ou apenas revisadas com apoio da ferramenta.
