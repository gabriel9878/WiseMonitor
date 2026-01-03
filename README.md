# WiseMonitor (WiseFinder)

Aplicação web para **cadastro/autenticação de usuários** e **gerenciamento de dispositivos** associados.

## Estrutura do projeto

- `backend/` — API REST (Spring Boot)
- `frontend/` — Aplicação web (React)

## Tecnologias

### Backend
- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA / Hibernate
- MySQL
- Maven (com Maven Wrapper)

### Frontend
- React (Create React App)
- React Router
- Node.js / npm

## Requisitos

### Para o backend
- Java 17 instalado
- MySQL rodando localmente (ou acessível)
- (Opcional) Maven — o projeto já inclui Maven Wrapper

### Para o frontend
- Node.js + npm

## Configuração do backend

As configurações ficam em:

- `backend/src/main/resources/application.properties`

Principais propriedades (exemplo do projeto):

- `spring.datasource.url=jdbc:mysql://localhost:3306/wisemonitordb?...`
- `spring.datasource.username=...`
- `spring.datasource.password=...`
- `api.security.token.secret=${JWT_SECRET:my-secret-key}`

Observação: definir `JWT_SECRET` no ambiente (obrigatoriamente quando em produção).

## Como executar

### 1 Backend (Spring Boot)

Abra um terminal na pasta `backend/` e execute:

```bash
./mvnw spring-boot:run
```

No Windows:

```bat
mvnw.cmd spring-boot:run
```

A API sobe, por padrão, em: `http://localhost:8080`

### 2 Frontend (React)

Abra um terminal na pasta `frontend/` e execute:

```bash
npm install
npm start
```

O frontend sobe, por padrão, em: `http://localhost:3000`

## Autenticação e segurança (JWT)

O backend utiliza JWT com Spring Security:

- Geração/validação de token: `backend/src/main/java/br/com/WiseMonitor/servico/AcessTokenService.java`

- Login/sessão: `backend/src/main/java/br/com/WiseMonitor/servico/SessionService.java`

- Filtro de autenticação: `backend/src/main/java/br/com/WiseMonitor/infra/SecurityFilter.java`

- Regras de segurança: `backend/src/main/java/br/com/WiseMonitor/infra/SecurityConfiguration.java`

### Rotas liberadas (sem autenticação)

Conforme configuração de segurança:
- `POST /login`
- `POST /cadastroUsuario`

Demais rotas exigem header:

- `Authorization: Bearer <token>`

## Endpoints principais (API)

### Sessão
- `POST /login` — autentica e retorna tokens (access/refresh)  
	Implementação: `backend/src/main/java/br/com/WiseMonitor/gerencia/SessionController.java`

> Observação: endpoints antigos de logoff/usuário ativo estão comentados no `SessionController`.

### Usuários

- `POST /cadastroUsuario` — cria usuário
- `GET /exibicaoUsuarios` — lista usuários
- `GET /exibicaoUsuario/{id}` — busca usuário por id
- `PUT /edicaoUsuario` — edita usuário
- `DELETE /exclusaoUsuario/{id}` — remove usuário
- `GET /exibicaoDispositivosUsuario/{id}` — lista dispositivos do usuário (retorna nomes)

Controller:

- `backend/src/main/java/br/com/WiseMonitor/gerencia/UserController.java`

DTO de resposta:

- `backend/src/main/java/br/com/WiseMonitor/modelos/UserResponseDto.java`

### Dispositivos

- `GET /exibicaoDispositivos`
- `GET /exibicaoDispositivo/{id}`
- `POST /cadastroDispositivo`
- `PUT /edicaoDispositivo`
- `DELETE /exclusaoDispositivo/{id}`

Controller:

- `backend/src/main/java/br/com/WiseMonitor/gerencia/DeviceController.java`

## Testes

### Backend
Na pasta `backend/`:

```bash
./mvnw test
```

### Frontend
Na pasta `frontend/`:

```bash
npm test
```
