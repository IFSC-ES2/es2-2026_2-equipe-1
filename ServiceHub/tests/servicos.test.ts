import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import {
  clienteAceitarProposta,
  criarEndereco,
  enviarProposta,
  listarEnderecos,
  requisitarApi,
} from "../src/servicos/api";
import {
  cadastrarUsuario,
  definirUsuarioLogado,
  entrarUsuario,
  obterDashboardPorPerfil,
  obterUsuarioLogado,
  validarUsuarioLogado,
} from "../src/servicos/autenticacao";

const usuarioApi = {
  id: 8,
  nome: "Maria",
  email: "MARIA@EMAIL.COM",
  telefone: "9999",
  tipoPerfil: "PRESTADOR" as const,
  especialidades: ["ELETRICISTA" as const],
};

function respostaJson(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    status,
    headers: { "Content-Type": "application/json" },
  });
}

describe("serviço de API", () => {
  beforeEach(() => {
    vi.stubGlobal("document", { cookie: "XSRF-TOKEN=token%2Bseguro" });
  });

  afterEach(() => {
    vi.restoreAllMocks();
    vi.unstubAllGlobals();
  });

  it("faz requisição GET com credenciais e sem token CSRF", async () => {
    const fetchMock = vi.fn().mockResolvedValue(respostaJson([]));
    vi.stubGlobal("fetch", fetchMock);

    await requisitarApi("/solicitacao");

    expect(fetchMock).toHaveBeenCalledWith(
      "http://localhost:8080/api/solicitacao",
      expect.objectContaining({ method: "GET", credentials: "include" }),
    );
    const init = fetchMock.mock.calls[0][1] as RequestInit;
    expect(new Headers(init.headers).has("X-XSRF-TOKEN")).toBe(false);
  });

  it("inclui Content-Type e CSRF em operações mutáveis", async () => {
    const fetchMock = vi.fn().mockResolvedValue(respostaJson({ ok: true }));
    vi.stubGlobal("fetch", fetchMock);

    await requisitarApi("/usuarios/logout", { method: "POST", body: "{}" });

    const init = fetchMock.mock.calls[0][1] as RequestInit;
    const headers = new Headers(init.headers);
    expect(headers.get("Content-Type")).toBe("application/json");
    expect(headers.get("X-XSRF-TOKEN")).toBe("token+seguro");
  });

  it("busca o token no endpoint CSRF quando o cookie não existe", async () => {
    vi.stubGlobal("document", { cookie: "" });
    const fetchMock = vi
      .fn()
      .mockResolvedValueOnce(respostaJson({ token: "ok" }))
      .mockResolvedValueOnce(respostaJson({ ok: true }));
    vi.stubGlobal("fetch", fetchMock);
    vi.stubGlobal("document", { get cookie() { return fetchMock.mock.calls.length ? "XSRF-TOKEN=novo-token" : ""; } });

    await requisitarApi("/usuarios/logout", { method: "POST" });

    expect(fetchMock).toHaveBeenNthCalledWith(
      1,
      "http://localhost:8080/api/usuarios/csrf",
      { credentials: "include" },
    );
    expect(new Headers((fetchMock.mock.calls[1][1] as RequestInit).headers).get("X-XSRF-TOKEN")).toBe("novo-token");
  });

  it("converte endereços da API para o modelo usado pela tela", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(respostaJson([
      { id: 3, logradouro: "Rua A", numero: "10", complemento: "Casa", bairro: "Centro", cidade: "São José", estado: "SC", cep: "88100" },
    ])));

    await expect(listarEnderecos()).resolves.toEqual([
      { id: "3", rua: "Rua A", numero: "10", bairro: "Centro", cidade: "São José", estado: "SC" },
    ]);
  });

  it("envia o payload correto para criar endereço", async () => {
    const fetchMock = vi.fn().mockResolvedValue(respostaJson({
      id: 4, logradouro: "Rua B", numero: "20", complemento: "", bairro: "Bairro", cidade: "São José", estado: "SC", cep: "88101",
    }, 201));
    vi.stubGlobal("fetch", fetchMock);

    await criarEndereco({ cep: "88101", rua: "Rua B", numero: "20", complemento: "", bairro: "Bairro", cidade: "São José", estado: "SC" });

    expect(JSON.parse((fetchMock.mock.calls[0][1] as RequestInit).body as string)).toEqual({
      cep: "88101", logradouro: "Rua B", numero: "20", complemento: "", bairro: "Bairro", cidade: "São José", estado: "SC",
    });
  });

  it("envia proposta e aceita proposta pelo endpoint correto", async () => {
    const fetchMock = vi.fn().mockImplementation(() => respostaJson({ id: 1 }));
    vi.stubGlobal("fetch", fetchMock);

    await enviarProposta(1, 175.5);
    await clienteAceitarProposta(1, 8);

    expect(fetchMock.mock.calls[0][0]).toBe("http://localhost:8080/api/solicitacoes/1/propostas");
    expect(JSON.parse((fetchMock.mock.calls[0][1] as RequestInit).body as string)).toEqual({ valorProposto: 175.5 });
    expect(fetchMock.mock.calls[1][0]).toBe("http://localhost:8080/api/solicitacoes/1/propostas/8/aceitar");
  });

  it("transforma erro JSON da API em mensagem útil", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(respostaJson({ message: "Solicitação inválida" }, 400)));

    await expect(listarEnderecos()).rejects.toThrow("Solicitação inválida");
  });
});

describe("serviço de autenticação", () => {
  beforeEach(() => {
    const storage = new Map<string, string>();
    vi.stubGlobal("window", {
      sessionStorage: {
        getItem: (key: string) => storage.get(key) ?? null,
        setItem: (key: string, value: string) => storage.set(key, value),
        removeItem: (key: string) => storage.delete(key),
      },
    });
  });

  afterEach(() => {
    vi.restoreAllMocks();
    vi.unstubAllGlobals();
  });

  it("normaliza usuário e escolhe dashboard por perfil", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(respostaJson(usuarioApi)));
    const resultado = await validarUsuarioLogado();

    expect(resultado).toMatchObject({ id: 8, perfil: "PRESTADOR", telefone: "9999" });
    expect(obterDashboardPorPerfil("CLIENTE")).toBe("/dashboard-cliente");
    expect(obterDashboardPorPerfil("PRESTADOR")).toBe("/dashboard-prestador");
  });

  it("remove sessão quando /me retorna erro", async () => {
    definirUsuarioLogado({ id: 1, nome: "A", email: "a@a.com", perfil: "CLIENTE", telefone: "", especialidades: [] });
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(respostaJson({}, 401)));

    await expect(validarUsuarioLogado()).resolves.toBeNull();
    expect(obterUsuarioLogado()).toBeNull();
  });

  it("valida cadastro antes de chamar a API", async () => {
    const fetchMock = vi.fn();
    vi.stubGlobal("fetch", fetchMock);

    await expect(cadastrarUsuario({ nome: "", email: "invalido", senha: "123", confirmarSenha: "123", perfil: "CLIENTE", telefone: "", especialidades: [] }))
      .resolves.toEqual({ ok: false, mensagem: "Preencha todos os campos obrigatórios." });
    expect(fetchMock).not.toHaveBeenCalled();
  });

  it("recusa senha divergente e prestador sem especialidade", async () => {
    const base = { nome: "Maria", email: "maria@email.com", senha: "senha-segura", telefone: "999", especialidades: [] as never[] };
    await expect(cadastrarUsuario({ ...base, confirmarSenha: "outra", perfil: "CLIENTE" })).resolves.toMatchObject({ ok: false, mensagem: "As senhas não conferem." });
    await expect(cadastrarUsuario({ ...base, confirmarSenha: base.senha, perfil: "PRESTADOR" })).resolves.toMatchObject({ ok: false, mensagem: "Selecione pelo menos uma especialidade para prestadores." });
  });

  it("realiza login, normaliza email e persiste o usuário", async () => {
    const fetchMock = vi.fn().mockResolvedValue(respostaJson(usuarioApi));
    vi.stubGlobal("fetch", fetchMock);
    vi.stubGlobal("document", { cookie: "XSRF-TOKEN=token" });

    const resultado = await entrarUsuario("  Maria@Email.com ", "senha-segura");

    expect(resultado.ok).toBe(true);
    expect(JSON.parse((fetchMock.mock.calls[0][1] as RequestInit).body as string).email).toBe("maria@email.com");
    expect(obterUsuarioLogado()).toMatchObject({ id: 8, perfil: "PRESTADOR" });
  });

  it("retorna mensagem do backend quando login falha", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(respostaJson({ message: "Credenciais inválidas" }, 401)));
    vi.stubGlobal("document", { cookie: "XSRF-TOKEN=token" });

    await expect(entrarUsuario("a@email.com", "errada")).resolves.toEqual({ ok: false, mensagem: "Credenciais inválidas" });
  });
});
