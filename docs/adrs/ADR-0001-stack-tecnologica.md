# ADR-0001: Definição da Stack Tecnológica (Java Spring Boot, React e PostgreSQL)

## Status

Aceito

## Contexto

Para o desenvolvimento da plataforma web **ServiceHub**, a equipe precisa definir a linguagem, o framework backend, a biblioteca frontend e o banco de dados. O projeto exige a aplicação rigorosa do paradigma Orientado a Objetos, facilidade de containerização via Docker e compatibilidade com ferramentas de CI/CD.

## Decisão

Decidimos utilizar **Java 17+ com Spring Boot** para o backend, **React (TypeScript/JavaScript)** para a interface web, **PostgreSQL** para persistência relacional e **Docker** para padronização do ambiente, pois essa combinação garante forte tipagem, facilidade para implementar o padrão ORM (Hibernate/JPA) e facilidade na escrita de testes unitários e de integração.

## Alternativas consideradas

| Alternativa | Vantagens | Desvantagens | Motivo da rejeição |
|---|---|---|---|
| **Node.js / Express** | Desenvolvimento rápido e ecossistema JS unificado. | Menor rigor arquitetural nativo na aplicação dos princípios clássicos de Orientação a Objetos. | Preferência da equipe por Java para consolidação de Padrões OO. |
| **C#** | Excelente desempenho e ecossistema robusto. | Menor familiaridade da maioria dos membros da equipe no momento da decisão. | A equipe possui maior domínio produtivo na stack Java/Spring. |
| **MySQL** | Banco de dados relacional difundido. | Suporte a certas extensões e integração com ecossistema Docker ligeiramente inferior ao PostgreSQL. | O PostgreSQL oferece excelente robustez, conformidade ACID e ótimo desempenho com Spring Data JPA. |

## Consequências

### Consequências positivas
- Separação clara de responsabilidades entre Backend (API RESTful) e Frontend (SPA React).
- Facilidade no mapeamento objeto-relacional com Spring Data JPA/Hibernate.
- Facilidade de automação do build com Maven/Gradle e integração com ecossistema Docker.

### Consequências negativas ou riscos
- Necessidade de gerenciar a comunicação assíncrona/CORS entre o frontend React e a API Spring Boot durante o desenvolvimento local.

### Ações necessárias
- Inicializar a estrutura do projeto Spring Boot no repositório.
- Criar a estrutura inicial do projeto React.
- Configurar o arquivo `docker-compose.yml` para execução do PostgreSQL em ambiente de desenvolvimento.

## Relação com o MVP

Garante uma base arquitetural sólida e escalável para suportar as funcionalidades do MVP, especialmente a gestão de solicitações e o processamento de pagamentos.

## Referências

- Issue: #4
- Pull request: #5
- Documentação: [Spring Boot Documentation](https://spring.io/projects/spring-boot)