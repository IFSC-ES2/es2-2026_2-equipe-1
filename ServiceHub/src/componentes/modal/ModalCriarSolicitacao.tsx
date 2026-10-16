import React, { useState } from 'react';
import { especialidades, nomesEspecialidades, type Especialidade } from '../../servicos/especialidades';

export type EnderecoSelecionavel = {
  id: string;
  rua: string;
  numero: string;
  bairro: string;
  cidade: string;
  estado: string;
};

export type DadosSolicitacao = {
  titulo: string;
  descricao: string;
  valorEstimado: number;
  enderecoId: string;
  especialidadesRequiridas: Especialidade[];
};

interface ModalProps {
  show: boolean;
  handleClose: () => void;
  enderecos: EnderecoSelecionavel[];
  onCriarEndereco: () => void;
  onCriarSolicitacao: (dados: DadosSolicitacao) => Promise<boolean>;
}

export default function ModalCriarSolicitacao({
  show,
  handleClose,
  enderecos,
  onCriarEndereco,
  onCriarSolicitacao,
}: ModalProps) {
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [valorEstimado, setValorEstimado] = useState('');
  const [enderecoId, setEnderecoId] = useState('');
  const [especialidadesSelecionadas, setEspecialidadesSelecionadas] = useState<Especialidade[]>([]);
  const [enviando, setEnviando] = useState(false);
  const [validacao, setValidacao] = useState('');

  if (!show) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (especialidadesSelecionadas.length === 0) {
      setValidacao('Selecione pelo menos uma especialidade.');
      return;
    }

    if (!enderecoId) {
      setValidacao('Selecione um endereço para o atendimento.');
      return;
    }

    setEnviando(true);
    setValidacao('');
    try {
      const criada = await onCriarSolicitacao({
        titulo,
        descricao,
        valorEstimado: Number(valorEstimado || '0'),
        enderecoId,
        especialidadesRequiridas: especialidadesSelecionadas,
      });

      if (criada) {
        setTitulo('');
        setDescricao('');
        setValorEstimado('');
        setEnderecoId('');
        setEspecialidadesSelecionadas([]);
        handleClose();
      }
    } finally {
      setEnviando(false);
    }
  };

  return (
    <div className="dashboard-modal-backdrop" role="presentation">
      <section className="dashboard-modal" role="dialog" aria-modal="true" aria-labelledby="solicitacao-modal-title">
        <header className="dashboard-modal-header">
          <div>
            <p className="dashboard-eyebrow">NOVO PEDIDO</p>
            <h2 id="solicitacao-modal-title">Criar solicitação</h2>
          </div>
          <button type="button" className="dashboard-close" onClick={handleClose} disabled={enviando} aria-label="Fechar">×</button>
        </header>

        <form onSubmit={(event) => void handleSubmit(event)}>
          <div className="dashboard-form-group">
            <label className="dashboard-label" htmlFor="solicitacao-titulo">Título do serviço</label>
            <input
              id="solicitacao-titulo"
              type="text"
              className="dashboard-field"
              placeholder="Ex.: Conserto de vazamento na cozinha"
              value={titulo}
              onChange={(event) => setTitulo(event.target.value)}
              required
            />
          </div>

          <div className="dashboard-form-group">
            <label className="dashboard-label" htmlFor="solicitacao-valor">Valor estimado (R$)</label>
            <input
              id="solicitacao-valor"
              type="number"
              className="dashboard-field"
              min="0.01"
              step="0.01"
              value={valorEstimado}
              onChange={(event) => setValorEstimado(event.target.value)}
              required
            />
          </div>

          <div className="dashboard-form-group">
            <label className="dashboard-label" htmlFor="solicitacao-descricao">Descrição detalhada</label>
            <textarea
              id="solicitacao-descricao"
              className="dashboard-field"
              rows={3}
              placeholder="Descreva o que precisa ser feito..."
              value={descricao}
              onChange={(event) => setDescricao(event.target.value)}
              required
            />
          </div>

          <div className="dashboard-form-group">
            <span className="dashboard-label">Especialidades necessárias</span>
            <div className="dashboard-specialties" role="group" aria-label="Especialidades necessárias">
              {especialidades.map((especialidade) => (
                <label className="dashboard-specialty" key={especialidade}>
                  <input
                    type="checkbox"
                    checked={especialidadesSelecionadas.includes(especialidade)}
                    onChange={() => {
                      setEspecialidadesSelecionadas((atuais) =>
                        atuais.includes(especialidade)
                          ? atuais.filter((item) => item !== especialidade)
                          : [...atuais, especialidade],
                      );
                      setValidacao("");
                    }}
                  />
                  {nomesEspecialidades[especialidade]}
                </label>
              ))}
            </div>
          </div>

          <div className="dashboard-form-group">
            <label className="dashboard-label" htmlFor="endereco-solicitacao">Endereço do serviço</label>
            {enderecos.length > 0 ? (
              <select
                id="endereco-solicitacao"
                className="dashboard-field"
                value={enderecoId}
                onChange={(event) => {
                  setEnderecoId(event.target.value);
                  setValidacao("");
                }}
                required
              >
                <option value="">Selecione um endereço</option>
                {enderecos.map((endereco) => (
                  <option key={endereco.id} value={endereco.id}>
                    {endereco.rua}, {endereco.numero} - {endereco.bairro}, {endereco.cidade}/{endereco.estado}
                  </option>
                ))}
              </select>
            ) : (
              <p className="dashboard-muted">
                Você ainda não tem endereço cadastrado.{" "}
                <button type="button" className="dashboard-inline-action" onClick={onCriarEndereco}>
                  Cadastrar endereço
                </button>
              </p>
            )}
          </div>

          {validacao && <p className="dashboard-error" role="alert">{validacao}</p>}
          <footer className="dashboard-modal-actions">
            <button type="button" className="dashboard-button dashboard-button-secondary" onClick={handleClose} disabled={enviando}>
              Cancelar
            </button>
            <button type="submit" className="dashboard-button" disabled={enviando}>
              {enviando ? "Publicando..." : "Publicar solicitação"}
            </button>
          </footer>
        </form>
      </section>
    </div>
  );
}