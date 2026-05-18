# 📋 RESUMO - API REST Java Spring com Pipeline CI/CD

## ✅ O que foi criado

### 1. **API REST Spring Boot**
- `CalculadoraController.java` - REST endpoints
- `CalculadoraService.java` - Lógica de negócio
- `CalculadoraControllerTest.java` - Testes do controller
- `CalculadoraServiceTest.java` - Testes unitários
- `application.properties` - Configurações
- `DemoApplication.java` - Main entry point (já existia)

### 2. **Containerização**
- `Dockerfile` - Multi-stage build com Maven e Java 17
- `.dockerignore` - Ignore de arquivos desnecessários
- Image registrada no GitHub Container Registry (ghcr.io)

### 3. **Pipeline CI/CD (GitHub Actions)**
- `.github/workflows/ci-cd.yml` - Workflow completo com:
  - ✅ Build com Maven
  - ✅ Execução de testes
  - ✅ Build Docker image
  - ✅ Push para container registry
  - ✅ Deploy automático na EC2

### 4. **Documentação**
- `README.md` - Documentação principal
- `SETUP_PIPELINE.md` - Guia de configuração
- `DEVELOPMENT.md` - Guia de desenvolvimento local

## 🚀 Próximos Passos

### PASSO 1: Criar repositório GitHub
```bash
1. Acesse https://github.com/new
2. Nome: calculadora-api (ou similar)
3. Descrição: API REST para operações matemáticas
4. Public (para GitHub Actions grátis)
```

### PASSO 2: Inicializar Git e fazer push
```bash
cd C:\Users\valta\Downloads\demo

git init
git add .
git commit -m "Initial commit: Calculadora API com pipeline CI/CD"

# Adicionar remote (substitua USUARIO/REPO)
git remote add origin https://github.com/USUARIO/REPO-NAME.git
git branch -M main
git push -u origin main
```

### PASSO 3: Configurar Secrets no GitHub
Acesse: https://github.com/USUARIO/REPO-NAME/settings/secrets/actions

Adicione:
```
EC2_HOST      = seu-ip-publico-ec2
EC2_USER      = ec2-user (ou ubuntu, admin)
EC2_SSH_KEY   = conteúdo da chave .pem
EC2_SSH_PORT  = 22
```

### PASSO 4: Preparar EC2
Crie uma instância EC2 com:
- AMI: Amazon Linux 2 ou Ubuntu
- Tipo: t2.micro (free tier)
- Security Group: portas 22 (SSH) e 8080 (app)
- Key pair: nova ou existente

Execute na EC2:
```bash
sudo yum update -y
sudo yum install docker -y
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker ec2-user
docker login ghcr.io -u USUARIO -p TOKEN
```

(Gere TOKEN em: https://github.com/settings/tokens)

### PASSO 5: Testar o Pipeline
```bash
# Fazer qualquer push para main
git add .
git commit -m "test pipeline"
git push

# Assistir o workflow em: 
# https://github.com/USUARIO/REPO-NAME/actions
```

## 📋 Checklist de Configuração

- [ ] Criar repositório no GitHub
- [ ] Fazer push do código
- [ ] Adicionar secrets do GitHub
- [ ] Criar instância EC2
- [ ] Instalar Docker na EC2
- [ ] Gerar GitHub Personal Access Token
- [ ] Fazer login Docker na EC2
- [ ] Fazer primeiro push para main
- [ ] Verificar workflow no GitHub Actions
- [ ] Verificar se container está rodando na EC2
- [ ] Testar endpoint: `curl -X POST http://seu-ip:8080/divisao/10/2`

## 🧪 Testar Localmente

Antes de fazer push:

```bash
cd C:\Users\valta\Downloads\demo\demo

# Compilar
mvn clean compile

# Testes
mvn test

# Build
mvn clean package

# Executar
java -jar target/demo-0.0.1-SNAPSHOT.jar

# Em outro terminal:
curl -X POST http://localhost:8080/divisao/10/2
# Resposta: 5.0
```

## 🐳 Teste Docker Localmente

```bash
cd C:\Users\valta\Downloads\demo\demo

# Build
docker build -t calculadora-api:latest .

# Run
docker run -d -p 8080:8080 --name calculadora-api calculadora-api:latest

# Teste
curl -X POST http://localhost:8080/divisao/10/2

# Logs
docker logs -f calculadora-api

# Cleanup
docker stop calculadora-api
docker rm calculadora-api
```

## 📊 Endpoints Disponíveis

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/divisao/{a}/{b}` | Divide a por b |
| GET | `/actuator/health` | Status da aplicação |
| GET | `/actuator/info` | Informações da app |

### Exemplo de Teste:
```bash
# Sucesso
curl -X POST http://localhost:8080/divisao/10/2
# Resposta: 5.0

# Erro - Divisão por zero
curl -X POST http://localhost:8080/divisao/10/0
# Resposta: HTTP 400 - Divisão por zero não permitida
```

## 🔄 Fluxo do Pipeline

```
Você faz push para main
        ↓
GitHub detecta push
        ↓
Workflow inicia (build-and-test)
        ↓
[1] Checkout código
[2] Setup JDK 17
[3] Maven clean package (compila + cria JAR)
[4] Maven test (executa testes)
        ↓
Se testes passarem:
        ↓
[5] Build Docker image
[6] Push para ghcr.io
        ↓
Workflow deploy inicia
        ↓
[7] SSH na EC2
[8] docker pull ghcr.io/usuario/repo:latest
[9] Para container antigo
[10] Inicia novo container
[11] Verifica saúde (health check)
        ↓
✅ Aplicação disponível em http://seu-ip-ec2:8080
```

## 🔒 Segurança

- Testes garantem validação de entrada
- Health checks monitoram disponibilidade
- Logging estruturado para debug
- HTTPS pode ser adicionado com Nginx/Load Balancer
- Secrets não são expostos em logs

## 📖 Documentação Incluída

1. **README.md** - Overview, endpoints, como executar
2. **SETUP_PIPELINE.md** - Configuração detalhada do CI/CD
3. **DEVELOPMENT.md** - Guia para desenvolvimento local

## 🆘 Suporte Rápido

### Erro na compilação?
```bash
cd demo
mvn clean compile -X  # modo verbose
```

### Erro nos testes?
```bash
mvn test -Dtest=NomeDaTeste
# ou
mvn test -DskipTests  # pular testes (não recomendado)
```

### Erro ao fazer push?
```bash
git remote -v  # verificar URL
git status     # status dos arquivos
git log        # histórico
```

### Container não inicia?
```bash
docker logs calculadora-api  # ver erro específico
docker run -it calculadora-api:latest bash  # entrar no container
```

---

## ✨ Pronto!

O projeto está 100% configurado e pronto para:
- ✅ Desenvolvimento local
- ✅ Testes automatizados
- ✅ Build e containerização
- ✅ Deploy automático na EC2

Basta seguir os 5 passos acima! 🚀

**Qualquer dúvida, verifique os arquivos de documentação!**

