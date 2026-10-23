# Manutenção e Reengenharia de Software — Entrega 7

**Marco:** Entrega 7 - Sprint 3  
**Data:** 22 de Outubro de 2026  
**Foco:** Refatoração Orientada a Design e Integridade de Domínio  

---

## 1. Motivação e Identificação dos Problemas Técnicos

Durante a revisão do estado do MVP ao final da Sprint 2, a equipe identificou duas fragilidades estruturais no código-fonte:

### Problema 1: Alto Acoplamento e Falta de Rastreabilidade nos Orçamentos (Design de Domínio)
* **Diagnóstico:** A solicitação de serviço armazenava a decisão de atendimento diretamente na entidade `SolicitacaoServico`. Isso favorecia a sobrescrita unilateral de valores sem a aprovação prévia do cliente e não mantinha o histórico dos orçamentos ofertados pelos profissionais interessados.
* **Risco:** Condições de corrida (*race conditions*) em acessos simultâneos e acoplamento excessivo na camada de serviço.

### Problema 2: Retenção Indevida de Sessão no Navegador (Issue #24 - Bug de Alternância de Contas)
* **Diagnóstico:** Ao realizar o login com um perfil (ex.: Cliente) e tentar alternar para outro (ex.: Prestador), os cookies de sessão mantidos pelo Spring Security não eram limpos de forma explícita.
* **Risco:** Falha nos requisitos de isolamento e segurança (ISO/IEC 25010) e impedimento de testes ponta a ponta sem a limpeza manual do cache do navegador.

---

## 2. Solução de Reengenharia Aplicada

A equipe realizou uma refatoração profunda no modelo de negócio e na arquitetura do backend:

### 2.1 Refatoração do Domínio (`PropostaServico`)
1. **Criação da Entidade de Proposta:** Introduziu-se a classe `PropostaServico`, contendo o valor ofertado, data de envio e o vínculo com o `Usuario` prestador e a `SolicitacaoServico`.
2. **Encapsulamento de Comportamento:** A regra para adicionar propostas foi movida para dentro do agregador de domínio, utilizando o método `adicionarProposta`.
3. **Restrição de Integridade no Banco (Unicidade):** Adicionou-se uma restrição única (*Unique Constraint*) no banco de dados para a combinação `(solicitacao_id, prestador_id)`, garantindo no nível do SGBD que um prestador só envie uma proposta por chamado.
4. **Isolamento Concorrente com Transação:** No `SolicitacaoServicoService`, aplicou-se `@Transactional` e a consulta com bloqueio `findByIdForUpdate` para evitar que duas propostas sejam aceitas ao mesmo tempo.

### 2.2 Reengenharia do Fluxo de Autenticação e Sessão
1. **Endpoint de Encerramento de Sessão:** Implementou-se no `UsuarioController` a invalidação explícita da `HttpSession` e a expiração dos cookies de sessão e CSRF no Spring Security.
2. **Desacoplamento no Frontend:** No arquivo `api.ts`, isolou-se a função de logout para garantir o redirecionamento limpo da interface.

---

## 3. Validação Objetiva e Preservação do Comportamento

Para garantir que a refatoração preservou o comportamento correto e não introduziu regressões, executou-se uma suíte de validação automatizada em três níveis:

1. **Testes Unitários de Serviço (Backend - Gradle):**
   * `SolicitacaoServicoServiceTest.java`: Valida o fluxo de inclusão de propostas, alteração de status para `EM_ANDAMENTO` e bloqueio de propostas por usuários não proprietários.
   * `UsuarioServiceAdditionalTest.java` e `EnderecoServiceTest.java`: Garantem que o cadastro e o mapeamento de endereços mantiveram a retrocompatibilidade.
2. **Testes de Integração de Segurança (Backend - Spring Security):**
   * `SecurityIntegrationTest.java`: Testa os endpoints protegidos, o envio de tokens CSRF e valida que requisições efetuadas após o logout retornam HTTP 401 (Unauthorized).
3. **Testes do Frontend (Vitest / React):**
   * `tests/servicos.test.ts`: Testes automatizados no frontend para validar o envio dos formulários de proposta e a conversão dos objetos JSON da API REST.

---

## 4. Comparação de Métricas Antes vs. Depois da Reengenharia

Como exigência do edital da Entrega 7, a tabela abaixo apresenta os indicadores mensuráveis comparando o estado do repositório na branch `main` (Sprint 2) em relação à branch `entrega-7` (Sprint 3):

| Métrica / Indicador | Antes (`main` - Sprint 2) | Depois (`entrega-7` - Sprint 3) | Interpretação / Ganho Técnico |
|:---|:---:|:---:|:---|
| **Entidades de Domínio Específicas para Orçamento** | 0 (Vínculo solto em `SolicitacaoServico`) | 1 (`PropostaServico`) | **Ganho de Coesão:** Isola regras de orçamentos e valores da solicitação original. |
| **Garantia de Proposta Única por Prestador** | 0 (Regra inexistente no banco) | 1 (Restrição Única no SGBD) | **Garantia de Integridade:** Impede propostas duplicadas no nível da persistência. |
| **Controle de Concorrência em Aceites** | Ausente (Risco de aceites simultâneos) | Presente (`findByIdForUpdate` + Transação) | **Segurança de Transação:** Bloqueio pessimista no banco para impedir aceite duplo. |
| **Suíte de Testes no Frontend** | 0 testes automatizados no React | Suíte Vitest em `tests/servicos.test.ts` | **Evolução da Qualidade:** Validação automatizada na camada web. |
| **Invalidação de Sessão (Issue #24)** | 0% (Sessão presa nos cookies) | 100% (Invalidação e logout no Spring Security) | **Atributo de Segurança:** Resolução do bug de alternância de contas. |

---

## 5. Relação com os Atributos de Qualidade (ISO/IEC 25010)

* **Manutenibilidade:** A criação da classe `PropostaServico` separou a gestão de orçamentos da solicitação principal, simplificando futuras alterações de código sem efeitos colaterais.
* **Confiabilidade:** O controle transacional