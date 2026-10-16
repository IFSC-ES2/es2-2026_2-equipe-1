import type { DadosSolicitacao, EnderecoSelecionavel } from "../componentes/modal/ModalCriarSolicitacao";
import type { Endereco } from "../componentes/modal/ModalCriarEndereço";
import type { Especialidade } from "./especialidades";

const API_URL = "http://localhost:8080/api";

function obterCookie(nome: string): string | null {
  const prefixo = `${nome}=`;
  const cookie = document.cookie.split(";").map((parte) => parte.trim()).find((parte) => parte.startsWith(prefixo));
  return cookie ? decodeURIComponent(cookie.slice(prefixo.length)) : null;
}

async function obterTokenCsrf(): Promise<string> {
  let token = obterCookie("XSRF-TOKEN");
  if (token) {
    return token;
  }

  const resposta = await fetch(`${API_URL}/usuarios/csrf`, { credentials: "include" });
  if (!resposta.ok) {
    throw new Error("Não foi possível iniciar uma sessão segura.");
  }

  token = obterCookie("XSRF-TOKEN");
  if (!token) {
    throw new Error("O servidor não forneceu o token de segurança.");
  }
  return token;
}

export async function requisitarApi(rota: string, opcoes: RequestInit = {}): Promise<Response> {
  const metodo = (opcoes.method ?? "GET").toUpperCase();
  const headers = new Headers(opcoes.headers);
  if (opcoes.body && !headers.has("Content-Type")) {
    headers.set("Content-Type", "application/json");
  }

  if (!["GET", "HEAD", "OPTIONS"].includes(metodo)) {
    headers.set("X-XSRF-TOKEN", await obterTokenCsrf());
  }

  return fetch(`${API_URL}${rota}`, {
    ...opcoes,
    method: metodo,
    headers,
    credentials: "include",
  });
}

async function erroDaResposta(resposta: Response, padrao: string): Promise<Error> {
  const detalhe = await resposta.text();
  if (!detalhe) {
    return new Error(`${padrao} (${resposta.status}).`);
  }

  try {
    const payload: unknown = JSON.parse(detalhe);
    if (payload && typeof payload === "object") {
      const problema = payload as { detail?: string; message?: string; title?: string };
      return new Error(problema.detail ?? problema.message ?? problema.title ?? detalhe);
    }
  } catch {
    return new Error(detalhe);
  }

  return new Error(detalhe);
}

export async function logout(): Promise<void> {
  const resposta = await requisitarApi("/usuarios/logout", { method: "POST" });
  if (!resposta.ok) {
    throw await erroDaResposta(resposta, "Não foi possível encerrar a sessão");
  }
}

type EnderecoApi = {
  id: number;
  logradouro: string;
  numero: string;
  complemento: string;
  bairro: string;
  cidade: string;
  estado: string;
  cep: string;
};

export type UsuarioSolicitacaoApi = {
  id: number;
  nome: string;
  email: string;
  telefone: string;
  tipoPerfil: "CLIENTE" | "PRESTADOR" | "ADMIN";
  especialidades: Especialidade[];
};

export type SolicitacaoApi = {
  id: number;
  titulo: string;
  descricao: string;
  valorEstimado: number;
  statusSolicitacao: "SOLICITADO" | "ACEITO" | "RECUSADO" | "EM_ANDAMENTO" | "CONCLUIDO" | "CANCELADO";
  dataCriacao: string;
  enderecoAtendimento: EnderecoApi;
  especialidadesRequiridas: Especialidade[];
  cliente: Pick<UsuarioSolicitacaoApi, "id" | "nome"> | null;
  prestador: UsuarioSolicitacaoApi | null;
};

async function requisicao<T>(rota: string, opcoes?: RequestInit): Promise<T> {
  const resposta = await requisitarApi(rota, {
    headers: { "Content-Type": "application/json" },
    ...opcoes,
  });

  if (!resposta.ok) {
    throw await erroDaResposta(resposta, "Não foi possível concluir a operação");
  }

  return resposta.json() as Promise<T>;
}

function converterEndereco(endereco: EnderecoApi): EnderecoSelecionavel {
  return {
    id: String(endereco.id),
    rua: endereco.logradouro,
    numero: endereco.numero,
    bairro: endereco.bairro,
    cidade: endereco.cidade,
    estado: endereco.estado,
  };
}

export async function listarEnderecos(): Promise<EnderecoSelecionavel[]> {
  const enderecos = await requisicao<EnderecoApi[]>("/enderecos");
  return enderecos.map(converterEndereco);
}

export async function criarEndereco(endereco: Endereco): Promise<EnderecoSelecionavel> {
  const enderecoCriado = await requisicao<EnderecoApi>("/enderecos", {
    method: "POST",
    body: JSON.stringify({
      cep: endereco.cep,
      logradouro: endereco.rua,
      numero: endereco.numero,
      complemento: endereco.complemento,
      bairro: endereco.bairro,
      cidade: endereco.cidade,
      estado: endereco.estado,
    }),
  });

  return converterEndereco(enderecoCriado);
}

export async function listarSolicitacoes(): Promise<SolicitacaoApi[]> {
  return requisicao<SolicitacaoApi[]>("/solicitacao");
}

export async function listarMinhasSolicitacoes(): Promise<SolicitacaoApi[]> {
  return requisicao<SolicitacaoApi[]>("/solicitacao/minhas");
}

export async function listarSolicitacoesDisponiveis(): Promise<SolicitacaoApi[]> {
  return requisicao<SolicitacaoApi[]>("/solicitacoes/disponiveis");
}

export async function listarMeusChamados(): Promise<SolicitacaoApi[]> {
  return requisicao<SolicitacaoApi[]>("/solicitacoes/meus-chamados");
}

export async function aceitarSolicitacao(id: number, valorProposto: number): Promise<SolicitacaoApi> {
  return requisicao<SolicitacaoApi>(`/solicitacoes/${id}/aceitar`, {
    method: "POST",
    body: JSON.stringify({ valorProposto }),
  });
}

export async function criarSolicitacao(
  dados: DadosSolicitacao,
): Promise<SolicitacaoApi> {
  return requisicao<SolicitacaoApi>("/solicitacao", {
    method: "POST",
    body: JSON.stringify({
      titulo: dados.titulo,
      descricao: dados.descricao,
      valorEstimado: Number(dados.valorEstimado),
      enderecoAtendimento: { id: Number(dados.enderecoId) },
      especialidadesRequiridas: dados.especialidadesRequiridas,
    }),
  });
}
