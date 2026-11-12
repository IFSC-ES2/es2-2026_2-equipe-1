# Evidência de testes e cobertura — Entrega 8

**Data da execução:** 11/11/2026  
**Comando executado:**

```bash
cd back-end
./gradlew test jacocoTestReport --no-daemon
```

## Resultado

- Testes executados: **129**
- Falhas: **0**
- Erros: **0**
- Testes ignorados: **0**
- Cobertura de linhas: **93,37%**
- Cobertura de instruções: **93,03%**
- Cobertura de branches: **80,80%**
- Cobertura de métodos: **87,22%**
- Cobertura de classes: **100,00%**

A cobertura de linhas supera a meta de 70% definida na métrica M-01, e a cobertura de branches passou
de 63,89% (Entrega 7) para 80,80%. O crescimento vem dos testes acrescentados na Entrega 8:
`UsuarioServiceRegrasAdicionaisTest` (31 casos) e `SolicitacaoPropostaIntegrationTest` (11 casos), este
último autenticando pelo endpoint real de login e reutilizando sessão e token CSRF.

### Execução por classe de teste

| Classe de teste | Casos |
|:---|---:|
| `service.UsuarioServiceTest` | 29 |
| `service.UsuarioServiceRegrasAdicionaisTest` | 31 |
| `service.SolicitacaoServicoServiceTest` | 21 |
| `SecurityIntegrationTest` | 18 |
| `SolicitacaoPropostaIntegrationTest` | 11 |
| `service.SolicitacaoServicoServiceAdditionalTest` | 9 |
| `service.UsuarioServiceAdditionalTest` | 4 |
| `service.EnderecoServiceTest` | 2 |
| `service.PropostaValidacaoTest` | 2 |
| `UsuarioSessionIntegrationTest` | 1 |
| `ServicehubApplicationTests` | 1 |
| **Total** | **129** |

## Frontend

```bash
cd ServiceHub
npm run test:coverage
```

- Testes executados: **29** (3 arquivos), sem falhas
- Cobertura de linhas: **25,22%** — medida a partir da Entrega 8
- Cobertura de branches: **67,02%**
- Cobertura de funções: **61,66%**
- Relatório: `ServiceHub/coverage/index.html`

A cobertura do frontend é menor porque as páginas (`DashboardCliente`, `DashboardPrestador`,
`Cadastro`, `Home`, `SelecaoUser`) não possuem testes de componente e são validadas apenas pelos
três cenários E2E do Playwright. Está registrada como oportunidade de melhoria em `docs/METRICAS.md`.

## Relatórios gerados

O comando gera os relatórios locais em:

```text
back-end/build/reports/jacoco/test/html/index.html
back-end/build/reports/jacoco/test/jacocoTestReport.xml
```

A execução também valida o fluxo de sessão no teste `UsuarioSessionIntegrationTest`, incluindo login, acesso autenticado, logout e bloqueio posterior.
