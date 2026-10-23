# ADR-0005: Evolução da Arquitetura e Implementação do Fluxo de Negociação de Serviços

- **Status:** Implementada na Entrega 7 — Sprint 3
- **Data:** 22/10/2026

## Contexto

Durante a Sprint 3, o ServiceHub precisava evoluir o fluxo de contratação de serviços, permitindo que prestadores enviassem propostas de valores e que clientes analisassem e aceitassem os orçamentos recebidos.

Anteriormente, a solicitação de serviço não possuía uma estrutura independente para armazenar propostas, dificultando o gerenciamento dos valores oferecidos e aumentando o acoplamento entre as regras de negócio.

Além disso, foram identificadas necessidades de melhorias nas interfaces dos clientes e prestadores, correções no encerramento de sessão, ampliação dos testes automatizados e atualização da documentação arquitetural.

## Decisão

1. **Criação da entidade `PropostaServico`** no backend para representar os orçamentos enviados pelos prestadores, armazenando o valor proposto e os vínculos com a solicitação e o prestador.
2. **Implementação do fluxo de negociação**, permitindo que prestadores enviem propostas e que o cliente responsável pela solicitação escolha uma delas. O aceite atualiza o valor, associa o prestador e altera o status da solicitação para `EM_ANDAMENTO`.
3. **Controle de integridade e concorrência**, utilizando restrições de unicidade no banco de dados, transações e bloqueio pessimista (`findByIdForUpdate`) para evitar propostas duplicadas e aceites simultâneos.
4. **Padronização do encerramento de sessão**, corrigindo o comportamento do botão de sair e garantindo a invalidação da sessão autenticada.
5. **Melhorias nas interfaces**, incluindo ajustes visuais nos dashboards de clientes e prestadores, padronização de componentes, correções de CSS e funcionamento dos carrosséis.
6. **Ampliação dos testes automatizados**, utilizando Vitest no frontend e JUnit/Spring Boot Test no backend para validar funcionalidades e reduzir regressões.
7. **Atualização da documentação arquitetural**, utilizando diagramas de contexto, contêineres e classes, além de registrar as mudanças de reengenharia, contribuições individuais e métricas da Sprint 3.

## Alternativas consideradas

| Alternativa | Por que não |
|:---|:---|
| Manter os valores diretamente em `SolicitacaoServico` | Dificulta o armazenamento de diferentes propostas e aumenta o acoplamento das regras de negócio |
| Permitir que o prestador aceite diretamente uma solicitação | Não permite que o cliente compare e escolha entre diferentes orçamentos |
| Controlar propostas duplicadas apenas no frontend | Não garante a integridade dos dados em requisições simultâneas ou realizadas diretamente à API |
| Realizar o aceite sem controle transacional | Permite inconsistências quando diferentes propostas são aceitas simultaneamente |
| Manter o encerramento de sessão apenas na interface | Não garante a invalidação da sessão autenticada no backend |
| Realizar somente testes manuais | Dificulta a identificação de regressões e a validação contínua das regras de negócio |
| Manter apenas a documentação arquitetural anterior | Não representa corretamente as novas entidades, relacionamentos e funcionalidades implementadas |

## Consequências

- **Positivas:** maior organização das regras de negócio; possibilidade de negociação entre clientes e prestadores; integridade das propostas; redução de inconsistências em operações concorrentes; melhoria da experiência dos usuários; encerramento de sessão mais seguro; ampliação da cobertura de testes e documentação alinhada à arquitetura implementada.
- **Negativas:** aumento da complexidade do modelo de domínio; necessidade de manter novas entidades, relacionamentos e validações; maior quantidade de testes e documentação para atualizar; dependência de mecanismos transacionais do banco de dados.
- **Ações:** ampliar os testes automatizados do fluxo de negociação; implementar ou formalizar o replanejamento da recusa de propostas; avaliar melhorias na experiência de negociação; realizar a integração com o Asaas; preparar a migração para PostgreSQL e manter os diagramas e métricas atualizados nas próximas entregas.

## Código relacionado

- `ServiceHub/src/paginas/DashboardCliente/index.tsx`
- `ServiceHub/src/paginas/DashboardPrestador/index.tsx`
- `ServiceHub/src/componentes/modal/ModalAceitarSolicitacao.tsx`
- `ServiceHub/src/servicos/api.ts`
- `ServiceHub/tests/servicos.test.ts`
- `back-end/src/main/java/com/es/servicehub/service/SolicitacaoServicoService.java`
- `back-end/src/main/java/com/es/servicehub/config/SecurityConfig.java`
- `back-end/src/test/java/com/es/servicehub/service/SolicitacaoServicoServiceTest.java`
- `back-end/src/test/java/com/es/servicehub/SecurityIntegrationTest.java`

## Documentação relacionada

- [`docs/ARQUITETURA.md`](../ARQUITETURA.md)
- [`docs/REENGENHARIA.md`](../REENGENHARIA.md)
- [`docs/CONTRIBUICOES-SPRINT-3.md`](../CONTRIBUICOES-SPRINT-3.md)
- [`docs/METRICAS.md`](../METRICAS.md)
- [`docs/entregas/sprint-3-planejamento.md`](../entregas/sprint-3-planejamento.md)

Detalhamento completo da arquitetura e das alterações realizadas na Sprint 3: [`docs/ARQUITETURA.md`](../ARQUITETURA.md).
