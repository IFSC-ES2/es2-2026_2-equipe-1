import { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import marca from "../../assets/logo/ServiceHub_azul.png";
import { entrarUsuario, obterDashboardPorPerfil, validarUsuarioLogado } from "../../servicos/autenticacao";
import "../../paginas/Home/Home.css";

function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");

  useEffect(() => {
    let cancelado = false;
    void validarUsuarioLogado().then((usuario) => {
      if (cancelado) return;
      if (usuario) {
        navigate(obterDashboardPorPerfil(usuario.perfil), { replace: true });
        return;
      }

      const params = new URLSearchParams(location.search);
      if (params.get("cadastro") === "sucesso") {
        setSucesso("Cadastro realizado com sucesso! Faça seu login para continuar.");
      }
    });
    return () => { cancelado = true; };
  }, [location.search, navigate]);

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setErro("");
    setSucesso("");

    const resultado = await entrarUsuario(email, senha);

    if (!resultado.ok) {
      setErro(resultado.mensagem);
      return;
    }

    navigate(obterDashboardPorPerfil(resultado.usuario.perfil), { replace: true });
  }

  return (
    <main className="auth-page">
      <header className="auth-header">
        <Link className="home-brand" to="/" aria-label="ServiceHub, página inicial">
          <img src={marca} alt="" />
          <span>Service<span>Hub</span></span>
        </Link>
        <Link className="home-login" to="/cadastro">Criar conta</Link>
      </header>

      <div className="auth-shell auth-shell-login">
        <section className="auth-copy">
          <p className="eyebrow"><span /> BEM-VINDO DE VOLTA</p>
          <h1>Entre na sua conta e siga em frente.</h1>
          <p className="auth-description">
            Acesse o painel certo para o seu perfil e aproveite todas as funcionalidades do ServiceHub.
          </p>
          <div className="auth-badges">
            <span>Cliente</span>
            <span>Prestador</span>
            <span>Atendimento seguro</span>
          </div>
        </section>

        <section className="auth-card" aria-label="Formulário de login">
          <div className="auth-card-header">
            <p className="eyebrow"><span /> LOGIN</p>
            <h2>Entrar</h2>
          </div>

          <form className="auth-form" onSubmit={handleSubmit} noValidate>
            <label className="auth-field">
              <span>E-mail</span>
              <input value={email} onChange={(event) => setEmail(event.target.value)} type="email" placeholder="seu@email.com" required />
            </label>

            <label className="auth-field">
              <span>Senha</span>
              <input value={senha} onChange={(event) => setSenha(event.target.value)} type="password" placeholder="Digite sua senha" required />
            </label>

            {erro && <p className="auth-message auth-message-error">{erro}</p>}
            {sucesso && <p className="auth-message auth-message-success">{sucesso}</p>}

            <button type="submit" className="home-button auth-submit">Entrar</button>
          </form>
        </section>
      </div>
    </main>
  );
}

export default Login;