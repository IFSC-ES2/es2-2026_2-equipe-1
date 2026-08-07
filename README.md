# ServiceHub

Projeto desenvolvido para a disciplina de **Engenharia de Software II** do curso de **Análise e Desenvolvimento de Sistemas** do **IFSC - Campus São José**.

## Integrantes

* Ana Clara Teixeira Ronzani
* Davi Israel Quirino
* João Vitor Schmitt
* Leonardo Victor Muller De Morais

## Tema do Projeto

O **ServiceHub** é uma plataforma inspirada no modelo do Uber para conectar clientes e prestadores de serviços, como encanadores, eletricistas, pintores, diaristas e outros profissionais autônomos.

O sistema busca facilitar a contratação de serviços, permitindo que o cliente faça uma solicitação e que profissionais cadastrados possam aceitar ou recusar o atendimento. Além disso, os pagamentos serão realizados pela própria plataforma por meio da integração com o gateway de pagamentos **Asaas**.

O projeto pretende tornar o processo de contratação mais rápido, seguro e organizado, ao mesmo tempo em que amplia as oportunidades de trabalho para prestadores de serviços.

## Linha do Projeto

O projeto está relacionado à linha:

**2. Gestão de serviços e processos institucionais.**

## Escopo Inicial (MVP)

O MVP terá como objetivo validar a proposta da plataforma por meio das funcionalidades essenciais.

### Funcionalidades

* Cadastro e login de clientes e prestadores de serviços;
* Cadastro de solicitações de serviços;
* Envio da solicitação para profissionais disponíveis;
* Aceite ou recusa da solicitação pelo prestador;
* Acompanhamento do status do serviço;
* Pagamento pela plataforma utilizando integração com o Asaas.
* Geolocalização em tempo real dos profissionais e clientes;
* Chat em tempo real;
* Avaliações de clientes e prestadores;

### Fora do Escopo

Nesta primeira versão, não serão implementadas as seguintes funcionalidades:

* Sistema de promoções e cupons;
* Aplicativo mobile;
* Painel financeiro avançado para prestadores;

## Tecnologias (previstas)

* Java
* Spring Boot
* React
* PostgreSQL
* WebSocket
* Asaas API
* Docker
* Git e GitHub
