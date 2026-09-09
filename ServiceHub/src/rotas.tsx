import { BrowserRouter, Route, Routes } from "react-router";
import Login from "./components/modal/Login";
import Home from "./páginas/Home";

function Rotas() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="*" element={<h1>Página não encontrada (404)</h1>} />
            </Routes>
        </BrowserRouter>
    );
}

export default Rotas;