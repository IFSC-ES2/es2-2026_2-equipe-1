# Release Candidate v1.0.0-rc.1 - Entrega 9

**Projeto:** ServiceHub  
**Marco:** Entrega 9 - Release Candidate  
**Data da consolidação:** 18/11/2026

## 1. Resumo da Release Candidate

A versão `v1.0.0-rc.1` consolida o fluxo funcional do MVP revisado:

- cadastro de clientes e prestadores;
- login e acesso autenticado por JWT stateless;
- cadastro e associação de endereço;
- edição permitida do perfil;
- publicação, listagem e filtro de solicitações;
- envio e reenvio de propostas por prestadores;
- aceite pelo cliente, associação do prestador e mudança para `EM_ANDAMENTO`;
- conclusão do serviço pelo cliente.

### Ações de hardening realizadas

- **Documentação:** conciliadas arquitetura, ADR de autenticação, deploy, plano de testes,
  critérios de aceitação, riscos, métricas e retrospectiva com o comportamento efetivo do código.
- **Autenticação JWT:** substituição da sessão/CSRF por `Authorization: Bearer <token>`, política
  stateless, emissão e validação com HS256 e token com validade de uma hora.
- **CEP e endereço:** consolidação do cadastro, associação e listagem de endereços; a consulta
  externa de CEP possui implementação no frontend, mas não foi coberta por chamada externa
  automatizada nesta validação.
- **Perfil:** correção do fluxo de edição para permitir somente os campos pertinentes e preservar
  e-mail/tipo de perfil; o backend valida o usuário solicitante.
- **CI:** checks do GitHub Actions para arquivos obrigatórios, testes JaCoCo do backend,
  testes/lint/build do frontend e validação do Docker Compose. A cobertura do frontend é
  uma medição local, não um resultado do workflow.

### Limitações conhecidas

- não há integração concluída com o gateway Asaas;
- não existe recusa formal persistida pelo prestador;
- não há notificação ativa em tempo real;
- o logout remove o token do navegador, mas não possui blacklist nem revogação server-side;
- o JWT continua válido até expirar em uma cópia já obtida;
- o Compose local usa PostgreSQL com volume persistente e reproduz a topologia de aplicação e
  banco; a execução direta/testes usam H2 em memória e o proxy/configuração do staging permanecem
  externos ao repositório;
- a cobertura do frontend é menor que a cobertura do backend;
- o staging depende da infraestrutura JVS e dos dados podem ser limpos periodicamente.

## 2. Dados de Acesso e Ambiente

### Staging

- **Frontend:** <https://frontservicehub.jvs.dev.br>
- **API:** <https://servicehub.jvs.dev.br>
- **Base da API nas chamadas:** `https://servicehub.jvs.dev.br/api`

Use credenciais sintéticas ou crie contas próprias no ambiente de demonstração. Não use dados
pessoais reais.

### Contas sintéticas da suíte E2E

| Perfil | E-mail | Senha | Uso |
|:---|:---|:---|:---|
| Cliente | `servicehub.e2e.cliente@example.test` | `ServiceHubE2E!2026` | Publicar solicitação, aceitar proposta e concluir |
| Prestador | `servicehub.e2e.prestador@example.test` | `ServiceHubE2E!2026` | Encontrar solicitação disponível e enviar proposta |

As contas são criadas pela suíte quando ainda não existem. Os cenários alteram dados sintéticos;
não execute os testes E2E contra uma base com dados reais.

### Execução local resumida

Pré-requisitos: JDK 25, Node.js 24/npm e Docker Compose.

**Backend com JWT**

No Bash/Linux/macOS:

```bash
cd back-end
export JWT_SECRET=$(openssl rand -base64 32)
./gradlew bootRun
```

No PowerShell:

```powershell
cd back-end
$bytes = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
.\gradlew.bat bootRun
```

A API local usa `http://localhost:8081/api`. `JWT_SECRET` é obrigatório e deve ter pelo menos
32 bytes.

**Frontend**

```bash
cd ServiceHub
npm ci
npm run dev
```

O frontend local fica em `http://localhost:5173`.

**Validação automatizada**

```bash
cd back-end
./gradlew test jacocoTestReport --no-daemon

cd ../ServiceHub
npm ci
npm test
npm run test:coverage
npm run lint
npm run build
```

## 3. Roteiro curto de demonstração - 5 a 7 minutos

1. **Cadastro/Login (cerca de 1 minuto):** abra o frontend publicado. Faça login como cliente
   sintético ou cadastre uma conta de cliente. Confirme o redirecionamento ao dashboard.
2. **Cadastro de endereço por CEP (cerca de 1 minuto):** abra a edição de perfil ou o modal de
   criação de endereço, informe um CEP sintético e confira os campos preenchidos. Salve o endereço
   e mantenha-o disponível para a solicitação.
3. **Publicação da solicitação (cerca de 1 minuto):** no dashboard do cliente, clique em criar
   solicitação. Informe título, descrição, valor estimado, endereço e especialidade; publique.
4. **Envio da proposta pelo prestador (cerca de 1 a 2 minutos):** encerre a sessão do cliente,
   entre com o prestador sintético e encontre a solicitação disponível. Abra a solicitação e envie
   uma proposta com valor definido.
5. **Aceite pelo cliente (cerca de 1 minuto):** volte ao login do cliente. Abra a solicitação criada,
   confirme a proposta recebida e aceite-a. Verifique o prestador responsável e o status
   `Em andamento`.
6. **Conclusão do serviço (cerca de 1 minuto):** ainda como cliente proprietário, clique em concluir
   o serviço. Confirme o status concluída no cliente e, no login do prestador, confira o item na
   lista de serviços concluídos.

Ao final, registre que pagamento, recusa formal e notificações em tempo real estão fora ou parciais
nesta RC.

## 4. Registro de Contribuição Individual - Entrega 9

| Integrante | Contribuições na Entrega 9 |
|:---|:---|
| **Leonardo Victor Muller De Morais** | Revisou e consolidou a arquitetura efetiva com JWT stateless; participou da correção e revisão das regras de negociação, autorização por proprietário e integração de proposta/aceite/conclusão; implementou/parametrizou cenários Playwright; revisou os critérios de aceitação e a retrospectiva; validou os riscos residuais de JWT, logout e permissões em rotas secundárias. |
| **João Vitor Schmitt** | Manteve e revisou o staging em <https://frontservicehub.jvs.dev.br>, a API em <https://servicehub.jvs.dev.br>, Docker/Compose, CORS e domínios permitidos; consolidou a configuração de `JWT_SECRET` e o checklist de deploy; atualizou a validação técnica da infraestrutura no registro da RC e revisou os riscos de disponibilidade e CORS. |
| **Davi Israel Quirino** | Consolidou o hardening visual e funcional dos dashboards, perfil, componente de senha, correção do carrossel, revisou o fluxo de cadastro/endereço e a usabilidade da apresentação; validou a experiência da demonstração entre cliente e prestador. |
| **Ana Clara Teixeira Ronzani** | Coordenou a consolidação da qualidade da RC e revisou os resultados reais de testes backend, frontend e E2E; atualizou métricas, riscos, plano de testes, matriz de aceitação, retrospectiva e registro de uso de IA; verificou cobertura JaCoCo, 29 testes Vitest e os três cenários Playwright antes da apresentação. |

## 5. Evidências relacionadas

- [Matriz de Aceitação](../ACEITACAO.md)
- [Retrospectiva da RC](../RETROSPECTIVA.md)
- [Métricas](../METRICAS.md)
- [Riscos](../riscos.md)
- [Plano de Testes](../PLANO-DE-TESTES.md)
- [Guia de Deploy](../DEPLOY.md)
