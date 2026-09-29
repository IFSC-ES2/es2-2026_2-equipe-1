import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { listarSolicitacoes, type SolicitacaoApi } from "../../servicos/api";
import { especialidades, nomesEspecialidades, type Especialidade } from "../../servicos/especialidades";
import { obterDashboardPorPerfil, obterUsuarioLogado } from "../../servicos/autenticacao";

type Solicitacao = {
	id: number;
	especialidades: Especialidade[];
	servico: string;
	cliente: string;
	local: string;
	valor: number;
	horario: string;
};

export default function DashboardPrestador() {
	const navigate = useNavigate();
	const usuario = obterUsuarioLogado();
	const [disponivel, setDisponivel] = useState(true);
	const [solicitacoes, setSolicitacoes] = useState<Solicitacao[]>([]);
	const [filtroEspecialidade, setFiltroEspecialidade] = useState<Especialidade | "TODAS">("TODAS");
	const [mensagem, setMensagem] = useState("");

	useEffect(() => {
		if (!usuario) {
			navigate("/login", { replace: true });
			return;
		}

		if (usuario.perfil !== "PRESTADOR") {
			navigate(obterDashboardPorPerfil(usuario.perfil), { replace: true });
			return;
		}

		listarSolicitacoes()
			.then((solicitacoesAtuais) => setSolicitacoes(solicitacoesAtuais.map(formatarSolicitacao)))
			.catch(() => setMensagem("Não foi possível carregar as solicitações."));
	}, [navigate, usuario]);

	const solicitacoesFiltradas = filtroEspecialidade === "TODAS"
		? solicitacoes
		: solicitacoes.filter((solicitacao) => solicitacao.especialidades.includes(filtroEspecialidade));

	function aceitarServico(id: number) {
		setSolicitacoes((atuais) => atuais.filter((solicitacao) => solicitacao.id !== id));
		setMensagem("Serviço aceito! O cliente foi notificado.");
		setTimeout(() => setMensagem(""), 3000);
	}

	function formatarSolicitacao(solicitacao: SolicitacaoApi): Solicitacao {
		return {
			id: solicitacao.id,
			especialidades: solicitacao.especialidadesRequiridas ?? [],
			servico: solicitacao.titulo,
			cliente: "Cliente ServiceHub",
			local: `${solicitacao.enderecoAtendimento.logradouro}, ${solicitacao.enderecoAtendimento.numero} - ${solicitacao.enderecoAtendimento.cidade}`,
			valor: solicitacao.valorEstimado,
			horario: new Date(solicitacao.dataCriacao).toLocaleString("pt-BR", { dateStyle: "short", timeStyle: "short" }),
		};
	}

	return (
		<main style={styles.page}>
			<header style={styles.header}>
				<div>
					<span style={styles.logo}>ServiceHub</span>
					<p style={styles.subtitle}>Painel do prestador</p>
				</div>
				<button style={styles.profileButton}>Olá, {usuario?.nome?.split(" ")[0] ?? "Prestador"} ▾</button>
			</header>

			<section style={styles.welcome}>
				<div>
					<h1 style={styles.title}>Bom dia, {usuario?.nome?.split(" ")[0] ?? "Prestador"}!</h1>
					<p style={styles.muted}>Encontre novos serviços e acompanhe seus atendimentos.</p>
				</div>
				<button
					onClick={() => setDisponivel(!disponivel)}
					style={{ ...styles.statusButton, background: disponivel ? "#dcfce7" : "#f1f5f9", color: disponivel ? "#15803d" : "#64748b" }}
				>
					<span style={{ ...styles.dot, background: disponivel ? "#22c55e" : "#94a3b8" }} />
					{disponivel ? "Disponível para serviços" : "Indisponível"}
				</button>
			</section>

			{mensagem && <div style={styles.success}>{mensagem}</div>}

			<section style={styles.cards}>
				<div style={styles.card}><span style={styles.cardIcon}>📋</span><div><p style={styles.label}>Solicitações novas</p><strong style={styles.number}>{solicitacoes.length}</strong></div></div>
				<div style={styles.card}><span style={styles.cardIcon}>✅</span><div><p style={styles.label}>Serviços concluídos</p><strong style={styles.number}>24</strong></div></div>
				<div style={styles.card}><span style={styles.cardIcon}>💰</span><div><p style={styles.label}>Ganhos este mês</p><strong style={styles.number}>R$ 2.840</strong></div></div>
				<div style={styles.card}><span style={styles.cardIcon}>⭐</span><div><p style={styles.label}>Avaliação média</p><strong style={styles.number}>4,9</strong></div></div>
			</section>

			<section style={styles.content}>
				<div style={styles.listSection}>
					<div style={styles.sectionHeading}>
						<h2 style={styles.heading}>Novas solicitações</h2>
						<span style={styles.counter}>{solicitacoesFiltradas.length} disponíveis</span>
						<select value={filtroEspecialidade} onChange={(event) => setFiltroEspecialidade(event.target.value as Especialidade | "TODAS")}>
							<option value="TODAS">Todas as especialidades</option>
							{especialidades.map((especialidade) => <option key={especialidade} value={especialidade}>{nomesEspecialidades[especialidade]}</option>)}
						</select>
					</div>
					{solicitacoesFiltradas.length === 0 ? <div style={styles.empty}>Você não tem novas solicitações no momento.</div> : solicitacoesFiltradas.map((solicitacao) => (
						<article key={solicitacao.id} style={styles.request}>
							<div style={styles.requestTop}><div><h3 style={styles.service}>{solicitacao.servico}</h3><p style={styles.muted}>Solicitado por {solicitacao.cliente}</p></div><strong style={styles.price}>{solicitacao.valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}</strong></div>
							<div style={styles.details}><span>📍 {solicitacao.local}</span><span>🕒 {solicitacao.horario}</span></div>
							<div>{solicitacao.especialidades.map((especialidade) => <span key={especialidade} style={styles.specialty}>{nomesEspecialidades[especialidade]}</span>)}</div>
							<button onClick={() => aceitarServico(solicitacao.id)} style={styles.accept}>Aceitar serviço</button>
						</article>
					))}
				</div>
				<aside style={styles.tip}><h2 style={styles.heading}>Dica do dia</h2><p style={styles.muted}>Mantenha seu perfil atualizado e responda rapidamente para aumentar suas chances de conseguir serviços.</p><button style={styles.link}>Ver meu perfil →</button></aside>
			</section>
		</main>
	);
}

const styles: Record<string, React.CSSProperties> = {
	page: { minHeight: "100vh", background: "#f8fafc", color: "#172033", fontFamily: "Arial, sans-serif", paddingBottom: 48 },
	header: { background: "#fff", borderBottom: "1px solid #e2e8f0", padding: "18px 7%", display: "flex", justifyContent: "space-between", alignItems: "center" },
	logo: { color: "#2563eb", fontSize: 24, fontWeight: 800 }, subtitle: { margin: "4px 0 0", color: "#64748b", fontSize: 13 }, profileButton: { background: "none", border: 0, color: "#334155", fontWeight: 600, fontSize: 14 },
	welcome: { maxWidth: 1120, margin: "36px auto 24px", padding: "0 24px", display: "flex", justifyContent: "space-between", gap: 20, alignItems: "center" }, title: { margin: 0, fontSize: 30 }, muted: { color: "#64748b", margin: "8px 0", lineHeight: 1.5 }, statusButton: { border: 0, borderRadius: 24, padding: "12px 16px", fontWeight: 700, cursor: "pointer" }, dot: { display: "inline-block", width: 9, height: 9, borderRadius: "50%", marginRight: 8 },
	success: { maxWidth: 1072, margin: "0 auto 20px", padding: "12px 24px", background: "#dcfce7", color: "#166534", borderRadius: 8 }, cards: { maxWidth: 1072, margin: "0 auto 30px", padding: "0 24px", display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 16 }, card: { background: "#fff", border: "1px solid #e2e8f0", borderRadius: 10, padding: 18, display: "flex", gap: 14, alignItems: "center" }, cardIcon: { fontSize: 24 }, label: { color: "#64748b", margin: 0, fontSize: 13 }, number: { display: "block", fontSize: 22, marginTop: 6 }, content: { maxWidth: 1072, margin: "0 auto", padding: "0 24px", display: "grid", gridTemplateColumns: "2fr 1fr", gap: 24 }, listSection: {}, sectionHeading: { display: "flex", alignItems: "center", gap: 12, marginBottom: 14 }, heading: { margin: 0, fontSize: 20 }, counter: { color: "#2563eb", background: "#dbeafe", borderRadius: 20, padding: "5px 10px", fontSize: 12, fontWeight: 700 }, request: { background: "#fff", border: "1px solid #e2e8f0", borderRadius: 10, padding: 20, marginBottom: 14 }, requestTop: { display: "flex", justifyContent: "space-between", gap: 12 }, service: { margin: 0, fontSize: 17 }, price: { color: "#16a34a", whiteSpace: "nowrap" }, details: { display: "flex", gap: 20, color: "#64748b", fontSize: 13, margin: "18px 0" }, specialty: { display: "inline-block", color: "#1d4ed8", background: "#dbeafe", borderRadius: 14, padding: "5px 9px", margin: "0 8px 12px 0", fontSize: 12, fontWeight: 700 }, accept: { width: "100%", border: 0, borderRadius: 7, padding: 11, background: "#2563eb", color: "#fff", fontWeight: 700, cursor: "pointer" }, tip: { background: "#eff6ff", borderRadius: 10, padding: 22, alignSelf: "start" }, link: { color: "#2563eb", border: 0, background: "none", padding: 0, fontWeight: 700, cursor: "pointer" }, empty: { background: "#fff", border: "1px solid #e2e8f0", borderRadius: 10, padding: 28, color: "#64748b" },
};
