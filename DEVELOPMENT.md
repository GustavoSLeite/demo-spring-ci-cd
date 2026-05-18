# Desenvolvimento Local

## 🔧 Compilar e Executar Localmente

### Build
```bash
cd demo
mvn clean package
```

### Executar a aplicação
```bash
mvn spring-boot:run
```

Ou:
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## 🧪 Testes

### Executar todos os testes
```bash
mvn test
```

### Executar teste específico
```bash
mvn test -Dtest=CalculadoraServiceTest
mvn test -Dtest=CalculadoraControllerTest
```

### Testes com coverage
```bash
mvn test jacoco:report
```

## 🐳 Docker Local

### Build da imagem
```bash
cd demo
docker build -t calculadora-api:latest .
```

### Executar container
```bash
docker run -d -p 8080:8080 --name calculadora-api calculadora-api:latest
```

### Logs do container
```bash
docker logs -f calculadora-api
```

### Parar e remover
```bash
docker stop calculadora-api
docker rm calculadora-api
```

## 🧩 Estrutura de Pastas

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java           # Main entry point
│   │   │   ├── CalculadoraController.java     # REST endpoints
│   │   │   └── CalculadoraService.java        # Business logic
│   │   └── resources/
│   │       └── application.properties         # Config
│   └── test/
│       └── java/com/example/demo/
│           ├── CalculadoraControllerTest.java # Controller tests
│           └── CalculadoraServiceTest.java    # Service tests
├── .mvn/                    # Maven wrapper files
├── target/                  # Build output (gitignored)
├── pom.xml                  # Dependencies
├── Dockerfile               # Container config
├── .dockerignore            # Docker ignore rules
├── mvnw / mvnw.cmd         # Maven wrapper scripts
└── HELP.md                  # Spring Boot info
```

## 📝 Adicionando Novos Endpoints

Exemplo: Adicionar endpoint de soma

### 1. Adicionar método no Service

Editar `CalculadoraService.java`:
```java
public double somar(double a, double b) {
    return a + b;
}
```

### 2. Adicionar endpoint no Controller

Editar `CalculadoraController.java`:
```java
@PostMapping("/soma/{a}/{b}")
public ResponseEntity<Double> soma(@PathVariable double a, @PathVariable double b) {
    return ResponseEntity.ok(calculadoraService.somar(a, b));
}
```

### 3. Adicionar testes

Editar `CalculadoraServiceTest.java`:
```java
@Test
public void testSomaSucesso() {
    double resultado = calculadoraService.somar(5, 3);
    assertEquals(8.0, resultado);
}
```

Editar `CalculadoraControllerTest.java`:
```java
@Test
public void testSomaSucesso() throws Exception {
    when(calculadoraService.somar(5, 3)).thenReturn(8.0);
    
    mockMvc.perform(post("/soma/5/3"))
            .andExpect(status().isOk())
            .andExpect(content().string("8.0"));
}
```

### 4. Testar

```bash
mvn test
mvn spring-boot:run
curl -X POST http://localhost:8080/soma/5/3
```

## 🔍 Debug

### Modo debug com IDE
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

### Variáveis de ambiente
Editar `application.properties`:
```properties
logging.level.root=DEBUG
logging.level.com.example.demo=DEBUG
```

## 📊 Verificar Saúde da Aplicação

```bash
# Health check
curl http://localhost:8080/actuator/health

# Info
curl http://localhost:8080/actuator/info
```

## 🚀 Deploy Local com Docker Compose (Opcional)

Criar `docker-compose.yml`:
```yaml
version: '3.8'
services:
  calculadora:
    build: ./demo
    ports:
      - "8080:8080"
    environment:
      - JAVA_OPTS=-Xmx512m
    restart: always
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
```

Executar:
```bash
docker-compose up -d
docker-compose logs -f
docker-compose down
```

---

**Dica:** Use extensões de IDE como Lombok, SonarLint para melhor desenvolvimento!

