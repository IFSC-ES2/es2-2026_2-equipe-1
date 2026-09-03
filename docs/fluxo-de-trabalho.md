# Fluxo de Trabalho - ServiceHub

## 1. Branch de desenvolvimento

Cada entrega X deve ter uma branch:

`entrega-x`

As alterações da entrega devem ser desenvolvidas nessa branch.

## 2. Pull Requests

As alterações não devem ser enviadas diretamente para a branch `main`.

O fluxo utilizado é:

1. Desenvolver na branch `entrega-x`.
2. Criar um Pull Request para `main`.
3. Aguardar a revisão de outro integrante.
4. Obter pelo menos 1 aprovação.
5. Verificar os checks obrigatórios.
6. Realizar o merge para `main`.

## 3. Revisão

Todo Pull Request deve ser revisado por pelo menos um integrante da equipe.

O revisor deve verificar principalmente:

* funcionamento da alteração;
* organização do código/documentação;
* atendimento aos critérios da tarefa;
* execução dos checks;
* atualização da documentação quando necessário.

## 4. Integração

A branch `main` é protegida e não permite alterações diretas.

A integração deve ocorrer somente através de Pull Request e merge, mantendo o histórico do projeto.

## 5. Template de Pull Request

A equipe utiliza o arquivo:

`.github/PULL_REQUEST_TEMPLATE.md`

O template possui um checklist mínimo para orientar a revisão.

## 6. Checks

Os Pull Requests devem executar automaticamente os checks definidos no CI do projeto antes da integração.
