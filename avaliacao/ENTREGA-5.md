# Avaliação da Entrega 5 - Sprint 1

## Identificação

- Equipe: es2-2026_2-equipe-1
- Projeto: ServiceHub
- Entrega: 5 - Sprint 1
- Repositório confirmado pela configuração: `IFSC-ES2/es2-2026_2-equipe-1`
- Pull requests avaliados: PR #21, de `front-end` para `entrega-5`; PRs #22 e #23, de `entrega-5` para `main`
- Versão considerada: commit `086ad63`, merge do PR #23 em 18/09/2026 às 00:08:40 (UTC-3)
- Incremento principal: commit `a1693be`, merge do PR #22 em 18/09/2026 às 00:02:30 (UTC-3)
- Release esperada: `v0.1.0`

## Documentos Consultados

- `README.md`.
- `ServiceHub/README.md`.
- `USO-IA.md`.
- `.github/PULL_REQUEST_TEMPLATE.md`.
- `.github/ISSUE_TEMPLATE/task_template.md`.
- `.github/workflows/ci.yml`.
- `docs/sprint-1-planejamento.md`.
- `docs/inception.md`.
- `docs/dod.md`.
- `docs/BASELINE.md`.
- `docs/ESTIMATIVAS.md`.
- `docs/METRICAS.md`.
- `docs/metricas/M-01.md`.
- `docs/metricas/M-02.md`.
- `docs/metricas/M-03.md`.
- `docs/ci.md`.
- `docs/fluxo-de-trabalho.md`.
- `docs/qualidade.md`.
- `docs/riscos.md`.
- `docs/adrs/ADR-0001-stack-tecnologica.md`.
- `docs/adrs/ADR-0002-integracao-pagamentos-asaas.md`.
- `ServiceHub/package.json`.
- `back-end/build.gradle`.
- Código-fonte e testes em `ServiceHub/` e `back-end/`.
- Issues GitHub #8, #13 e #18.
- Pull requests GitHub #21, #22 e #23.
- Histórico de commits, checks, tag/release e regras do repositório no GitHub.

## Resumo da Entrega

O MVP declarado no README abrange cadastro e login de clientes e prestadores, publicação de solicitações, envio a profissionais disponíveis, aceite ou recusa e acompanhamento do status do serviço. Para a Sprint 1, a equipe registrou o replanejamento de cadastro/autenticação para a criação e publicação de solicitações, por considerar esse fluxo mais representativo do valor central do ServiceHub.

O incremento entregue constitui um vertical slice parcial, mas concreto: o front-end permite cadastrar endereço e publicar solicitação; a API Spring Boot recebe os dados; os serviços atribuem estado e data; e as entidades são persistidas por JPA em H2. Há testes unitários versionados para serviços do back-end e participação observável dos quatro integrantes.

As principais lacunas são a ausência de coleta das métricas da Sprint 1, de fechamento rastreável do planejado versus executado, de registro final das contribuições, de review e aprovação formal nos PRs, de checks obrigatórios capazes de executar build e testes, de README operacional e da release `v0.1.0`. A infraestrutura planejada com Docker e PostgreSQL também não foi concluída no snapshot.

## Critérios Atendidos

- O planejamento registra participantes, meta, itens #8 e #13, capacidade, justificativa do recorte e Definition of Done: `docs/sprint-1-planejamento.md`, linhas 1-48 no snapshot.
- O replanejamento do item #7 para o item #8 foi justificado pela centralidade da publicação de solicitações no MVP: `docs/sprint-1-planejamento.md`, linhas 23-32.
- Há um fluxo funcional entre interface, API, serviço e persistência para cadastro de endereço e publicação de solicitação.
- O formulário de solicitação contempla título, valor, descrição, especialidades e endereço: `ServiceHub/src/componentes/modal/ModalCriarSolicitacao.tsx`, linhas 46-81 e 92-204.
- A integração HTTP para endereços e solicitações está em `ServiceHub/src/servicos/api.ts`, linhas 54-92.
- O back-end possui endpoints, serviços, repositórios e entidades JPA para o incremento.
- `SolicitacaoServicoService` atribui o status inicial `SOLICITADO` e a data de criação quando ausentes: linhas 28-35.
- Há testes unitários automatizados e versionados para `SolicitacaoServicoService` e `UsuarioService`.
- A branch `entrega-5` foi integrada à `main` por merge commits nos PRs #22 e #23.
- Os PRs #22 e #23 executaram o check `CI/check` com sucesso.
- Os quatro integrantes possuem contribuições observáveis no histórico da Entrega 5.
- Os registros de uso de IA possuem correspondência geral com os artefatos entregues.

## Critérios Parcialmente Atendidos

- O incremento atravessa interface, lógica e persistência, mas a demonstrabilidade por uma pessoa externa é prejudicada pela ausência de instruções no snapshot e pelo `gradlew` sem permissão de execução.
- A persistência usa H2 em memória, embora o planejamento e o ADR da stack indiquem Docker e PostgreSQL.
- O painel do prestador lista solicitações reais, mas o aceite apenas altera o estado local da interface e não é persistido. O envio a profissionais e o acompanhamento de estados ainda são simplificados.
- Os testes cobrem operações CRUD, mas não verificam explicitamente o status e a data atribuídos na criação, não cobrem `EnderecoService` e não são executados pelo CI.
- A issue #8 foi vinculada e fechada pelo PR #22, porém a issue #13 de infraestrutura permaneceu aberta.
- O planejamento apresenta a divisão inicial das tarefas, mas não há fechamento da Sprint 1 com contribuições efetivamente realizadas e links por integrante.
- Há evidências de uso do board na movimentação da issue #8, mas o quadro completo não pôde ser consultado porque o token atual não possui o escopo `read:project`.
- A proteção atual da `main` foi confirmada pela API GraphQL: dois rulesets ativos exigem pull request e bloqueiam exclusão, criação e atualização não fast-forward. Não foi observada regra de `REQUIRED_STATUS_CHECKS`, e a API disponível não informa quando esses rulesets foram ativados.
- O `USO-IA.md` referencia artefatos existentes, mas a extensão do apoio e a validação humana não estão registradas com precisão uniforme.

## Critérios Não Atendidos

- As métricas M-01, M-02 e M-03 não foram coletadas ao final da Sprint 1; as fichas mantêm apenas o baseline da Entrega 3.
- Não há análise final do planejado versus executado nem classificação dos itens concluídos, parciais e replanejados.
- Não há registro final e rastreável das contribuições individuais no snapshot.
- Os PRs #21, #22 e #23 não possuem review nem aprovação formal: a API retornou `reviews: []` para todos.
- O PR #22 foi mesclado 28 segundos após a abertura e o PR #23, 18 segundos após a abertura, sem evidência formal de revisão.
- O workflow de CI verifica somente a existência de `README.md` e `.github/PULL_REQUEST_TEMPLATE.md`; não executa testes, build, lint ou cobertura.
- Não foi comprovado que o check executado nos PRs era obrigatório.
- O README do snapshot termina nos artefatos da Entrega 4 e não informa o estado da Sprint 1 nem como executar a aplicação e os testes.
- `ServiceHub/README.md` permanece como o texto padrão do Vite.
- Não havia tag nem release `v0.1.0` no snapshot. A tag posterior `0.1.0`, sem o prefixo `v`, aponta para outro commit e a release foi publicada apenas em 01/10/2026.

## Achados com Evidências

- **Planejamento sem fechamento:** `docs/sprint-1-planejamento.md` registra adequadamente o início da sprint, mas não foi atualizado com os resultados. A issue #18 foi fechada mesmo mantendo infraestrutura, métricas e release como pendentes. Impacto: o escopo final não é verificável apenas pela documentação. Recomendação: encerrar cada sprint com planejado versus executado, impedimentos e replanejamento.
- **Vertical slice real, mas limitado:** os modais do front-end, `api.ts`, os controllers, os services e as entidades implementam cadastro de endereço e publicação de solicitação. Entretanto, os dados ficam em H2 em memória e não há associação da solicitação ao cliente. Impacto: há valor demonstrável, mas com limitações importantes de uso e persistência. Recomendação: alinhar a persistência à arquitetura decidida e modelar o solicitante.
- **Regras centrais pouco testadas:** `SolicitacaoServicoServiceTest.java` testa CRUD, mas o teste de criação já prepara o status e não verifica a atribuição automática de status/data. Não há teste para endereço. Impacto: regressões no comportamento principal podem passar despercebidas. Recomendação: testar as regras adicionadas na Sprint 1 e integrar a suíte ao CI.
- **CI nominal:** os runs dos PRs #22 e #23 ficaram verdes, mas `.github/workflows/ci.yml`, linhas 16-22, executa apenas `test -f` para dois arquivos. Impacto: o check não comprova compilação ou funcionamento. Recomendação: executar Gradle, testes e build do front-end.
- **Métricas ausentes:** `docs/metricas/M-01.md`, `M-02.md` e `M-03.md` não contêm coleta da Sprint 1. Impacto: não há base objetiva para analisar qualidade, lead time ou progresso do MVP. Recomendação: registrar valores observados, metas e análise das diferenças.
- **Fluxo sem revisão:** PRs #21, #22 e #23 não têm reviews formais. O checklist de aprovação do PR #22 permaneceu desmarcado, e o PR #23 marcou aprovação apesar de a API não registrar review. Impacto: descumpre o fluxo e a Definition of Done definidos pela própria equipe. Recomendação: exigir aprovação formal antes do merge.
- **Proteção atual confirmada, sem checks obrigatórios:** a API GraphQL retornou dois rulesets ativos sobre `~DEFAULT_BRANCH`, ambos com regra de pull request, mas sem `REQUIRED_STATUS_CHECKS`. Impacto: atualmente commits diretos estão restringidos, porém um PR pode ser integrado sem que build e testes sejam requisitos observáveis. Recomendação: exigir aprovação e checks técnicos nos rulesets.
- **Documentação operacional ausente:** o README não contém o estado da Entrega 5 nem comandos de execução/teste no snapshot. Impacto: reduz a demonstrabilidade e a reprodutibilidade. Recomendação: atualizar o README dentro do marco avaliado.
- **Release ausente no marco:** a release posterior usa `0.1.0`, foi publicada em 01/10/2026 e não identifica o snapshot `086ad63`. Impacto: não atende ao requisito da Entrega 5. Recomendação: publicar a tag e a release previstas no fechamento da sprint.

## Análise das Contribuições Individuais

A equipe informou em `docs/sprint-1-planejamento.md`, linhas 34-38, a divisão planejada das responsabilidades. Essa declaração foi confrontada com os commits sem merge no intervalo `ec043fd..086ad63`, com issues e com os PRs. Não há integrante sem contribuição observável, mas nenhum realizou review formal registrado.

- **Ana Clara Teixeira Ronzani:** a equipe informou responsabilidade por documentação, correções e avaliação da qualidade/testes. Os commits `887ce7a`, `371b8ad`, `1496dbf` e `6ee8012` comprovam planejamento, criação das suítes de testes e atualização do registro de IA; Ana também foi responsável formal pela issue #18. A contribuição é substancial e coerente com o papel declarado. O commit `24fcc5c` incluiu artefatos gerados junto de uma correção documental, removidos posteriormente, o que indica necessidade de maior cuidado na revisão.
- **Davi Israel Quirino:** a equipe informou responsabilidade pelo front-end. Os commits `7047cb9`, `fb08baa`, `2d67495`, `7666187` e `babaa0a` comprovam estrutura React/Vite, Home, rotas, seleção de usuário, componentes visuais e fluxo de criação de solicitação. Davi também mesclou os PRs #21, #22 e #23. A contribuição é forte e compatível com o informado, embora os merges sem review formal enfraqueçam a evidência de controle do processo.
- **João Vitor Schmitt:** a equipe informou responsabilidade pela API e persistência da solicitação. Os commits `3dceda3` e `eb9a838` comprovam entidade/serviços iniciais e configuração H2. O commit `1c4e2d5`, de `joao.schmitt <joao.schmitt@sejaouze.com.br>`, implementa endpoints de solicitação e é compatível com o nome, com a issue #8 e com a responsabilidade declarada, mas o alias não está tecnicamente associado à conta acadêmica pela API. A contribuição é menor em quantidade, porém tecnicamente relevante e coerente com o planejamento.
- **Leonardo Victor Muller De Morais:** a equipe informou responsabilidade por arquitetura, UML e suporte a testes. Os commits `c5ba930`, `b3ea1bb`, `1d0b969` e `b6a608b` comprovam UML, páginas e integração front-end/back-end para endereços, solicitações, especialidades e painel do prestador. Leonardo abriu os PRs #21, #22 e #23. A contribuição é forte e supera o escopo inicialmente declarado, concentrando parte significativa da integração final.

## Notas Individuais

As notas abaixo avaliam a contribuição individual observável e são independentes da nota coletiva da entrega. Foram usados os mesmos critérios para todos os integrantes:

| Dimensão | Máximo |
|---|---:|
| Aderência à responsabilidade declarada | 2,5 |
| Relevância e resultado da contribuição | 3,5 |
| Rastreabilidade das evidências | 2,5 |
| Colaboração e processo GitHub | 1,5 |
| **Total** | **10,0** |

| Integrante | Aderência | Relevância | Rastreabilidade | Colaboração | Nota individual |
|---|---:|---:|---:|---:|---:|
| Ana Clara Teixeira Ronzani | 2,4 | 3,0 | 2,1 | 0,4 | **7,9** |
| Davi Israel Quirino | 2,5 | 3,3 | 2,3 | 0,5 | **8,6** |
| João Vitor Schmitt | 2,3 | 2,9 | 1,8 | 0,3 | **7,3** |
| Leonardo Victor Muller De Morais | 2,3 | 3,4 | 2,4 | 0,5 | **8,6** |

- **Ana — 7,9:** a nota decorre da forte aderência ao papel de qualidade, dos testes, do planejamento e da documentação rastreável. As perdas decorrem da inclusão temporária de artefatos gerados no commit `24fcc5c` e da ausência de review formal.
- **Davi — 8,6:** a nota reflete impacto direto no front-end e no fluxo de criação de solicitações, com autoria bem rastreável. As perdas decorrem de commits grandes, inclusão temporária de arquivo volumoso e merges sem review/aprovação formal.
- **João — 7,3:** a nota reconhece a relevância da API, persistência e configuração H2. A rastreabilidade é menor porque o commit central `1c4e2d5` usa um alias não associado tecnicamente à conta acadêmica, além de não haver PR ou review próprio.
- **Leonardo — 8,6:** a nota reflete contribuição ampla em UML, páginas e integração do fluxo principal, além da autoria dos PRs. As perdas decorrem de mensagens de commit vagas, concentração da integração no fim da sprint e PRs sem revisão formal.

## Notas Compostas por Integrante

A nota composta combina 30% da nota coletiva da entrega e 70% da nota individual de contribuição:

`nota composta = (nota coletiva × 0,3) + (nota individual × 0,7)`

Foi utilizada a nota coletiva de 4,8. Os resultados foram arredondados para uma casa decimal.

| Integrante | Nota coletiva | Nota individual | Cálculo | Nota composta |
|---|---:|---:|---:|---:|
| Ana Clara Teixeira Ronzani | 4,8 | 7,9 | `(4,8 × 0,3) + (7,9 × 0,7) = 6,97` | **7,0** |
| Davi Israel Quirino | 4,8 | 8,6 | `(4,8 × 0,3) + (8,6 × 0,7) = 7,46` | **7,5** |
| João Vitor Schmitt | 4,8 | 7,3 | `(4,8 × 0,3) + (7,3 × 0,7) = 6,55` | **6,6** |
| Leonardo Victor Muller De Morais | 4,8 | 8,6 | `(4,8 × 0,3) + (8,6 × 0,7) = 7,46` | **7,5** |

## Recomendações para a Equipe

- Registrar, ao final de cada sprint, as contribuições realizadas por integrante com links para issues, commits, PRs, revisões e documentos.
- Atualizar o README dentro do marco com estado do MVP, limitações e instruções reproduzíveis de execução e testes.
- Coletar as métricas definidas, comparar resultado e meta e registrar a análise do planejado versus executado.
- Exigir review e aprovação formal antes de mesclar PRs.
- Configurar checks obrigatórios que executem testes do back-end e build/lint do front-end.
- Publicar a tag e a release com o nome e no momento exigidos.
- Alinhar Docker/PostgreSQL ao planejamento ou justificar e documentar formalmente o uso temporário de H2.
- Testar status/data padrão e o cadastro de endereços, que são comportamentos centrais do incremento.
- Evitar versionar artefatos gerados e manter commits menores e diretamente vinculados às issues.
- Tornar os registros de IA precisos quanto a ferramenta, extensão do apoio, caminhos dos artefatos e validação concluída.

## Nota da Entrega

| Critério | Máximo | Nota |
|---|---:|---:|
| Planejamento inicial da Sprint 1 | 1,0 | 0,8 |
| Incremento funcional como vertical slice | 2,0 | 1,3 |
| Testes de unidade automatizados | 1,5 | 0,8 |
| Escopo, backlog, board, métricas e contribuições | 1,5 | 0,6 |
| Fluxo de trabalho | 2,0 | 0,9 |
| README, documentação, uso de IA e release | 2,0 | 0,4 |
| **Total** | **10,0** | **4,8** |

## Justificativa da Nota

A equipe planejou e implementou um incremento técnico real, alinhado ao núcleo do MVP e distribuído entre os quatro integrantes. O slice atravessa front-end, API, lógica e persistência, e há testes unitários versionados.

A nota é reduzida pelas lacunas em quase todos os mecanismos de verificação e fechamento da sprint: testes que não cobrem plenamente as regras centrais e não foram executados pelo CI; métricas ausentes; infraestrutura incompleta; contribuições não consolidadas pela equipe; PRs sem review/aprovação; checks não obrigatórios e apenas estruturais; README sem execução/testes; e release normativa ausente. Essas lacunas impedem considerar o incremento plenamente reproduzível, testável e rastreável.

## Observações sobre Uso de IA

No período da Entrega 5, o `USO-IA.md` declara Gemini e Illustrator no front-end e nas imagens, DeepSeek no aperfeiçoamento dos testes e GitHub Copilot/Gemini na integração entre front-end e back-end.

- O registro 7 é compatível com `ServiceHub/src/paginas/SelecaoUser/index.tsx`, `ImagemFlexivel.tsx`, `ImagemFundo.tsx` e as imagens em `ServiceHub/src/assets/logo/`, mas descreve os artefatos genericamente e mantém a validação do código no futuro.
- O registro 8 cita testes que existem e foram introduzidos no commit `371b8ad`, porém a validação menciona incorretamente os requisitos da Entrega 4 e não registra execução ou cobertura dos testes da Entrega 5.
- O registro 9 é compatível com os commits `1d0b969` e `b6a608b` e com os artefatos de solicitação, endereço e especialidades. A declaração de execução dos testes e do build não possui evidência preservada no CI, e o registro não identifica precisamente os caminhos dos artefatos.

Conclusão: o uso declarado de IA é **parcialmente compatível** com os artefatos entregues. Os artefatos citados existem e o tipo geral de apoio é plausível, mas há divergências e fragilidades na validação e na rastreabilidade. Não foram encontradas evidências concretas suficientes para afirmar uso de IA não declarado.
