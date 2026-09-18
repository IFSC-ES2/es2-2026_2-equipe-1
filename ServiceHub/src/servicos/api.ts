import type { DadosSolicitacao, EnderecoSelecionavel } from "../componentes/modal/ModalCriarSolicitacao";
import type { Endereco } from "../componentes/modal/ModalCriarEndereço";
import type { Especialidade } from "./especialidades";

const API_URL = "http://localhost:8080/api";

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

export type SolicitacaoApi = {
  id: number;
  titulo: string;
  descricao: string;
  valorEstimado: number;
  statusSolicitacao: string;
  dataCriacao: string;
  enderecoAtendimento: EnderecoApi;
  especialidadesRequiridas: Especialidade[];
};

async function requisicao<T>(rota: string, opcoes?: RequestInit): Promise<T> {
  const resposta = await fetch(`${API_URL}${rota}`, {
    headers: { "Content-Type": "application/json" },
    ...opcoes,
  });

  if (!resposta.ok) {
    const detalhe = await resposta.text();
    throw new Error(detalhe || `Não foi possível concluir a operação (${resposta.status}).`);
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
