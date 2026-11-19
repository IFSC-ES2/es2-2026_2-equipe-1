import { afterEach, describe, expect, it, vi } from "vitest";
import { buscarCep, formatarCep, limparCep } from "../src/servicos/cep";

const respostaViaCep = {
  cep: "88101000",
  logradouro: "Rua das Flores",
  complemento: "até 100",
  bairro: "Centro",
  localidade: "São José",
  uf: "SC",
};

function respostaJson(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    status,
    headers: { "Content-Type": "application/json" },
  });
}

describe("consulta de CEP", () => {
  afterEach(() => {
    vi.restoreAllMocks();
    vi.unstubAllGlobals();
  });

  it("mantém somente os dígitos do CEP, limitado a oito", () => {
    expect(limparCep("881.01-000999")).toBe("88101000");
    expect(limparCep("abc")).toBe("");
  });

  it("formata o CEP apenas quando há oito dígitos", () => {
    expect(formatarCep("88101000")).toBe("88101-000");
    expect(formatarCep("88101")).toBe("88101");
  });

  it("não consulta a API quando o CEP está incompleto", async () => {
    const fetchMock = vi.fn();
    vi.stubGlobal("fetch", fetchMock);

    await expect(buscarCep("8810")).resolves.toBeNull();
    expect(fetchMock).not.toHaveBeenCalled();
  });

  it("converte a resposta do ViaCEP no endereço usado pelo formulário", async () => {
    const fetchMock = vi.fn().mockResolvedValue(respostaJson(respostaViaCep));
    vi.stubGlobal("fetch", fetchMock);

    await expect(buscarCep("88101-000")).resolves.toEqual({
      cep: "88101-000",
      rua: "Rua das Flores",
      complemento: "até 100",
      bairro: "Centro",
      cidade: "São José",
      estado: "SC",
    });
    expect(fetchMock).toHaveBeenCalledWith("https://viacep.com.br/ws/88101000/json/", { signal: undefined });
  });

  it("retorna nulo para CEP válido e inexistente", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(respostaJson({ erro: true })));

    await expect(buscarCep("99999999")).resolves.toBeNull();
  });

  it("propaga erro quando a consulta externa falha", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(respostaJson({}, 500)));

    await expect(buscarCep("88101000")).rejects.toThrow("Não foi possível consultar o CEP.");
  });
});
