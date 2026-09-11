import { BrowserRouter, Route, Routes } from "react-router";
import Home from "./paginas/Home";
import Login from "./componentes/modal/login";

function Rotas() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path='/' element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="*" element={<h1>Página não encontrada (404)</h1>} />
            </Routes>
        </BrowserRouter>
    );
}

export default Rotas;