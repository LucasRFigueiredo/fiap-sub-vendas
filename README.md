# 🛒 Vendas API

## Descrição do Projeto
A **Vendas API** é um microsserviço desenvolvido em **Java 21** com **Spring Boot 3.4.1** para gerenciar o processo de venda de veículos. Ele oferece funcionalidades para registrar vendas, atualizar status de pagamento e consultar vendas concluídas ou pendentes.

---

## 📚 Tecnologias Utilizadas
- Java 21
- Spring Boot 3.4.1
- PostgreSQL
- Docker
- GitHub Actions (CI/CD)
- AWS ECR (Elastic Container Registry)
- OpenFeign (para comunicação com a API de Veículos)

---

## 📂 Estrutura do Projeto
```
Vendas/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com.venda.api/
│   │           ├── application/
│   │           │   ├── port/
│   │           │   │   ├── input/
│   │           │   │   └── output/
│   │           │   └── service/
│   │           ├── domain/
│   │           ├── infrastructure/
│   │           │   ├── adapter/
│   │           │   ├── client/
│   │           │   ├── controller/
│   │           │   └── configuration/
│   │           └── VendaApplication.java
│   └── resources/
│       └── application.properties
│
├── Dockerfile
├── README.md
└── .github/
    └── workflows/
        └── deploy.yml
```

---

## 🚀 Como Rodar Localmente

### 1️⃣ Clonar o repositório
```sh
git clone https://github.com/LucasRFigueiredo/fiap-sub-vendas.git
cd vendas-api
```

### 2️⃣ Configurar o banco de dados
- Banco de dados PostgreSQL hospedado na AWS RDS.
- Atualize o arquivo `application.properties` com suas credenciais.

### 3️⃣ Build e execução
```sh
mvn clean package
java -jar target/venda-0.0.1-SNAPSHOT.jar
```

---

## 📦 Docker
### Build e execução com Docker
```sh
docker build -t vendas-api .
docker run -p 8081:8081 vendas-api
```

---

## 📬 Endpoints Principais
- **POST** `/sales` - Registrar uma nova venda
- **POST** `/sales/webhook` - Atualizar status de pagamento
- **GET** `/sales/disponiveis` - Listar veículos disponíveis para venda
- **GET** `/sales/vendidos` - Listar veículos vendidos

