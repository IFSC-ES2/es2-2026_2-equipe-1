# ADR-0002: Escolha do Gateway de Pagamentos Asaas

## Status

**Adiada — não implementada** (revisão de 18/11/2026, Entrega 9 - Release Candidate `v1.0.0-rc.1`).

- **Histórico preservado:** a decisão original foi registrada como *Aceito* na Entrega 2 e permanece
  abaixo como registro histórico. O texto original não é apagado nem reescrito.
- **Estado atual:** a escolha do Asaas como gateway continua sendo a alternativa preferida da equipe,
  mas **nenhuma integração foi implementada**. Não existe cobrança, liquidação, status financeiro nem
  webhook de pagamento no código da RC.
- **Evidências da retirada do escopo:** `docs/RETROSPECTIVA.md` (seção "Justificativa para a remoção do
  Asaas"), `docs/entregas/release-candidate.md` (limitações conhecidas) e riscos `R03`, `R11` e `R20` em
  `docs/riscos.md`.
- **Para reativar esta ADR:** criar issue de replanejamento, homologar a conta sandbox, implementar o
  serviço de integração e os webhooks, cobrir com testes automatizados e então registrar a nova
  decisão (por exemplo, uma ADR-0006) sem alterar o histórico desta.

### Registro histórico da decisão original (Entrega 2)

> Status original: **Aceito** — decisão de escolher o Asaas como gateway, tomada na fase de inception
> e mantida como referência. Vale para o momento em que o pagamento entrar no escopo do produto.

## Contexto

A plataforma **ServiceHub** necessita processar pagamentos na própria aplicação web, permitindo a liquidação financeira do serviço contratado entre cliente e prestador. Para isso, é indispensável adotar um gateway de pagamentos com API REST bem documentada e ambiente de testes (*sandbox*).

## Decisão

Decidimos utilizar a **API do Asaas** como gateway de pagamentos da plataforma, pois ela oferece um ambiente de testes (*sandbox*) robusto, suporte completo a PIX e Boleto/Cartão, além de SDKs e documentação clara para integração RESTful.

## Alternativas consideradas

| Alternativa | Vantagens | Desvantagens | Motivo da rejeição |
|---|---|---|---|
| **Mercado Pago API** | Bastante popular no mercado brasileiro. | Processo de homologação em ambiente de sandbox mais verboso para contas de teste. | A documentação e criação de webhooks no Asaas mostrou-se mais direta para o escopo acadêmico. |
| **Stripe API** | Padrão global, excelente documentação. | Foco primário em moedas estrangeiras/cartão internacional, com menor apelo nativo para PIX no ecossistema brasileiro. | O Asaas oferece suporte nativo e simplificado a fluxos financeiros no Brasil. |

## Consequências

### Consequências positivas
- Possibilidade de simular pagamentos reais em ambiente Sandbox sem custos financeiros.
- Facilidade para escutar atualizações de status de pagamento por meio de Webhooks REST no Spring Boot.

### Consequências negativas ou riscos
- Dependência de disponibilidade da API externa do Asaas para o fluxo de finalização do chamado.

### Ações necessárias
- Criar conta no ambiente Sandbox do Asaas.
- Isolar a integração em um módulo/serviço no backend (`AsaasPaymentService`) utilizando o padrão *Adapter* ou *Strategy*.

## Relação com o MVP

**Planejado:** atendia ao requisito original de processar os pagamentos na própria plataforma.

**Situação na RC (`v1.0.0-rc.1`):** o pagamento foi **removido do escopo entregue**. O MVP entregue
cobre cadastro, autenticação JWT, perfil e endereço, publicação de solicitações, propostas, aceite e
conclusão do serviço. Pagamento não faz parte da matriz de funcionalidades concluídas
(`docs/ACEITACAO.md`) nem da demonstração final.

### Ações necessárias (não executadas)

As ações listadas na seção "Consequências" — criar conta sandbox e isolar a integração em um serviço
com padrão *Adapter*/*Strategy* — **não foram realizadas**. Permanecem como pré-requisitos de uma
entrega futura, caso a integração seja retomada.

## Referências

- Issue: #4
- Pull request: #5
- Documentação: [Asaas API Docs](https://docs.asaas.com/)