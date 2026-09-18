/*Isso é um props, ou seja, como precisamos ajustar várias imagens 
para não ficar poluindo o código e colocando diversos Componentes para cada
imagem, podemos reutilizar como se fosse um Override */
/* Precisamos criar uma interface para definir os tipos exatos 
da propriedade assim, só colocamos o tipo dela na função
*/

interface ImagemFlexivelProps {
    src: string;
    alt: string;
}

/* Com isso, tipamos o objeto das props com a interface criada acima*/
function ImagemFlexivel ({ src, alt}:ImagemFlexivelProps) {
    return (
        <img
        src= {src}
        alt= {alt}
        style={{width: '100%', maxWidth: '300px', margin:'10px', borderRadius:'8px'}}
        />
    )
}

export default ImagemFlexivel;