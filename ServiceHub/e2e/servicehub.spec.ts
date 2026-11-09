import { expect, test, type Page } from "@playwright/test";

const senha = "ServiceHubE2E!2026";
const sufixoAleatorio = () => Math.random().toString(36).slice(2, 8);

function gerarCpfValido(): string {
  const digitos = Array.from({ length: 9 }, () => Math.floor(Math.random() * 10));
  if (new Set(digitos).size === 1) {
    digitos[8] = (digitos[8] + 1) % 10;
  }

  const calcularDigito = (numeros: number[], pesoInicial: number) => {
    const soma = numeros.reduce((total, numero, indice) => total + numero * (pesoInicial - indice), 0);
    const resto = soma % 11;
    return resto < 2 ? 0 : 11 - resto;
  };

  digitos.push(calcularDigito(digitos, 10));
  digitos.push(calcularDigito(digitos, 11));
  return digitos.join("");
}

async function criarConta(
  page: Page,
  perfil: "Cliente" | "Prestador",
  email: string,
  cpf?: string,
) {
  await page.goto("/");
  await page.getByRole("link", { name: /Criar conta/ }).first().click();
  await expect(page.getByRole("heading", { name: "Abra sua conta" })).toBeVisible();
  await page.getByLabel("Nome completo").fill(`Teste ${perfil} ${sufixoAleatorio()}`);
  await page.getByLabel("E-mail").fill(email);
  await page.getByLabel("Telefone").fill("(48) 99999-0000");

  if (cpf) {
    await page.getByLabel("CPF").fill(cpf);
  }

  await page.getByLabel("Senha", { exact: true }).fill(senha);
  await page.getByLabel("Confirmar senha").fill(senha);

  if (perfil === "Prestador") {
    await page.getByRole("radio", { name: /Prestador/ }).check();
    await page.getByRole("checkbox", { name: "Eletricista" }).check();
  }

  await page.getByRole("button", { name: "Criar conta" }).click();
  await expect(page).toHaveURL(/\/login\?cadastro=sucesso/);
}

async function fazerLogin(page: Page, email: string) {
  await page.getByLabel("E-mail").fill(email);
  await page.getByRole("textbox", { name: /^Senha/ }).fill(senha);
  await page.getByRole("button", { name: "Entrar" }).click();
}

test("cliente publica solicitação e prestador envia proposta", async ({ page }) => {
  const identificador = `${Date.now()}-${sufixoAleatorio()}`;
  const emailCliente = `cliente-${identificador}@example.test`;
  const emailPrestador = `prestador-${identificador}@example.test`;
  const cpfCliente = gerarCpfValido();
  const tituloSolicitacao = `Troca de Chuveiro ${identificador}`;
  const endereco = {
    cep: "88101-000",
    rua: `Rua E2E ${identificador}`,
    numero: "123",
    bairro: "Centro",
    cidade: "São José",
    estado: "SC",
  };

  await page.goto("/");
  await criarConta(page, "Cliente", emailCliente, cpfCliente);
  await fazerLogin(page, emailCliente);
  await expect(page).toHaveURL(/\/dashboard-cliente$/);

  await page.getByRole("button", { name: /Adicionar endereço/ }).click();
  const dialogEndereco = page.getByRole("dialog", { name: "Cadastrar endereço" });
  await dialogEndereco.getByLabel("CEP").fill(endereco.cep);
  await dialogEndereco.getByLabel("Rua").fill(endereco.rua);
  await dialogEndereco.getByLabel("Número").fill(endereco.numero);
  await dialogEndereco.getByLabel("Bairro").fill(endereco.bairro);
  await dialogEndereco.getByLabel("Cidade").fill(endereco.cidade);
  await dialogEndereco.getByLabel("Estado").fill(endereco.estado);
  await dialogEndereco.getByRole("button", { name: "Salvar endereço" }).click();
  await expect(page.getByRole("status")).toContainText("Endereço cadastrado.");

  await page.getByRole("button", { name: /Criar solicitação/ }).click();
  const dialogSolicitacao = page.getByRole("dialog", { name: "Criar solicitação" });
  await dialogSolicitacao.getByLabel("Título do serviço").fill(tituloSolicitacao);
  await dialogSolicitacao.getByLabel("Valor estimado (R$)").fill("150.00");
  await dialogSolicitacao.getByLabel("Descrição detalhada").fill("Preciso de eletricista");
  await dialogSolicitacao.getByRole("checkbox", { name: "Eletricista" }).check();
  await dialogSolicitacao.getByLabel("Endereço do serviço").selectOption({
    label: `${endereco.rua}, ${endereco.numero} - ${endereco.bairro}, ${endereco.cidade}/${endereco.estado}`,
  });
  await dialogSolicitacao.getByRole("button", { name: "Publicar solicitação" }).click();
  await expect(page.getByRole("status")).toContainText("Sua solicitação foi publicada.");
  await expect(page.getByRole("heading", { name: tituloSolicitacao })).toBeVisible();

  await page.getByRole("button", { name: "Sair" }).click();
  await expect(page).toHaveURL(/\/login$/);

  await criarConta(page, "Prestador", emailPrestador);
  await fazerLogin(page, emailPrestador);
  await expect(page).toHaveURL(/\/dashboard-prestador$/);

  const solicitacao = page.getByRole("article").filter({
    has: page.getByRole("heading", { name: tituloSolicitacao }),
  });
  await expect(solicitacao).toBeVisible();
  await solicitacao.getByRole("button", { name: /Enviar Proposta/ }).click();

  const dialogProposta = page.getByRole("dialog", { name: "Quanto você cobra?" });
  await expect(dialogProposta).toContainText(tituloSolicitacao);
  await dialogProposta.getByLabel("Valor proposto (R$)").fill("140.00");
  await dialogProposta.getByRole("button", { name: "Enviar proposta" }).click();
  await expect(page.getByRole("status")).toContainText("Sua proposta foi enviada ao cliente.");
});
