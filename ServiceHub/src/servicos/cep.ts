export type EnderecoCep = {
  cep: string;
  rua: string;
  complemento: string;
  bairro: string;
  cidade: string;
  estado: string;
};

type RespostaViaCep = {
  cep: string;
  logradouro: string;
  complemento: string;
  bairro: string;
  localidade: string;
  uf: string;
  erro?: boolean | string;
};

export const limparCep = (valor: string) => valor.replace(/\D/g, "").slice(0, 8);

export const formatarCep = (valor: string) => {
  const digitos = limparCep(valor);
  return digitos.length > 5 ? `${digitos.slice(0, 5)}-${digitos.slice(5)}` : digitos;
};

export async function buscarCep(cep: string, signal?: AbortSignal): Promise<EnderecoCep | null> {
  const digitos = limparCep(cep);
  if (digitos.length !== 8) return null;

  const resposta = await fetch(`https://viacep.com.br/ws/${digitos}/json/`, { signal });
  if (!resposta.ok) throw new Error("Não foi possível consultar o CEP.");

  const dados: RespostaViaCep = await resposta.json();
  if (dados.erro) return null; // CEP com formato válido, mas inexistente

  return {
    cep: formatarCep(dados.cep),
    rua: dados.logradouro,
    complemento: dados.complemento,
    bairro: dados.bairro,
    cidade: dados.localidade,
    estado: dados.uf,
  };
}