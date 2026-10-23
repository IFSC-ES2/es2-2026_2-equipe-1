import { useState, type FormEvent } from "react";
import type { SolicitacaoApi } from "../../servicos/api";

type ModalAceitarSolicitacaoProps = {
  solicitacao: SolicitacaoApi | null;
  enviando: boolean;
  onFechar: () => void;
  onConfirmar: (valorProposto: number) => Promise<void>;
};

export default function ModalAceitarSolicitacao({
  solicitacao,
  enviando,
  onFechar,
  onConfirmar,
}: ModalAceitarSolicitacaoProps) {
  const [valor, setValor] = useState("");
  const [erro, setErro] = useState("");

  if (!solicitacao) return null;

  async function submeter(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const valorProposto = Number(valor);
    if (!Number.isFinite(valorProposto) || valorProposto <= 0) {
      setErro("Informe um valor maior que zero.");
      return;
    }

    setErro("");
    try {
      await onConfirmar(valorProposto);
      setValor("");
    } catch (erroEnvio) {
      setErro(erroEnvio instanceof Error ? erroEnvio.message : "Não foi possível aceitar a solicitação.");
    }
  }

  return (
    <div className="dashboard-modal-backdrop" role="presentation" onMouseDown={(event) => {
      if (event.target === event.currentTarget && !enviando) onFechar();
    }}>
      <section className="dashboard-modal" role="dialog" aria-modal="true" aria-labelledby="aceitar-titulo">
        <header className="dashboard-modal-header">
          <div>
            <p className="dashboard-eyebrow">PROPOSTA DE SERVIÇO</p>
            <h2 id="aceitar-titulo">Quanto você cobra?</h2>
          </div>
          <button className="dashboard-close" type="button" onClick={onFechar} disabled={enviando} aria-label="Fechar">
            ×
          </button>
        </header>
        <p className="dashboard-muted">{solicitacao.titulo}</p>
        <form onSubmit={(event) => void submeter(event)}>
          <label className="dashboard-label" htmlFor="valor-proposto">Valor proposto (R$)</label>
          <div className="dashboard-currency-field">
            <span>R$</span>
            <input
              id="valor-proposto"
              type="number"
              min="0.01"
              step="0.01"
              value={valor}
              onChange={(event) => setValor(event.target.value)}
              placeholder="0,00"
              required
              autoFocus
            />
          </div>
          {erro && <p className="dashboard-error" role="alert">{erro}</p>}
          <div className="dashboard-modal-actions">
            <button className="dashboard-button dashboard-button-secondary" type="button" onClick={onFechar} disabled={enviando}>
              Cancelar
            </button>
            <button className="dashboard-button" type="submit" disabled={enviando}>
              {enviando ? "Enviando..." : "Enviar proposta"}
            </button>
          </div>
        </form>
      </section>
    </div>
  );
}
