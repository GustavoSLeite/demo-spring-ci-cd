# Instruções de Configuração - Pipeline CI/CD

## 1️⃣ Criar Repositório GitHub

```bash
# Inicializar git local
git init
git add .
git commit -m "Initial commit: Calculadora API"

# Criar repositório no GitHub
# https://github.com/new

# Adicionar remote
git remote add origin https://github.com/USUARIO/REPO-NAME.git
git branch -M main
git push -u origin main
```

## 2️⃣ Configurar Secrets do GitHub

Acesse: `https://github.com/USUARIO/REPO-NAME/settings/secrets/actions`

### Adicionar os seguintes secrets:

**EC2_HOST**
- Valor: IP público da instância EC2
- Exemplo: `54.123.45.67`

**EC2_USER**
- Valor: Usuário SSH
- Exemplo: `ec2-user` (Amazon Linux 2)
- Ou: `ubuntu` (Ubuntu)
- Ou: `admin` (Debian)

**EC2_SSH_KEY**
- Valor: Conteúdo completo da chave privada (.pem)
- Como obter:
  ```bash
  cat ~/Downloads/sua-chave.pem
  # Copiar todo o conteúdo (incluindo -----BEGIN e -----END)
  ```

**EC2_SSH_PORT**
- Valor: `22` (padrão)

## 3️⃣ Preparar Instância EC2

### Pré-requisitos
- Instância EC2 running
- Security Group com porta 22 (SSH) e 8080 (aplicação) aberta
- Chave SSH configurada

### Script de Setup

Execute na EC2:

```bash
#!/bin/bash

# Atualizar sistema
sudo yum update -y

# Instalar Docker
sudo yum install docker -y

# Iniciar Docker
sudo systemctl start docker
sudo systemctl enable docker

# Adicionar usuário ao grupo docker
sudo usermod -aG docker ec2-user

# Instalar Docker Compose (opcional)
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Criar diretório para aplicação
mkdir -p ~/calculadora-api

# Verificar instalação
docker --version
```

### Login no Container Registry

```bash
# Gerar Personal Access Token no GitHub
# https://github.com/settings/tokens
# Selecionar: read:packages, write:packages

# Fazer login
docker login ghcr.io -u SEU_USUARIO -p SEU_TOKEN
```

## 4️⃣ Fluxo do Pipeline

```
┌─────────────────┐
│  Push para main │
└────────┬────────┘
         │
         ├─→ [1] CHECKOUT do código
         │
         ├─→ [2] SETUP JDK 17
         │
         ├─→ [3] MAVEN CLEAN PACKAGE
         │
         ├─→ [4] EXECUTAR TESTES
         │        └─ Se falhar: Pipeline para aqui ❌
         │
         ├─→ [5] BUILD DOCKER IMAGE
         │        └─ Push para ghcr.io
         │
         └─→ [6] DEPLOY NA EC2
                  ├─ SSH para EC2
                  ├─ Pull da imagem
                  ├─ Stop do container antigo
                  ├─ Start do novo container
                  └─ Health check
```

## 5️⃣ Monitorar Pipeline

### No GitHub
1. Acesse: `https://github.com/USUARIO/REPO-NAME/actions`
2. Clique no workflow "CI/CD Pipeline"
3. Veja os logs de execução em tempo real

### Na EC2
```bash
# Ver logs do container
docker logs -f calculadora-api

# Ver containers rodando
docker ps

# Testar a API
curl -X POST http://localhost:8080/divisao/10/2

# Health check
curl http://localhost:8080/actuator/health
```

## 6️⃣ Troubleshooting

### Erro: "Permission denied (publickey)"
- Verificar se a chave SSH está no formato correto
- No GitHub Secrets, a chave deve incluir: `-----BEGIN` e `-----END`

### Erro: "docker: not found"
- Executar script de setup novamente
- Fazer logout e login na EC2 para atualizar grupo docker

### Erro: "ghcr.io: unauthorized"
- Regenerar Personal Access Token
- Verificar se tem permissão `write:packages`

### Container não inicia
```bash
# Ver erro do container
docker logs calculadora-api

# Remover container com erro
docker rm -f calculadora-api

# Testar imagem localmente
docker run -p 8080:8080 ghcr.io/USUARIO/REPO-NAME:latest
```

## 7️⃣ Próximos Passos

- [ ] Adicionar mais endpoints à API
- [ ] Configurar logging (ELK Stack)
- [ ] Adicionar autenticação (JWT)
- [ ] Implementar rate limiting
- [ ] Adicionar testes de integração
- [ ] Configurar monitoramento (Prometheus + Grafana)
- [ ] Adicionar CD para staging/production

---

**Pronto! O pipeline está configurado e pronto para uso!** 🚀

