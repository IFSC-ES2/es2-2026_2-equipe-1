# ADR-0004: Adoção dos padrões Adapter (frontend) e Strategy (CSRF no backend)

- **Status:** Implementada na Entrega 6
- **Data:** 01/10/2026

## Contexto

Com a autenticação por sessão (ADR-0003), o frontend precisa (a) conviver com um contrato de API cujos nomes e tipos diferem do modelo das telas e (b) enviar o token CSRF de forma compatível com o Spring Security. Ambos os pontos estavam gerando acoplamento: páginas conhecendo detalhes HTTP e lógica de resolução de token sem lugar próprio.

## Decisão

1. **Adapter** no frontend para converter dados da API em modelos de tela (`mapearUsuario`, `converterEndereco`, `formatarSolicitacao`).
2. **Strategy** no backend para escolher o tratamento do token CSRF (`SpaCsrfTokenRequestHandler` delegando a `CsrfTokenRequestAttributeHandler` ou `XorCsrfTokenRequestAttributeHandler`).

## Alternativas consideradas

| Alternativa | Por que não |
|:---|:---|
| Usar os tipos da API diretamente nas telas | Acopla UI ao contrato HTTP; cada mudança de campo afeta várias páginas |
| Desabilitar CSRF | Remove proteção para requisições autenticadas por cookie |
| Usar apenas o handler padrão (XOR) | Quebra o fluxo da SPA, que devolve o valor puro do cookie no cabeçalho |
| Usar apenas o handler simples | Perde a proteção contra BREACH |

## Consequências

- **Positivas:** menor acoplamento UI-API; proteção CSRF compatível com a SPA; pontos de mudança localizados.
- **Negativas:** conversores precisam acompanhar o contrato; camada de delegação adicional no CSRF; ausência de testes automatizados desses dois pontos.
- **Ações:** mover `formatarSolicitacao` para `servicos/api.ts`; criar teste `MockMvc` que valide requisição mutável com e sem token; avaliar o padrão State para `StatusSolicitacao` na Sprint 3.

## Código relacionado

- `ServiceHub/src/servicos/autenticacao.ts`, `ServiceHub/src/servicos/api.ts`, `ServiceHub/src/paginas/DashboardCliente/index.tsx`
- `back-end/src/main/java/com/es/servicehub/config/SecurityConfig.java`

Detalhamento completo: [`docs/PADROES-DE-PROJETO.md`](../PADROES-DE-PROJETO.md).
