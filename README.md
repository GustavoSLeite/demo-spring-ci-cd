# Calculadora API - REST Service

API REST desenvolvida em Java com Spring Boot para operações matemáticas (divisão).

## 📋 Tecnologias

- **Java 17**
- **Spring Boot 4.0.6**
- **Maven**
- **Docker**
- **GitHub Actions (CI/CD)**

## 🚀 Começando

### Pré-requisitos

- Java 17+
- Maven 3.9+
- Docker (para executar localmente com container)

### Instalação Local

```bash
cd demo
mvn clean install
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

## 📚 Endpoints

### POST /divisao/{a}/{b}
Realiza divisão de dois números.

**Exemplo:**
```bash
curl -X POST http://localhost:8080/divisao/10/2
```

**Resposta:**
```json
5.0
```

**Erro (divisão por zero):**
```bash
curl -X POST http://localhost:8080/divisao/10/0
```

**Resposta:**
```
HTTP 400 - Divisão por zero não permitida
```

## 🧪 Testes

```bash
cd demo
mvn test
```

## 🐳 Docker

### Build da imagem
```bash
cd demo
docker build -t calculadora-api:latest .
```

### Executar container
```bash
docker run -d -p 8080:8080 --name calculadora-api calculadora-api:latest
```

### Verificar saúde da aplicação
```bash
curl http://localhost:8080/actuator/health
```

## 🔄 CI/CD Pipeline

O pipeline automático executa:

1. ✅ **Build** - Compila a aplicação com Maven
2. ✅ **Testes** - Executa testes unitários e de integração
3. ✅ **Docker** - Cria imagem Docker e faz push para registry
4. ✅ **Deploy** - Entrega para EC2 via SSH

### Configuração de Secrets no GitHub

Adicione os seguintes secrets em `Settings > Secrets and variables > Actions`:

| Secret | Descrição |
|--------|-----------|
| `EC2_HOST` | IP público da instância EC2 |
| `EC2_USER` | Usuário SSH da EC2 (ex: ec2-user) |
| `EC2_SSH_KEY` | Chave privada SSH (.pem) |
| `EC2_SSH_PORT` | Porta SSH (default: 22) |

## 🛠️ Setup EC2

### 1. Instalar Docker na EC2

```bash
#!/bin/bash
sudo yum update -y
sudo yum install docker -y
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker ec2-user
```

### 2. Fazer login no GitHub Container Registry

```bash
docker login ghcr.io -u USERNAME -p TOKEN
```

### 3. Verificar logs do container

```bash
docker logs -f calculadora-api
```

## 📊 Health Check

A aplicação possui um endpoint de health check:

```bash
curl http://localhost:8080/actuator/health
```

## 📝 Estrutura do Projeto

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java (Main)
│   │   │   ├── CalculadoraController.java
│   │   │   └── CalculadoraService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/demo/
│           ├── CalculadoraControllerTest.java
│           └── CalculadoraServiceTest.java
├── Dockerfile
├── .dockerignore
└── pom.xml
```

## 🔐 Segurança

- Validação de entrada (divisão por zero)
- Tratamento de exceções adequado
- Health checks configurados
- Logs estruturados

## 📄 Licença

MIT License

## 👥 Contribuindo

1. Crie um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

**Desenvolvido com ❤️**

