import { type FormEvent, useState } from "react";

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
	erro?: string;
};

const inicial: Endereco = {
	cep: "", rua: "", numero: "", complemento: "", cidade: "", bairro: "", estado: "",
};

export default function ModalCriarEndereco({ aberto, onFechar, onSalvar, erro }: Props) {
	const [endereco, setEndereco] = useState<Endereco>(inicial);
	const [salvando, setSalvando] = useState(false);

	if (!aberto) return null;

	const alterar = (campo: keyof Endereco, valor: string) =>
		setEndereco((atual) => ({ ...atual, [campo]: valor }));

	const enviar = async (evento: FormEvent<HTMLFormElement>) => {
		evento.preventDefault();
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
		<div className="dashboard-modal-backdrop" onMouseDown={(e) => e.target === e.currentTarget && !salvando && onFechar()}>
			<section role="dialog" aria-modal="true" aria-labelledby="titulo-modal" className="dashboard-modal dashboard-modal-wide">
				<header className="dashboard-modal-header">
					<div>
						<p className="dashboard-eyebrow">LOCAL DO SERVIÇO</p>
						<h2 id="titulo-modal">Cadastrar endereço</h2>
					</div>
					<button type="button" onClick={onFechar} aria-label="Fechar" className="dashboard-close" disabled={salvando}>×</button>
				</header>
				{erro && <p className="dashboard-error" role="alert">{erro}</p>}
				<form onSubmit={enviar}>
					<div className="dashboard-address-grid">
						{(["cep", "rua", "numero", "complemento", "cidade", "bairro", "estado"] as const).map((campo) => (
							<label key={campo} className="dashboard-label">
								{campo === "numero" ? "Número" : campo.charAt(0).toUpperCase() + campo.slice(1)}
								<input
									required={campo !== "complemento"}
									maxLength={campo === "estado" ? 2 : undefined}
									placeholder={campo === "cep" ? "00000-000" : campo === "estado" ? "UF" : undefined}
									value={endereco[campo]}
									onChange={(e) => alterar(campo, campo === "estado" ? e.target.value.toUpperCase() : e.target.value)}
									className="dashboard-field"
								/>
							</label>
						))}
					</div>
					<footer className="dashboard-modal-actions">
						<button type="button" onClick={onFechar} className="dashboard-button dashboard-button-secondary" disabled={salvando}>Cancelar</button>
						<button type="submit" className="dashboard-button" disabled={salvando}>{salvando ? "Salvando..." : "Salvar endereço"}</button>
					</footer>
				</form>
			</section>
		</div>
	);
}
