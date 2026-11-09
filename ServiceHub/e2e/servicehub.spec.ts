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
  const login = await fazerLogin(page, conta);

  if (login.status === 401 && login.mensagem === mensagemCredenciaisInvalidas) {
    await criarConta(page, conta);
    const primeiroLogin = await fazerLogin(page, conta);
    expect(primeiroLogin.status, "A conta de teste recém-criada deve permitir login").toBe(200);
  } else {
    expect(login.status, `Falha inesperada no login de ${conta.email}`).toBe(200);
  }

  await expect(page).toHaveURL(dashboard);
}

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
  test.setTimeout(60_000);
  const tituloSolicitacao = "Troca de Chuveiro E2E";

  await entrarOuCadastrar(page, contas.cliente, /\/dashboard-cliente$/);
  await selecionarOuCadastrarEndereco(page);

  const modalSolicitacao = page.getByRole("dialog", { name: "Criar solicitação" });
  await modalSolicitacao.getByLabel("Título do serviço").fill(tituloSolicitacao);
  await modalSolicitacao.getByLabel("Valor estimado (R$)").fill("150.00");
  await modalSolicitacao.getByLabel("Descrição detalhada").fill("Preciso de eletricista");
  await modalSolicitacao.getByRole("checkbox", { name: "Eletricista" }).check();
  await modalSolicitacao.getByRole("button", { name: "Publicar solicitação" }).click();
  await expect(page.getByRole("status")).toContainText("Sua solicitação foi publicada.");
  await expect(page.getByRole("heading", { name: tituloSolicitacao }).first()).toBeVisible();

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
});
