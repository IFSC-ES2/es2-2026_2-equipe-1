# Avaliação da Entrega 2 - Inception

## Identificação

- Equipe: es2-2026_2-equipe-1
- Projeto: ServiceHub
- Entrega: 2 - Inception
- Data limite considerada para avaliação: 15/08/2026
- Pull request avaliado: PR #6, branch `entrega-2` para `main`
- Versão considerada: estado atual de `origin/main` após atualização do repositório

## Documentos Consultados

- `README.md` da equipe.
- `USO-IA.md` da equipe.
- `docs/inception.md`.
- `docs/dod.md`.
- `docs/adrs/ADR-0001-stack-tecnologica.md`.
- `docs/adrs/ADR-0002-integracao-pagamentos-asaas.md`.
- `.github/ISSUE_TEMPLATE/task_template.md`.
- `.github/PULL_REQUEST_TEMPLATE.md`.
- Issues GitHub #4, #7, #8, #9 e #10.
- Pull requests GitHub #6 e #12.
- Histórico Git local e remoto.

## Resumo da Entrega

A equipe entregou os principais artefatos esperados para a Inception: documento de visão do produto e definição de MVP em `docs/inception.md`, DoD em `docs/dod.md`, duas ADRs iniciais, README atualizado com referências para os artefatos da etapa e backlog em issues. A versão atual do repositório também corrige informações de equipe e completa a validação do registro de uso de IA.

A entrega apresenta boa evolução em relação ao Kickoff, com documentação estruturada, melhor contextualização do produto, backlog com critérios de aceitação, board configurado e ADRs iniciais. As principais lacunas permanecem na ausência de aprovação formal e checks registrados no PR #6, na ausência de proteção da branch `main`, na falta de prioridade relativa explícita nas issues do backlog e na inconsistência atual entre o MVP do README, que não lista pagamento, e a Inception/ADR, que mantêm Asaas como parte do MVP.

## Critérios Atendidos

- A visão do produto está documentada em `docs/inception.md`, cobrindo problema, área de aplicação, usuários, contexto, proposta de valor, objetivos do semestre e premissas/restrições.
- O tema permanece coerente com a linha temática de gestão de serviços e processos institucionais.
- O README atual informa os quatro integrantes, matrículas e papéis, incluindo Scrum Master, DevOps/Infra, Arquiteto de Software e Engenheira de Qualidade.
- O MVP foi definido com objetivo, funcionalidades essenciais, itens fora do escopo, justificativa de viabilidade e critérios de decisão.
- O escopo atual do README remove itens complexos que estavam no Kickoff, como geolocalização em tempo real, chat, avaliações, cupons e aplicativo mobile.
- Foram criadas issues de backlog para funcionalidades centrais: cadastro/autenticação, criação de solicitação, aceite/recusa e conclusão/pagamento.
- As issues de backlog possuem descrições em formato de história de usuário e critérios de aceitação verificáveis.
- A equipe registrou uma Definition of Done em `docs/dod.md` com critérios para fluxo Git, revisão, critérios de aceitação, documentação, testes, pipeline e integração.
- Foram registradas ADRs iniciais sobre stack tecnológica e gateway de pagamentos.
- O README foi atualizado com referências para Inception, DoD, ADRs, uso de IA e board/backlog.
- A entrega foi desenvolvida na branch `entrega-2` e integrada à `main` por merge commit do PR #6.
- A entrega foi integrada em 14/08/2026 01:31 UTC, dentro do prazo autorizado para a Entrega 2.

## Critérios Parcialmente Atendidos

- A visão do produto é clara, mas os interessados aparecem principalmente como clientes e prestadores; outros interessados, como administradores da plataforma, são citados no README, mas não são detalhados no documento de Inception.
- O MVP está mais enxuto no README atual, mas há inconsistência entre artefatos: o README não lista pagamento no escopo do MVP, enquanto `docs/inception.md` e a ADR-0002 mantêm o processamento de pagamento com Asaas como parte do MVP.
- O backlog contém issues com critérios de aceitação e está vinculado ao GitHub Project #32, mas não apresenta prioridade relativa explícita por labels, milestone específica, campo de prioridade ou ordenação documentada.
- O GitHub Project #32 contém a issue organizadora da Entrega 2 em `Done` e as histórias #7 a #10 em `Todo`, confirmando o vínculo do backlog ao board.
- A DoD é objetiva, mas inclui build, testes automatizados e pipeline como critérios de pronto sem haver checks executados no PR da entrega, e o repositório não possui regras de proteção da branch `main`.
- As ADRs têm contexto, decisão, alternativas e consequências, mas ambas referenciam o PR #5, que corresponde à avaliação da Entrega 1, não ao PR #6 da Entrega 2.
- O registro de IA atual informa validação para o uso da Entrega 2, mas esse complemento foi integrado posteriormente no PR #12, de recuperação da Entrega 1, e não no PR #6 da própria entrega.

## Critérios Não Atendidos

- Não há aprovação formal registrada no PR #6 via mecanismo de review do GitHub; a consulta retornou `reviews: []`.
- Não há checks obrigatórios executados ou registrados no PR #6; a consulta retornou `statusCheckRollup: []`.
- Não há regras de proteção da branch `main`.

## Achados com Evidências

- README atual com integrantes, matrículas e papéis: `README.md`, linhas 5-10.
- Visão do produto documentada: `docs/inception.md`, linhas 3-30.
- Problema e contexto regional definidos: `docs/inception.md`, linhas 5-16.
- Proposta de valor e objetivos do semestre definidos: `docs/inception.md`, linhas 18-25.
- Premissas, restrições e limitações conhecidas: `docs/inception.md`, linhas 27-30.
- MVP em `docs/inception.md` com objetivo e funcionalidades essenciais: `docs/inception.md`, linhas 34-45.
- Itens fora do escopo, viabilidade e critérios de decisão do MVP: `docs/inception.md`, linhas 47-60.
- MVP no README atual sem pagamento no escopo: `README.md`, linhas 49-63.
- Inconsistência de escopo: `docs/inception.md`, linhas 36-45, inclui pagamento via Asaas no MVP; `README.md`, linhas 51-57, não inclui pagamento no MVP atual.
- README atualizado com links para artefatos da Inception e board: `README.md`, linhas 86-94.
- DoD registrada: `docs/dod.md`, linhas 1-34.
- DoD exige review, critérios de aceitação, build, testes e pipeline: `docs/dod.md`, linhas 18-28.
- ADR de stack tecnológica registrada com contexto, decisão, alternativas e consequências: `docs/adrs/ADR-0001-stack-tecnologica.md`, linhas 7-36.
- ADR de gateway de pagamentos registrada com contexto, decisão, alternativas e consequências: `docs/adrs/ADR-0002-integracao-pagamentos-asaas.md`, linhas 7-33.
- ADR-0002 mantém Asaas como requisito diretamente relacionado ao MVP: `docs/adrs/ADR-0002-integracao-pagamentos-asaas.md`, linhas 35-37.
- ADRs com referência incorreta ao PR #5: `docs/adrs/ADR-0001-stack-tecnologica.md`, linhas 42-46; `docs/adrs/ADR-0002-integracao-pagamentos-asaas.md`, linhas 39-43.
- Issue organizadora da Entrega 2: issue #4, com checklist para visão do produto, MVP, DoD, ADRs, backlog e atualização do README.
- Backlog inicial em issues: #7 `Cadastro e autenticação de usuários`, #8 `Criação e publicação de solicitação de serviço`, #9 `Aceite ou recusa de solicitação pelo prestador` e #10 `Concluir serviço e realizar pagamento via Asaas`.
- Board verificado no GitHub Project #32: issues #7, #8, #9 e #10 constam no status `Todo`; issue #4 consta em `Done`.
- Issues #7, #8, #9 e #10 possuem critérios de aceitação observáveis no formato Dado/Quando/Então.
- Issues #7, #8, #9 e #10 não possuem labels ou milestone associados na consulta da API, reduzindo a evidência de prioridade relativa.
- PR de entrega existente e mesclado: PR #6, branch `entrega-2` para `main`, estado `MERGED`, URL `https://github.com/IFSC-ES2/es2-2026_2-equipe-1/pull/6`.
- PR #6 sem review formal registrado: consulta do PR retornou `reviews: []`.
- PR #6 sem checks registrados: consulta do PR retornou `statusCheckRollup: []`.
- Integração por merge commit: commit `f40b217`, mensagem `Merge pull request #6 from IFSC-ES2/entrega-2`.
- O primeiro-parent de `origin/main` preserva a integração da Entrega 2 por merge commit, seguida posteriormente pela recuperação da Entrega 1.
- PR #12, de recuperação da Entrega 1, alterou `README.md` e `USO-IA.md`, impactando o estado atual usado nesta revisão.
- Registro de IA atual da Entrega 2: `USO-IA.md`, linhas 27-35, declara uso do Gemini para documentação da Entrega 2, com artefatos `.docs` e `README.md`, incluindo validação.

## Recomendações para a Equipe

- Corrigir a inconsistência entre README, Inception e ADR sobre o pagamento via Asaas, definindo claramente se ele faz ou não parte do MVP atual.
- Tornar a prioridade relativa do backlog explícita nas issues, por exemplo com labels, milestone da entrega, campo de prioridade ou ordenação documentada no board.
- Garantir que os próximos PRs tenham aprovação formal registrada pelo mecanismo de review do GitHub, não apenas checklist marcado no corpo do PR.
- Configurar checks obrigatórios ou, enquanto ainda não houver pipeline, documentar claramente quais validações manuais substituem temporariamente os checks automatizados.
- Ajustar a DoD para diferenciar critérios já aplicáveis nesta etapa de critérios planejados para etapas futuras, evitando exigir pipeline/testes quando eles ainda não existem.
- Corrigir as referências das ADRs para apontarem para o PR correto da Entrega 2 e manter a rastreabilidade entre decisão, issue e pull request.
- Detalhar melhor os interessados do produto no documento de Inception, incluindo papéis administrativos ou operacionais que precisarão gerenciar a plataforma.
- Manter o registro de uso de IA completo já no PR da entrega correspondente, incluindo validação humana para cada uso declarado.
- Configurar proteção de branch ou regras equivalentes para exigir revisão e checks antes do merge.

## Nota da Entrega

Nota: 4,0 / 5,0

## Justificativa da Nota

- Visão do produto clara, contextualizada e coerente com o tema aprovado: 0,9 / 1,0. A visão cobre os pontos exigidos e melhora a contextualização, com pequena perda por detalhar pouco interessados além de clientes e prestadores no documento de Inception.
- MVP definido com escopo viável, funcionalidades essenciais e itens fora do escopo: 0,8 / 1,0. O recorte está mais claro e o README atual remove itens complexos, mas há inconsistência entre README, Inception e ADR sobre o pagamento via Asaas.
- Backlog inicial priorizado, registrado em issues/board e com critérios de aceitação: 0,8 / 1,0. Há issues vinculadas ao board com critérios de aceitação verificáveis, mas a prioridade relativa não está explícita.
- DoD objetiva e compatível com o fluxo de trabalho da equipe: 0,7 / 1,0. A DoD é objetiva e cobre práticas relevantes, mas exige pipeline, testes e checks que ainda não aparecem executados no PR da entrega, e não há regras de proteção na branch `main`.
- ADRs iniciais registradas e README atualizado com referências para os artefatos da etapa: 0,8 / 1,0. Há duas ADRs completas e README com referências, mas as ADRs têm referência incorreta ao PR #5 e há inconsistência de escopo envolvendo Asaas.

## Observações sobre Uso de IA

A equipe declarou uso do Gemini para apoiar a criação das documentações necessárias da Entrega 2, com impacto em `.docs` e `README.md`, e informou validação por verificação do formato exigido. A declaração é compatível com os artefatos entregues, pois a entrega inclui documentação em `docs/` e atualização do README. A ressalva é que a validação do uso da Entrega 2 aparece no estado atual após alteração posterior em PR de recuperação, não na versão originalmente integrada pelo PR #6.
