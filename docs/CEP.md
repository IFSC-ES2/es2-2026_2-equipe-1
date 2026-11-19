# Consulta de CEP (ViaCEP)

## Objetivo

Registrar como o ServiceHub consulta endereços a partir do CEP, o que está coberto por
teste automatizado e o que continua sendo verificação manual.

## Implementação

| Item | Detalhe | Fonte |
|:---|:---|:---|
| Onde a consulta acontece | Exclusivamente no frontend, no módulo de serviços | `ServiceHub/src/servicos/cep.ts` |
| Endpoint externo | `GET https://viacep.com.br/ws/{cep}/json/` | `buscarCep()` em `cep.ts` |
| Normalização | `limparCep` mantém só dígitos e limita a 8; `formatarCep` aplica `00000-000` | `cep.ts` |
| Conversão | `logradouro → rua`, `localidade → cidade`, `uf → estado`, `cep` formatado | `buscarCep()` |
| CEP incompleto | Retorna `null` sem chamar a rede | `buscarCep()` |
| CEP inexistente | Resposta com `erro` retorna `null` | `buscarCep()` |
| Falha da API | Lança `Não foi possível consultar o CEP.` | `buscarCep()` |
| Backend | Não consulta CEP; persiste apenas os campos de endereço recebidos | `EnderecoController`, `EnderecoService` |

O backend recebe o endereço já preenchido (`POST /api/enderecos` ou `PUT /api/usuarios/{id}`) e não
tem dependência de rede com o ViaCEP. Por isso, indisponibilidade do ViaCEP não afeta a API nem os
testes de integração.

## Cobertura automatizada

`ServiceHub/tests/cep.test.ts` (Vitest, ambiente `node`, `fetch` mockado) cobre:

1. `limparCep` mantém somente dígitos e corta em oito caracteres;
2. `formatarCep` só aplica o hífen a partir de seis dígitos;
3. CEP incompleto não dispara chamada de rede e devolve `null`;
4. resposta do ViaCEP é convertida no endereço usado pelo formulário, com a URL correta;
5. CEP válido e inexistente (`{"erro": true}`) devolve `null`;
6. falha HTTP da consulta propaga a mensagem de erro esperada pela interface.

O que **não** é coberto:

- chamada real à internet durante o CI (o teste usa mock, o que mantém o pipeline reprodutível e
  independente de terceiros);
- preenchimento do formulário `ModalCriarEndereço` a partir da consulta: o cenário Playwright
  `cliente publica solicitação e prestador envia proposta` digita os campos do endereço
  manualmente.

## Verificação manual recomendada

Antes de uma demonstração, com backend e frontend locais em execução:

1. abrir o modal de cadastro de endereço;
2. informar um CEP válido e conferir os campos preenchidos (rua, bairro, cidade, estado);
3. informar um CEP inexistente e conferir que nenhum campo é preenchido;
4. desligar a rede e confirmar a mensagem de erro amigável.

O resultado dessa verificação deve ser anotado em `docs/PLANO-DE-TESTES.md` como execução manual,
nunca como resultado do CI.
