# Deploy e validação — Entrega 8 (Sprint 4) e Release Candidate v1.0.0-rc.1

## 1. Visão geral do ambiente

O ambiente de staging publica:

| Recurso | Endereço público | Base das chamadas |
|:---|:---|:---|
| Frontend (SPA) | <https://frontservicehub.jvs.dev.br> | — |
| API (Spring Boot) | <https://servicehub.jvs.dev.br> | `https://servicehub.jvs.dev.br/api` |

O domínio da API **está versionado** neste documento, no `README.md`, no registro da RC e nas labels
do Traefik em `docker-compose.yml` (`traefik.http.routers.servicehub-api.rule=Host(\`servicehub.jvs.dev.br\`)`).
O frontend recebe esse endereço em `VITE_API_URL`, incluindo o sufixo `/api`.

A publicação foi declarada como feita via Coolify, a partir da branch `main`, com um banco PostgreSQL de staging. A configuração do Coolify, do proxy e do banco fica fora deste repositório; as etapas abaixo registram o procedimento operacional informado e apontam o que precisa ser conferido no painel.

O staging é destinado à validação da equipe e da disciplina. Não deve ser tratado como ambiente de produção nem receber dados pessoais ou credenciais reais.

> O Compose reproduz a topologia de aplicação e banco do staging (backend, frontend de produção e PostgreSQL com volume nomeado). Ele não reproduz o proxy HTTPS nem o domínio público, que continuam sendo responsabilidades do servidor de staging.

## 2. Execução local

### Pré-requisitos

- Git.
- JDK 25 para executar o backend diretamente.
- Node.js 24 e npm para executar o frontend e os testes.
- Docker Desktop em execução.
- Docker Compose v2 ou superior (`docker compose`).

### Clonar o repositório

```bash
git clone https://github.com/IFSC-ES2/es2-2026_2-equipe-1.git
cd es2-2026_2-equipe-1
```

### Execução Local do Backend

O Spring Boot exige a variável `JWT_SECRET` para iniciar. Os comandos abaixo criam 32 bytes aleatórios e os exportam em Base64. Execute-os a partir da raiz do repositório; a variável ficará disponível apenas no terminal atual.

**PowerShell (Windows):**

```powershell
cd back-end
$bytes = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
.\gradlew.bat bootRun
```

**Bash (Linux/macOS):**

```bash
cd back-end
export JWT_SECRET=$(openssl rand -base64 32)
./gradlew bootRun
```

O backend local fica disponível em <http://localhost:8081/api>. Gere um segredo novo para cada terminal ou ambiente; não versione nem compartilhe o valor.

### Variáveis locais e Compose

Copie `.env.example` para `.env` e substitua os valores de exemplo. O `.env` não deve ser versionado. `JWT_SECRET` e `POSTGRES_PASSWORD` são obrigatórios para o Compose.

No PowerShell, a partir da raiz do repositório:

```powershell
$bytes = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
$env:VITE_API_URL = "http://localhost:8081/api"
docker compose up --build -d
```

No Bash (Linux/macOS), também a partir da raiz:

```bash
export JWT_SECRET=$(openssl rand -base64 32)
export VITE_API_URL=http://localhost:8081/api
docker compose up --build -d
```

As variáveis ficam disponíveis apenas no terminal atual. Para acompanhar a inicialização:

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

### Topologia reproduzida pelo Compose

O Compose é um ambiente local de desenvolvimento, não uma reprodução do staging:

1. O serviço PostgreSQL usa o volume `servicehub_db_data`, portanto os dados sobrevivem a `docker compose restart`.
2. O backend usa PostgreSQL no Compose e H2 apenas na execução direta e nos testes.
3. O frontend é compilado com Vite e servido pelo Nginx na porta `5173`.
4. As portas locais são `5173` (frontend) e `8081` (API).

## 3. Deploy no staging via Coolify

O procedimento abaixo registra a publicação operacional informada pela equipe. Os recursos e segredos do Coolify não estão versionados; confirme no painel o repositório, domínio da API, gatilho de deploy e valores atuais antes de publicar.

1. Integre as alterações aprovadas na branch `main`.
2. No Coolify, confira que os recursos de frontend e backend apontam para o repositório e para a branch `main`. Se o webhook/auto-deploy estiver habilitado, a integração da branch inicia uma publicação; caso contrário, acione **Deploy** no painel para o commit desejado.
3. Confirme os domínios dos recursos: frontend em <https://frontservicehub.jvs.dev.br> e API no endereço público exibido pelo recurso backend. Configure `VITE_API_URL` com esse endereço e `/api`.
4. Configure as variáveis nos recursos correspondentes do Coolify:

| Recurso | Variável | Valor esperado |
|---|---|---|
| Frontend | `VITE_API_URL` | URL pública da API com `/api`, por exemplo `https://<dominio-da-api>/api`. |
| Backend | `JWT_SECRET` | Segredo aleatório de 32 bytes codificado em Base64; não reutilize o valor local nem o registre no Git. |
| Backend | `SPRING_DATASOURCE_URL` | JDBC URL do PostgreSQL de staging, usando o host interno do serviço no Coolify: `jdbc:postgresql://<host-interno>:5432/<base>`. |
| Backend | `SPRING_DATASOURCE_USERNAME` | Usuário dedicado da base de staging. |
| Backend | `SPRING_DATASOURCE_PASSWORD` | Senha dedicada, armazenada somente no painel do Coolify. |

5. Dispare a publicação e confira o estado e os logs dos dois recursos. Registre no controle da entrega o SHA do commit efetivamente implantado e a data da publicação.

> O driver PostgreSQL e as propriedades parametrizadas estão versionados. No Coolify, configure as mesmas variáveis da tabela e confirme nos logs que o backend iniciou com a URL JDBC do banco de staging.

### Checklist pós-deploy

- [ ] O Coolify concluiu o deploy sem erro e mostra o SHA esperado da branch `main`.
- [ ] A página inicial do frontend retorna HTTP 200 em <https://frontservicehub.jvs.dev.br>.
- [ ] O login com uma conta de teste autorizada funciona pela interface.
- [ ] Após o login, o frontend mantém o token JWT na chave `token` do `localStorage` e as chamadas autenticadas enviam `Authorization: Bearer <token>`.
- [ ] Uma chamada autenticada a `<VITE_API_URL>/usuarios/me` retorna HTTP 200 e os dados do usuário.
- [ ] O console do navegador não aponta falhas de CORS e os logs do backend não mostram erro de conexão com o banco.
- [ ] Se a conexão PostgreSQL estiver habilitada na versão implantada, um dado de teste permanece disponível após reiniciar o backend.

O repositório não configura Spring Boot Actuator nem uma rota dedicada `/actuator/health`. Para a API, use login e a chamada autenticada a `/usuarios/me` como smoke checks; não interprete apenas a resposta do frontend como prova de API ou persistência.

### Contas de teste e dados de demonstração

| Ambiente | Onde obter a conta | Observação |
|:---|:---|:---|
| Staging | Cadastro público em <https://frontservicehub.jvs.dev.br> ou `POST https://servicehub.jvs.dev.br/api/usuarios/cadastro` | A equipe não mantém contas fixas no staging. Os dados podem ser limpos periodicamente (risco R16); recrie a conta se necessário. |
| Local | As contas sintéticas da suíte E2E (`servicehub.e2e.cliente@example.test` / `servicehub.e2e.prestador@example.test`, senha `ServiceHubE2E!2026`) | São criadas pela própria suíte na base H2 local. **São credenciais locais**: não existem no staging e não devem ser reutilizadas nele. |

Para a demonstração, cadastre um cliente e um prestador no ambiente escolhido, publique uma
solicitação com especialidade `Eletricista` e deixe uma solicitação sem proposta para o passo de envio
de orçamento do roteiro.

## 4. Testes locais e CI

### Testes executados pelo CI

O workflow `.github/workflows/ci.yml` roda em pull requests direcionados a `main`:

- Arquivos obrigatórios: `check_files` verifica a presença dos documentos essenciais.
- Backend: `./gradlew test jacocoTestReport --no-daemon`, incluindo testes JUnit unitários e de integração, com publicação do relatório HTML como artefato.
- Frontend: `npm ci`, `npm run build`, `npm test -- --run` (Vitest) e `npm run lint`.
- Configuração: validação sintática do Compose (`docker compose config --quiet`).

O CI **não** executa Playwright/E2E, **não** roda `npm run test:coverage` (cobertura do frontend) e
**não** faz varredura de segredos. Ele também não publica o staging.

### Testes End-to-End locais com Playwright

`npm run test:e2e` é executado localmente, contra frontend e backend locais e a base H2 isolada em memória. A configuração Playwright inicia os servidores locais; ela não deve ser apontada ao staging compartilhado. No PowerShell, na raiz do repositório, gere o segredo e execute:

```powershell
$bytes = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
cd ServiceHub
npm ci
npx playwright install chromium
npm run test:e2e
```

Os cenários usam contas sintéticas e as cadastram quando ainda não existem. Eles criam e alteram dados; a base H2 de teste é descartada ao reiniciar o backend. Para ver o navegador, execute `npm run test:e2e:visivel`. Não use esses testes contra o staging ou uma base com dados reais.

`ServiceHub/playwright.config.ts` sobe o frontend (`npm run dev`) e o backend a partir de `back-end/`,
escolhendo o wrapper do sistema: `gradlew.bat bootRun` no Windows e `./gradlew bootRun` no
Linux/macOS. Se `JWT_SECRET` não estiver definido, a configuração usa um segredo local só para os
testes. Em Linux/macOS, o wrapper precisa estar executável (`chmod +x back-end/gradlew`, já garantido
pelo Git em clones novos). Se o backend já estiver em execução em `http://localhost:8081`, o Playwright
o reaproveita. Para apontar para outra API, defina `PLAYWRIGHT_API_URL`; para outro frontend, defina
`PLAYWRIGHT_BASE_URL`.

## 5. Variáveis de ambiente e autenticação

O backend usa autenticação JWT stateless: o login emite um token com expiração de uma hora; o frontend o armazena em `localStorage` (chave `token`) e envia `Authorization: Bearer <token>` nas chamadas protegidas. O backend não depende de sessão HTTP para autenticar as chamadas da API. O logout local remove o token do navegador; como o JWT não tem revogação no servidor, um token emitido permanece válido até expirar.

| Variável | Finalidade | Exemplo/observação |
|---|---|---|
| `JWT_SECRET` | Segredo usado para assinatura e validação do JWT; obrigatório para iniciar o backend. | Gere 32 bytes aleatórios e codifique em Base64. Passe pelo ambiente do terminal local ou pelo painel do Coolify. |
| `VITE_API_URL` | URL base usada pelo frontend para acessar a API. | Local: `http://localhost:8081/api`. Staging: endereço público da API configurado no Coolify, com `/api`. |
| `SPRING_DATASOURCE_URL` | JDBC URL do banco do backend. | Staging PostgreSQL: `jdbc:postgresql://<host-interno>:5432/<base>`. No checkout atual, a configuração padrão é H2 em memória. |
| `SPRING_DATASOURCE_USERNAME` | Usuário da conexão JDBC. | Defina diretamente no terminal local ou no painel; não registre credenciais no repositório. |
| `SPRING_DATASOURCE_PASSWORD` | Senha da conexão JDBC. | Defina diretamente no terminal local ou no painel; não registre credenciais no repositório. |

> **Importante para o build do frontend:** `ServiceHub/src/servicos/api.ts` lê `import.meta.env.VITE_API_URL` com valor padrão `http://localhost:8081/api`. O Vite **embute** a variável no bundle em tempo de build. Para servir o frontend em outro domínio, defina `VITE_API_URL` **antes** de `npm run build` / `docker compose build`.

**Proteção de segredos:**

- Não versione tokens, chaves privadas, senhas reais ou dados pessoais.
- Configure segredos de staging no mecanismo privado de configuração do servidor, nunca neste documento ou no repositório.
- Não publique arquivos de configuração preenchidos com valores reais em logs, capturas de tela ou relatórios.

## 6. Validação do ambiente e da versão entregue

Use este checklist após o deploy para confirmar a revisão implantada e os fluxos essenciais:

| # | Verificação | Como validar | Resultado esperado |
|:--:|:---|:---|:---|
| 1 | Revisão implantada | conferir o SHA no Coolify contra o commit aprovado em `main` | SHA correspondente e deploy concluído |
| 2 | Frontend | abrir <https://frontservicehub.jvs.dev.br> ou executar `curl -i https://frontservicehub.jvs.dev.br/` | HTTP 200 e Home carregada |
| 3 | Login e API | entrar com conta de teste autorizada; depois consultar `<VITE_API_URL>/usuarios/me` com `Authorization: Bearer <token>` | login concluído e `/usuarios/me` responde HTTP 200 |
| 4 | Autenticação no browser | conferir a chave `token` em `localStorage` e o header da requisição protegida | `Authorization: Bearer ...` presente |
| 5 | Banco | verificar logs do backend e persistência de um registro de teste após reiniciar o serviço | conexão sem erros; registro persiste se PostgreSQL estiver realmente integrado |
| 6 | CORS | observar a chamada do frontend à API no console/network do navegador | sem bloqueio CORS |

O projeto não expõe endpoint dedicado do Actuator para healthcheck. A página inicial e o fluxo login + `/usuarios/me` são smoke checks; confirme separadamente a conexão e a persistência do banco.

O workflow `.github/workflows/ci.yml` não faz deploy. Os resultados de testes registrados para a Sprint 4 estão em `docs/PLANO-DE-TESTES.md`.

## 7. Limitações conhecidas

- **Sandbox financeiro (Asaas):** integração postergada nesta entrega para priorizar deploy e estabilidade. Não considerar pagamentos reais disponíveis ou validados.
- **Compose local:** usa `jdbc:h2:mem:testdb` em memória e perde os dados ao reiniciar o backend; não reproduz o PostgreSQL de staging.
- **Frontend em execução direta:** `npm run dev` usa Vite para desenvolvimento; no Compose o
  Dockerfile compila o bundle e o serve com Nginx.
- **Execução direta do backend:** sem variáveis `SPRING_DATASOURCE_*`, usa H2 em memória para desenvolvimento e testes; essa configuração não é o perfil recomendado para staging.
- **Coolify:** a configuração do serviço, os gatilhos e os valores de ambiente não estão versionados (o domínio público da API, `https://servicehub.jvs.dev.br`, está versionado neste documento e nas labels do Compose). O procedimento depende de conferência no painel e registro do SHA implantado.
- **Playwright:** os testes E2E são locais, alteram dados e não fazem parte do pipeline CI; execute-os somente com a base H2 isolada.
- **Staging:** a disponibilidade depende do servidor e do proxy mantidos pela equipe; não presuma que a URL pública ou a persistência estejam operacionais sem repetir o checklist pós-deploy.
- **Cobertura do frontend:** o CI executa Vitest, mas não Playwright nem `npm run test:coverage`; a cobertura do frontend (20,81% de linhas na auditoria da RC) é uma medição local e não é produzida pelo workflow atual.
