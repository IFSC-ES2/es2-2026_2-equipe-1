# ADR-0003: Autenticação stateless com JWT

- **Status:** Implementada
- **Data da revisão:** 18/11/2026

## Contexto

A API precisa autenticar clientes e prestadores em cada chamada protegida, mantendo a autorização independente entre requisições. O frontend deve apresentar o token recebido no login sem depender de um estado de autenticação mantido pelo servidor.

## Decisão

1. Usar Spring Security com `SessionCreationPolicy.STATELESS` e validação JWT pelo OAuth2 Resource Server.
2. No login, validar a senha com BCrypt e emitir um JWT HS256 por `JwtTokenService`, com expiração de uma hora.
3. Enviar o token nas chamadas protegidas pelo cabeçalho `Authorization: Bearer <token>`. O filtro do Resource Server que processa Bearer é `BearerTokenAuthenticationFilter`; o sistema não configura autenticação HTTP Basic por `BasicAuthenticationFilter`.
4. Armazenar o token no frontend em `localStorage`, na chave `token`. O cliente REST o inclui automaticamente nas chamadas à API.
5. No logout, chamar `/api/usuarios/logout`, limpar o `SecurityContext` da requisição atual e remover o token dos armazenamentos do navegador. Não implementar revogação server-side ou blacklist: o JWT emitido continua válido até expirar.
6. Manter BCrypt para senhas e CORS configurado para as origens da aplicação.

## Consequências

- Cada requisição protegida é validada pelo JWT apresentado no cabeçalho; o backend não guarda estado de autenticação do usuário entre chamadas.
- O logout encerra o uso do token pelo navegador, mas não invalida uma cópia já obtida por terceiros; essa cópia pode ser usada até a expiração de uma hora.
- `JWT_SECRET` precisa ser mantido em configuração privada e ter comprimento suficiente para a chave HS256.
- O armazenamento em `localStorage` exige proteção contra execução de scripts não confiáveis na origem do frontend.

## Código relacionado

- `back-end/src/main/java/com/es/servicehub/config/SecurityConfig.java`
- `back-end/src/main/java/com/es/servicehub/config/JwtConfiguration.java`
- `back-end/src/main/java/com/es/servicehub/service/JwtTokenService.java`
- `back-end/src/main/java/com/es/servicehub/controller/UsuarioController.java`
- `ServiceHub/src/servicos/api.ts`
- `ServiceHub/src/servicos/autenticacao.ts`
