# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: servicehub.spec.ts >> cliente publica solicitação e prestador envia proposta
- Location: e2e\servicehub.spec.ts:192:1

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('.dashboard-request-accordion').filter({ hasText: 'Troca de Chuveiro E2E 1794448077353' })
Expected: visible
Timeout: 5000ms
Error: element(s) not found

Call log:
  - Expect "toBeVisible" locator('.dashboard-request-accordion').filter({ hasText: 'Troca de Chuveiro E2E 1794448077353' }) with timeout 5000ms
  - waiting for locator('.dashboard-request-accordion').filter({ hasText: 'Troca de Chuveiro E2E 1794448077353' })

```

```yaml
- banner:
  - link "ServiceHub, página inicial":
    - /url: /
    - text: ServiceHub
  - button "Olá, Cliente"
  - button "Sair"
- main:
  - paragraph: SEU ESPAÇO SERVICEHUB
  - heading "Olá, Cliente. Vamos resolver?" [level=1]:
    - text: Olá, Cliente.
    - emphasis: Vamos resolver?
  - paragraph: Acompanhe seus pedidos e combine o serviço com o profissional certo.
  - button "+ Adicionar endereço"
  - button "Criar solicitação"
  - status: Sua solicitação foi publicada.
  - paragraph: ACOMPANHAMENTO
  - heading "Minhas solicitações" [level=2]
  - text: "1"
  - group "Filtrar solicitações por status":
    - button "Todas" [pressed]
    - button "Criado"
    - button "Em andamento"
    - button "Concluído"
  - article:
    - text: "Troca de Chuveiro E2E 1794448077353 Pedido #1 · 11 de nov. de 2026, 22:47"
    - strong: R$ 150,00
    - text: Criado
    - button "Ações da solicitação Troca de Chuveiro E2E 1794448077353"
    - paragraph: Preciso de eletricista
    - text: ", · , Eletricista Aguardando profissional"
```

# Test source

```ts
  106 | 
  107 | test("cliente edita perfil e os dados persistem após recarregar", async ({ page }) => {
  108 |   test.setTimeout(60_000);
  109 |   await entrarOuCadastrar(page, contas.cliente, /\/dashboard-cliente$/);
  110 |   await expect(page.getByText("Você ainda não publicou solicitações.")).toBeVisible();
  111 |   await page.locator(".dashboard-profile-trigger").click();
  112 | 
  113 |   const modal = page.getByRole("dialog", { name: "Meu perfil" });
  114 |   const tipoPerfil = modal.locator('input[name="tipoPerfil"]');
  115 |   await expect(modal.getByLabel("E-mail")).toBeDisabled();
  116 |   await expect(tipoPerfil).toBeDisabled();
  117 |   await expect(tipoPerfil).toHaveValue("Cliente");
  118 |   for (const campo of ["Nome", "Telefone", "CEP", "Rua", "Número", "Bairro", "Cidade", "Estado"]) {
  119 |     await expect(modal.getByLabel(campo)).toBeDisabled();
  120 |   }
  121 | 
  122 |   await modal.getByRole("button", { name: "Editar Perfil" }).click();
  123 |   const nomeAtualizado = `Cliente E2E Editado ${Date.now()}`;
  124 |   await modal.getByLabel("Nome").fill(nomeAtualizado);
  125 |   await modal.getByLabel("Telefone").fill("(48) 98888-7777");
  126 |   await modal.getByLabel("CEP").fill(enderecoTeste.cep);
  127 |   await modal.getByLabel("Rua").fill(enderecoTeste.rua);
  128 |   await modal.getByLabel("Número").fill(enderecoTeste.numero);
  129 |   await modal.getByLabel("Bairro").fill(enderecoTeste.bairro);
  130 |   await modal.getByLabel("Cidade").fill(enderecoTeste.cidade);
  131 |   await modal.getByLabel("Estado").fill(enderecoTeste.estado);
  132 |   for (const campo of ["Nome", "Telefone", "CEP", "Rua", "Número", "Bairro", "Cidade", "Estado"]) {
  133 |     await expect(modal.getByLabel(campo)).toBeEnabled();
  134 |   }
  135 |   await expect(modal.getByLabel("E-mail")).toBeDisabled();
  136 |   await expect(tipoPerfil).toBeDisabled();
  137 | 
  138 |   const respostaAtualizacao = page.waitForResponse(
  139 |     (resposta) => /^https?:\/\/[^/]+\/api\/usuarios\/\d+$/.test(resposta.url())
  140 |       && resposta.request().method() === "PUT",
  141 |   );
  142 |   await modal.getByRole("button", { name: "Salvar Alterações" }).click();
  143 |   const resposta = await respostaAtualizacao;
  144 |   expect(resposta.status()).toBe(200);
  145 |   await expect(modal.getByRole("status")).toHaveText("Perfil atualizado.");
  146 |   await expect(modal.getByLabel("Nome")).toBeDisabled();
  147 |   await modal.getByRole("button", { name: "Fechar", exact: true }).click();
  148 |   await expect(modal).toBeHidden();
  149 | 
  150 |   await page.reload();
  151 |   await expect(page).toHaveURL(/\/dashboard-cliente$/);
  152 |   await expect(page.getByText("Você ainda não publicou solicitações.")).toBeVisible();
  153 |   await page.locator(".dashboard-profile-trigger").click();
  154 |   const perfilRecarregado = page.getByRole("dialog", { name: "Meu perfil" });
  155 |   await expect(perfilRecarregado.getByLabel("Nome")).toHaveValue(nomeAtualizado);
  156 |   await expect(perfilRecarregado.getByLabel("Telefone")).toHaveValue("(48) 98888-7777");
  157 |   await expect(perfilRecarregado.getByLabel("Rua")).toHaveValue(enderecoTeste.rua);
  158 |   await expect(perfilRecarregado.getByLabel("Estado")).toHaveValue(enderecoTeste.estado);
  159 | });
  160 | 
  161 | async function selecionarOuCadastrarEndereco(page: Page) {
  162 |   await page.getByRole("button", { name: /Criar solicitação/ }).click();
  163 |   let modal = page.getByRole("dialog", { name: "Criar solicitação" });
  164 |   let seletorEndereco = modal.getByLabel("Endereço do serviço");
  165 | 
  166 |   if (!(await seletorEndereco.isVisible())) {
  167 |     await modal.getByRole("button", { name: "Cadastrar endereço" }).click();
  168 |     const modalEndereco = page.getByRole("dialog", { name: "Cadastrar endereço" });
  169 |     await modalEndereco.getByLabel("CEP").fill(enderecoTeste.cep);
  170 |     await modalEndereco.getByLabel("Rua").fill(enderecoTeste.rua);
  171 |     await modalEndereco.getByLabel("Número").fill(enderecoTeste.numero);
  172 |     await modalEndereco.getByLabel("Bairro").fill(enderecoTeste.bairro);
  173 |     await modalEndereco.getByLabel("Cidade").fill(enderecoTeste.cidade);
  174 |     await modalEndereco.getByLabel("Estado").fill(enderecoTeste.estado);
  175 |     await modalEndereco.getByRole("button", { name: "Salvar endereço" }).click();
  176 |     await expect(page.getByRole("status")).toContainText("Endereço cadastrado.");
  177 | 
  178 |     await page.getByRole("button", { name: /Criar solicitação/ }).click();
  179 |     modal = page.getByRole("dialog", { name: "Criar solicitação" });
  180 |     seletorEndereco = modal.getByLabel("Endereço do serviço");
  181 |   }
  182 | 
  183 |   const opcoes = seletorEndereco.locator("option");
  184 |   const primeiraOpcaoDisponivel = opcoes.nth(1);
  185 |   const enderecoId = await primeiraOpcaoDisponivel.getAttribute("value");
  186 |   if (!enderecoId) {
  187 |     throw new Error("A conta de teste precisa ter um endereço salvo.");
  188 |   }
  189 |   await seletorEndereco.selectOption(enderecoId);
  190 | }
  191 | 
  192 | test("cliente publica solicitação e prestador envia proposta", async ({ page }) => {
  193 |   test.setTimeout(90_000);
  194 |   const tituloSolicitacao = `Troca de Chuveiro E2E ${Date.now()}`;
  195 | 
  196 |   await entrarOuCadastrar(page, contas.cliente, /\/dashboard-cliente$/);
  197 |   await selecionarOuCadastrarEndereco(page);
  198 | 
  199 |   const modalSolicitacao = page.getByRole("dialog", { name: "Criar solicitação" });
  200 |   await modalSolicitacao.getByLabel("Título do serviço").fill(tituloSolicitacao);
  201 |   await modalSolicitacao.getByLabel("Valor estimado (R$)").fill("150.00");
  202 |   await modalSolicitacao.getByLabel("Descrição detalhada").fill("Preciso de eletricista");
  203 |   await modalSolicitacao.getByRole("checkbox", { name: "Eletricista" }).check();
  204 |   await modalSolicitacao.getByRole("button", { name: "Publicar solicitação" }).click();
  205 |   await expect(page.getByRole("status")).toContainText("Sua solicitação foi publicada.");
> 206 |   await expect(page.locator(".dashboard-request-accordion").filter({ hasText: tituloSolicitacao })).toBeVisible();
      |                                                                                                     ^ Error: expect(locator).toBeVisible() failed
  207 | 
  208 |   await page.getByRole("button", { name: "Sair" }).click();
  209 |   await expect(page).toHaveURL(/\/login$/);
  210 | 
  211 |   await entrarOuCadastrar(page, contas.prestador, /\/dashboard-prestador$/);
  212 | 
  213 |   const solicitacao = page.getByRole("article").filter({
  214 |     has: page.getByRole("heading", { name: tituloSolicitacao, exact: true }),
  215 |   }).first();
  216 |   await expect(solicitacao).toBeVisible();
  217 |   await solicitacao.getByRole("button", { name: /Enviar Proposta/ }).click();
  218 | 
  219 |   const modalProposta = page.getByRole("dialog", { name: "Quanto você cobra?" });
  220 |   await expect(modalProposta).toContainText(tituloSolicitacao);
  221 |   await modalProposta.getByLabel("Valor proposto (R$)").fill("140.00");
  222 |   await modalProposta.getByRole("button", { name: "Enviar proposta" }).click();
  223 |   await expect(page.getByRole("status")).toContainText("Sua proposta foi enviada ao cliente.");
  224 | 
  225 |   await page.getByRole("button", { name: "Sair" }).click();
  226 |   await expect(page).toHaveURL(/\/login$/);
  227 |   await entrarOuCadastrar(page, contas.cliente, /\/dashboard-cliente$/);
  228 | 
  229 |   await page.getByRole("button", { name: "Criado", exact: true }).click();
  230 |   let pedido = page.locator(".dashboard-request-accordion").filter({ hasText: tituloSolicitacao });
  231 |   await expect(pedido).toHaveCount(1);
  232 |   await pedido.locator(".dashboard-request-toggle").click();
  233 |   await expect(pedido).toContainText("Preciso de eletricista");
  234 |   await expect(pedido).toContainText(contas.prestador.nome);
  235 |   await expect(pedido).toContainText("R$ 140,00");
  236 |   await pedido.getByRole("button", { name: /Aceitar Proposta de/ }).click();
  237 |   await expect(page.getByRole("status")).toContainText("Proposta aceita. O contrato foi confirmado.");
  238 | 
  239 |   await page.getByRole("button", { name: "Em andamento", exact: true }).click();
  240 |   pedido = page.locator(".dashboard-request-accordion").filter({ hasText: tituloSolicitacao });
  241 |   await expect(pedido).toHaveCount(1);
  242 |   await pedido.locator(".dashboard-request-toggle").click();
  243 |   await expect(pedido).toContainText("Profissional responsável");
  244 |   await expect(pedido.getByRole("link", { name: /WhatsApp/ })).toBeVisible();
  245 |   await pedido.getByRole("button", { name: "Concluir Serviço" }).click();
  246 |   await expect(page.getByRole("status")).toContainText("Serviço concluído.");
  247 | 
  248 |   await page.getByRole("button", { name: "Sair" }).click();
  249 |   await expect(page).toHaveURL(/\/login$/);
  250 |   await entrarOuCadastrar(page, contas.prestador, /\/dashboard-prestador$/);
  251 |   await page.getByRole("tab", { name: /Serviços concluídos/ }).click();
  252 |   await expect(page.getByRole("heading", { name: "Serviços concluídos" })).toBeVisible();
  253 |   await expect(page.getByRole("article").filter({ hasText: tituloSolicitacao })).toContainText("Concluído");
  254 |   await expect(page.getByRole("button", { name: "Eletricista", exact: true })).toBeVisible();
  255 | 
  256 |   await page.getByRole("button", { name: "Sair" }).click();
  257 |   await expect(page).toHaveURL(/\/login$/);
  258 |   await entrarOuCadastrar(page, contas.cliente, /\/dashboard-cliente$/);
  259 |   await page.getByRole("button", { name: "Concluído", exact: true }).click();
  260 |   pedido = page.locator(".dashboard-request-accordion").filter({ hasText: tituloSolicitacao });
  261 |   await expect(pedido).toHaveCount(1);
  262 |   await expect(pedido.locator(".dashboard-request-status")).toHaveText("Concluída");
  263 | 
  264 |   page.once("dialog", (dialog) => dialog.accept());
  265 |   await pedido.getByRole("button", { name: `Ações da solicitação ${tituloSolicitacao}` }).click();
  266 |   await pedido.getByRole("button", { name: "Excluir solicitação" }).click();
  267 |   await expect(page.getByRole("status")).toContainText("Solicitação excluída.");
  268 |   await expect(pedido).toHaveCount(0);
  269 | });
  270 | 
```