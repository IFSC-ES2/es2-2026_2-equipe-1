import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./paginas/Home";
import Login from "./componentes/modal/login";
import AppTela from "./paginas/App/appTela";
import Sobre from "./paginas/Sobre/sobre";
import EscolhaUser from "./paginas/SelecaoUser/escolhaUser";

function Rotas() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/appTela" element={<AppTela />} />
                <Route path="/escolhaUser" element={<EscolhaUser />} />
                <Route path="/sobre" element={<Sobre />} />
                <Route path="*" element={<h1>Página não encontrada (404)</h1>} />
            </Routes>
        </BrowserRouter>
    );
}

export default Rotas;