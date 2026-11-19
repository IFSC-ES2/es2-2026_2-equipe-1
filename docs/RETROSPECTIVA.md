# Retrospectiva da Entrega 9 - Release Candidate

**Projeto:** ServiceHub  
**Marco:** Entrega 9 - Release Candidate  
**Base de evidências:** código atual, documentação das entregas anteriores e histórico de commits/PRs

## 1. Comparativo Planejado vs. Entregue

O planejamento original incluía os 88 horas do MVP, entre elas a integração do pagamento via Asaas.
Durante o desenvolvimento, a equipe fez replanejamento explícito do escopo e deixou registrada a
integração financeira como pendência de uma etapa futura. A tabela abaixo apresenta a situação real
do Release Candidate.

| Categoria | Item / funcionalidade | Situação na Entrega 9 | Evidência e consideração |
|:---|:---|:---|:---|
| **100% Concluído** | Cadastro de usuários | Concluído | Cadastro de clientes e prestadores, validação de perfil e documentos, tratamento de duplicidade e senha com BCrypt. |
| **100% Concluído** | Login com autenticação JWT | Concluído | Login gera token; a API opera em modo stateless e envia e valida o `Bearer` token nas requisições protegidas. |
| **100% Concluído** | Endereço com consulta de CEP | Concluído | Cadastro e associação de endereço ao perfil, com dados de logradouro, número, bairro, cidade, estado e CEP. |
| **100% Concluído** | Perfil do usuário | Concluído | Consulta e atualização de dados pessoais, telefone, especialidades e endereço, respeitando o proprietário do recurso. |
| **100% Concluído** | Publicação, listagem e filtro de solicitações | Concluído | Cliente publica solicitação; dashboards listam solicitações próprias, disponíveis e por especialidade. |
| **100% Concluído** | Envio de proposta | Concluído | Prestador envia proposta vinculada à solicitação, com persistência e atualização em caso de reenvio pelo mesmo prestador. |
| **100% Concluído** | Aceite de proposta | Concluído | Cliente aceita a proposta de um prestador; valor e prestador são associados e a solicitação passa para `EM_ANDAMENTO`. |
| **100% Concluído** | Conclusão do serviço | Concluído | Fluxo integrado permite alterar a solicitação de `EM_ANDAMENTO` para `CONCLUIDO`, com autorização do proprietário. |
| **Entregue Parcialmente / Com Ressalvas** | Notificação ativa em tempo real da negociação | Parcialmente / ressalva | Não há canal ativo de notificação por WebSocket ou mecanismo equivalente. A negociação ocorre por reconsulta da API e atualização das telas. |
| **Entregue Parcialmente / Com Ressalvas** | Recusa registrada pelo prestador | Parcialmente / ressalva | Não existe um fluxo dedicado em que o prestador registre formalmente uma recusa. Na versão atual, o cliente ignora propostas indesejadas ou aceita outra, sem uma transição persistida de recusa pelo prestador. |
| **Removido do Escopo** | Pagamento integrado com gateway Asaas | Removido desta entrega | Não iniciado ou integrado. Foram removidos também criação de cobrança, liquidação financeira, status financeiro e tratamento de webhooks. |

### Justificativa para a remoção do Asaas

A integração com o Asaas foi retirada do Release Candidate por dependência externa e pela necessidade
de priorizar funcionalidades já centrais do MVP que deveriam estar estáveis para a demonstração:
autenticação, autorização, perfil, solicitações, propostas, conclusão e ambiente de staging. A
integração envolve cadastro/homologação em sandbox, estados assíncronos, endpoints de webhook,
tratamento de falhas e testes de ponta a ponta. Esses requisitos técnicos consomem tempo além do
escopo disponível nesta entrega e não foram contabilizados como concluídos. Portanto, a retirada foi
uma decisão de prazo justificada pelos riscos R03 e R11, não uma conclusão parcial: não há fluxo
de pagamento disponível no sistema.

## 2. Retrospectiva do Projeto

### Evolução do processo

O desenvolvimento foi incremental. Os commits concentram funcionalidades, correções, testes e
documentação em marcos identificáveis, enquanto merges com Pull Requests preservaram a integração
das mudanças na branch principal. Também há rastreabilidade de correções de segurança, autenticação,
testes e infraestrutura. A equipe evoluiu da integração por sessão para uma API stateless com JWT,
alinhando o estado da implementação com o histórico de Pull Requests e a documentação técnica.

O processo se beneficiou de:

- incrementalidade de commits e separação de tipos de mudança;
- integração de mudanças por PRs e merge na branch principal;
- CI acionado em Pull Requests para validar arquivos essenciais, backend, frontend e Docker Compose;
- documentação viva, com ADRs, planejamento de sprints, riscos, métricas e plano de testes;
- replanejamento do backlog para retirar itens de maior dependência externa quando necessário.

O uso de PRs e CI reduziu riscos de integração, mas o lead time continua dependente da revisão dos
registros reais do GitHub. O pipeline atual é suficiente como gate básico; o E2E precisa ser mantido
como check explícito e estável para evitar que a validação de navegador dependa apenas de execução
manual.

### Decisões arquiteturais

A autenticação iniciou com sessão HTTP, Spring Security, cookie `JSESSIONID` e proteção CSRF. Com a
evolução do frontend e das necessidades de staging, a implementação migrou para JWT stateless:

- o backend desativou CSRF e passou a usar `Authorization: Bearer <token>`;
- a sessão deixou de ser criada pelo Spring Security;
- o frontend guarda o token e o envia nas chamadas à API;
- os perfis continuam protegidos por papel e algumas operações verificam o proprietário do recurso;
- CORS e origens permitidas foram ajustados para o ambiente local e publicado.

Essa decisão reduziu a dependência de cookies de sessão entre chamadas, mas exige gestão segura do
segredo `JWT_SECRET`, definição adequada da validade do token e armazenamento correto no cliente.
O histórico já registra correções nessa área e ela deve ser revisada em futuras mudanças de segurança.

### Qualidade e testes

A qualidade evoluiu de cobertura não medida para uma suíte relevante no backend e em partes do
frontend. A última evidência registrada na entrega anterior informa:

- 129 testes backend aprovados;
- 93,37% de cobertura de linhas no backend e 80,80% de branches;
- 29 testes frontend aprovados e 25,22% de cobertura de linhas;
- 3 cenários E2E relatados como aprovados;
- build e lint do frontend sem erros;
- testes de integração cobrindo login, autorização, proposta, aceite, conclusão e proteção contra
  requisições sem controle JWT/CSRF conforme a arquitetura vigente.

Os principais pontos positivos são a cobertura das regras de cadastro e do fluxo HTTP de negociação
e a detecção de regressões em sessão, CORS e perfil. Os pontos a melhorar são testes de componente
das páginas React, concorrência real no bloqueio de propostas e maior proteção contra falhas do
pipeline E2E. A cobertura baixa do frontend não invalida os testes existentes, mas limita a
confiança em comparações numéricas isoladas.

### Gestão de riscos

O registro de riscos mostrou aprendizagem ao longo das sprints: atraso e aumento de escopo foram
trabalhados por priorização do MVP; falhas de CI foram mitigadas com checks no PR; autorização,
CSRF/CORS e exposição de dados receberam testes de integração; o Docker local foi alinhado ao
PostgreSQL persistente e ao frontend de produção, enquanto a publicação continua dependente da
infraestrutura externa de staging;
e a integração Asaas foi explicitamente postergada em vez de anunciada como concluída.

Os riscos restantes mais relevantes são:

- validação do comportamento do PostgreSQL no ambiente de staging após a publicação;
- configuração do proxy e do serviço de deploy do staging, que permanecem externos ao repositório;
- URL da API incorreta embutida no bundle do frontend;
- limpeza periódica dos dados de demonstração no staging;
- ausência de notificação ativa e recusa dedicada no fluxo de negociação;
- cobertura insuficiente de páginas no frontend.

Como lição para releases futuras, o escopo deve ser fechado antes da implementação ou replanejado
com evidência; riscos externos precisam de spike técnico e validação no ambiente real; e CI, testes
de segurança e métricas devem permanecer como gates obrigatórios para integração na principal.
