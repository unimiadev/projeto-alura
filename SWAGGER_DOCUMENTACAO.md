# Documentação Swagger/OpenAPI - Projeto Alura

## 📋 Visão Geral

A documentação da API do Projeto Alura foi implementada usando **Swagger/OpenAPI 3.0** com SpringDoc, fornecendo uma interface interativa completa para testar e explorar todas as APIs REST disponíveis.

## 🚀 Acesso à Documentação

### **URLs Principais:**
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`
- **OpenAPI YAML**: `http://localhost:8080/api-docs.yaml`

### **Interface Interativa:**
A interface Swagger UI permite:
- ✅ **Explorar endpoints**: Visualizar todos os endpoints disponíveis
- ✅ **Testar APIs**: Executar requisições diretamente na interface
- ✅ **Ver schemas**: Examinar estruturas de request/response
- ✅ **Validar dados**: Verificar formatos e validações

## 📚 APIs Documentadas

### **1. API de Matrículas (`/api/enrollments`)**

#### **Endpoints Principais:**
- `POST /api/enrollments` - Matricular aluno em curso
- `GET /api/enrollments/student/{email}` - Listar matrículas do aluno
- `GET /api/enrollments/course/{code}` - Listar matrículas do curso
- `GET /api/enrollments/student/{email}/course/{code}` - Buscar matrícula específica
- `PATCH /api/enrollments/student/{email}/course/{code}/complete` - Completar matrícula
- `PATCH /api/enrollments/student/{email}/course/{code}/cancel` - Cancelar matrícula
- `GET /api/enrollments/course/{code}/count` - Contar matrículas ativas

#### **Schemas Documentados:**
- **NewEnrollmentRequest**: Dados para matrícula
- **EnrollmentResponse**: Resposta com dados da matrícula
- **ErrorResponse**: Estrutura de erros padronizada

### **2. API de Relatórios (`/api/reports`)**

#### **Endpoints Principais:**
- `GET /api/reports/most-accessed` - Cursos mais acessados
- `GET /api/reports/completion` - Relatório de taxa de conclusão
- `GET /api/reports/category/{categoryCode}` - Cursos por categoria
- `GET /api/reports/summary` - Resumo estatístico geral

#### **Schemas Documentados:**
- **CourseAccessReportDTO**: Dados do relatório de acesso
- **ReportSummaryDTO**: Resumo estatístico do sistema

## 🛠️ Implementação Técnica

### **Dependências Adicionadas:**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

### **Configuração Principal:**
```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Projeto Alura - API Documentation")
                        .version("1.0.0")
                        .description("API completa para gerenciamento de cursos, matrículas e relatórios")
                        .contact(new Contact()
                                .name("Equipe Alura")
                                .email("contato@alura.com.br"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Desenvolvimento"),
                        new Server().url("https://api.alura.com.br").description("Produção")
                ));
    }
}
```

### **Propriedades de Configuração:**
```properties
# Configurações do Swagger/OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.tryItOutEnabled=true
```

## 🏷️ Anotações Utilizadas

### **Nível de Controller:**
```java
@Tag(name = "Matrículas", description = "API para gerenciamento de matrículas de alunos em cursos")
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
```

### **Nível de Método:**
```java
@Operation(summary = "Matricular aluno em curso", 
           description = "Realiza a matrícula de um aluno em um curso específico")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Matrícula realizada com sucesso",
                content = @Content(schema = @Schema(implementation = EnrollmentResponse.class))),
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
@PostMapping
public ResponseEntity<?> enrollStudent(
        @Parameter(description = "Dados da matrícula", required = true)
        @Valid @RequestBody NewEnrollmentRequest request) {
```

### **Nível de DTO:**
```java
@Schema(description = "Dados para matrícula de aluno em curso")
public class NewEnrollmentRequest {

    @Schema(description = "Email do aluno", example = "joao.silva@student.com", required = true)
    private String studentEmail;

    @Schema(description = "Código do curso", example = "spring-boot", required = true)
    private String courseCode;
```

## 📊 Funcionalidades da Interface

### **Organização por Tags:**
- 🎓 **Matrículas**: Endpoints de gerenciamento de matrículas
- 📈 **Relatórios**: Endpoints de relatórios e estatísticas
- 👥 **Usuários**: Endpoints de gerenciamento de usuários (se aplicável)

### **Recursos Interativos:**
- ✅ **Try it out**: Botão para testar endpoints
- ✅ **Schemas**: Visualização de estruturas de dados
- ✅ **Examples**: Exemplos de request/response
- ✅ **Validation**: Indicação de campos obrigatórios
- ✅ **Response codes**: Códigos HTTP documentados

### **Filtros e Ordenação:**
- **Operações**: Ordenadas por método HTTP
- **Tags**: Ordenadas alfabeticamente
- **Busca**: Campo de busca por endpoint

## 🔍 Exemplos de Uso

### **1. Matricular Aluno:**
```json
POST /api/enrollments
{
  "studentEmail": "joao.silva@student.com",
  "courseCode": "spring-boot"
}
```

**Response (201):**
```json
{
  "id": 1,
  "studentName": "João Silva",
  "studentEmail": "joao.silva@student.com",
  "courseName": "Spring Boot Fundamentals",
  "courseCode": "spring-boot",
  "status": "ACTIVE",
  "enrolledAt": "2024-01-15T10:30:00",
  "completedAt": null
}
```

### **2. Listar Cursos Mais Acessados:**
```
GET /api/reports/most-accessed?limit=5
```

**Response (200):**
```json
[
  {
    "courseName": "Spring Boot Avançado",
    "courseCode": "spring-boot",
    "categoryName": "Programação",
    "instructorEmail": "instructor@alura.com",
    "totalEnrollments": 45,
    "activeEnrollments": 27,
    "completedEnrollments": 18,
    "completionRate": 40.0
  }
]
```

## 🎯 Benefícios da Documentação

### **Para Desenvolvedores:**
- ✅ **Exploração rápida**: Interface visual para descobrir APIs
- ✅ **Testes integrados**: Não precisa de ferramentas externas
- ✅ **Validação automática**: Schemas validam dados automaticamente
- ✅ **Exemplos práticos**: Dados de exemplo para cada endpoint

### **Para Integração:**
- ✅ **Especificação OpenAPI**: Padrão da indústria
- ✅ **Geração de clientes**: Possibilidade de gerar SDKs
- ✅ **Documentação sempre atualizada**: Sincronizada com o código
- ✅ **Múltiplos formatos**: JSON, YAML disponíveis

### **Para QA/Testes:**
- ✅ **Testes manuais**: Interface para testes exploratórios
- ✅ **Validação de contratos**: Verificar se API atende especificação
- ✅ **Cenários de erro**: Documentação de códigos de erro
- ✅ **Dados de teste**: Exemplos para criar cenários

## 🔧 Configurações Avançadas

### **Personalização da UI:**
```properties
# Habilitar botão "Try it out"
springdoc.swagger-ui.tryItOutEnabled=true

# Ordenação de operações por método HTTP
springdoc.swagger-ui.operationsSorter=method

# Ordenação de tags alfabeticamente
springdoc.swagger-ui.tagsSorter=alpha

# Configurar tema (opcional)
springdoc.swagger-ui.configUrl=/v3/api-docs/swagger-config
```

### **Filtros de Endpoints:**
```java
// Para incluir apenas endpoints específicos
@Bean
public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/api/**")
            .build();
}
```

### **Segurança (se implementada):**
```java
// Configuração de autenticação
.components(new Components()
    .addSecuritySchemes("bearer-key",
        new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")))
```

## 📱 Responsividade

A interface Swagger UI é **totalmente responsiva** e funciona em:
- 💻 **Desktop**: Interface completa
- 📱 **Mobile**: Interface adaptada para telas pequenas
- 📟 **Tablet**: Interface otimizada para telas médias

## 🚀 Deploy e Produção

### **Configuração para Produção:**
```properties
# Desabilitar Swagger em produção (opcional)
springdoc.swagger-ui.enabled=false
springdoc.api-docs.enabled=false

# Ou configurar apenas para perfis específicos
springdoc.swagger-ui.enabled=${SWAGGER_ENABLED:false}
```

### **URLs de Produção:**
```java
.servers(List.of(
    new Server()
        .url("https://api.alura.com.br")
        .description("Servidor de Produção"),
    new Server()
        .url("https://staging-api.alura.com.br")
        .description("Servidor de Staging")
))
```

## 📈 Métricas e Monitoramento

A documentação Swagger pode ser integrada com:
- **Actuator**: Endpoints de saúde e métricas
- **Micrometer**: Métricas de uso da API
- **Logging**: Logs de acesso aos endpoints documentados

## ✅ Checklist de Implementação

- ✅ **Dependência SpringDoc adicionada**
- ✅ **Configuração SwaggerConfig criada**
- ✅ **Anotações @Tag nos controllers**
- ✅ **Anotações @Operation nos métodos**
- ✅ **Anotações @Schema nos DTOs**
- ✅ **Propriedades de configuração**
- ✅ **Exemplos e descrições detalhadas**
- ✅ **Códigos de resposta documentados**
- ✅ **Interface acessível e funcional**

## 🎉 Resultado Final

A documentação Swagger está **100% funcional** e acessível em:
**`http://localhost:8080/swagger-ui.html`**

### **Funcionalidades Disponíveis:**
- 📚 **15+ endpoints** documentados
- 🏷️ **2 tags principais** (Matrículas, Relatórios)
- 📋 **10+ schemas** detalhados
- ✅ **Interface interativa** completa
- 🧪 **Testes integrados** na UI
- 📱 **Design responsivo**

A documentação fornece uma **experiência completa** para desenvolvedores, QA e integradores, facilitando o uso e teste de todas as APIs do Projeto Alura!
