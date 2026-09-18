import { type FormEvent, useState, type CSSProperties } from "react";

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
	onSalvar: (endereco: Endereco) => void;
};

const inicial: Endereco = {
	cep: "", rua: "", numero: "", complemento: "", cidade: "", bairro: "", estado: "",
};

export default function ModalCriarEndereco({ aberto, onFechar, onSalvar }: Props) {
	const [endereco, setEndereco] = useState<Endereco>(inicial);

	if (!aberto) return null;

	const alterar = (campo: keyof Endereco, valor: string) =>
		setEndereco((atual) => ({ ...atual, [campo]: valor }));

	const enviar = (evento: FormEvent<HTMLFormElement>) => {
		evento.preventDefault();
		onSalvar(endereco);
		setEndereco(inicial);
	};

	return (
		<div style={styles.overlay} onMouseDown={(e) => e.target === e.currentTarget && onFechar()}>
			<section role="dialog" aria-modal="true" aria-labelledby="titulo-modal" style={styles.modal}>
				<header style={styles.header}>
					<h2 id="titulo-modal" style={{ margin: 0 }}>Cadastrar endereço</h2>
					<button type="button" onClick={onFechar} aria-label="Fechar" style={styles.close}>×</button>
				</header>
				<form onSubmit={enviar}>
					<div style={styles.grid}>
						{(["cep", "rua", "numero", "complemento", "cidade", "bairro", "estado"] as const).map((campo) => (
							<label key={campo} style={styles.label}>
								{campo === "numero" ? "Número" : campo.charAt(0).toUpperCase() + campo.slice(1)}
								<input
									required={campo !== "complemento"}
									maxLength={campo === "estado" ? 2 : undefined}
									placeholder={campo === "cep" ? "00000-000" : campo === "estado" ? "UF" : undefined}
									value={endereco[campo]}
									onChange={(e) => alterar(campo, campo === "estado" ? e.target.value.toUpperCase() : e.target.value)}
									style={styles.input}
								/>
							</label>
						))}
					</div>
					<footer style={styles.actions}>
						<button type="button" onClick={onFechar} style={styles.cancel}>Cancelar</button>
						<button type="submit" style={styles.save}>Salvar endereço</button>
					</footer>
				</form>
			</section>
		</div>
	);
}

const styles: Record<string, CSSProperties> = {
	overlay: { position: "fixed", inset: 0, zIndex: 1000, display: "flex", alignItems: "center", justifyContent: "center", padding: 16, background: "rgba(0,0,0,.45)" },
	modal: { width: "100%", maxWidth: 620, padding: 24, borderRadius: 10, background: "#fff", boxShadow: "0 10px 30px rgba(0,0,0,.2)" },
	header: { display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 20 },
	close: { border: 0, background: "transparent", fontSize: 28, cursor: "pointer" },
	grid: { display: "grid", gridTemplateColumns: "repeat(2, minmax(0, 1fr))", gap: 16 },
	label: { display: "flex", flexDirection: "column", gap: 6, fontSize: 14, fontWeight: 600 },
	input: { padding: "10px 12px", border: "1px solid #ccc", borderRadius: 6, fontSize: 16 },
	actions: { display: "flex", justifyContent: "flex-end", gap: 10, marginTop: 24 },
	cancel: { padding: "10px 16px", border: "1px solid #ccc", borderRadius: 6, background: "#fff", cursor: "pointer" },
	save: { padding: "10px 16px", border: 0, borderRadius: 6, background: "#2563eb", color: "#fff", cursor: "pointer" },
};
