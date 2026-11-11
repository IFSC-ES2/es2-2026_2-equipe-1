import { type FormEvent, useEffect, useMemo, useRef, useState } from "react";
import { buscarCep, formatarCep, limparCep } from "../../servicos/cep";

export type Endereco = {
  cep: string;
  rua: string;
  numero: string;
  complemento: string;
  cidade: string;
  bairro: string;
  estado: string;
};

type Props = {
  aberto: boolean;
  onFechar: () => void;
  onSalvar: (endereco: Endereco) => Promise<boolean>;
  enderecosExistentes?: Endereco[];
  erro?: string;
};

const inicial: Endereco = {
  cep: "", rua: "", numero: "", complemento: "", cidade: "", bairro: "", estado: "",
};

const normalizar = (valor: string) => valor.trim().toLowerCase();

export default function ModalCriarEndereco({
  aberto,
  onFechar,
  onSalvar,
  enderecosExistentes = [],
  erro,
}: Props) {
  const [endereco, setEndereco] = useState<Endereco>(inicial);
  const [salvando, setSalvando] = useState(false);
  const [buscandoCep, setBuscandoCep] = useState(false);
  const [erroCep, setErroCep] = useState("");
  const numeroRef = useRef<HTMLInputElement>(null);

  const cepDigitos = limparCep(endereco.cep);

  const jaCadastrado = useMemo(() => {
    if (cepDigitos.length !== 8 || !endereco.numero.trim()) return false;

    return enderecosExistentes.some(
      (existente) =>
        limparCep(existente.cep) === cepDigitos &&
        normalizar(existente.numero) === normalizar(endereco.numero) &&
        normalizar(existente.complemento) === normalizar(endereco.complemento)
    );
  }, [enderecosExistentes, cepDigitos, endereco.numero, endereco.complemento]);

  useEffect(() => {
    if (cepDigitos.length !== 8) {
      setBuscandoCep(false);
      setErroCep("");
      return;
    }

    const controlador = new AbortController();
    setBuscandoCep(true);
    setErroCep("");

    buscarCep(cepDigitos, controlador.signal)
      .then((dados) => {
        if (!dados) {
          setErroCep("CEP não encontrado. Preencha o endereço manualmente.");
          return;
        }
        setEndereco((atual) => ({
          ...atual,
          rua: dados.rua,
          bairro: dados.bairro,
          cidade: dados.cidade,
          estado: dados.estado,
          complemento: atual.complemento || dados.complemento,
        }));
        numeroRef.current?.focus();
      })
      .catch((e) => {
        if (e.name !== "AbortError") {
          setErroCep("Não foi possível consultar o CEP. Preencha manualmente.");
        }
      })
      .finally(() => {
        if (!controlador.signal.aborted) setBuscandoCep(false);
      });

    return () => controlador.abort();
  }, [cepDigitos]);

  if (!aberto) return null;

  const alterar = (campo: keyof Endereco, valor: string) => {
    if (campo === "cep") valor = formatarCep(valor);
    if (campo === "estado") valor = valor.toUpperCase();
    setEndereco((atual) => ({ ...atual, [campo]: valor }));
  };

  const enviar = async (evento: FormEvent<HTMLFormElement>) => {
    evento.preventDefault();
    if (jaCadastrado) return; // proteção extra além do botão desabilitado

    setSalvando(true);
    try {
      if (await onSalvar(endereco)) {
        setEndereco(inicial);
      }
    } finally {
      setSalvando(false);
    }
  };

  return (
    <div
      className="dashboard-modal-backdrop"
      onMouseDown={(e) => e.target === e.currentTarget && !salvando && onFechar()}
    >
      <section
        role="dialog"
        aria-modal="true"
        aria-labelledby="titulo-modal"
        className="dashboard-modal dashboard-modal-wide"
      >
        <header className="dashboard-modal-header">
          <div>
            <p className="dashboard-eyebrow">LOCAL DO SERVIÇO</p>
            <h2 id="titulo-modal">Cadastrar endereço</h2>
          </div>
          <button
            type="button"
            onClick={onFechar}
            aria-label="Fechar"
            className="dashboard-close"
            disabled={salvando}
          >
            ×
          </button>
        </header>

        {erro && <p className="dashboard-error" role="alert">{erro}</p>}

        <form onSubmit={enviar}>
          <div className="dashboard-address-grid">
            {(["cep", "rua", "numero", "complemento", "cidade", "bairro", "estado"] as const).map((campo) => (
              <label key={campo} className="dashboard-label">
                {campo === "numero" ? "Número" : campo.charAt(0).toUpperCase() + campo.slice(1)}
                <input
                  ref={campo === "numero" ? numeroRef : undefined}
                  required={campo !== "complemento"}
                  maxLength={campo === "estado" ? 2 : campo === "cep" ? 9 : undefined}
                  inputMode={campo === "cep" ? "numeric" : undefined}
                  autoComplete={campo === "cep" ? "postal-code" : undefined}
                  placeholder={campo === "cep" ? "00000-000" : campo === "estado" ? "UF" : undefined}
                  value={endereco[campo]}
                  onChange={(e) => alterar(campo, e.target.value)}
                  className="dashboard-field"
                />
                {campo === "cep" && buscandoCep && <small>Buscando endereço...</small>}
                {campo === "cep" && erroCep && <small role="alert">{erroCep}</small>}
                {campo === "numero" && jaCadastrado && (
                  <small role="alert">Este endereço já foi cadastrado.</small>
                )}
              </label>
            ))}
          </div>

          <footer className="dashboard-modal-actions">
            <button
              type="button"
              onClick={onFechar}
              className="dashboard-button dashboard-button-secondary"
              disabled={salvando}
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="dashboard-button"
              disabled={salvando || buscandoCep || jaCadastrado}
            >
              {salvando ? "Salvando..." : "Salvar endereço"}
            </button>
          </footer>
        </form>
      </section>
    </div>
  );
}