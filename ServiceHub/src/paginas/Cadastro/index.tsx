import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import marca from "../../assets/logo/ServiceHub_azul.png";
import { especialidades, nomesEspecialidades, type Especialidade } from "../../servicos/especialidades";
import { cadastrarUsuario, type PerfilUsuario } from "../../servicos/autenticacao";
import "../Home/Home.css";

const tiposPerfil: Array<{ value: PerfilUsuario; label: string; descricao: string }> = [
  { value: "CLIENTE", label: "Cliente", descricao: "Quero contratar serviços" },
  { value: "PRESTADOR", label: "Prestador", descricao: "Quero oferecer serviços" },
];

export default function Cadastro() {
  const navigate = useNavigate();
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [telefone, setTelefone] = useState("");
  const [senha, setSenha] = useState("");
  const [confirmarSenha, setConfirmarSenha] = useState("");
  const [perfil, setPerfil] = useState<PerfilUsuario>("CLIENTE");
  const [selecionadas, setSelecionadas] = useState<Especialidade[]>([]);
  const [erro, setErro] = useState("");

  function alternarEspecialidade(especialidade: Especialidade) {
    setSelecionadas((atuais) =>
      atuais.includes(especialidade)
        ? atuais.filter((item) => item !== especialidade)
        : [...atuais, especialidade],
    );
  }

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setErro("");

    const resultado = await cadastrarUsuario({
      nome,
      email,
      senha,
      confirmarSenha,
      perfil,
      telefone,
      especialidades: selecionadas,
    });

    if (!resultado.ok) {
      setErro(resultado.mensagem);
      return;
    }

    navigate("/login?cadastro=sucesso");
  }

  return (
    <main className="auth-page">
      <header className="auth-header">
        <Link className="home-brand" to="/" aria-label="ServiceHub, página inicial">
          <img src={marca} alt="" />
          <span>Service<span>Hub</span></span>
        </Link>
        <Link className="home-login" to="/login">Já tenho conta</Link>
      </header>

      <div className="auth-shell">
        <section className="auth-copy">
          <p className="eyebrow"><span /> ACESSO RÁPIDO</p>
          <h1>Crie sua conta e comece a conectar.</h1>
          <p className="auth-description">
            Cadastre-se como cliente para solicitar serviços ou como prestador para divulgar seu trabalho.
          </p>
          <div className="auth-badges">
            <span>Atendimento local</span>
            <span>Cadastro fácil</span>
            <span>Perfis personalizados</span>
          </div>
        </section>

        <section className="auth-card" aria-label="Formulário de cadastro">
          <div className="auth-card-header">
            <p className="eyebrow"><span /> CADASTRO</p>
            <h2>Abra sua conta</h2>
          </div>

          <form className="auth-form" onSubmit={handleSubmit} noValidate>
            <div className="auth-grid">
              <label className="auth-field">
                <span>Nome completo</span>
                <input value={nome} onChange={(event) => setNome(event.target.value)} type="text" placeholder="Seu nome" required />
              </label>

              <label className="auth-field">
                <span>E-mail</span>
                <input value={email} onChange={(event) => setEmail(event.target.value)} type="email" placeholder="seu@email.com" required />
              </label>
            </div>

            <label className="auth-field">
              <span>Telefone</span>
              <input value={telefone} onChange={(event) => setTelefone(event.target.value)} type="tel" placeholder="(99) 99999-9999" required />
            </label>

            <div className="auth-grid">
              <label className="auth-field">
                <span>Senha</span>
                <input value={senha} onChange={(event) => setSenha(event.target.value)} type="password" placeholder="Mínimo 6 caracteres" required />
              </label>

              <label className="auth-field">
                <span>Confirmar senha</span>
                <input value={confirmarSenha} onChange={(event) => setConfirmarSenha(event.target.value)} type="password" placeholder="Repita a senha" required />
              </label>
            </div>

            <fieldset className="auth-fieldset">
              <legend>Qual perfil você quer usar?</legend>
              <div className="auth-role-grid">
                {tiposPerfil.map((tipo) => (
                  <label key={tipo.value} className={`auth-role-option ${perfil === tipo.value ? "selected" : ""}`}>
                    <input
                      type="radio"
                      name="perfil"
                      value={tipo.value}
                      checked={perfil === tipo.value}
                      onChange={() => setPerfil(tipo.value)}
                    />
                    <span>
                      <strong>{tipo.label}</strong>
                      <small>{tipo.descricao}</small>
                    </span>
                  </label>
                ))}
              </div>
            </fieldset>

            {perfil === "PRESTADOR" && (
              <fieldset className="auth-fieldset">
                <legend>Especialidades</legend>
                <div className="auth-specialties">
                  {especialidades.map((especialidade) => (
                    <label className="auth-check" key={especialidade}>
                      <input
                        type="checkbox"
                        checked={selecionadas.includes(especialidade)}
                        onChange={() => alternarEspecialidade(especialidade)}
                      />
                      <span>{nomesEspecialidades[especialidade]}</span>
                    </label>
                  ))}
                </div>
              </fieldset>
            )}

            {erro && <p className="auth-message auth-message-error">{erro}</p>}

            <button type="submit" className="home-button auth-submit">Criar conta</button>
          </form>
        </section>
      </div>
    </main>
  );
}
