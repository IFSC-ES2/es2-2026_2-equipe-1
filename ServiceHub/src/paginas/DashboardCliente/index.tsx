import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ChevronDown, ChevronUp, MoreVertical } from "lucide-react";
import marca from "../../assets/logo/ServiceHub_azul.png";
import ModalCriarEndereco, { type Endereco } from "../../componentes/modal/ModalCriarEndereço";
import ModalCriarSolicitacao, {
  type DadosSolicitacao,
  type EnderecoSelecionavel,
} from "../../componentes/modal/ModalCriarSolicitacao";
import {
  criarEndereco,
  criarSolicitacao,
  clienteAceitarProposta,
  concluirSolicitacao,
  excluirSolicitacao,
  listarEnderecos,
  listarMinhasSolicitacoes,
  type SolicitacaoApi,
} from "../../servicos/api";
import { obterDashboardPorPerfil, obterUsuarioLogado, sairUsuario, validarUsuarioLogado, type Usuario } from "../../servicos/autenticacao";
import { nomesEspecialidades } from "../../servicos/especialidades";
import "../Dashboard.css";

function formatarData(data: string) {
  return new Date(data).toLocaleString("pt-BR", { dateStyle: "medium", timeStyle: "short" });
}

function formatarMoeda(valor: number) {
  return valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

function rotuloStatus(status: SolicitacaoApi["statusSolicitacao"]) {
  const rotulos: Record<SolicitacaoApi["statusSolicitacao"], string> = {
    SOLICITADO: "Criado",
    ACEITO: "Aceita",
    RECUSADO: "Recusada",
    EM_ANDAMENTO: "Em andamento",
    CONCLUIDO: "Concluída",
    CANCELADO: "Cancelada",
  };
  return rotulos[status] ?? status;
}

type FiltroSolicitacao = "TODAS" | "SOLICITADO" | "EM_ANDAMENTO" | "CONCLUIDO";

const filtrosSolicitacao: { valor: FiltroSolicitacao; rotulo: string }[] = [
  { valor: "TODAS", rotulo: "Todas" },
  { valor: "SOLICITADO", rotulo: "Criado" },
  { valor: "EM_ANDAMENTO", rotulo: "Em andamento" },
  { valor: "CONCLUIDO", rotulo: "Concluído" },
];

function classeStatus(status: SolicitacaoApi["statusSolicitacao"]) {
  if (status === "SOLICITADO") return "dashboard-request-status-created";
  if (status === "EM_ANDAMENTO" || status === "ACEITO") return "dashboard-request-status-progress";
  if (status === "CONCLUIDO") return "dashboard-request-status-completed";
  return "dashboard-request-status-other";
}

export default function DashboardCliente() {
  const navigate = useNavigate();
  const [usuario, setUsuario] = useState<Usuario | null>(() => obterUsuarioLogado());
  const [solicitacoes, setSolicitacoes] = useState<SolicitacaoApi[]>([]);
  const [enderecos, setEnderecos] = useState<EnderecoSelecionavel[]>([]);
  const [modalAberto, setModalAberto] = useState(false);
  const [modalEnderecoAberto, setModalEnderecoAberto] = useState(false);
  const [carregando, setCarregando] = useState(true);
  const [saindo, setSaindo] = useState(false);
  const [erro, setErro] = useState("");
  const [erroEndereco, setErroEndereco] = useState("");
  const [sucesso, setSucesso] = useState("");
  const [aceitandoProposta, setAceitandoProposta] = useState<string | null>(null);
  const [filtroSolicitacao, setFiltroSolicitacao] = useState<FiltroSolicitacao>("TODAS");
  const [solicitacaoExpandida, setSolicitacaoExpandida] = useState<number | null>(null);
  const [menuAberto, setMenuAberto] = useState<number | null>(null);
  const [concluindoSolicitacao, setConcluindoSolicitacao] = useState<number | null>(null);
  const [excluindoSolicitacao, setExcluindoSolicitacao] = useState<number | null>(null);

  const solicitacoesFiltradas = useMemo(
    () => filtroSolicitacao === "TODAS"
      ? solicitacoes
      : solicitacoes.filter((solicitacao) => solicitacao.statusSolicitacao === filtroSolicitacao),
    [filtroSolicitacao, solicitacoes],
  );

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
      if (usuarioAtual.perfil !== "CLIENTE") {
        navigate(obterDashboardPorPerfil(usuarioAtual.perfil), { replace: true });
        return;
      }

      try {
        const [minhasSolicitacoes, meusEnderecos] = await Promise.all([
          listarMinhasSolicitacoes(),
          listarEnderecos(),
        ]);
        if (cancelado) return;
        setSolicitacoes(minhasSolicitacoes);
        setEnderecos(meusEnderecos);
      } catch (erroApi) {
        if (!cancelado) setErro(erroApi instanceof Error ? erroApi.message : "Não foi possível carregar seus dados.");
      } finally {
        if (!cancelado) setCarregando(false);
      }
    }

    void carregar();
    return () => { cancelado = true; };
  }, [navigate]);

  async function publicar(dados: DadosSolicitacao) {
    try {
      const criada = await criarSolicitacao(dados);
      setSolicitacoes((atuais) => [criada, ...atuais]);
      setErro("");
      setSucesso("Sua solicitação foi publicada.");
      return true;
    } catch (erroApi) {
      setErro(erroApi instanceof Error ? erroApi.message : "Não foi possível publicar a solicitação.");
      return false;
    }
  }

  async function salvarEndereco(endereco: Endereco): Promise<boolean> {
    setErroEndereco("");
    try {
      const criado = await criarEndereco(endereco);
      setEnderecos((atuais) => [...atuais, criado]);
      setErro("");
      setModalEnderecoAberto(false);
      setSucesso("Endereço cadastrado.");
      return true;
    } catch (erroApi) {
      const mensagem = erroApi instanceof Error ? erroApi.message : "Não foi possível salvar o endereço.";
      setErro(mensagem);
      setErroEndereco(mensagem);
      return false;
    }
  }

  async function aceitarProposta(solicitacaoId: number, prestadorId: number) {
    const chave = `${solicitacaoId}-${prestadorId}`;
    setAceitandoProposta(chave);
    setErro("");
    setSucesso("");
    try {
      const atualizada = await clienteAceitarProposta(solicitacaoId, prestadorId);
      setSolicitacoes((atuais) => atuais.map((item) => item.id === atualizada.id ? atualizada : item));
      setSucesso("Proposta aceita. O contrato foi confirmado.");
    } catch (erroApi) {
      setErro(erroApi instanceof Error ? erroApi.message : "Não foi possível aceitar a proposta.");
    } finally {
      setAceitandoProposta(null);
    }
  }

  async function concluirServico(solicitacaoId: number) {
    setConcluindoSolicitacao(solicitacaoId);
    setErro("");
    setSucesso("");
    try {
      const atualizada = await concluirSolicitacao(solicitacaoId);
      setSolicitacoes((atuais) => atuais.map((item) => item.id === atualizada.id ? atualizada : item));
      setSucesso("Serviço concluído.");
    } catch (erroApi) {
      setErro(erroApi instanceof Error ? erroApi.message : "Não foi possível concluir o serviço.");
    } finally {
      setConcluindoSolicitacao(null);
    }
  }

  async function excluirPedido(solicitacao: SolicitacaoApi) {
    const confirmado = window.confirm(`Deseja excluir a solicitação "${solicitacao.titulo}"? Esta ação não pode ser desfeita.`);
    setMenuAberto(null);
    if (!confirmado) return;

    setExcluindoSolicitacao(solicitacao.id);
    setErro("");
    setSucesso("");
    try {
      await excluirSolicitacao(solicitacao.id);
      setSolicitacoes((atuais) => atuais.filter((item) => item.id !== solicitacao.id));
      setSolicitacaoExpandida((atual) => atual === solicitacao.id ? null : atual);
      setSucesso("Solicitação excluída.");
    } catch (erroApi) {
      setErro(erroApi instanceof Error ? erroApi.message : "Não foi possível excluir a solicitação.");
    } finally {
      setExcluindoSolicitacao(null);
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

  return (
    <div className="home-page dashboard-page">
      <header className="dashboard-nav">
        <Link className="home-brand" to="/" aria-label="ServiceHub, página inicial">
          <img src={marca} alt="" />
          <span>Service<span>Hub</span></span>
        </Link>
        <div className="dashboard-nav-actions">
          <span>Olá, {usuario?.nome.split(" ")[0] ?? "Cliente"}</span>
          <button type="button" onClick={() => void sair()} disabled={saindo}>
            {saindo ? "Saindo..." : "Sair"}
          </button>
        </div>
      </header>

      <main className="dashboard-main">
        <section className="dashboard-intro">
          <div>
            <p className="dashboard-eyebrow">SEU ESPAÇO SERVICEHUB</p>
            <h1 className="dashboard-title">Olá, {usuario?.nome.split(" ")[0] ?? "cliente"}.<br /><em>Vamos resolver?</em></h1>
            <p className="dashboard-muted">Acompanhe seus pedidos e combine o serviço com o profissional certo.</p>
          </div>
          <div className="dashboard-nav-actions">
            <button className="dashboard-button dashboard-button-secondary" type="button" onClick={() => {
              setErroEndereco("");
              setModalEnderecoAberto(true);
            }}>
              + Adicionar endereço
            </button>
            <button className="dashboard-button" type="button" onClick={() => setModalAberto(true)}>
              Criar solicitação <span aria-hidden="true">↗</span>
            </button>
          </div>
        </section>

        {erro && <p className="dashboard-error" role="alert">{erro}</p>}
        {sucesso && <p className="dashboard-success" role="status">{sucesso}</p>}

        <section className="dashboard-card">
          <div className="dashboard-section-heading">
            <div>
              <p className="dashboard-eyebrow">ACOMPANHAMENTO</p>
              <h2>Minhas solicitações</h2>
            </div>
            <span className="dashboard-count">{solicitacoesFiltradas.length}</span>
          </div>

          <div className="dashboard-request-filters" role="group" aria-label="Filtrar solicitações por status">
            {filtrosSolicitacao.map((filtro) => (
              <button
                className={`dashboard-filter-chip${filtroSolicitacao === filtro.valor ? " is-active" : ""}`}
                type="button"
                key={filtro.valor}
                aria-pressed={filtroSolicitacao === filtro.valor}
                onClick={() => {
                  setFiltroSolicitacao(filtro.valor);
                  setSolicitacaoExpandida(null);
                  setMenuAberto(null);
                }}
              >
                {filtro.rotulo}
              </button>
            ))}
          </div>

          {carregando ? (
            <p className="dashboard-empty">Carregando suas solicitações...</p>
          ) : solicitacoes.length === 0 ? (
            <p className="dashboard-empty">Você ainda não publicou solicitações.</p>
          ) : solicitacoesFiltradas.length === 0 ? (
            <p className="dashboard-empty">Não há solicitações neste status.</p>
          ) : (
            <div className="dashboard-request-list dashboard-request-list-scroll">
              {solicitacoesFiltradas.map((solicitacao) => {
                const expandida = solicitacaoExpandida === solicitacao.id;
                const acoesAbertas = menuAberto === solicitacao.id;
                return (
                  <article
                    className={`dashboard-request dashboard-request-accordion${expandida ? " is-expanded" : ""}`}
                    key={solicitacao.id}
                    aria-busy={excluindoSolicitacao === solicitacao.id}
                  >
                    <div className="dashboard-request-summary">
                      <button
                        className="dashboard-request-toggle"
                        type="button"
                        aria-expanded={expandida}
                        aria-controls={`solicitacao-detalhes-${solicitacao.id}`}
                        onClick={() => setSolicitacaoExpandida(expandida ? null : solicitacao.id)}
                      >
                        <span className="dashboard-request-summary-main">
                          <span className="dashboard-request-summary-title">{solicitacao.titulo}</span>
                          <span className="dashboard-request-summary-meta">
                            Pedido #{solicitacao.id} · {formatarData(solicitacao.dataCriacao)}
                          </span>
                        </span>
                        <strong className="dashboard-price">{formatarMoeda(solicitacao.valorEstimado)}</strong>
                        <span className={`dashboard-request-status ${classeStatus(solicitacao.statusSolicitacao)}`}>
                          {rotuloStatus(solicitacao.statusSolicitacao)}
                        </span>
                        {expandida
                          ? <ChevronUp size={19} aria-hidden="true" />
                          : <ChevronDown size={19} aria-hidden="true" />}
                      </button>
                      <div className="dashboard-request-actions">
                        <button
                          className="dashboard-icon-button"
                          type="button"
                          aria-label={`Ações da solicitação ${solicitacao.titulo}`}
                          aria-expanded={acoesAbertas}
                          onClick={() => setMenuAberto(acoesAbertas ? null : solicitacao.id)}
                        >
                          <MoreVertical size={19} aria-hidden="true" />
                        </button>
                        {acoesAbertas && (
                          <div className="dashboard-action-menu">
                            <button
                              type="button"
                              disabled={excluindoSolicitacao !== null}
                              onClick={() => void excluirPedido(solicitacao)}
                            >
                              {excluindoSolicitacao === solicitacao.id ? "Excluindo..." : "Excluir solicitação"}
                            </button>
                          </div>
                        )}
                      </div>
                    </div>

                    {expandida && (
                      <div className="dashboard-request-expanded" id={`solicitacao-detalhes-${solicitacao.id}`}>
                        <p className="dashboard-description">{solicitacao.descricao}</p>
                        <div className="dashboard-details">
                          <span>
                            {solicitacao.enderecoAtendimento.logradouro}, {solicitacao.enderecoAtendimento.numero}
                            {solicitacao.enderecoAtendimento.complemento
                              ? `, ${solicitacao.enderecoAtendimento.complemento}`
                              : ""} · {solicitacao.enderecoAtendimento.bairro}, {solicitacao.enderecoAtendimento.cidade}
                          </span>
                        </div>
                        <div className="dashboard-tags">
                          {solicitacao.especialidadesRequiridas.map((especialidade) => (
                            <span className="dashboard-tag" key={especialidade}>{nomesEspecialidades[especialidade]}</span>
                          ))}
                        </div>

                        {solicitacao.prestador ? (
                          <div className="dashboard-provider">
                            <strong>Profissional responsável: {solicitacao.prestador.nome}</strong>
                            <span>Valor acordado: <b>{formatarMoeda(solicitacao.valorEstimado)}</b></span>
                            {solicitacao.prestador.telefone && (
                              <a href={`https://wa.me/${solicitacao.prestador.telefone.replace(/\D/g, "")}`} target="_blank" rel="noreferrer">
                                WhatsApp: {solicitacao.prestador.telefone}
                              </a>
                            )}
                            <a href={`mailto:${solicitacao.prestador.email}`}>{solicitacao.prestador.email}</a>
                            <span>
                              Especialidades: {solicitacao.prestador.especialidades.length
                                ? solicitacao.prestador.especialidades.map((especialidade) => nomesEspecialidades[especialidade]).join(", ")
                                : "Não informadas"}
                            </span>
                          </div>
                        ) : solicitacao.propostas.length > 0 ? (
                          <div className="dashboard-provider">
                            <strong>Propostas recebidas</strong>
                            {solicitacao.propostas.map((proposta) => {
                              const chave = `${solicitacao.id}-${proposta.prestador.id}`;
                              return (
                                <div className="dashboard-proposal" key={proposta.prestador.id}>
                                  <span>De: <b>{proposta.prestador.nome}</b></span>
                                  <span>Valor proposto: <b>{formatarMoeda(proposta.valorProposto)}</b></span>
                                  <button
                                    className="dashboard-button"
                                    type="button"
                                    disabled={aceitandoProposta !== null}
                                    onClick={() => void aceitarProposta(solicitacao.id, proposta.prestador.id)}
                                  >
                                    {aceitandoProposta === chave
                                      ? "Confirmando..."
                                      : `Aceitar Proposta de ${formatarMoeda(proposta.valorProposto)}`}
                                  </button>
                                </div>
                              );
                            })}
                          </div>
                        ) : (
                          <span className="dashboard-status">Aguardando profissional</span>
                        )}

                        {solicitacao.statusSolicitacao === "EM_ANDAMENTO" && (
                          <button
                            className="dashboard-button dashboard-complete-button"
                            type="button"
                            disabled={concluindoSolicitacao !== null}
                            onClick={() => void concluirServico(solicitacao.id)}
                          >
                            {concluindoSolicitacao === solicitacao.id ? "Concluindo..." : "Concluir Serviço"}
                          </button>
                        )}
                      </div>
                    )}
                  </article>
                );
              })}
            </div>
          )}
        </section>
      </main>

      <ModalCriarSolicitacao
        show={modalAberto}
        handleClose={() => setModalAberto(false)}
        enderecos={enderecos}
        onCriarEndereco={() => {
          setModalAberto(false);
          setErroEndereco("");
          setModalEnderecoAberto(true);
        }}
        onCriarSolicitacao={publicar}
      />
      <ModalCriarEndereco
        aberto={modalEnderecoAberto}
        onFechar={() => setModalEnderecoAberto(false)}
        onSalvar={salvarEndereco}
        erro={erroEndereco}
      />
    </div>
  );
}
