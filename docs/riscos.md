# Registro de Riscos - ServiceHub

## 1. Critério de classificação

A probabilidade e o impacto serão classificados de 1 a 3:

* 1 = Baixo
* 2 = Médio
* 3 = Alto

A prioridade é calculada por:

**Prioridade = Probabilidade × Impacto**

* 1 a 2: Baixa
* 3 a 4: Média
* 6 a 9: Alta

## 2. Registro de riscos

| ID  | Risco                              | Causa                                      | Consequência                        | Prob. | Impacto | Prioridade | Mitigação                                                       | Responsável |
| --- | ---------------------------------- | ------------------------------------------ | ----------------------------------- | ----: | ------: | ---------: | --------------------------------------------------------------- | ----------- |
| R01 | Atraso no desenvolvimento          | Poucas horas disponíveis por integrante    | Atraso nas entregas                 |     2 |       3 |   6 - Alta | Acompanhar o progresso e priorizar funcionalidades do MVP       | Leonardo    |
| R02 | Aumento do escopo                  | Inclusão de funcionalidades não planejadas | Aumento do tempo de desenvolvimento |     2 |       3 |   6 - Alta | Manter o escopo definido para o MVP                             | Davi        |
| R03 | Problemas na integração com Asaas  | Dificuldade com API ou ambiente sandbox    | Falha no fluxo de pagamento         |     2 |       3 |   6 - Alta | Testar a integração antecipadamente e utilizar ambiente sandbox | João        |
| R04 | Falhas no CI                       | Configuração incorreta do pipeline         | PRs sem validação automática        |     2 |       2 |  4 - Média | Manter checks simples e testar o pipeline nos PRs               | João        |
| R05 | Baixa cobertura de testes          | Pouco tempo para criação de testes         | Falhas não identificadas no sistema |     2 |       2 |  4 - Média | Criar testes para as principais regras de negócio               | Ana Clara   |
| R06 | Problemas de comunicação da equipe | Falta de acompanhamento das tarefas        | Retrabalho e atrasos                |     1 |       2 |  2 - Baixa | Utilizar Issues e GitHub Projects para acompanhar as tarefas    | Davi        |

## 3. Riscos mais críticos

Os riscos R01, R02 e R03 possuem prioridade alta.

O atraso no desenvolvimento pode comprometer o cronograma do semestre. O aumento do escopo pode consumir as horas disponíveis para o MVP. Já problemas na integração com o Asaas podem impedir a conclusão do fluxo de pagamento.

Por isso, esses riscos devem ser acompanhados com maior frequência.

## 4. Plano de resposta

### R01 - Atraso no desenvolvimento

**Prevenção:** acompanhar o progresso das tarefas e priorizar o MVP.

**Caso aconteça:** reavaliar tarefas de menor prioridade e concentrar os esforços nas funcionalidades essenciais.

### R02 - Aumento do escopo

**Prevenção:** manter as funcionalidades definidas no MVP.

**Caso aconteça:** adiar funcionalidades não essenciais para uma etapa futura.

### R03 - Problemas com Asaas

**Prevenção:** realizar testes utilizando o ambiente sandbox.

**Caso aconteça:** analisar os erros da API e ajustar a integração antes da implementação das demais funcionalidades.

### R04 - Falhas no CI

**Prevenção:** manter o pipeline simples e testar sua execução nos PRs.

**Caso aconteça:** corrigir a configuração do workflow antes da aprovação do PR.

### R05 - Baixa cobertura de testes

**Prevenção:** criar testes para as principais regras de negócio.

**Caso aconteça:** adicionar testes antes da integração da funcionalidade.

### R06 - Problemas de comunicação

**Prevenção:** utilizar Issues, PRs e GitHub Projects.

**Caso aconteça:** realizar uma revisão das tarefas e responsabilidades da equipe.

## 5. Acompanhamento

Os riscos serão revisados pela equipe a cada entrega ou Sprint. A probabilidade, o impacto e as ações de mitigação poderão ser atualizados conforme o andamento do projeto.

## 6. Revisão da Sprint 2 - em andamento


Registro do estado dos riscos em 01/10/2026, baseado no código, workflow e validações locais antes da abertura do PR da Entrega 6. Os riscos sem evidência de mitigação permanecem ativos; checks remotos e eventos do PR ainda não ocorreram. As probabilidades, impactos, prioridades e responsáveis do registro inicial são mantidos quando não há evidência suficiente para recalculá-los.

| ID | Situação observada | Estado em 01/10 | Ação de resposta |
|:---|:---|:---|:---|
| R02 - Aumento do escopo | Comunicação cliente-prestador foi levantada; chat em tempo real permanece fora do MVP. | Monitorado; prioridade inicial 6 (alta). | Definir alternativa de comunicação no planejamento antes de assumir implementação ou ampliar escopo. |
| R03 - Integração Asaas | Pagamento não foi implementado nesta Sprint e continua no backlog futuro. | Ativo para trabalho futuro; prioridade inicial 6 (alta). | Planejar teste antecipado no sandbox quando a integração for priorizada. |
| R04 - Falhas no CI | Workflow ampliado no branch, ainda sem execução remota no PR. | Mitigação iniciada; ativo até checks remotos verdes; prioridade inicial 4 (média). | Verificar jobs no PR e exigir checks verdes antes do merge conforme regras disponíveis. |
| R07 - Vulnerabilidade em dependência frontend | `npm ci` reportou uma vulnerabilidade de severidade alta, sem identificar o pacote no registro desta Sprint. | Novo; ainda não classificado numericamente por falta de triagem do pacote e advisory. | Consultar o relatório de auditoria do npm e decidir correção compatível antes da release. |

Este registro descreve o estado comprovável no branch antes do PR e não declara riscos mitigados sem evidência. R07 permanece sem nota numérica até que o advisory seja identificado.
