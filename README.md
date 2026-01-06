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

### 1. Executando com Docker (recomendado)

Pré‑requisitos:

- Docker e Docker Compose instalados

Na raiz do projeto (onde está o `docker-compose.yml`), execute:

```bash
docker compose up -d --build
```

Isso irá subir:

- Backend Spring Boot na porta `8080`
- Frontend React na porta `3000`
- Banco de dados MySQL (conforme configuração do `docker-compose.yml` e `application.properties`)

Para parar os containers:

```bash
docker compose down
```

### 2. Executando manualmente (sem Docker)

#### Backend (Spring Boot)

Abra um terminal na pasta `backend/` e execute:

```bash
./mvnw spring-boot:run
```

No Windows (PowerShell / CMD):

```bat
mvnw.cmd spring-boot:run
```

A API sobe, por padrão, em: `http://localhost:8080`

#### Frontend (React)

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

### Requisitos não funcionais de segurança

- **Autenticação baseada em JWT**: todas as rotas (exceto login e cadastro de usuário) exigem token válido no header `Authorization: Bearer <token>`.
- **Sessão stateless**: o backend não mantém sessão de usuário em memória/servidor; todas as requisições autenticadas devem enviar o token.
- **Controle de acesso por roles**: a entidade `User` possui um campo `role` (`UserRole`) que diferencia, por exemplo, `ADMIN` e `USER`.
	- Usuários com role `ADMIN` podem gerenciar outros usuários.
	- Usuários com role padrão só podem editar/remover a si mesmos.
- **Senha armazenada com hash**: senhas são persistidas de forma segura (ex.: `BCryptPasswordEncoder` via Spring Security).
- **Segredo JWT externo**: a chave usada para assinar tokens (`JWT_SECRET`) não deve ser hardcoded, e sim fornecida via variável de ambiente em produção.

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
- `PUT /edicaoDispositivo/{id}`
- `DELETE /exclusaoDispositivo/{id}`

Controller:

- `backend/src/main/java/br/com/WiseMonitor/gerencia/DeviceController.java`

> Importante: os endpoints de **edição** (`PUT /edicaoDispositivo/{id}`) e **remoção** (`DELETE /exclusaoDispositivo/{id}`) de dispositivos estão implementados e funcionando no backend, mas a integração completa desses fluxos no frontend ainda não está finalizada. Na versão atual, os botões de editar/remover dispositivo na tela de Home podem não refletir corretamente as mudanças na interface ou podem apresentar comportamentos inconsistentes. Isso pode ser ajustado com pequenas correções em versões futuras, sem alterar os contratos da API.

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
