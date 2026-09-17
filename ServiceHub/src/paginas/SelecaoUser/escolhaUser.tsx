import React from "react";
import FundoPagina from "../../componentes/ImagemFundo";
import imagemFundo from "../../assets/logo/ServiceHub_sem_nome.jpeg"

function EscolhaUser() {
    return (
        <FundoPagina src={imagemFundo}>

            {/*Topo da tela */}
            <div className="container-fluid d-flex flex-column justify-content-between vh-100 p-4">
                <div className="text-center mt-3 text-secondary">
                    <h1>Tela de Escolha </h1>
                    <p className="fw-semibold-" > Escolha o seu perfil abaixo para continuar</p>
                </div>

                {/*Parte inferior abaixo do texto*/}
                <div className="row text-center mb-5 align-items-end">

                    {/*Lado esquerdo Prestador de Serviço*/}
                    <div className="col-md-6 mb-4 mb-md-0">
                        <div className="p-3">
                            <h3 className="text-dark fw-bold"> Prestador de Serviço</h3>
                            <p className="text-muted"> Cadastre-se e Encontre trabalhos próximos de Você!</p>
                            <button type="button" className="btn btn-primary btn-lg px-4 shadow">Sou Prestador de Serviço</button>
                        </div>
                    </div>

                    {/*Lado direito Cliente*/}
                    <div className="col-md-6">
                        <div className="p-3">
                            <h3 className="text-white fw-bold"> Cliente </h3>
                            <p className="text-white-50"> Encontre os melhores profissionais para a sua obra ou reforma com segurança</p>
                            <button type="button" className="btn btn-primary btn-lg px-4 shadow">Sou Cliente</button>
                        </div>
                    </div>
                </div>
            </div>
    </FundoPagina>
    );
}

export default EscolhaUser;