# Definition of Done (DoD) - Equipe ServiceHub

Um item do backlog ou funcionalidade do **ServiceHub** só será considerado **Concluído ("Done")** quando atender integralmente aos seguintes critérios de qualidade:

---

## 📋 Critérios Gerais de Conclusão

1. **Implementação do Código:**
   - O código backend segue os princípios de Orientação a Objetos (SOLID, boas práticas em Java/Spring Boot).
   - O código frontend em React está modularizado e integrado às APIs REST correspondentes.

2. **Fluxo do Git e Versionamento:**
   - Todo o desenvolvimento foi realizado em uma ramificação específica da entrega (ex.: `entrega-2`).
   - O histórico de commits possui mensagens descritivas.
   - As alterações foram submetidas exclusivamente via **Pull Request (PR)** para o ramo principal.

3. **Revisão de Código (Code Review):**
   - O Pull Request conta com pelo menos **1 aprovação (review)** efetuada por outro integrante da equipe.
   - Todas as observações levantadas na revisão foram sanadas.

4. **Critérios de Aceitação:**
   - Todos os critérios de aceitação definidos na Issue associada foram testados e validados.

5. **Automação e Pipeline (CI):**
   - O build do projeto (Maven/Gradle no backend e NPM no frontend) executa sem falhas.
   - Os testes automatizados integrados ao pipeline foram executados e aprovados.

6. **Documentação:**
   - O arquivo `README.md` e a documentação técnica relevante foram devidamente atualizados.
   - Novas decisões arquiteturais tomadas durante a implementação foram registradas em um novo arquivo ADR em `docs/adrs/`.

7. **Integração:**
   - A mesclagem no ramo principal foi concluída via **commit de mesclagem (*merge commit*)**, preservando a rastreabilidade do histórico.