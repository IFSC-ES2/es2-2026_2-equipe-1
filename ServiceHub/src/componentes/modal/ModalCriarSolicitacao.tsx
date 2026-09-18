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
    <div className="modal show d-block" style={{ backgroundColor: 'rgba(0, 0, 0, 0.5)' }}>
      <div className="modal-dialog modal-dialog-centered w-100" style={{ maxWidth: '500px' }}>
        <div className="modal-content shadow-lg border-0 rounded-4">
          <div className="modal-header border-bottom-0 pb-0">
            <h5 className="modal-title fw-bold text-dark">Nova Solicitação de Serviço</h5>
            <button type="button" className="btn-close" onClick={handleClose}></button>
          </div>
          
          <form onSubmit={handleSubmit}>
            <div className="modal-body py-3">
              <div className="mb-3">
                <label className="form-label fw-semibold text-secondary">Título do Serviço</label>
                <input 
                  type="text" 
                  className="form-control" 
                  placeholder="Ex: Conserto de vazamento na cozinha"
                  value={titulo}
                  onChange={(e) => setTitulo(e.target.value)}
                  required
                />
              </div>

              <div className="mb-3">
                <label className="form-label fw-semibold text-secondary">Valor estimado</label>
                <input
                  type="number"
                  className="form-control"
                  placeholder="Ex: R$250.00"
                  min="0"
                  step="0.01"
                  value={valorEstimado}
                  onChange={(e) => setValorEstimado(e.target.value)}
                  required
                />
              </div>

              <div className="mb-3">
                <label className="form-label fw-semibold text-secondary">Descrição detalhada</label>
                <textarea 
                  className="form-control" 
                  rows={3}
                  placeholder="Descreva o que precisa ser feito..."
                  value={descricao}
                  onChange={(e) => setDescricao(e.target.value)}
                  required
                />
              </div>

              <div className="mb-3">
                <label className="form-label fw-semibold text-secondary">
                  Especialidades necessárias
                </label>
                <div className="border rounded p-2" role="group" aria-label="Especialidades necessárias">
                  {especialidades.map((especialidade) => (
                    <div className="form-check" key={especialidade}>
                      <input
                        id={`especialidade-${especialidade}`}
                        className="form-check-input"
                        type="checkbox"
                        checked={especialidadesSelecionadas.includes(especialidade)}
                        onChange={() => {
                          setEspecialidadesSelecionadas((atuais) =>
                            atuais.includes(especialidade)
                              ? atuais.filter((item) => item !== especialidade)
                              : [...atuais, especialidade],
                          );
                          setValidacao('');
                        }}
                      />
                      <label className="form-check-label" htmlFor={`especialidade-${especialidade}`}>
                        {nomesEspecialidades[especialidade]}
                      </label>
                    </div>
                  ))}
                </div>
                <small className="text-muted">Selecione uma ou mais especialidades.</small>
              </div>

              <div className="mb-3">
                <label className="form-label fw-semibold text-secondary" htmlFor="endereco-solicitacao">
                  Endereço do serviço
                </label>
                {enderecos.length > 0 ? (
                  <select
                    id="endereco-solicitacao"
                    className="form-select"
                    value={enderecoId}
                    onChange={(e) => {
                      setEnderecoId(e.target.value);
                      setValidacao('');
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
                  <div className="alert alert-warning mb-0">
                    Você ainda não tem um endereço cadastrado.{' '}
                    <button type="button" className="btn btn-link p-0 align-baseline" onClick={onCriarEndereco}>
                      Cadastrar endereço
                    </button>
                  </div>
                )}
              </div>
            </div>

            <div className="modal-footer border-top-0 pt-0">
              {validacao && <div className="alert alert-warning w-100 mb-2">{validacao}</div>}
              <button type="button" className="btn btn-light" onClick={handleClose} disabled={enviando}>
                Cancelar
              </button>
              <button type="submit" className="btn btn-primary px-4" disabled={enviando}>
                {enviando ? 'Publicando...' : 'Publicar Solicitação'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}