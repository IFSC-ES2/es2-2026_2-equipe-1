# Padrões de Projeto - Sprint 2

## Strategy

**Problema:** o frontend usa um fluxo CSRF próprio, mas precisa preservar o tratamento de token esperado pelo Spring Security.

**Uso no código:** `SpaCsrfTokenRequestHandler` escolhe entre os handlers `CsrfTokenRequestAttributeHandler` e `XorCsrfTokenRequestAttributeHandler` conforme a requisição.

**Benefício:** separa a escolha do tratamento CSRF da configuração principal de segurança. **Custo:** há mais uma camada de delegação para manter consistente.

## Adapter

**Problema:** os dados da API têm formatos diferentes dos modelos usados nas telas.

**Uso no código:** `mapearUsuario`, `converterEndereco` e `formatarSolicitacao` convertem respostas da API para os modelos do frontend.

**Benefício:** as telas não precisam conhecer diretamente todos os detalhes do contrato HTTP. **Custo:** os conversores precisam ser atualizados quando esse contrato mudar.

DTO e Repository também são usados no projeto, mas não são contados aqui como os dois padrões desta entrega.
