# Order Management System (OMS)

Sistema de gestão de pedidos cloud-native na AWS.

## Stack

| Camada      | Tecnologia                        |
|-------------|-----------------------------------|
| Backend     | Java 21 + Spring Boot 3.2         |
| Frontend    | HTML / CSS / JavaScript (Nginx)   |
| Banco       | PostgreSQL 16 (Amazon RDS)        |
| Lambda      | Python 3.12 (boto3-free)          |
| Contêineres | Docker + ECS Fargate              |
| Gateway     | Amazon API Gateway                |

## Arquitetura AWS

```
Internet → API Gateway
  ├── GET /report            → Lambda Python (stats)
  ├── /api/customers/**      → ECS Fargate → RDS
  ├── /api/orders/**         → ECS Fargate → RDS
  ├── /api/dashboard/stats   → ECS Fargate → RDS
  └── /                      → ECS Fargate (Nginx + frontend)
```

## Desenvolvimento local

### Pré-requisitos
- Docker Desktop
- Java 21 + Maven (opcional, para rodar sem Docker)

### Subir tudo com Docker Compose

```bash
cd infrastructure
docker-compose up --build
```

Acesse `http://localhost` no browser.

A API REST fica em `http://localhost:8080/api`.

### Testar a Lambda localmente

```bash
cd lambda/report
python handler.py
```

> Defina `API_BASE_URL=http://localhost:8080` antes de rodar se o backend estiver local.

## Deploy na AWS

### 1. Build e push das imagens

```bash
# backend
cd backend
docker build -t oms-backend .
docker tag oms-backend:latest <ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/oms-backend:latest
docker push <ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/oms-backend:latest

# frontend
cd ../frontend
docker build -t oms-frontend .
docker tag oms-frontend:latest <ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/oms-frontend:latest
docker push <ACCOUNT_ID>.dkr.ecr.<REGION>.amazonaws.com/oms-frontend:latest
```

### 2. Registrar a task definition

```bash
aws ecs register-task-definition \
  --cli-input-json file://infrastructure/ecs-task-definition.json
```

Substitua os placeholders `<ACCOUNT_ID>`, `<REGION>`, `<RDS_ENDPOINT>` e `<CHANGE_ME>` antes de registrar.

### 3. Lambda

```bash
cd lambda/report
zip function.zip handler.py
aws lambda create-function \
  --function-name oms-report \
  --runtime python3.12 \
  --role arn:aws:iam::<ACCOUNT_ID>:role/lambda-execution-role \
  --handler handler.lambda_handler \
  --zip-file fileb://function.zip \
  --environment Variables={API_BASE_URL=http://<ECS_ALB_DNS>}
```

### 4. API Gateway — rotas

| Método | Rota                        | Destino           |
|--------|-----------------------------|-------------------|
| ANY    | /api/customers/{proxy+}     | ECS Fargate       |
| ANY    | /api/orders/{proxy+}        | ECS Fargate       |
| GET    | /api/dashboard/stats        | ECS Fargate       |
| GET    | /report                     | Lambda oms-report |

## Variáveis de ambiente

| Variável      | Descrição                              |
|---------------|----------------------------------------|
| `DB_URL`      | JDBC URL do RDS PostgreSQL             |
| `DB_USER`     | Usuário do banco                       |
| `DB_PASSWORD` | Senha do banco                         |
| `API_BASE_URL`| (Lambda) URL base da API no ECS/ALB   |

## Estrutura do projeto

```
order-management-system/
├── backend/        # Spring Boot (Java 21)
├── frontend/       # HTML/CSS/JS + Nginx
├── lambda/report/  # Python Lambda
└── infrastructure/ # docker-compose + ECS task definition
```
