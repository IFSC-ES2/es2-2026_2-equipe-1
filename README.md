# ServiceHub

Projeto desenvolvido para a disciplina de **Engenharia de Software II** do curso de **Análise e Desenvolvimento de Sistemas** do **IFSC - Campus São José**.

## Integrantes

* Ana Clara Teixeira Ronzani (202510703694) - Engenheira de Qualidade
* Davi Israel Quirino (202510703620) - Scrum Master & Front End
* João Vitor Schmitt (202510703638) - DevOps/Infra
* Leonardo Victor Muller De Morais (202510703650) - Arquiteto de Software

## Tema do Projeto

O **ServiceHub** é uma plataforma inspirada no modelo do Uber para conectar clientes e prestadores de serviços, como encanadores, eletricistas, pintores, diaristas e outros profissionais autônomos.

O sistema busca facilitar a contratação de serviços, permitindo que o cliente faça uma solicitação e que profissionais cadastrados possam aceitar ou recusar o atendimento. Além disso, os pagamentos serão realizados pela própria plataforma por meio da integração com o gateway de pagamentos **Asaas**.

O projeto pretende tornar o processo de contratação mais rápido, seguro e organizado, ao mesmo tempo em que amplia as oportunidades de trabalho para prestadores de serviços.

## Linha do Projeto

O projeto está relacionado à linha:

**2. Gestão de serviços e processos institucionais.**

## Escopo do MVP (Minimum Viable Product)

### 🟢 Dentro do Escopo
- Cadastro e login (perfis de Cliente e Prestador).
- Cadastro e publicação de solicitações de serviços.
- Notificação/envio para profissionais disponíveis.
- Aceite ou recusa da solicitação pelo prestador.
- Acompanhamento do ciclo/status do serviço.
- Pagamento integrado via API Asaas.

### 🔴 Fora do Escopo (Nesta versão)
- Geolocalização em tempo real dos profissionais e clientes no mapa.
- Chat em tempo real via WebSockets.
- Sistema de avaliação e comentários.
- Sistema de cupons e promoções.
- Aplicativo mobile nativo.

---

## Tecnologias (previstas)

* Java
* Spring Boot
* React
* PostgreSQL
* WebSocket
* Asaas API
* Docker
* Git e GitHub

## Artefatos da Inception (Entrega 2)

- **Documento Completo de Inception:** [docs/inception.md](docs/inception.md)
- **Definition of Done (DoD):** [docs/dod.md](docs/dod.md)
- **Decisões Arquiteturais (ADRs):**
  - [ADR-0001: Definição da Stack Tecnológica (Java Spring Boot, React, Postgres)](docs/adrs/ADR-0001-stack-tecnologica.md)
  - [ADR-0002: Escolha do Gateway de Pagamentos Asaas](docs/adrs/ADR-0002-integracao-pagamentos-asaas.md)
- **Uso de Inteligência Artificial:** [USO-IA.md](USO-IA.md)
- **Quadro de Acompanhamento (Board & Backlog):** [Acessar GitHub Projects da Equipe](https://github.com/orgs/IFSC-ES2/projects/32/views/2)

---
