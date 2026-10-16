import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import marca from "../../assets/logo/ServiceHub_azul.png";
import ModalAceitarSolicitacao from "../../componentes/modal/ModalAceitarSolicitacao";
import {
  aceitarSolicitacao,
  listarMeusChamados,
  listarSolicitacoesDisponiveis,
  type SolicitacaoApi,
} from "../../servicos/api";
import { nomesEspecialidades } from "../../servicos/especialidades";
import { obterDashboardPorPerfil, obterUsuarioLogado, sairUsuario, validarUsuarioLogado, type Usuario } from "../../servicos/autenticacao";
import "../Dashboard.css";

function formatarData(data: string) {
  return new Date(data).toLocaleString("pt-BR", { dateStyle: "medium", timeStyle: "short" });
}

function formatarMoeda(valor: number) {
  return valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

export default function DashboardPrestador() {
  const navigate = useNavigate();
  const [usuario, setUsuario] = useState<Usuario | null>(() => obterUsuarioLogado());
  const [disponiveis, setDisponiveis] = useState<SolicitacaoApi[]>([]);
  const [meusChamados, setMeusChamados] = useState<SolicitacaoApi[]>([]);
  const [solicitacaoSelecionada, setSolicitacaoSelecionada] = useState<SolicitacaoApi | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [aceitando, setAceitando] = useState(false);
  const [saindo, setSaindo] = useState(false);
  const [erro, setErro] = useState("");

  useEffect(() => {
    let cancelado = false;

    async function carregar() {
      const usuarioAtual = await validarUsuarioLogado();
      if (cancelado) return;
      setUsuario(usuarioAtual);

      if (!usuarioAtual) {
        navigate("/login", { replace: true });
        return;
      }
      if (usuarioAtual.perfil !== "PRESTADOR") {
        navigate(obterDashboardPorPerfil(usuarioAtual.perfil), { replace: true });
        return;
      }

      try {
        const [solicitacoesDisponiveis, chamados] = await Promise.all([
          listarSolicitacoesDisponiveis(),
          listarMeusChamados(),
        ]);
        if (cancelado) return;
        setDisponiveis(solicitacoesDisponiveis);
        setMeusChamados(chamados);
      } catch (erroApi) {
        if (!cancelado) setErro(erroApi instanceof Error ? erroApi.message : "Não foi possível carregar as solicitações.");
      } finally {
        if (!cancelado) setCarregando(false);
      }
    }

    void carregar();
    return () => { cancelado = true; };
  }, [navigate]);

  async function confirmarAceite(valorProposto: number) {
    if (!solicitacaoSelecionada) return;
    setAceitando(true);
    try {
      const aceita = await aceitarSolicitacao(solicitacaoSelecionada.id, valorProposto);
      setDisponiveis((atuais) => atuais.filter((item) => item.id !== aceita.id));
      setMeusChamados((atuais) => [aceita, ...atuais]);
      setSolicitacaoSelecionada(null);
      setErro("");
    } catch (erroApi) {
      const mensagem = erroApi instanceof Error ? erroApi.message : "Não foi possível aceitar a solicitação.";
      setErro(mensagem);
      throw new Error(mensagem);
    } finally {
      setAceitando(false);
    }
  }

  async function sair() {
    setSaindo(true);
    setErro("");
    try {
      await sairUsuario();
      setUsuario(null);
      navigate("/login", { replace: true });
    } catch (erroLogout) {
      setErro(erroLogout instanceof Error ? erroLogout.message : "Não foi possível encerrar a sessão.");
      setSaindo(false);
    }
  }

  function renderSolicitacao(solicitacao: SolicitacaoApi, permitirAceite: boolean) {
    return (
      <article className="dashboard-request" key={solicitacao.id}>
        <div className="dashboard-request-top">
          <div>
            <h3>{solicitacao.titulo}</h3>
            <p className="dashboard-muted">
              Pedido #{solicitacao.id} · {formatarData(solicitacao.dataCriacao)}
            </p>
          </div>
          <strong className="dashboard-price">{formatarMoeda(solicitacao.valorEstimado)}</strong>
        </div>
        <p className="dashboard-description">{solicitacao.descricao}</p>
        <div className="dashboard-details">
          <span>{solicitacao.enderecoAtendimento.logradouro}, {solicitacao.enderecoAtendimento.numero} · {solicitacao.enderecoAtendimento.cidade}</span>
          {solicitacao.cliente && <span>Cliente: {solicitacao.cliente.nome}</span>}
        </div>
        <div className="dashboard-tags">
          {solicitacao.especialidadesRequiridas.map((especialidade) => (
            <span className="dashboard-tag" key={especialidade}>{nomesEspecialidades[especialidade]}</span>
          ))}
        </div>
        {permitirAceite ? (
          <button
            className="dashboard-button"
            type="button"
            onClick={() => setSolicitacaoSelecionada(solicitacao)}
          >
            Aceitar e propor valor <span aria-hidden="true">↗</span>
          </button>
        ) : (
          <span className="dashboard-status">Em andamento · {formatarMoeda(solicitacao.valorEstimado)}</span>
        )}
      </article>
    );
  }

  return (
    <div className="home-page dashboard-page">
      <header className="dashboard-nav">
        <Link className="home-brand" to="/" aria-label="ServiceHub, página inicial">
          <img src={marca} alt="" />
          <span>Service<span>Hub</span></span>
        </Link>
        <div className="dashboard-nav-actions">
          <span>Olá, {usuario?.nome.split(" ")[0] ?? "profissional"}</span>
          <button type="button" onClick={() => void sair()} disabled={saindo}>
            {saindo ? "Saindo..." : "Sair"}
          </button>
        </div>
      </header>

      <main className="dashboard-main">
        <section className="dashboard-intro">
          <div>
            <p className="dashboard-eyebrow">SEU ESPAÇO SERVICEHUB</p>
            <h1 className="dashboard-title">Seu talento encontra<br /><em>novos caminhos.</em></h1>
            <p className="dashboard-muted">Veja oportunidades reais e acompanhe os serviços que você aceitou.</p>
          </div>
        </section>

        {erro && <p className="dashboard-error" role="alert">{erro}</p>}

        <div className="dashboard-grid">
          <section className="dashboard-card">
            <div className="dashboard-section-heading">
              <div>
                <p className="dashboard-eyebrow">NOVAS OPORTUNIDADES</p>
                <h2>Solicitações disponíveis</h2>
              </div>
              <span className="dashboard-count">{disponiveis.length}</span>
            </div>
            {carregando ? (
              <p className="dashboard-empty">Carregando solicitações...</p>
            ) : disponiveis.length === 0 ? (
              <p className="dashboard-empty">Não há solicitações disponíveis no momento.</p>
            ) : (
              <div className="dashboard-request-list">
                {disponiveis.map((solicitacao) => renderSolicitacao(solicitacao, true))}
              </div>
            )}
          </section>

          <section className="dashboard-card">
            <div className="dashboard-section-heading">
              <div>
                <p className="dashboard-eyebrow">SERVIÇOS ACEITOS</p>
                <h2>Meus chamados em andamento</h2>
              </div>
              <span className="dashboard-count">{meusChamados.length}</span>
            </div>
            {carregando ? (
              <p className="dashboard-empty">Carregando seus chamados...</p>
            ) : meusChamados.length === 0 ? (
              <p className="dashboard-empty">Os serviços que você aceitar aparecerão aqui.</p>
            ) : (
              <div className="dashboard-request-list">
                {meusChamados.map((solicitacao) => renderSolicitacao(solicitacao, false))}
              </div>
            )}
          </section>
        </div>
      </main>

      <ModalAceitarSolicitacao
        key={solicitacaoSelecionada?.id ?? "fechado"}
        solicitacao={solicitacaoSelecionada}
        enviando={aceitando}
        onFechar={() => setSolicitacaoSelecionada(null)}
        onConfirmar={confirmarAceite}
      />
    </div>
  );
}
