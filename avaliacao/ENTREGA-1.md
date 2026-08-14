# Avaliação da Entrega 1 - Kickoff

## Identificação

- Equipe: es2-2026_2-equipe-1
- Projeto: ServiceHub
- Entrega: 1 - Kickoff
- Data limite considerada para avaliação: 07/08/2026
- Branch de avaliação: avaliacao-1

## Documentos Consultados

- `README.md` da equipe.
- `USO-IA.md` da equipe.
- `.github/ISSUE_TEMPLATE/task_template.md`.
- `.github/PULL_REQUEST_TEMPLATE/pull_request_template.md`.
- Issues GitHub #1 e #2.
- Pull request GitHub #3.
- Histórico Git local e remoto.

## Resumo da Entrega

A equipe entregou um README inicial para o projeto ServiceHub, uma proposta de plataforma web para conectar clientes e prestadores de serviços. O repositório contém registro inicial de uso de IA, template de issue, template de pull request, issues organizacionais e a branch `entrega-1` integrada em `main` por meio do PR #3.

A entrega apresenta os principais elementos esperados para o Kickoff, mas possui lacunas relevantes em completude da identificação da equipe, delimitação do MVP e governança mínima verificável.

## Critérios Atendidos

- O README identifica o nome do projeto e apresenta uma descrição geral do sistema.
- O README informa quatro integrantes e atribui papéis iniciais: Engenheira de Qualidade, Scrum Master, DevOps/Infra e Arquiteto de Software.
- O tema está associado a uma linha temática prevista: gestão de serviços e processos institucionais.
- O README apresenta uma seção de escopo inicial do MVP com funcionalidades e itens fora do escopo.
- O repositório contém arquivo `USO-IA.md` com registros de uso de ChatGPT e Gemini.
- Há template de issue em `.github/ISSUE_TEMPLATE/task_template.md` com seção de critérios de aceitação.
- Há template de pull request em `.github/PULL_REQUEST_TEMPLATE/pull_request_template.md`.
- Há evidências de uso de branch `entrega-1`, integrada a `main` por merge commit do PR #3.
- Há issues criadas e fechadas para organização inicial do kickoff: #1 e #2.
- Há milestone e labels criadas no repositório, conforme evidenciado pela API do GitHub.

## Critérios Parcialmente Atendidos

- A equipe está formada com quatro integrantes e papéis, mas o README não informa as matrículas, exigidas no item obrigatório 4.1 da Entrega 1.
- O tema está descrito e é compreensível, mas a contextualização não responde explicitamente a todas as perguntas obrigatórias, especialmente usuários, local/organização/contexto de aplicação e justificativa de relevância em formato verificável.
- O MVP está declarado, mas o escopo é amplo para uma primeira versão: inclui pagamento via Asaas, geolocalização em tempo real, chat em tempo real e avaliações. Isso contraria a orientação de MVP com poucas funcionalidades principais bem implementadas.
- A governança mínima possui templates, issues, branch de entrega e PR, mas não há evidência de checks obrigatórios configurados ou planejados de forma concreta.
- O PR #3 foi mesclado, mas a API do GitHub indica `reviews: []`, sem evidência de pelo menos uma aprovação em pull request.
- As issues possuem critérios de aceitação, mas a issue #1 foi fechada com o item de governança mínima ainda desmarcado no corpo da issue.
- O registro de IA declara ferramentas, finalidade e artefato, mas não registra explicitamente a validação realizada pela equipe, exigida pelo protocolo de uso de IA.

## Critérios Não Atendidos

- A entrega não apresenta matrículas dos integrantes no README.
- Não há evidência de board utilizado para acompanhamento, embora o repositório esteja com Projects habilitado.
- Não há evidência de checks obrigatórios nos pull requests; o PR #3 apresenta `statusCheckRollup: []`.
- Não há evidência de aprovação no PR #3; o PR foi mesclado sem reviews registrados.

## Achados com Evidências

- README sem matrículas: `README.md`, linhas 5-10, lista nomes e papéis, mas não informa matrículas.
- Tema e proposta descritos: `README.md`, linhas 12-18.
- Linha temática informada: `README.md`, linhas 20-24.
- MVP amplo demais para kickoff: `README.md`, linhas 30-40, inclui pagamento, geolocalização em tempo real, chat em tempo real e avaliações.
- Fora do escopo definido: `README.md`, linhas 42-48.
- Registro de IA incompleto quanto à validação: `USO-IA.md`, linhas 5-23, registra ferramenta, uso e artefato, mas não explicita validação humana.
- Template de issue com critérios de aceitação: `.github/ISSUE_TEMPLATE/task_template.md`, linhas 6-15.
- Template de PR com checklist e revisão: `.github/PULL_REQUEST_TEMPLATE/pull_request_template.md`, linhas 13-17.
- PR de entrega existente e mesclado: PR #3, branch `entrega-1` para `main`, estado `MERGED`, URL `https://github.com/IFSC-ES2/es2-2026_2-equipe-1/pull/3`.
- PR sem revisão registrada: consulta do PR #3 retornou `reviews: []`.
- PR sem checks registrados: consulta do PR #3 retornou `statusCheckRollup: []`.
- Branch de entrega preservada no remoto: `origin/entrega-1` aponta para o commit `4fdde6b`.
- Integração por merge commit: commit `6818780`, mensagem `Merge pull request #3 from IFSC-ES2/entrega-1`.
- Issues de kickoff existentes: issue #1 `Criação do README.md` e issue #2 `Criação de Labels + Milestone para organização`.
- Issue #1 fechada com governança mínima ainda pendente no corpo: critério `Governança mínima` aparece desmarcado na issue #1.

## Recomendações para a Equipe

- Incluir matrículas dos integrantes no README nas próximas entregas.
- Reestruturar a descrição do tema para responder explicitamente às perguntas obrigatórias do enunciado.
- Reduzir o MVP para poucas funcionalidades centrais, deixando itens complexos como geolocalização em tempo real, chat em tempo real e integração de pagamento para etapas futuras, salvo justificativa forte de viabilidade.
- Registrar de forma objetiva a política de branches e checks obrigatórios no README ou em documento de governança.
- Configurar proteção de branch ou regras equivalentes para exigir revisão e checks antes do merge, quando o plano da equipe permitir.
- Garantir que os próximos PRs tenham ao menos uma aprovação registrada antes do merge.
- Criar ou evidenciar o board de acompanhamento e vincular issues a ele.
- Completar o `USO-IA.md` com a validação realizada pela equipe para cada uso de IA.
- Evitar fechar issues com critérios de aceitação ainda pendentes ou justificar claramente o motivo.

## Nota da Entrega

Nota: 3,6 / 5,0

## Justificativa da Nota

- Equipe formada dentro do prazo: 0,8 / 1,0. A equipe possui quatro integrantes e papéis definidos, mas não informa matrículas. Para esta entrega, foi considerada a extensão de prazo até 07/08/2026.
- Tema definido de forma clara, contextualizada, relevante e em conformidade com as restrições: 1,4 / 2,0. O tema é relevante e aderente à linha temática, mas a contextualização não cobre explicitamente todas as perguntas obrigatórias e o MVP proposto está amplo demais para a restrição de poucas funcionalidades principais.
- README inicial preenchido, incluindo escopo do MVP e informações básicas: 0,8 / 1,0. O README existe e contém informações essenciais, mas falta maior delimitação do MVP e alguns dados obrigatórios da equipe.
- Governança mínima do repositório: 0,6 / 1,0. Há branch de entrega, PR, issues, milestone, labels e templates, mas faltam evidências de review aprovado, checks obrigatórios e board efetivamente utilizado. Para esta entrega, a pasta `docs/` interna não foi considerada obrigatória.

## Observações sobre Uso de IA

A equipe declarou uso de ChatGPT para sugestão de nome e organização/formatação do README, e Gemini para apoio na criação de templates. A declaração é compatível com os artefatos entregues, pois o repositório contém `README.md` estruturado e templates em `.github/`. A declaração é positiva por atender ao princípio de transparência, mas ainda está incompleta em relação ao protocolo porque não informa a validação realizada pela equipe para cada artefato impactado.
