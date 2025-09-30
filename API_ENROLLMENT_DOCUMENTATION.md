# API de Matrícula de Alunos - Documentação

## Visão Geral
Esta API REST permite gerenciar matrículas de alunos em cursos da plataforma Alura.

## Base URL
```
http://localhost:8080/api/enrollments
```

## Endpoints

### 1. Matricular Aluno
**POST** `/api/enrollments`

Matricula um aluno em um curso específico.

**Request Body:**
```json
{
  "studentEmail": "aluno@email.com",
  "courseCode": "spring-boot"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "studentName": "João Silva",
  "studentEmail": "aluno@email.com",
  "courseName": "Spring Boot Fundamentals",
  "courseCode": "spring-boot",
  "status": "ACTIVE",
  "enrolledAt": "2024-01-15T10:30:00",
  "completedAt": null
}
```

**Possíveis Erros:**
- `400 Bad Request`: Aluno não encontrado, curso inativo, já matriculado
- `400 Bad Request`: Dados inválidos (email, código do curso)

### 2. Listar Matrículas do Aluno
**GET** `/api/enrollments/student/{email}`

Retorna todas as matrículas de um aluno específico.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "studentName": "João Silva",
    "studentEmail": "joao@email.com",
    "courseName": "Spring Boot Fundamentals",
    "courseCode": "spring-boot",
    "status": "ACTIVE",
    "enrolledAt": "2024-01-15T10:30:00",
    "completedAt": null
  }
]
```

### 3. Listar Matrículas do Curso
**GET** `/api/enrollments/course/{code}`

Retorna todas as matrículas de um curso específico.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "studentName": "João Silva",
    "studentEmail": "joao@email.com",
    "courseName": "Spring Boot Fundamentals",
    "courseCode": "spring-boot",
    "status": "ACTIVE",
    "enrolledAt": "2024-01-15T10:30:00",
    "completedAt": null
  }
]
```

### 4. Buscar Matrícula Específica
**GET** `/api/enrollments/student/{email}/course/{code}`

Retorna uma matrícula específica de um aluno em um curso.

**Response (200 OK):**
```json
{
  "id": 1,
  "studentName": "João Silva",
  "studentEmail": "joao@email.com",
  "courseName": "Spring Boot Fundamentals",
  "courseCode": "spring-boot",
  "status": "ACTIVE",
  "enrolledAt": "2024-01-15T10:30:00",
  "completedAt": null
}
```

**Response (404 Not Found):** Matrícula não encontrada

### 5. Completar Matrícula
**PATCH** `/api/enrollments/student/{email}/course/{code}/complete`

Marca uma matrícula como concluída.

**Response (200 OK):**
```json
{
  "message": "Enrollment completed successfully"
}
```

**Possíveis Erros:**
- `400 Bad Request`: Matrícula não encontrada ou não está ativa

### 6. Cancelar Matrícula
**PATCH** `/api/enrollments/student/{email}/course/{code}/cancel`

Cancela uma matrícula ativa.

**Response (200 OK):**
```json
{
  "message": "Enrollment cancelled successfully"
}
```

**Possíveis Erros:**
- `400 Bad Request`: Matrícula não encontrada ou não está ativa

### 7. Contar Matrículas Ativas
**GET** `/api/enrollments/course/{code}/count`

Retorna o número de matrículas ativas em um curso.

**Response (200 OK):**
```json
{
  "courseCode": "spring-boot",
  "activeEnrollments": 25
}
```

### 8. Listar Todas as Matrículas
**GET** `/api/enrollments`

Retorna todas as matrículas do sistema.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "studentName": "João Silva",
    "studentEmail": "joao@email.com",
    "courseName": "Spring Boot Fundamentals",
    "courseCode": "spring-boot",
    "status": "ACTIVE",
    "enrolledAt": "2024-01-15T10:30:00",
    "completedAt": null
  }
]
```

## Status de Matrícula

- **ACTIVE**: Matrícula ativa, aluno cursando
- **COMPLETED**: Curso concluído pelo aluno
- **CANCELLED**: Matrícula cancelada

## Validações

### Matrícula de Aluno:
1. **Aluno deve existir** no sistema
2. **Aluno deve ter role STUDENT**
3. **Curso deve existir** e estar ativo
4. **Não pode haver matrícula duplicada** (mesmo aluno no mesmo curso)

### Operações de Status:
1. **Apenas matrículas ACTIVE** podem ser completadas ou canceladas
2. **Matrícula já concluída ou cancelada** não pode ser alterada

## Exemplos de Uso

### Matricular um aluno:
```bash
curl -X POST http://localhost:8080/api/enrollments \
  -H "Content-Type: application/json" \
  -d '{
    "studentEmail": "teste@email.com",
    "courseCode": "spring-boot"
  }'
```

### Listar matrículas de um aluno:
```bash
curl http://localhost:8080/api/enrollments/student/teste@email.com
```

### Completar uma matrícula:
```bash
curl -X PATCH http://localhost:8080/api/enrollments/student/teste@email.com/course/spring-boot/complete
```

### Contar matrículas ativas:
```bash
curl http://localhost:8080/api/enrollments/course/spring-boot/count
```

## Códigos de Status HTTP

- **200 OK**: Operação realizada com sucesso
- **201 Created**: Matrícula criada com sucesso
- **400 Bad Request**: Dados inválidos ou regra de negócio violada
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro interno do servidor
