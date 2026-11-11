# Deploy e validação — Entrega 8 (Sprint 4)

## 1. Visão geral do ambiente

O ambiente online de staging/validação informado para a Entrega 8 é:

- **Aplicação:** <https://servicehub.jvs.dev.br>
- **Frontend:** aplicação SPA em React, compilada/servida com Vite.
- **Backend:** API REST em Java com Spring Boot.
- **Banco de dados previsto para o ambiente online:** PostgreSQL.
- **Acesso externo:** proxy reverso mantido no servidor do João, que encaminha as requisições ao frontend e à API.

O staging é destinado à validação da equipe e da disciplina. Não deve ser tratado como ambiente de produção nem receber dados pessoais ou credenciais reais.

> **Diferença entre staging e o Compose deste repositório:** a descrição do staging acima é a arquitetura informada para o ambiente online. No estado atual do repositório, o `docker-compose.yml` não declara um serviço PostgreSQL nem configura o backend para utilizá-lo. O `application.properties` configura H2 em memória e o backend escuta na porta `8081`. O Compose publica o frontend em `5173` e o backend em `8081`; portanto, ele ainda não reproduz localmente a arquitetura de staging descrita nesta seção.

## 2. Execução local

### Pré-requisitos

- Git.
- Docker Desktop em execução.
- Docker Compose v2 ou superior (`docker compose`).

### Clonar a branch da entrega

```bash
git clone --branch entrega-8 https://github.com/IFSC-ES2/es2-2026_2-equipe-1.git
cd es2-2026_2-equipe-1
```

### Configurar o ambiente

O arquivo de exemplo **`.env.example` está versionado** na raiz do repositório (Entrega 8). Copie-o e ajuste apenas os valores locais:

```bash
cp .env.example .env
```

O `.env` está no `.gitignore` e **não deve ser commitado**. Não crie um `.env` com valores de produção ou reutilize credenciais reais.

As variáveis disponíveis estão descritas na seção [Variáveis de ambiente e segurança](#4-variáveis-de-ambiente-e-segurança). As variáveis do PostgreSQL e do Asaas estão comentadas no exemplo, porque essas integrações ainda não estão ligadas ao Compose/backend.

### Construir e iniciar os serviços

Com o arquivo de ambiente disponível e preenchido com valores locais de desenvolvimento:

```bash
docker compose up --build -d
```

O comando legado equivalente é `docker-compose up --build -d`. Para acompanhar a inicialização:

```bash
docker compose ps
docker compose logs -f
```

Para encerrar os containers:

```bash
docker compose down
```

### Endereços locais

De acordo com as portas configuradas atualmente no repositório:

- **Frontend:** <http://localhost:5173>
- **API do backend:** <http://localhost:8081/api>

O endereço `http://localhost:8080/api` não corresponde ao mapeamento atual do Compose. Para usá-lo, seria necessário alterar a configuração de portas do backend e garantir que a URL da API do frontend também corresponda.

### Limitações de reprodutibilidade do Compose atual

Antes de considerar o Compose uma reprodução completa do ambiente, é necessário alinhar os arquivos de infraestrutura com esta documentação. No estado atual:

1. O `.env.example` está versionado (Entrega 8), mas o Compose não injeta todas as variáveis listadas nele no backend.
2. O Compose não declara um container PostgreSQL.
3. O backend usa H2 em memória, não PostgreSQL; os dados podem ser perdidos ao reiniciar a aplicação.
4. O Compose publica a API em `8081`, não em `8080`.
5. O Compose passa `VITE_API_URL` ao frontend, mas o valor depende do `.env` ausente.
6. As configurações `SPRING_DATASOURCE_URL`, `POSTGRES_USER`, `POSTGRES_PASSWORD` e `JWT_SECRET` não estão atualmente ligadas ao Compose e/ou à configuração do backend.

Consequentemente, os comandos acima registram o procedimento esperado, mas a execução local plenamente reproduzível depende dessas pendências. Não use a URL de staging como substituição automática para a API local.

## 3. Validação automatizada com Playwright

Os testes de navegador ficam em `ServiceHub/e2e`. Requerem Node.js/npm e o Chromium do Playwright instalado. Na primeira preparação do ambiente, instale as dependências e o navegador:

```bash
cd ServiceHub
npm ci
npx playwright install chromium
```

Com o backend acessível na URL configurada em `ServiceHub/playwright.config.ts` (atualmente `http://localhost:8081`), execute:

```bash
npm run test:e2e
```

O comando roda os testes em modo headless. Para executar com o navegador visível:

```bash
npm run test:e2e:visivel
```

O modo visível usa `--headed --workers=1`. Apesar de ser interativo/visível, a configuração atual não define desaceleração (`slowMo`).

### Contas de teste e dados

As contas de demonstração indicadas para validação são:

| Perfil | E-mail | Senha informada para demonstração |
|---|---|---|
| Cliente E2E | `servicehub.e2e.cliente@example.test` | `SenhaE2E123!` |
| Prestador E2E | `servicehub.e2e.prestador@example.test` | `SenhaE2E123!` |

Esses usuários não são criados por migração ou seed do Compose. Os testes Playwright tentam autenticar e, se a conta ainda não existir, realizam o cadastro durante a execução. Portanto, a disponibilidade dessas credenciais no staging não é garantida.

> **Divergência a resolver:** o arquivo E2E atual (`ServiceHub/e2e/servicehub.spec.ts`) usa a senha `ServiceHubE2E!2026`, diferente da senha de demonstração acima. Antes de divulgar ou usar `SenhaE2E123!`, alinhe o teste e a senha das contas no ambiente que será validado. As contas E2E e seus dados são exclusivamente sintéticos; não reutilize essa senha em contas pessoais ou de produção.

Os testes podem criar ou atualizar dados persistentes. Em particular, o cenário de fluxo de negócio publica uma solicitação, cria uma proposta e percorre as etapas do serviço. Execute-os apenas contra uma base isolada para testes; não os direcione ao staging compartilhado sem autorização e preparação da equipe.

## 4. Variáveis de ambiente e segurança

As variáveis abaixo são a configuração prevista para parametrizar a aplicação. Os valores da tabela são exemplos de desenvolvimento local, não credenciais de staging ou produção.

| Variável | Finalidade | Exemplo/observação |
|---|---|---|
| `VITE_API_URL` | URL base da API usada pelo frontend. | Para o mapeamento backend atual: `http://localhost:8081/api`. |
| `SPRING_DATASOURCE_URL` | JDBC URL que o Spring usaria para conectar ao banco. | Exemplo de rede Compose: `jdbc:postgresql://db:5432/servicehub`. Não está conectado à configuração atual, que usa H2. |
| `POSTGRES_USER` | Usuário do banco PostgreSQL local. | Defina um valor exclusivo de desenvolvimento; não reutilize usuário de staging. |
| `POSTGRES_PASSWORD` | Senha do usuário PostgreSQL local. | Gere uma senha local forte e não a compartilhe nem a versione. |
| `JWT_SECRET` | Segredo destinado à assinatura de tokens, caso a autenticação JWT seja configurada. | Use um segredo aleatório local. A configuração atualmente verificada usa sessão HTTP/Spring Security, não demonstra consumo desta variável. |

**Estado dos arquivos de configuração:** `.env.example` está versionado na raiz do repositório com apenas valores de exemplo e variáveis sensíveis comentadas. O Compose referencia `VITE_API_URL`, mas não injeta as demais variáveis no backend; o `application.properties` usa H2 em memória. Portanto, a tabela descreve os campos previstos para a configuração futura e não significa que todos estejam operacionais hoje.

> **Importante para o build do frontend:** `ServiceHub/src/servicos/api.ts` lê `import.meta.env.VITE_API_URL` com valor padrão `http://localhost:8081/api`. O Vite **embute** a variável no bundle em tempo de build. Para servir o frontend em outro domínio, defina `VITE_API_URL` **antes** de `npm run build` / `docker compose build`.

**Proteção de segredos:**

- Não versione `.env`, tokens, chaves privadas, senhas reais ou dados pessoais.
- Mantenha `.env` fora do Git e use apenas valores fictícios para desenvolvimento.
- Configure segredos de staging no mecanismo privado de configuração do servidor, nunca neste documento ou no repositório.
- Não publique arquivos de configuração preenchidos com valores reais em logs, capturas de tela ou relatórios.
- Na inspeção dos arquivos de ambiente versionados, não foi localizado `.env` nem `.env.example`. Isso não substitui uma varredura formal de segredos em todo o histórico Git; qualquer suspeita de segredo comprometido deve ser tratada com rotação/revogação imediata.

## 5. Validação do ambiente e da versão entregue

Checklist objetivo para confirmar que o ambiente implantado corresponde à versão `v0.4.0`:

| # | Verificação | Como validar | Resultado esperado |
|:--:|:---|:---|:---|
| 1 | Aplicação no ar | abrir a URL do frontend | tela inicial do ServiceHub (Home) carregada |
| 2 | API respondendo | `curl -i https://<frontend>/api/usuarios/csrf` ou `http://localhost:8081/api/usuarios/csrf` | `200` com `{"token": ...}` |
| 3 | Versão entregue | comparar o commit implantado com a tag `v0.4.0` | mesmo commit |
| 4 | Login | entrar com uma conta de teste (cliente e prestador) | redirecionamento ao dashboard do perfil |
| 5 | Fluxo principal | publicar solicitação (cliente), enviar proposta (prestador) e aceitar (cliente) | mensagens de sucesso na interface |
| 6 | CORS | requisição do frontend ao domínio da API | sem erro de CORS no console |
| 7 | Segurança | acessar `/dashboard-cliente` sem login | redirecionamento para `/login` |
| 8 | Suíte automatizada | `./gradlew test jacocoTestReport` e `npm test` | 129 e 29 testes aprovados |

O pipeline `.github/workflows/ci.yml` executa os mesmos testes a cada pull request, além do E2E com
Playwright. Os resultados da Sprint 4 estão em `docs/PLANO-DE-TESTES.md`.

## 6. Limitações conhecidas

- **Sandbox financeiro (Asaas):** integração postergada nesta entrega para priorizar deploy e estabilidade. Não considerar pagamentos reais disponíveis ou validados.
- **Banco de staging:** pode ser limpo periodicamente para permitir a execução das suítes automatizadas. Não depender da permanência de contas ou registros de teste.
- **Compose local:** ainda não reproduz o PostgreSQL de staging; usa H2 em memória e portas diferentes da URL `localhost:8080/api` inicialmente prevista.
- **Contas E2E:** são dados sintéticos e podem precisar ser recriados após a limpeza do banco. A senha indicada para demonstração diverge da senha atualmente presente no teste Playwright.
- **Disponibilidade do staging:** depende do servidor e do proxy reverso mantidos pela equipe. Falhas de acesso devem ser verificadas junto ao responsável pela infraestrutura antes de concluir que a aplicação está indisponível.
- **Frontend em staging:** o domínio do frontend publicado é `https://frontservicehub.jvs.dev.br` (consta em `allowedHosts` do Vite e em CORS/`WebConfig` do backend); o domínio `https://servicehub.jvs.dev.br` é o endereço geral informado para a Entrega 8. Confirme qual dos dois deve ser usado na validação e registre-o na release.
- **Cobertura do frontend:** medida a partir da Entrega 8 (`npm run test:coverage`), mas ainda concentrada em `servicos/` e em componentes isolados; as páginas são cobertas apenas pelo E2E.
