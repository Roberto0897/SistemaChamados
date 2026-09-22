# Sistema de Gestão de Chamados

API REST para abertura, acompanhamento e gestão de chamados de suporte técnico, com autenticação via JWT e controle de permissões por perfil de usuário (USER/ADMIN).

## Tecnologias

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Security** + **JWT** (autenticação stateless)
- **Spring Data JPA** + **Hibernate**
- **PostgreSQL** (banco de dados, via Docker)
- **Lombok**
- **Maven**
- **Docker** (ambiente de banco de dados containerizado)

## Funcionalidades

- Registro e login de usuários com senha criptografada (BCrypt)
- Autenticação via token JWT
- Controle de permissão por perfil: `USER` só acessa os próprios chamados, `ADMIN` acessa todos
- CRUD completo de categorias
- Abertura, listagem e atualização de status de chamados
- Vínculo automático do chamado ao usuário autenticado (via token, não confiável pelo body da requisição)

## Modelagem

| Entidade | Descrição |
|---|---|
| `Usuario` | Dados de login e perfil (USER/ADMIN) |
| `Categoria` | Categorias de chamado (ex: Hardware, Software) |
| `Chamado` | Chamado de suporte, com título, descrição, status, prioridade |
| `Comentario` | Comentários vinculados a um chamado |

## Endpoints

### Autenticação (públicos)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/registrar` | Cria um novo usuário |
| POST | `/auth/login` | Autentica e retorna token JWT |

### Categorias (autenticado)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/categorias` | Lista todas as categorias |
| GET | `/categorias/{id}` | Busca categoria por id |
| POST | `/categorias` | Cria categoria |
| PUT | `/categorias/{id}` | Atualiza categoria |
| DELETE | `/categorias/{id}` | Remove categoria |

### Chamados (autenticado)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/chamados` | Lista chamados (USER vê os próprios, ADMIN vê todos) |
| POST | `/chamados` | Abre um novo chamado |
| PATCH | `/chamados/{id}/status` | Atualiza o status de um chamado |

## Como rodar localmente

### Pré-requisitos

- JDK 17+
- Docker
- Maven (ou usar o wrapper `./mvnw` incluído no projeto)

### 1. Subir o banco de dados

```bash
docker run --name pg-chamados -e POSTGRES_PASSWORD=senha123 -e POSTGRES_DB=chamados -p 5432:5432 -d postgres:16
```

### 2. Configurar a aplicação

As credenciais já estão em `src/main/resources/application.properties`, prontas para o container acima.

### 3. Rodar o projeto

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

### 4. Testar

Use o [Postman](https://www.postman.com/) para testar os endpoints. Fluxo básico:

1. `POST /auth/registrar` — cria um usuário
2. `POST /auth/login` — recebe o token JWT
3. Nas demais requisições, adicione o header `Authorization: Bearer <token>`

##  Decisões técnicas

- **JWT stateless**: sem sessão no servidor, cada requisição se autentica sozinha via token — escala melhor e é o padrão de mercado para APIs REST.
- **DTOs no `Chamado`**: o usuário dono do chamado é extraído do token JWT, nunca do corpo da requisição — evita que um usuário abra chamados em nome de outro.
- **BCrypt para senhas**: hash unidirecional, nunca armazenado em texto puro.
- **Docker para o banco**: ambiente isolado e reproduzível, sem instalação nativa no sistema operacional.

## Próximos passos

- [ ] Testes automatizados (JUnit + Mockito)
- [ ] Documentação via Swagger/OpenAPI
- [ ] Deploy em ambiente cloud (Render/Railway)
- [ ] CRUD de comentários

## Autor

Roberto — [https://www.linkedin.com/in/roberto-oliveira-613987254/](#) 

