import React from "react";

interface FundoPaginaProps {
    src: string;
    children: React.ReactNode;
}

function FundoPagina({ src, children}: FundoPaginaProps) {
    return(
       <div
      style={{
        backgroundImage: `url(${src})`,
        backgroundSize: 'cover',        // Garante que a imagem cubra a tela toda sem distorcer
        backgroundPosition: 'center',    // Centraliza a imagem
        backgroundRepeat: 'no-repeat',  // Não deixa a imagem duplicar
        minHeight: '100vh',             // Força a altura mínima a ser o tamanho da tela inteira
        width: '100vw',                 // Largura total da tela
      }}
    >
      {/* Todo conteúdo colocado dentro da tag vai aparecer aqui por cima */}
      {children}
    </div>
  );
}

export default FundoPagina;