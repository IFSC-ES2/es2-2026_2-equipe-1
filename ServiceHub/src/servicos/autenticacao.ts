import type { Especialidade } from "./especialidades";
import { requisitarApi } from "./api";

export type PerfilUsuario = "CLIENTE" | "PRESTADOR";

export type Usuario = {
  id: number;
  nome: string;
  email: string;
  perfil: PerfilUsuario;
  telefone: string;
  especialidades: Especialidade[];
};

export type UsuarioApi = {
  id: number;
  nome: string;
  email: string;
  telefone: string;
  tipoPerfil: PerfilUsuario | "ADMIN";
  especialidades?: Especialidade[];
};

const USUARIO_LOGADO_KEY = "servicehub.usuarioLogado";
function normalizarEmail(email: string) {
  return email.trim().toLowerCase();
}

function mapearUsuario(usuario: UsuarioApi): Usuario {
  return {
    id: usuario.id,
    nome: usuario.nome,
    email: usuario.email,
    perfil: usuario.tipoPerfil === "PRESTADOR" ? "PRESTADOR" : "CLIENTE",
    telefone: usuario.telefone ?? "",
    especialidades: usuario.especialidades ?? [],
  };
}

export function obterDashboardPorPerfil(perfil: PerfilUsuario) {
  return perfil === "CLIENTE" ? "/cliente/dashboard" : "/prestador/dashboard";
}

export function obterUsuarioLogado(): Usuario | null {
  if (typeof window === "undefined") {
    return null;
  }

  try {
    const usuario = window.sessionStorage.getItem(USUARIO_LOGADO_KEY);
    if (!usuario) {
      return null;
    }
    return JSON.parse(usuario) as Usuario;
  } catch {
    window.sessionStorage.removeItem(USUARIO_LOGADO_KEY);
    return null;
  }
}

export function definirUsuarioLogado(usuario: Usuario | null) {
  if (typeof window === "undefined") {
    return;
  }

  try {
    if (!usuario) {
      window.sessionStorage.removeItem(USUARIO_LOGADO_KEY);
      return;
    }

    window.sessionStorage.setItem(USUARIO_LOGADO_KEY, JSON.stringify(usuario));
  } catch {
    // O armazenamento local é apenas um cache de apresentação.
  }
}

export async function validarUsuarioLogado(): Promise<Usuario | null> {
  try {
    const resposta = await requisitarApi("/usuarios/me");
    if (!resposta.ok) {
      definirUsuarioLogado(null);
      return null;
    }

    const usuario = mapearUsuario(await resposta.json() as UsuarioApi);
    definirUsuarioLogado(usuario);
    return usuario;
  } catch {
    definirUsuarioLogado(null);
    return null;
  }
}

function extrairMensagemErro(payload: unknown): string {
  if (typeof payload === "string") {
    return payload;
  }

  if (payload && typeof payload === "object") {
    const candidato = payload as { message?: string; erro?: string; error?: string };
    return candidato.message ?? candidato.erro ?? candidato.error ?? "Não foi possível concluir a operação.";
  }

  return "Não foi possível concluir a operação.";
}

export async function cadastrarUsuario(dados: {
  nome: string;
  email: string;
  senha: string;
  confirmarSenha: string;
  perfil: PerfilUsuario;
  telefone: string;
  especialidades: Especialidade[];
}): Promise<{ ok: true; usuario: Usuario } | { ok: false; mensagem: string }> {
  const nome = dados.nome.trim();
  const email = normalizarEmail(dados.email);
  const telefone = dados.telefone.trim();
  const senha = dados.senha;
  const confirmarSenha = dados.confirmarSenha;

  if (!nome || !email || !telefone || !dados.perfil || !senha.trim()) {
    return { ok: false, mensagem: "Preencha todos os campos obrigatórios." };
  }

  if (!/\S+@\S+\.\S+/.test(email)) {
    return { ok: false, mensagem: "Informe um e-mail válido." };
  }

  if (senha.length < 10) {
    return { ok: false, mensagem: "A senha deve ter pelo menos 10 caracteres." };
  }

  if (senha !== confirmarSenha) {
    return { ok: false, mensagem: "As senhas não conferem." };
  }

  if (dados.perfil === "PRESTADOR" && dados.especialidades.length === 0) {
    return { ok: false, mensagem: "Selecione pelo menos uma especialidade para prestadores." };
  }

  try {
    const resposta = await requisitarApi("/usuarios/cadastro", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nome,
        email,
        senha,
        telefone,
        tipoPerfil: dados.perfil,
        especialidades: dados.especialidades,
      }),
    });

    const payload = await resposta.json().catch(() => null);

    if (!resposta.ok) {
      throw new Error(extrairMensagemErro(payload));
    }

    const usuario = mapearUsuario(payload as UsuarioApi);
    return { ok: true, usuario };
  } catch (erro) {
    return {
      ok: false,
      mensagem: erro instanceof Error ? erro.message : "Não foi possível concluir o cadastro.",
    };
  }
}

export async function entrarUsuario(
  email: string,
  senha: string,
): Promise<{ ok: true; usuario: Usuario } | { ok: false; mensagem: string }> {
  const emailNormalizado = normalizarEmail(email);
  const senhaInformada = senha;

  if (!emailNormalizado || !senhaInformada) {
    return { ok: false, mensagem: "Informe e-mail e senha para continuar." };
  }

  try {
    const resposta = await requisitarApi("/usuarios/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email: emailNormalizado, senha: senhaInformada }),
    });

    const payload = await resposta.json().catch(() => null);

    if (!resposta.ok) {
      throw new Error(extrairMensagemErro(payload));
    }

    const usuario = mapearUsuario(payload as UsuarioApi);
    definirUsuarioLogado(usuario);
    return { ok: true, usuario };
  } catch (erro) {
    return {
      ok: false,
      mensagem: erro instanceof Error ? erro.message : "Credenciais inválidas. Verifique e-mail e senha.",
    };
  }
}

export async function sairUsuario() {
  try {
    await requisitarApi("/usuarios/logout", { method: "POST" });
  } finally {
    definirUsuarioLogado(null);
  }
}
