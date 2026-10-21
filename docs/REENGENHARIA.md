# Manutenção e reengenharia — Entrega 7

## Alteração no projeto

**Problema tratado:** manter propostas e associação do prestador como dados soltos favoreceria duplicidade e regras espalhadas.  
**Solução:** entidade própria, método `adicionarProposta`, restrição de integridade, transações e `findByIdForUpdate`.  
**Benefícios esperados:** maior coesão, integridade no banco e menor risco de aceite concorrente.

## Validação

- Testes unitários de serviço estão versionados.
- `npm run build` e `npm run lint` do frontend passaram nesta análise.
- Pendente

## Métrica antes/depois

| Métrica | `main` | `entrega-7` | Interpretação |
|---|---:|---:|---|
| Entidade de proposta explícita | Não localizada | 1 | Melhoria estrutural observada |
| Restrição única proposta/solicitação | Não localizada | 1 | Melhoria de integridade observada |
| Cobertura de linhas | Não medida | Não medida | JaCoCo ausente |
| Testes HTTP de autorização | Não localizados | Não localizados | Lacuna permanece |

## Evidência de processo pendente

Pendente 

## Plano de fechamento

1. Adicionar testes de proposta, aceite, proprietário, CSRF, estado inválido e concorrência.
2. Executar com JDK 25.
3. Configurar JaCoCo e registrar cobertura antes/depois.
4. Vincular PR e execução do CI na matriz de evidências.