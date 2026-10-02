# ADR-0003: Autenticação de usuários

- **Status:** Implementada na Entrega 6
- **Data:** 01/10/2026

## Contexto

O sistema precisa reconhecer clientes e prestadores e proteger as operações da API. O frontend web precisa manter a sessão entre chamadas.

## Decisão

Usar sessão HTTP com Spring Security, senha com BCrypt, proteção CSRF e autorização conforme o perfil do usuário. A origem local do frontend é permitida via CORS com credenciais.

## Motivo

A sessão atende ao fluxo web atual sem introduzir gerenciamento de tokens no frontend. BCrypt protege as senhas armazenadas; CSRF protege operações autenticadas por cookie.

## Consequências

- As chamadas do frontend precisam enviar cookies e token CSRF.
- Configurações de origem e cookies devem ser ajustadas para produção.
- Testes de integração do login, sessão e autorização ainda são necessários.

## Código relacionado

- `back-end/src/main/java/com/es/servicehub/config/SecurityConfig.java`
- `back-end/src/main/java/com/es/servicehub/controller/UsuarioController.java`
- `ServiceHub/src/servicos/api.ts`
