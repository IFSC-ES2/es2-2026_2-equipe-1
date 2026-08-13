# ADR-0002: Escolha do Gateway de Pagamentos Asaas

## Status

Aceito

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

Permite atender diretamente ao requisito obrigatório do MVP de processar os pagamentos na própria plataforma.

## Referências

- Issue: #4
- Pull request: #5
- Documentação: [Asaas API Docs](https://docs.asaas.com/)