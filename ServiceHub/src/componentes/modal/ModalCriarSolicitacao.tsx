import React, { useState } from 'react';

interface ModalProps {
  show: boolean;
  handleClose: () => void;
}

export default function ModalCriarSolicitacao({ show, handleClose }: ModalProps) {
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');

  if (!show) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log({ titulo, descricao });
    handleClose();
  };

  return (
    <div 
      className="modal show d-block" 
      tabIndex={-1} 
      style={{ 
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100vw',
        height: '100vh',
        backgroundColor: 'rgba(0, 0, 0, 0.6)',
        zIndex: 9999,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
      }}
    >
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
            </div>

            <div className="modal-footer border-top-0 pt-0">
              <button type="button" className="btn btn-light" onClick={handleClose}>
                Cancelar
              </button>
              <button type="submit" className="btn btn-primary px-4">
                Publicar Solicitação
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}