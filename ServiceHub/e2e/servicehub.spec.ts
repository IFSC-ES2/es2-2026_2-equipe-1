import { expect, test, type Page } from "@playwright/test";

const senha = "ServiceHubE2E!2026";
const mensagemCredenciaisInvalidas = "Credenciais inválidas. Verifique e-mail e senha.";

const contas = {
  cliente: {
    nome: "Cliente E2E ServiceHub",
    email: "servicehub.e2e.cliente@example.test",
    perfil: "CLIENTE",
    documento: "52998224725",
  },
  prestador: {
    nome: "Prestador E2E ServiceHub",
    email: "servicehub.e2e.prestador@example.test",
    perfil: "PRESTADOR",
    documento: "04252011000110",
  },
} as const;

const enderecoTeste = {
  cep: "88101-000",
  rua: "Rua E2E ServiceHub",
  numero: "123",
  bairro: "Centro",
  cidade: "São José",
  estado: "SC",
};

type ContaTeste = (typeof contas)[keyof typeof contas];

async function fazerLogin(page: Page, conta: ContaTeste) {
  await page.goto("/login");
  await page.getByLabel("E-mail").fill(conta.email);
  await page.getByRole("textbox", { name: /^Senha/ }).fill(senha);

  const respostaLogin = page.waitForResponse(
    (resposta) => resposta.url().endsWith("/api/usuarios/login")
      && resposta.request().method() === "POST",
  );
  await page.getByRole("button", { name: "Entrar" }).click();
  const resposta = await respostaLogin;
  const corpo = await resposta.json() as { message?: string };

  return { status: resposta.status(), mensagem: corpo.message };
}

async function criarConta(page: Page, conta: ContaTeste) {
  await page.goto("/cadastro");
  await expect(page.getByRole("heading", { name: "Abra sua conta" })).toBeVisible();
  await page.getByLabel("Nome completo").fill(conta.nome);
  await page.getByLabel("E-mail").fill(conta.email);
  await page.getByLabel("Telefone").fill("(48) 99999-0000");
  await page.getByLabel("Senha", { exact: true }).fill(senha);
  await page.getByLabel("Confirmar senha").fill(senha);

  if (conta.perfil === "PRESTADOR") {
    await page.getByRole("radio", { name: /Prestador/ }).check();
    await page.getByRole("radio", { name: /^CNPJ/ }).check();
    await page.getByRole("textbox", { name: "CNPJ" }).fill(conta.documento);
    await page.getByRole("checkbox", { name: "Eletricista" }).check();
  } else {
    await page.getByRole("textbox", { name: "CPF" }).fill(conta.documento);
  }

  await page.getByRole("button", { name: "Criar conta" }).click();
  await expect(page).toHaveURL(/\/login\?cadastro=sucesso/);
}

async function entrarOuCadastrar(page: Page, conta: ContaTeste, dashboard: RegExp) {
  const usuarioCarregado = page.waitForResponse(
    (resposta) => resposta.url().endsWith("/api/usuarios/me")
      && resposta.request().method() === "GET"
      && resposta.status() === 200,
    { timeout: 60_000 },
  );
  const login = await fazerLogin(page, conta);

  if (login.status === 401 && login.mensagem === mensagemCredenciaisInvalidas) {
    await criarConta(page, conta);
    const primeiroLogin = await fazerLogin(page, conta);
    expect(primeiroLogin.status, "A conta de teste recém-criada deve permitir login").toBe(200);
  } else {
    expect(login.status, `Falha inesperada no login de ${conta.email}`).toBe(200);
  }

  await expect(page).toHaveURL(dashboard);
  await usuarioCarregado;
}

test("login apresenta erro para credenciais inválidas", async ({ page }) => {
  await page.goto("/login");
  await page.getByLabel("E-mail").fill(`inexistente-${Date.now()}@example.test`);
  await page.getByRole("textbox", { name: /^Senha/ }).fill("senha-incorreta");

  const respostaLogin = page.waitForResponse(
    (resposta) => resposta.url().endsWith("/api/usuarios/login")
      && resposta.request().method() === "POST",
  );
  await page.getByRole("button", { name: "Entrar" }).click();
  const resposta = await respostaLogin;

  expect(resposta.status()).toBe(401);
  await expect(page.getByText(mensagemCredenciaisInvalidas)).toBeVisible();
});

test("cliente edita perfil e os dados persistem após recarregar", async ({ page }) => {
  test.setTimeout(60_000);
  await entrarOuCadastrar(page, contas.cliente, /\/dashboard-cliente$/);
  await expect(page.getByText("Você ainda não publicou solicitações.")).toBeVisible();
  await page.locator(".dashboard-profile-trigger").click();

  const modal = page.getByRole("dialog", { name: "Meu perfil" });
  const tipoPerfil = modal.locator('input[name="tipoPerfil"]');
  await expect(modal.getByLabel("E-mail")).toBeDisabled();
  await expect(tipoPerfil).toBeDisabled();
  await expect(tipoPerfil).toHaveValue("Cliente");
  for (const campo of ["Nome", "Telefone", "CEP", "Rua", "Número", "Bairro", "Cidade", "Estado"]) {
    await expect(modal.getByLabel(campo)).toBeDisabled();
  }

  await modal.getByRole("button", { name: "Editar Perfil" }).click();
  const nomeAtualizado = `Cliente E2E Editado ${Date.now()}`;
  await modal.getByLabel("Nome").fill(nomeAtualizado);
  await modal.getByLabel("Telefone").fill("(48) 98888-7777");
  await modal.getByLabel("CEP").fill(enderecoTeste.cep);
  await modal.getByLabel("Rua").fill(enderecoTeste.rua);
  await modal.getByLabel("Número").fill(enderecoTeste.numero);
  await modal.getByLabel("Bairro").fill(enderecoTeste.bairro);
  await modal.getByLabel("Cidade").fill(enderecoTeste.cidade);
  await modal.getByLabel("Estado").fill(enderecoTeste.estado);
  for (const campo of ["Nome", "Telefone", "CEP", "Rua", "Número", "Bairro", "Cidade", "Estado"]) {
    await expect(modal.getByLabel(campo)).toBeEnabled();
  }
  await expect(modal.getByLabel("E-mail")).toBeDisabled();
  await expect(tipoPerfil).toBeDisabled();

  const respostaAtualizacao = page.waitForResponse(
    (resposta) => /^https?:\/\/[^/]+\/api\/usuarios\/\d+$/.test(resposta.url())
      && resposta.request().method() === "PUT",
  );
  await modal.getByRole("button", { name: "Salvar Alterações" }).click();
  const resposta = await respostaAtualizacao;
  expect(resposta.status()).toBe(200);
  await expect(modal.getByRole("status")).toHaveText("Perfil atualizado.");
  await expect(modal.getByLabel("Nome")).toBeDisabled();
  await modal.getByRole("button", { name: "Fechar", exact: true }).click();
  await expect(modal).toBeHidden();

  await page.reload();
  await expect(page).toHaveURL(/\/dashboard-cliente$/);
  await expect(page.getByText("Você ainda não publicou solicitações.")).toBeVisible();
  await page.locator(".dashboard-profile-trigger").click();
  const perfilRecarregado = page.getByRole("dialog", { name: "Meu perfil" });
  await expect(perfilRecarregado.getByLabel("Nome")).toHaveValue(nomeAtualizado);
  await expect(perfilRecarregado.getByLabel("Telefone")).toHaveValue("(48) 98888-7777");
  await expect(perfilRecarregado.getByLabel("Rua")).toHaveValue(enderecoTeste.rua);
  await expect(perfilRecarregado.getByLabel("Estado")).toHaveValue(enderecoTeste.estado);
});

async function selecionarOuCadastrarEndereco(page: Page) {
  await page.getByRole("button", { name: /Criar solicitação/ }).click();
  let modal = page.getByRole("dialog", { name: "Criar solicitação" });
  let seletorEndereco = modal.getByLabel("Endereço do serviço");

  if (!(await seletorEndereco.isVisible())) {
    await modal.getByRole("button", { name: "Cadastrar endereço" }).click();
    const modalEndereco = page.getByRole("dialog", { name: "Cadastrar endereço" });
    await modalEndereco.getByLabel("CEP").fill(enderecoTeste.cep);
    await modalEndereco.getByLabel("Rua").fill(enderecoTeste.rua);
    await modalEndereco.getByLabel("Número").fill(enderecoTeste.numero);
    await modalEndereco.getByLabel("Bairro").fill(enderecoTeste.bairro);
    await modalEndereco.getByLabel("Cidade").fill(enderecoTeste.cidade);
    await modalEndereco.getByLabel("Estado").fill(enderecoTeste.estado);
    await modalEndereco.getByRole("button", { name: "Salvar endereço" }).click();
    await expect(page.getByRole("status")).toContainText("Endereço cadastrado.");

    await page.getByRole("button", { name: /Criar solicitação/ }).click();
    modal = page.getByRole("dialog", { name: "Criar solicitação" });
    seletorEndereco = modal.getByLabel("Endereço do serviço");
  }

  const opcoes = seletorEndereco.locator("option");
  const primeiraOpcaoDisponivel = opcoes.nth(1);
  const enderecoId = await primeiraOpcaoDisponivel.getAttribute("value");
  if (!enderecoId) {
    throw new Error("A conta de teste precisa ter um endereço salvo.");
  }
  await seletorEndereco.selectOption(enderecoId);
}

test("cliente publica solicitação e prestador envia proposta", async ({ page }) => {
  test.setTimeout(90_000);
  const tituloSolicitacao = `Troca de Chuveiro E2E ${Date.now()}`;

  await entrarOuCadastrar(page, contas.cliente, /\/dashboard-cliente$/);
  await selecionarOuCadastrarEndereco(page);

  const modalSolicitacao = page.getByRole("dialog", { name: "Criar solicitação" });
  await modalSolicitacao.getByLabel("Título do serviço").fill(tituloSolicitacao);
  await modalSolicitacao.getByLabel("Valor estimado (R$)").fill("150.00");
  await modalSolicitacao.getByLabel("Descrição detalhada").fill("Preciso de eletricista");
  await modalSolicitacao.getByRole("checkbox", { name: "Eletricista" }).check();
  await modalSolicitacao.getByRole("button", { name: "Publicar solicitação" }).click();
  await expect(page.getByRole("status")).toContainText("Sua solicitação foi publicada.");
  await expect(page.locator(".dashboard-request-accordion").filter({ hasText: tituloSolicitacao })).toBeVisible();

  await page.getByRole("button", { name: "Sair" }).click();
  await expect(page).toHaveURL(/\/login$/);

  await entrarOuCadastrar(page, contas.prestador, /\/dashboard-prestador$/);

  const solicitacao = page.getByRole("article").filter({
    has: page.getByRole("heading", { name: tituloSolicitacao, exact: true }),
  }).first();
  await expect(solicitacao).toBeVisible();
  await solicitacao.getByRole("button", { name: /Enviar Proposta/ }).click();

  const modalProposta = page.getByRole("dialog", { name: "Quanto você cobra?" });
  await expect(modalProposta).toContainText(tituloSolicitacao);
  await modalProposta.getByLabel("Valor proposto (R$)").fill("140.00");
  await modalProposta.getByRole("button", { name: "Enviar proposta" }).click();
  await expect(page.getByRole("status")).toContainText("Sua proposta foi enviada ao cliente.");

  await page.getByRole("button", { name: "Sair" }).click();
  await expect(page).toHaveURL(/\/login$/);
  await entrarOuCadastrar(page, contas.cliente, /\/dashboard-cliente$/);

  await page.getByRole("button", { name: "Criado", exact: true }).click();
  let pedido = page.locator(".dashboard-request-accordion").filter({ hasText: tituloSolicitacao });
  await expect(pedido).toHaveCount(1);
  await pedido.locator(".dashboard-request-toggle").click();
  await expect(pedido).toContainText("Preciso de eletricista");
  await expect(pedido).toContainText(contas.prestador.nome);
  await expect(pedido).toContainText("R$ 140,00");
  await pedido.getByRole("button", { name: /Aceitar Proposta de/ }).click();
  await expect(page.getByRole("status")).toContainText("Proposta aceita. O contrato foi confirmado.");

  await page.getByRole("button", { name: "Em andamento", exact: true }).click();
  pedido = page.locator(".dashboard-request-accordion").filter({ hasText: tituloSolicitacao });
  await expect(pedido).toHaveCount(1);
  await pedido.locator(".dashboard-request-toggle").click();
  await expect(pedido).toContainText("Profissional responsável");
  await expect(pedido.getByRole("link", { name: /WhatsApp/ })).toBeVisible();
  await pedido.getByRole("button", { name: "Concluir Serviço" }).click();
  await expect(page.getByRole("status")).toContainText("Serviço concluído.");

  await page.getByRole("button", { name: "Sair" }).click();
  await expect(page).toHaveURL(/\/login$/);
  await entrarOuCadastrar(page, contas.prestador, /\/dashboard-prestador$/);
  await page.getByRole("tab", { name: /Serviços concluídos/ }).click();
  await expect(page.getByRole("heading", { name: "Serviços concluídos" })).toBeVisible();
  await expect(page.getByRole("article").filter({ hasText: tituloSolicitacao })).toContainText("Concluído");
  await expect(page.getByRole("button", { name: "Eletricista", exact: true })).toBeVisible();

  await page.getByRole("button", { name: "Sair" }).click();
  await expect(page).toHaveURL(/\/login$/);
  await entrarOuCadastrar(page, contas.cliente, /\/dashboard-cliente$/);
  await page.getByRole("button", { name: "Concluído", exact: true }).click();
  pedido = page.locator(".dashboard-request-accordion").filter({ hasText: tituloSolicitacao });
  await expect(pedido).toHaveCount(1);
  await expect(pedido.locator(".dashboard-request-status")).toHaveText("Concluída");

  page.once("dialog", (dialog) => dialog.accept());
  await pedido.getByRole("button", { name: `Ações da solicitação ${tituloSolicitacao}` }).click();
  await pedido.getByRole("button", { name: "Excluir solicitação" }).click();
  await expect(page.getByRole("status")).toContainText("Solicitação excluída.");
  await expect(pedido).toHaveCount(0);
});
