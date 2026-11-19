# ADR-0004: Adaptadores de dados e componentização do frontend

- **Status:** Implementada
- **Data da revisão:** 18/11/2026

## Contexto

Os contratos JSON da API e os modelos consumidos pelas telas têm diferenças de nomes, tipos e campos opcionais. Além disso, formulários e modais usados por mais de uma página não devem depender diretamente de chamadas HTTP ou persistência.

## Decisão

1. Manter adaptadores no frontend para converter os tipos da API em modelos adequados à interface, como `mapearUsuario`, `converterEndereco` e `formatarSolicitacao`.
2. Reutilizar componentes React de interface, como `ModalPerfilUsuario`, passando dados e callbacks por props. As páginas chamadoras ficam responsáveis por integrar o componente ao serviço de API e controlar o salvamento.
3. Manter as regras de autenticação e autorização na configuração Spring Security descrita em [ADR-0003](ADR-0003-autenticacao.md), sem introduzir estratégias de resolução de token no frontend.

## Alternativas consideradas

| Alternativa | Por que não |
|:---|:---|
| Usar os tipos da API diretamente nas telas | Acopla a UI ao contrato HTTP e espalha conversões e valores padrão pelas páginas. |
| Fazer `ModalPerfilUsuario` chamar a API diretamente | Acopla apresentação à infraestrutura e impede que cada dashboard controle o fluxo de salvamento. |
| Duplicar formulários de perfil nos dashboards | Produz regras e interface divergentes para clientes e prestadores. |

## Consequências

- **Positivas:** diferenças entre contratos ficam localizadas; componentes compartilhados reduzem duplicação; páginas controlam explicitamente as operações de persistência.
- **Negativas:** adaptadores precisam acompanhar mudanças no contrato; `formatarSolicitacao` permanece dentro do dashboard cliente e não é compartilhado.
- **Ações:** avaliar a extração de `formatarSolicitacao` para o módulo de serviços quando houver necessidade de reutilização.

## Código relacionado

- `ServiceHub/src/servicos/autenticacao.ts` (`mapearUsuario`)
- `ServiceHub/src/servicos/api.ts` (`converterEndereco`, chamadas REST)
- `ServiceHub/src/paginas/DashboardCliente/index.tsx` (`formatarSolicitacao`)
- `ServiceHub/src/componentes/modal/ModalPerfilUsuario.tsx`
- `ServiceHub/src/paginas/DashboardCliente/index.tsx`
- `ServiceHub/src/paginas/DashboardPrestador/index.tsx`

Detalhamento: [`docs/PADROES-DE-PROJETO.md`](../PADROES-DE-PROJETO.md).
