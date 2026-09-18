import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./paginas/Home";
import Login from "./componentes/modal/login";
import AppTela from "./paginas/App";
import Sobre from "./paginas/Sobre";
import EscolhaUser from "./paginas/SelecaoUser";
import Cadastro from "./paginas/Cadastro";
import DashboardPrestador from "./paginas/DashboardPrestador";
import DashboardCliente from "./paginas/DashboardCliente";

function Rotas() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/escolhaUser" element={<EscolhaUser />} />
                <Route path="/login" element={<Login />} />
                <Route path="/appTela" element={<AppTela />} />
                <Route path="/cadastro" element={<Cadastro />} />
                <Route path="/prestador/dashboard" element={<DashboardPrestador />} />
                <Route path="/cliente/dashboard" element={<DashboardCliente />} />
                <Route path="/sobre" element={<Sobre />} />
                <Route path="*" element={<h1>Página não encontrada (404)</h1>} />
            </Routes>
        </BrowserRouter>
    );
}

export default Rotas;