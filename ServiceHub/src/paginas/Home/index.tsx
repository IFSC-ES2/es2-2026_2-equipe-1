import { Link } from "react-router-dom";
import marca from "../../assets/logo/ServiceHub_azul.png";
import "./Home.css";

const profissionais = [
    {
        nome: "Marcos Vieira",
        servico: "Eletricista",
        local: "Florianópolis, SC",
        foto: "https://images.pexels.com/photos/5691588/pexels-photo-5691588.jpeg?auto=compress&cs=tinysrgb&w=640",
        avatar: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=640&q=85",
        descricao: "Eletricista instalando um interruptor na parede",
    },
    {
        nome: "Camila Rocha",
        servico: "Pintora",
        local: "São José, SC",
        foto: "https://images.pexels.com/photos/5641667/pexels-photo-5641667.jpeg?auto=compress&cs=tinysrgb&w=640",
        avatar: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=640&q=85",
        descricao: "Pintora cobrindo uma parede com tinta",
    },
    {
        nome: "André Martins",
        servico: "Marceneiro",
        local: "Palhoça, SC",
        foto: "https://images.pexels.com/photos/5974410/pexels-photo-5974410.jpeg?auto=compress&cs=tinysrgb&w=640",
        avatar: "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=640&q=85",
        descricao: "Marceneiro serrando uma peça de madeira na oficina",
    },
    {
        nome: "Juliana Alves",
        servico: "Jardinagem",
        local: "Florianópolis, SC",
        foto: "https://images.pexels.com/photos/38936338/pexels-photo-38936338.jpeg?auto=compress&cs=tinysrgb&w=640",
        avatar: "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=640&q=85",
        descricao: "Jardineiro aparando uma cerca viva com tesoura",
    },
    {
        nome: "Rafael Costa",
        servico: "Encanador",
        local: "Biguaçu, SC",
        foto: "https://images.pexels.com/photos/6419128/pexels-photo-6419128.jpeg?auto=compress&cs=tinysrgb&w=640",
        avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=640&q=85",
        descricao: "Encanador instalando conexões em uma tubulação",
    },
];

function Home() {
    return (
        <div className="home-page">
            <header className="home-nav">
                <Link className="home-brand" to="/" aria-label="ServiceHub, página inicial">
                    <img src={marca} alt="" />
                    <span>Service<span>Hub</span></span>
                </Link>
                <nav className="home-nav-links" aria-label="Navegação principal">
                    <a href="#como-funciona">Como funciona</a>
                    <a href="#profissionais">Profissionais</a>
                </nav>
                <div className="home-nav-actions">
                    <Link className="home-login" to="/login">Entrar</Link>
                    <Link className="home-button home-button-small" to="/cadastro">Criar conta <span aria-hidden="true">↗</span></Link>
                </div>
            </header>

            <main>
                <section className="home-hero">
                    <div className="hero-copy">
                        <p className="eyebrow"><span /> SERVIÇOS BONS COMEÇAM POR AQUI</p>
                        <h1>Seu próximo projeto merece <em>boas mãos.</em></h1>
                        <p className="hero-description">Encontre profissionais da sua região para cuidar da casa. Ou encontre novos clientes para fazer o seu trabalho crescer.</p>
                        <div className="hero-actions">
                            <Link className="home-button" to="/cadastro">Encontrar um profissional <span aria-hidden="true">↗</span></Link>
                            <a className="text-link" href="#para-profissionais">Sou profissional <span aria-hidden="true">→</span></a>
                        </div>

                    </div>
                    <div className="hero-visual">
                        <img className="hero-image" src="https://images.unsplash.com/photo-1621905251918-48416bd8575a?auto=format&fit=crop&w=1400&q=90" alt="Profissional eletricista trabalhando em uma instalação" />
                        <div className="hero-image-shade" />
                    </div>
                </section>

                <section className="service-ribbon" aria-label="Serviços disponíveis">
                    <div className="ribbon-track">
                        {[0, 1].map((copy) => <div className="ribbon-group" key={copy} aria-hidden={copy === 1}>
                            {["Elétrica", "Pintura", "Encanamento", "Jardinagem", "Montagem", "Reparos"].map((servico) => <span className="ribbon-item" key={servico}><i />{servico}</span>)}
                        </div>)}
                    </div>
                </section>

                <section className="professionals-section" id="profissionais">
                    <div className="section-heading">
                        <div><p className="eyebrow">QUEM FAZ ACONTECER</p><h2>Talento bom tem nome<br />e tem endereço.</h2></div>
                        <p>Profissionais independentes, com habilidades de verdade, conectados a quem precisa de uma mão.</p>
                    </div>
                    <div className="pro-carousel" aria-label="Conheça alguns profissionais">
                        <div className="pro-track">
                            {[0, 1].map((copy) => <div className="pro-group" key={copy} aria-hidden={copy === 1}>
                                {profissionais.map((profissional) => <article className="pro-card" key={`${copy}-${profissional.nome}`}>
                                    <img src={profissional.foto} alt={copy === 0 ? profissional.descricao : ""} loading="lazy" />
                                    <div className="pro-card-content"><span className="pro-role">{profissional.servico}</span><h3>{profissional.nome}</h3><p>{profissional.local}</p></div>
                                </article>)}
                            </div>)}
                        </div>
                    </div>
                </section>

                <section className="how-section" id="como-funciona">
                    <div className="how-intro"><p className="eyebrow">SIMPLES PARA TODO MUNDO</p><h2>Um encontro bom<br />para os dois lados.</h2><p>O ServiceHub aproxima quem precisa de um serviço de quem sabe fazer bem feito.</p></div>
                    <div className="audience-panel client-panel">
                        <span className="panel-index">01 / PARA CLIENTES</span><div className="panel-mark" aria-hidden="true">⌂</div>
                        <h3>Resolva o que importa.<br /><em>Sem complicar.</em></h3>
                        <p>Descubra profissionais da sua região e encontre a pessoa certa para o seu próximo serviço.</p>
                        <Link className="panel-link" to="/cadastro">Quero encontrar alguém <span aria-hidden="true">↗</span></Link>
                    </div>
                    <div className="audience-panel provider-panel" id="para-profissionais">
                        <span className="panel-index">02 / PARA PROFISSIONAIS</span><div className="panel-mark" aria-hidden="true">✳</div>
                        <h3>Seu talento merece<br /><em>ser encontrado.</em></h3>
                        <p>Apresente seu trabalho, escolha suas especialidades e conecte-se com novos clientes.</p>
                        <Link className="panel-link" to="/cadastro">Quero oferecer serviços <span aria-hidden="true">↗</span></Link>
                    </div>
                </section>

                <section className="home-cta">
                    <div><p className="eyebrow">O PRÓXIMO PASSO É SEU</p><h2>Vamos fazer boas<br />conexões?</h2></div>
                    <Link className="home-button home-button-light" to="/cadastro">Criar minha conta <span aria-hidden="true">↗</span></Link>
                </section>
            </main>

            <footer className="home-footer">
                <Link className="home-brand footer-brand" to="/" aria-label="ServiceHub, página inicial"><img src={marca} alt="" /><span>Service<span>Hub</span></span></Link>
                <p>Conectando talentos a novas possibilidades.</p>
                <div className="footer-links"><a href="#como-funciona">Como funciona</a><Link to="/login">Entrar</Link><Link to="/cadastro">Criar conta</Link></div>
                <small>© 2026 ServiceHub. Feito para aproximar.</small>
            </footer>
        </div>
    );
}

export default Home;