import { useState } from "react";

type Solicitacao = {
  id: string;
  servico: string;
  endereco: string;
  status: "Em andamento" | "Concluída";
  data: string;
};

const servicos = [
  { icone: "🚿", nome: "Encanamento", descricao: "Vazamentos e reparos" },
  { icone: "⚡", nome: "Eletricidade", descricao: "Instalações e consertos" },
  { icone: "🔧", nome: "Manutenção", descricao: "Reparos em geral" },
  { icone: "🎨", nome: "Pintura", descricao: "Pintura residencial" },
];

export default function DashboardCliente() {
  const [modalAberto, setModalAberto] = useState(false);
  const [solicitacoes, setSolicitacoes] = useState<Solicitacao[]>([
    { id: "#SH-1842", servico: "Reparo no chuveiro", endereco: "Rua das Flores, 120", status: "Em andamento", data: "Hoje, 14:30" },
    { id: "#SH-1798", servico: "Instalação de tomada", endereco: "Rua das Flores, 120", status: "Concluída", data: "12 jun, 09:15" },
  ]);
  const [servico, setServico] = useState("Encanamento");

  function criarSolicitacao(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSolicitacoes((atuais) => [
      {
        id: `#SH-${Math.floor(Math.random() * 900 + 100)}`,
        servico: `Solicitação de ${servico.toLowerCase()}`,
        endereco: "Meu endereço principal",
        status: "Em andamento",
        data: "Agora",
      },
      ...atuais,
    ]);
    setModalAberto(false);
  }

  return (
    <main className="dashboard">
      <style>{`
        * { box-sizing: border-box; } 
        .dashboard { min-height:100vh; background:#f6f8fb; color:#172033; font-family:Inter,Arial,sans-serif; padding-bottom:40px; }
        .topbar { background:#fff; border-bottom:1px solid #e8ebf0; padding:18px 7%; display:flex; justify-content:space-between; align-items:center; }
        .logo { color:#2166e3; font-size:22px; font-weight:800; } 
        .logo span { color:#172033; } 
        .profile { display:flex; gap:11px; align-items:center; color:#566176; font-size:14px; } 
        .avatar { background:#dce9ff; color:#2166e3; border-radius:50%; padding:10px 13px; font-weight:bold; }
        .content { max-width:1100px; margin:auto; padding:42px 24px; } 
        .welcome { display:flex; justify-content:space-between; align-items:center; gap:20px; margin-bottom:34px; } 
        h1 { margin:0 0 8px; font-size:30px; } 
        p { color:#69758a; margin:0; } 
        button { border:0; cursor:pointer; font-weight:700; } 
        .primary { background:#2166e3; color:#fff; border-radius:8px; padding:14px 20px; box-shadow:0 5px 12px #2166e32e; }
        .section-title { display:flex; justify-content:space-between; align-items:center; margin:28px 0 15px; } 
        h2 { font-size:19px; margin:0; } 
        .link { color:#2166e3; background:none; }
        .services { display:grid; grid-template-columns:repeat(4,1fr); gap:14px; } 
        .service { background:#fff; border:1px solid #e8ebf0; border-radius:12px; padding:20px; text-align:left; transition:.2s; } 
        .service:hover { border-color:#2166e3; transform:translateY(-2px); } 
        .icon { font-size:28px; display:block; margin-bottom:15px; } 
        .service strong { display:block; margin-bottom:6px; } 
        .service small { color:#7a8495; }
        .table { background:#fff; border:1px solid #e8ebf0; border-radius:12px; overflow:hidden; } 
        .row { display:grid; grid-template-columns:1.5fr 1.4fr 1fr .8fr; gap:15px; padding:18px 20px; align-items:center; border-bottom:1px solid #edf0f4; font-size:14px; } 
        .row:last-child { border:0; } 
        .head { color:#7b8494; font-size:12px; text-transform:uppercase; letter-spacing:.04em; } 
        .status { color:#14945a; background:#e4f7ed; border-radius:20px; padding:6px 10px; width:max-content; font-size:12px; font-weight:bold; } 
        .status.pending { color:#b47400; background:#fff3d7; } 
        .muted { color:#758095; }
        
        /* Ajuste do Modal Overlay para evitar conflito com Bootstrap */
        .overlay { position:fixed; top:0; left:0; width:100vw; height:100vh; background:rgba(23,32,51,0.6); display:grid; place-items:center; padding:20px; z-index:9999; } 
        .modal-card { background:#fff; border-radius:14px; width:min(480px,100%); padding:27px; box-shadow:0 20px 60px rgba(0,0,0,0.3); } 
        .modal-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:22px; } 
        .close { background:none; color:#718096; font-size:25px; } 
        label { display:block; color:#455268; font-size:14px; font-weight:bold; margin:15px 0 7px; } 
        select, textarea { width:100%; border:1px solid #dce1e9; border-radius:7px; padding:12px; font:inherit; } 
        textarea { resize:vertical; min-height:85px; } 
        .modal-actions { display:flex; justify-content:flex-end; gap:10px; margin-top:22px; } 
        .cancel { background:#eef1f5; color:#526076; border-radius:8px; padding:12px 17px; }
        
        @media(max-width:700px) { 
          .welcome { align-items:flex-start; flex-direction:column; } 
          .services { grid-template-columns:repeat(2,1fr); } 
          .row { grid-template-columns:1.5fr 1fr; } 
          .head { display:none; } 
          .row > :nth-child(2) { display:none; } 
        }
      `}</style>

      <header className="topbar">
        <div className="logo">
          Service<span>Hub</span>
        </div>
        <div className="profile">
          <span>Olá, Leonardo</span>
          <b className="avatar">L</b>
        </div>
      </header>

      <div className="content">
        <section className="welcome">
          <div>
            <h1>Olá, Leonardo! 👋</h1>
            <p>O que você precisa consertar hoje?</p>
          </div>
          <button className="primary" onClick={() => setModalAberto(true)}>
            ＋ Criar solicitação
          </button>
        </section>

        <div className="section-title">
          <h2>Encontre um serviço</h2>
        </div>

        <section className="services">
          {servicos.map((item) => (
            <button
              className="service"
              key={item.nome}
              onClick={() => {
                setServico(item.nome);
                setModalAberto(true);
              }}
            >
              <span className="icon">{item.icone}</span>
              <strong>{item.nome}</strong>
              <small>{item.descricao}</small>
            </button>
          ))}
        </section>

        <div className="section-title">
          <h2>Minhas solicitações</h2>
          <button className="link">Ver todas →</button>
        </div>

        <section className="table">
          <div className="row head">
            <span>Serviço</span>
            <span>Endereço</span>
            <span>Status</span>
            <span>Data</span>
          </div>
          {solicitacoes.map((item) => (
            <div className="row" key={item.id}>
              <span>
                <b>{item.servico}</b>
                <br />
                <small className="muted">{item.id}</small>
              </span>
              <span className="muted">{item.endereco}</span>
              <span>
                <b className={`status ${item.status === "Em andamento" ? "pending" : ""}`}>
                  {item.status}
                </b>
              </span>
              <span className="muted">{item.data}</span>
            </div>
          ))}
        </section>
      </div>

      {modalAberto && (
        <div className="overlay" role="dialog" aria-modal="true">
          {/* Alterado para modal-card para prevenir choque de classes CSS com Bootstrap */}
          <form className="modal-card" onSubmit={criarSolicitacao}>
            <div className="modal-header">
              <h2>Nova solicitação</h2>
              <button type="button" className="close" onClick={() => setModalAberto(false)}>
                ×
              </button>
            </div>
            <p>Conte o que precisa e encontre o profissional ideal.</p>

            <label htmlFor="servico">Tipo de serviço</label>
            <select id="servico" value={servico} onChange={(e) => setServico(e.target.value)}>
              {servicos.map((item) => (
                <option key={item.nome}>{item.nome}</option>
              ))}
            </select>

            <label htmlFor="descricao">Descreva o problema</label>
            <textarea id="descricao" required placeholder="Ex.: O chuveiro parou de esquentar..." />

            <div className="modal-actions">
              <button type="button" className="cancel" onClick={() => setModalAberto(false)}>
                Cancelar
              </button>
              <button type="submit" className="primary">
                Solicitar profissional
              </button>
            </div>
          </form>
        </div>
      )}
    </main>
  );
}