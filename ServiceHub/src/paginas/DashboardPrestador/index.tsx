import { useState } from "react";

type Solicitacao = {
	id: number;
	servico: string;
	cliente: string;
	local: string;
	valor: string;
	horario: string;
};

const solicitacoesIniciais: Solicitacao[] = [
	{
		id: 1,
		servico: "Instalação de torneira",
		cliente: "Mariana Souza",
		local: "Centro, Florianópolis",
		valor: "R$ 120,00",
		horario: "Hoje, 14:00",
	},
	{
		id: 2,
		servico: "Manutenção elétrica",
		cliente: "Carlos Oliveira",
		local: "Trindade, Florianópolis",
		valor: "R$ 180,00",
		horario: "Amanhã, 09:30",
	},
];

export default function DashboardPrestador() {
	const [disponivel, setDisponivel] = useState(true);
	const [solicitacoes, setSolicitacoes] = useState(solicitacoesIniciais);
	const [mensagem, setMensagem] = useState("");

	function aceitarServico(id: number) {
		setSolicitacoes((atuais) => atuais.filter((solicitacao) => solicitacao.id !== id));
		setMensagem("Serviço aceito! O cliente foi notificado.");
		setTimeout(() => setMensagem(""), 3000);
	}

	return (
		<main style={styles.page}>
			<header style={styles.header}>
				<div>
					<span style={styles.logo}>ServiceHub</span>
					<p style={styles.subtitle}>Painel do prestador</p>
				</div>
				<button style={styles.profileButton}>Olá, João ▾</button>
			</header>

			<section style={styles.welcome}>
				<div>
					<h1 style={styles.title}>Bom dia, João!</h1>
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
					<div style={styles.sectionHeading}><h2 style={styles.heading}>Novas solicitações</h2><span style={styles.counter}>{solicitacoes.length} disponíveis</span></div>
					{solicitacoes.length === 0 ? <div style={styles.empty}>Você não tem novas solicitações no momento.</div> : solicitacoes.map((solicitacao) => (
						<article key={solicitacao.id} style={styles.request}>
							<div style={styles.requestTop}><div><h3 style={styles.service}>{solicitacao.servico}</h3><p style={styles.muted}>Solicitado por {solicitacao.cliente}</p></div><strong style={styles.price}>{solicitacao.valor}</strong></div>
							<div style={styles.details}><span>📍 {solicitacao.local}</span><span>🕒 {solicitacao.horario}</span></div>
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
	success: { maxWidth: 1072, margin: "0 auto 20px", padding: "12px 24px", background: "#dcfce7", color: "#166534", borderRadius: 8 }, cards: { maxWidth: 1072, margin: "0 auto 30px", padding: "0 24px", display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 16 }, card: { background: "#fff", border: "1px solid #e2e8f0", borderRadius: 10, padding: 18, display: "flex", gap: 14, alignItems: "center" }, cardIcon: { fontSize: 24 }, label: { color: "#64748b", margin: 0, fontSize: 13 }, number: { display: "block", fontSize: 22, marginTop: 6 }, content: { maxWidth: 1072, margin: "0 auto", padding: "0 24px", display: "grid", gridTemplateColumns: "2fr 1fr", gap: 24 }, listSection: {}, sectionHeading: { display: "flex", alignItems: "center", gap: 12, marginBottom: 14 }, heading: { margin: 0, fontSize: 20 }, counter: { color: "#2563eb", background: "#dbeafe", borderRadius: 20, padding: "5px 10px", fontSize: 12, fontWeight: 700 }, request: { background: "#fff", border: "1px solid #e2e8f0", borderRadius: 10, padding: 20, marginBottom: 14 }, requestTop: { display: "flex", justifyContent: "space-between", gap: 12 }, service: { margin: 0, fontSize: 17 }, price: { color: "#16a34a", whiteSpace: "nowrap" }, details: { display: "flex", gap: 20, color: "#64748b", fontSize: 13, margin: "18px 0" }, accept: { width: "100%", border: 0, borderRadius: 7, padding: 11, background: "#2563eb", color: "#fff", fontWeight: 700, cursor: "pointer" }, tip: { background: "#eff6ff", borderRadius: 10, padding: 22, alignSelf: "start" }, link: { color: "#2563eb", border: 0, background: "none", padding: 0, fontWeight: 700, cursor: "pointer" }, empty: { background: "#fff", border: "1px solid #e2e8f0", borderRadius: 10, padding: 28, color: "#64748b" },
};
