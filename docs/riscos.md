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

## 6. Revisão da Sprint 2 (01/10/2026)

Critério: prioridade = probabilidade x impacto (escala 1 a 3). "Prop." indica valor proposto nesta revisão; a equipe deve confirmar na reunião de fechamento.

### 6.1 Riscos do registro original

| ID | Situação | Prob. | Impacto | Prioridade | Evidência | Ação para a Sprint 3 | Resp. |
|:---|:---|:---:|:---:|:---:|:---|:---|:---|
| R01 Atraso | **Concretizado parcialmente** | 3 (prop.) | 3 | 9 - Alta | Item #9, previsto para a Sprint 2 no baseline, não foi entregue; 36,4% do MVP concluído | Registrar horas; limitar a sprint a #9 + correções de segurança + testes | Leonardo |
| R02 Aumento do escopo | Ativo | 2 | 3 | 6 - Alta | Ajustes visuais e Dockerfiles consumiram esforço fora do backlog | Todo trabalho fora do backlog exige issue e justificativa; decisão de comunicação cliente-prestador apenas como ADR | Davi |
| R03 Integração Asaas | Ativo, **piorou** | 3 (prop.) | 3 | 9 - Alta | Nenhum teste em sandbox; pagamento fica para a Sprint 4, com 24h estimadas e inexperiência com webhooks | Spike de 2h no sandbox na Sprint 3 | João |
| R04 Falhas no CI | **Concretizado e corrigido** | 2 | 2 | 4 - Média | O `ci.yml` anterior era YAML inválido e não executaria; versão nova validada por parser | Manter ativo até os checks do PR ficarem verdes; depois reclassificar como mitigado | João |
| R05 Baixa cobertura | **Mitigado parcialmente** | 2 | 2 | 4 - Média | 57 testes backend passaram; JaCoCo registrou 77,35% de linhas e 63,89% de branches | Ampliar branches e manter JaCoCo no CI | Ana Clara |
| R06 Comunicação | Ativo | 1 | 2 | 2 - Baixa | Sem evidência de falha; board e issues devem refletir o estado real | Revisar board antes do fechamento | Davi |

### 6.2 Riscos novos

| ID | Risco | Causa | Consequência | Prob. | Impacto | Prioridade | Mitigação | Resp. |
|:---|:---|:---|:---|:---:|:---:|:---:|:---|:---|
| R07 | Vulnerabilidade em dependência do frontend | `npm ci` reportou 1 vulnerabilidade alta. Triagem: pacote `brace-expansion`, transitivo de `eslint` (via `minimatch`), advisories GHSA-qhr7-859c-m2p7 e GHSA-6j4f-fj2g-mc7p (DoS por recursão). É dependência de **ferramenta de desenvolvimento**, não vai para o bundle | Impacto limitado a build/lint | 1 | 2 | 2 - Baixa | `npm audit fix` (correção disponível); rodar `npm run build` e `npm run lint` depois | João |
| R08 | Autorização incompleta na API | Só `POST /api/solicitacao` e `POST /api/enderecos` exigem CLIENTE; `PUT`/`DELETE` de solicitação e `GET /api/enderecos` exigem só autenticação, sem checar o dono | Qualquer usuário logado altera/apaga solicitações de outros e lista endereços de todos (privacidade) | 3 | 3 | 9 - Alta | Restringir por perfil e por dono do recurso; testes `MockMvc` | Leonardo |
| R09 | Ambiente Docker inconsistente com a stack | `docker-compose.yml` usa porta 3000 e volumes de Node para o backend Spring (porta 8080) e não tem PostgreSQL; app usa H2 em memória | Ambiente de staging (Sprint 4) sem base; perda de dados a cada reinício | 3 | 2 | 6 - Alta | Corrigir compose; decidir PostgreSQL x H2 em novo ADR | João |
| R10 | Uso de IA sem validação humana registrada | `USO-IA.md` registros 6, 7 e 10 dizem "a equipe irá validar" / "necessário revisão" | Código ou documentos incorretos entrarem na release; infração ao protocolo de IA | 2 | 2 | 4 - Média | Registrar quem validou e como em cada entrada de IA antes do merge | Ana Clara |


---

## 7. Revisão da Sprint 3 (22/10/2026)

### 7.1 Reavaliação dos Riscos Anteriores

| ID | Situação na Sprint 3 | Prob. | Impacto | Prioridade | Evidência / Evolução | Ação para a Sprint 4 | Resp. |
|:---|:---|:---:|:---:|:---:|:---|:---|:---|
| **R01 Atraso** | **Mitigado parcialmente** | 2 | 3 | 6 - Alta | Conclusão do item #9 (Aceite com propostas de valor) e evolução para 54,5% do MVP. | Manter foco exclusivo no item #10 (Asaas) e #13 (Staging) na Sprint 4. | Leonardo |
| **R02 Escopo** | **Mitigado** | 1 | 3 | 3 - Média | O escopo da Sprint 3 foi estritamente focado em propostas, arquitetura C4 e reengenharia. | Manter a política de controle via Issues e PRs revisados. | Davi |
| **R03 Asaas** | **Ativo (Crítico)** | 3 | 3 | 9 - Alta | O fluxo de pagamento permanece como o maior desafio do MVP para a Sprint 4 (24h de estimativa). | Iniciar a Sprint 4 pela integração da API/Sandbox do Asaas e escuta de Webhooks. | João |
| **R04 CI** | **Mitigado** | 1 | 2 | 2 - Baixa | O pipeline `ci.yml` executou e validou com sucesso as compilações e suítes do Gradle e Vitest. | Manter monitoramento dos checks automatizados nos PRs. | João |
| **R05 Cobertura** | **Mitigado parcialmente** | 2 | 2 | 4 - Média | Criada a suíte de testes de frontend com Vitest e adicionado o `SecurityIntegrationTest` no backend. | Configurar o plugin JaCoCo no Gradle para medição numérica oficial da cobertura. | Ana Clara |
| **R06 Comunicação** | **Mitigado** | 1 | 2 | 2 - Baixa | Alinhamento contínuo das tarefas via GitHub Projects e issues associadas aos PRs. | Manter atualização do board. | Davi |
| **R07 Dependências** | **Mitigado** | 1 | 1 | 1 - Baixa | Dependências de desenvolvimento checadas no frontend durante o build. | Rodar `npm audit` preventivo. | João |
| **R08 Autorização** | **Mitigado (Reengenharia)** | 1 | 3 | 3 - Média | Resolvido o Bug #24 e reestruturado o acesso a solicitações e propostas com validação de proprietário. | Manter testes de integração cobrindo rotas protegidas. | Leonardo |
| **R09 Docker/Compose** | **Ativo** | 2 | 2 | 4 - Média | Aplicação rodou com H2 na Sprint 3. A configuração final do PostgreSQL/Staging ficou alocada para a Sprint 4. | Ajustar o `docker-compose.yml` para subir a imagem da API e o container PostgreSQL na Sprint 4. | João |
| **R10 IA sem Validação** | **Mitigado** | 1 | 2 | 2 - Baixa | Entradas de código e documentos gerados por IA validados e registrados pelos integrantes responsáveis. | Manter registros em `USO-IA.md`. | Ana Clara |

---

### 7.2 Riscos Novos Identificados na Sprint 3

| ID | Risco | Causa | Consequência | Prob. | Impacto | Prioridade | Mitigação | Resp. |
|:---|:---|:---|:---|:---:|:---:|:---:|:---|:---|
| **R11** | **Complexidade na Regra de Webhooks do Asaas** | Inexperiência com o tratamento assíncrono do retorno de pagamento via Webhook em ambiente de Sandbox. | Falha em atualizar o status da solicitação após a liquidação do pagamento pelo cliente. | 2 | 3 | 6 - Alta | Criar um endpoint/controller dedicado para simular os eventos de Webhook do Asaas no backend. | João |
| **R12** | **Migração de Dados H2 para PostgreSQL** | Diferenças de sintaxe SQL/DDL e comportamentos de tipos entre o banco H2 (em memória) e o PostgreSQL. | Falha na inicialização da aplicação ou erros de migração JPA no ambiente de staging. | 2 | 2 | 4 - Média | Validar as propriedades do Spring Data JPA (`ddl-auto` / scripts SQL) em container local do PostgreSQL. | Leonardo |