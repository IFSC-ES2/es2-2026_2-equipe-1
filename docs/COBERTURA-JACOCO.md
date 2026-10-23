# Evidência de testes e cobertura — Entrega 7

**Data da execução:** 22/10/2026  
**Comando executado:**

```bash
cd back-end
./gradlew test jacocoTestReport --no-daemon
```

## Resultado

- Testes executados: **57**
- Falhas: **0**
- Erros: **0**
- Testes ignorados: **0**
- Cobertura de linhas: **77,35%**
- Cobertura de instruções: **76,48%**
- Cobertura de branches: **63,89%**
- Cobertura de métodos: **67,26%**
- Cobertura de classes: **90,48%**

A cobertura de linhas supera a meta de 70% definida na métrica M-01. A cobertura de branches permanece registrada como oportunidade de melhoria.

## Relatórios gerados

O comando gera os relatórios locais em:

```text
back-end/build/reports/jacoco/test/html/index.html
back-end/build/reports/jacoco/test/jacocoTestReport.xml
```

A execução também valida o fluxo de sessão no teste `UsuarioSessionIntegrationTest`, incluindo login, acesso autenticado, logout e bloqueio posterior.
