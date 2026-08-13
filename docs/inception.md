# Documento de Inception - Projeto ServiceHub

## 1. Visão do Produto

### (a) Problema que o sistema pretende resolver
A contratação de serviços autônomos (como encanadores, eletricistas, pintores e diaristas) ainda ocorre de forma descentralizada e informal, resultando em falta de previsibilidade de preços, insegurança no atendimento e dificuldades de pagamento para ambas as partes.

### (b) Área de aplicação e Contexto Regional
O projeto está situado na linha de **Gestão de serviços e processos institucionais** e na economia sob demanda (*on-demand services*), tendo como foco inicial o atendimento à comunidade externa de **São José e da Grande Florianópolis**, conectando moradores locais a prestadores autônomos da região.

### (c) Usuários principais e interessados
- **Clientes:** Pessoas físicas ou jurídicas que necessitam da prestação pontual de serviços domésticos ou de manutenção.
- **Prestadores de Serviços:** Profissionais autônomos buscando oportunidades de trabalho e uma plataforma simplificada para gestão de chamados e recebimentos.

### (d) Contexto de aplicação
Plataforma web acessível via navegador para conexão direta entre clientes urbanos e profissionais locais autônomos.

### (e) Proposta de valor
Conectar clientes e prestadores de serviços no modelo sob demanda (inspirado no Uber), proporcionando agilidade no atendimento, segurança na intermediação e facilidade de pagamento integrado.

### (f) Objetivos do produto para este semestre
- Entregar um MVP funcional e orientado a objetos aplicado em ambiente Web.
- Permitir o fluxo completo: cadastro/login de perfis, criação de solicitações, aceite/recusa por profissionais e alteração de status.
- Integrar com o gateway de pagamentos Asaas para liquidação da transação na própria plataforma.
- Garantir a qualidade técnica do processo de software via pipeline de CI/CD, testes automatizados e arquitetura documentada.

### (g) Premissas, restrições e limitações conhecidas
- **Restrição de Escopo:** Aplicação exclusivamente Web (não móvel nativa), respeitando o edital.
- **Arquitetura e Linguagem:** Desenvolvimento Orientado a Objetos utilizando Java com Spring Boot no backend, React no frontend e PostgreSQL.
- **Prazo:** Escopo estritamente delimitado para viabilidade de entrega dentro do semestre acadêmico.

---

## 2. Definição do MVP (Minimum Viable Product)

### (a) Objetivo do MVP
Validar o ciclo central do negócio: permitir que um cliente solicite um serviço, que um profissional aceite o chamado e que o pagamento seja processado com sucesso via integração Asaas.

### (b) Funcionalidades Essenciais (DENTRO do escopo)
1. Cadastro e autenticação/login de clientes e prestadores de serviços.
2. Cadastro e publicação de solicitações de serviços por parte dos clientes.
3. Envio e visualização da solicitação para profissionais disponíveis.
4. Mecanismo de aceite ou recusa da solicitação pelo prestador.
5. Acompanhamento do ciclo de status do serviço (ex: Solicitado, Em Andamento, Concluído, Cancelado).
6. Processamento de pagamento integrado pela plataforma utilizando a API do Asaas.

### (c) Funcionalidades FORA do escopo do MVP
- Geolocalização e rastreamento em tempo real em mapa.
- Chat em tempo real via WebSocket.
- Sistema de avaliações e avaliações por estrelas/comentários.
- Sistema de cupons e códigos promocionais.
- Aplicativo mobile nativo.
- Painel de métricas financeiras avançadas para prestadores.

### (d) Viabilidade do recorte para o semestre
O recorte elimina funcionalidades de alta complexidade em tempo real (como mapas e WebSockets) e foca na consistência do modelo de domínio OO, regras de negócio e integração de pagamentos REST via Asaas.

### (e) Critérios de decisão para o escopo
1. **Foco no Core Value:** Priorizar o fluxo essencial que conecta cliente, prestador e transação financeira.
2. **Qualidade Técnica:** Priorizar a robustez da arquitetura, cobertura de testes e pipelines automatizados em relação à quantidade de telas.