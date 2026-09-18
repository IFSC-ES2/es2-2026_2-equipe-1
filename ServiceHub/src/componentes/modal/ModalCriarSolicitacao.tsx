import React, { useState } from 'react';

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
};

interface ModalProps {
  show: boolean;
  handleClose: () => void;
  enderecos: EnderecoSelecionavel[];
  onCriarEndereco: () => void;
  onCriarSolicitacao: (dados: DadosSolicitacao) => void;
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

  if (!show) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    onCriarSolicitacao({
      titulo,
      descricao,
      valorEstimado: Number(valorEstimado || '0'),
      enderecoId,
    });

    setTitulo('');
    setDescricao('');
    setValorEstimado('');
    setEnderecoId('');
    handleClose();
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
                <label className="form-label fw-semibold text-secondary" htmlFor="endereco-solicitacao">
                  Endereço do serviço
                </label>
                {enderecos.length > 0 ? (
                  <select
                    id="endereco-solicitacao"
                    className="form-select"
                    value={enderecoId}
                    onChange={(e) => setEnderecoId(e.target.value)}
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
              <button type="button" className="btn btn-light" onClick={handleClose}>
                Cancelar
              </button>
              <button type="submit" className="btn btn-primary px-4" disabled={enderecos.length === 0}>
                Publicar Solicitação
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}