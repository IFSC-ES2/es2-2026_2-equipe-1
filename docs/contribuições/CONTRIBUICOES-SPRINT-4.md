# Contribuições individuais — Sprint 4 (Entrega 8)

Este documento registra as principais contribuições dos integrantes para a Sprint 4, conforme o levantamento da equipe.

## Contribuições por integrante

| Integrante | Papel | Principais contribuições |
|:---|:---|:---|
| **Leonardo Victor Muller De Morais** | Arquiteto de Software | Implementação e parametrização dos testes End-to-End com Playwright, incluindo dados fixos e execução em modo visível (`test:e2e:visivel`); correções no `SecurityConfig.java` para resolver respostas `403 Forbidden`, liberar cadastro e login sem exigência prévia de CSRF e configurar rotas stateless; refatoração da lógica do modal `ModalPerfilUsuario.tsx`. |
| **João Vitor Schmitt** | DevOps / Infra | Disponibilização e configuração do ambiente de staging em <https://servicehub.jvs.dev.br>; criação do `docker-compose.yml`, preparação do suporte a variáveis de ambiente via `.env.example` e ajustes no Dockerfile do frontend; configuração de CORS, domínios permitidos (`allowedHosts`) e do header `XSRF-TOKEN`. |
| **Davi Israel Quirino** | Front-end | Criação do componente `InputSenha` com controle de visibilidade da senha usando os ícones `Eye` e `EyeOff` da `lucide-react`; refatoração dos estilos e implementação do filtro de solicitações por pílulas no Dashboard. |
| **Ana Clara Teixeira Ronzani** | Scrum Master & QA | Consolidação da documentação viva da Sprint 4, incluindo `METRICAS.md`, `RISCOS.md`, `USO-IA.md` e `sprint-4.md`; acompanhamento da release `v0.4.0` e validação do checklist da Entrega 8. |

## Destaques da entrega

- **Qualidade e validação:** testes E2E com Playwright para verificar fluxos completos da aplicação, além dos testes de segurança do backend.
- **Segurança e perfil:** ajustes de configuração de acesso/CSRF e melhoria do fluxo de edição de perfil.
- **Infraestrutura:** disponibilização do staging e evolução da execução conteinerizada.
- **Gestão da entrega:** documentação, checklist e acompanhamento da release organizados para o encerramento da Sprint 4.

## Observação de rastreabilidade

O suporte a `.env.example` está registrado conforme o levantamento das contribuições. No checkout consultado durante a elaboração deste documento, esse arquivo não foi localizado; inclua-o no repositório se fizer parte do artefato final de implantação.
