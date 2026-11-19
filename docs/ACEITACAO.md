# Matriz de testes de aceitação — v1.0.0-rc.1

**Projeto:** ServiceHub
**Data da validação local registrada:** 18/11/2026

Os cenários automatizados abaixo foram executados localmente. Os três cenários
Playwright passaram; os testes backend e frontend também passaram na execução
registrada. O GitHub Actions não executa Playwright, conforme
[Plano de Testes](./PLANO-DE-TESTES.md).

## CT-01 — Cadastro de cliente e prestador

- **Issue / critério:** [Issue #7](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/issues/7);
  cadastro público de cliente e prestador, conforme o critério de MVP de
  cadastro e autenticação em `docs/inception.md` §2.b.1.
- **Passos:** cadastrar uma conta de cliente com CPF; cadastrar uma conta de
  prestador com CNPJ e especialidade; verificar a conclusão do cadastro e
  entrar com as contas criadas.
- **Status:** **Aprovado**.
- **Evidências:** Playwright `cliente publica solicitação e prestador envia
  proposta` (o helper cadastra as contas quando ainda não existem);
  JUnit `UsuarioServiceTest.cadastrar_clienteValido_deveSalvarSenhaComHash` e
  `UsuarioServiceTest.cadastrar_prestadorComEspecialidade_deveSalvarPerfilEEspecialidade`.

## CT-02 — Login com JWT

- **Issue / critério:** [Issue #7](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/issues/7);
  login e acesso autenticado usando JWT stateless.
- **Passos:** autenticar com credenciais válidas; confirmar emissão do token;
  enviar o token no header `Authorization: Bearer` a uma rota protegida; tentar
  acessar rota protegida sem token e com token inválido.
- **Status:** **Aprovado**.
- **Evidências:** JUnit
  `UsuarioJwtIntegrationTest.loginEmiteJwtQueAutenticaRequisicoesStateless`,
  `UsuarioJwtIntegrationTest.rotaProtegidaSemToken_deveRecusarAcesso` e
  `UsuarioJwtIntegrationTest.rotaProtegidaComTokenInvalido_deveRecusarAcesso`;
  Playwright `login apresenta erro para credenciais inválidas`.

## CT-03 — Consulta de CEP e cadastro de endereço

- **Issue / critério:** critério de endereço associado à publicação de uma
  solicitação, conforme `docs/inception.md` §2.b.2; não foi identificada issue
  específica para a consulta de CEP.
- **Passos:** abrir o cadastro de endereço; informar CEP e dados do endereço;
  salvar; selecionar o endereço ao criar uma solicitação e confirmar que ele
  está disponível na listagem.
- **Status:** **Aprovado com ressalvas** — cadastro, associação e listagem de
  endereço foram validados; a chamada real à API externa de CEP não tem
  evidência automatizada nesta execução.
- **Evidências:** JUnit
  `SolicitacaoPropostaIntegrationTest.enderecoCadastrado_deveAparecerNaListagem`;
  Vitest `servicos.test.ts` — `envia o payload correto para criar endereço`;
  Playwright `cliente publica solicitação e prestador envia proposta` (preenche
  o endereço manualmente, sem exercitar a consulta externa).

## CT-04 — Edição do perfil sem alterar e-mail ou tipo

- **Issue / critério:** critério funcional do perfil: editar apenas os campos
  permitidos e preservar e-mail e tipo de perfil; não foi identificada issue
  específica para este critério.
- **Passos:** abrir o perfil; verificar que e-mail e tipo estão bloqueados;
  editar e salvar os campos permitidos; recarregar e conferir a persistência
  sem alteração do e-mail ou tipo.
- **Status:** **Aprovado**.
- **Evidências:** Playwright
  `cliente edita perfil e os dados persistem após recarregar`; JUnit
  `UsuarioServiceTest.atualizarPerfil_deveAlterarSomenteCamposPermitidos`;
  Vitest `ModalPerfilUsuario.test.tsx` — `habilita somente os campos permitidos
  ao entrar em edição` — e `servicos.test.ts` — `atualiza o perfil pelo ID e
  envia somente os campos editáveis`.

## CT-05 — Publicação e filtro de solicitações

- **Issue / critério:** [Issue #8](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/issues/8);
  cliente publica uma solicitação e ela fica disponível para prestador elegível.
- **Passos:** cadastrar endereço; publicar solicitação com especialidade;
  entrar como prestador dessa especialidade; localizar a solicitação na lista
  disponível.
- **Status:** **Aprovado**.
- **Evidências:** Playwright
  `cliente publica solicitação e prestador envia proposta`; JUnit
  `SolicitacaoServicoServiceTest.listarDisponiveis_deveFiltrarPelasEspecialidadesDoPrestador`
  e `SolicitacaoPropostaIntegrationTest.listagensPorPerfil_isolamDadosDoUsuario`.

## CT-06 — Envio de proposta pelo prestador

- **Issue / critério:** [Issue #9](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/issues/9),
  considerado junto ao fluxo de negociação implementado; o critério de MVP
  também inclui disponibilizar a solicitação a profissionais, em
  `docs/inception.md` §2.b.3.
- **Passos:** entrar como prestador elegível; abrir a solicitação publicada;
  informar um valor e enviar a proposta; conferir que ela aparece para o cliente.
- **Status:** **Aprovado**.
- **Evidências:** Playwright
  `cliente publica solicitação e prestador envia proposta`; JUnit
  `SolicitacaoPropostaIntegrationTest.fluxoPropostaAceite_eConcluidoViaHttp`;
  Vitest `servicos.test.ts` — `envia proposta e aceita proposta pelo endpoint correto`.

## CT-07 — Aceite de proposta pelo cliente

- **Issue / critério:** [Issue #9](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/issues/9)
  e critério de ciclo do serviço em `docs/inception.md` §2.b.5. A issue original
  descreve aceite/recusa pelo prestador; o fluxo atual implementa proposta do
  prestador e aceite pelo cliente.
- **Passos:** entrar como cliente proprietário; revisar a proposta recebida;
  aceitá-la; confirmar prestador responsável e mudança do serviço para
  “Em andamento”.
- **Status:** **Aprovado com ressalvas** — o aceite foi exercitado; recusa de
  proposta e notificações ativas permanecem limitações, sem evidência de fluxo
  concluído nesta RC.
- **Evidências:** Playwright
  `cliente publica solicitação e prestador envia proposta`; JUnit
  `SolicitacaoPropostaIntegrationTest.fluxoPropostaAceite_eConcluidoViaHttp`;
  Vitest `servicos.test.ts` — `envia proposta e aceita proposta pelo endpoint correto`.

## CT-08 — Conclusão do serviço

- **Issue / critério:** [Issue #9](https://github.com/IFSC-ES2/es2-2026_2-equipe-1/issues/9)
  e critério de acompanhamento de status do MVP em `docs/inception.md` §2.b.5.
- **Passos:** após o aceite, entrar como cliente proprietário; concluir o
  serviço; conferir o status “Concluída” no cliente e a solicitação na lista de
  serviços concluídos do prestador.
- **Status:** **Aprovado**.
- **Evidências:** Playwright
  `cliente publica solicitação e prestador envia proposta`; JUnit
  `SolicitacaoPropostaIntegrationTest.fluxoPropostaAceite_eConcluidoViaHttp` e
  `SolicitacaoServicoServiceTest.concluir_deveAtualizarStatusQuandoClienteProprietario`.
