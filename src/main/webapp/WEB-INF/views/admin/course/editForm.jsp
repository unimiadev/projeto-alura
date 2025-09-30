<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Editar Curso</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
    <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .error-message {
            color: #dc3545;
            font-size: 0.875em;
            margin-top: 0.25rem;
        }
        .form-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
            background: #f8f9fa;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .code-help {
            font-size: 0.875em;
            color: #6c757d;
            margin-top: 0.25rem;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="form-container">
        <h2>Editar Curso</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form:form modelAttribute="editCourseForm" method="post" action="/admin/course/edit/${course.code}">
            <div class="form-group mb-3">
                <label for="course-name">Nome do Curso: <span class="text-danger">*</span></label>
                <form:input path="name" id="course-name" cssClass="form-control" 
                           placeholder="Ex: Spring Boot Avançado" required="required" maxlength="100"/>
                <form:errors path="name" cssClass="error-message"/>
            </div>

            <div class="form-group mb-3">
                <label for="course-code">Código do Curso: <span class="text-danger">*</span></label>
                <form:input path="code" id="course-code" cssClass="form-control" 
                           placeholder="Ex: spring-boot-avancado" required="required"
                           pattern="[a-zA-Z0-9\\-]+" maxlength="10"/>
                <div class="code-help">
                    Entre 4 e 10 caracteres. Apenas letras, números e hífens. Espaços serão convertidos automaticamente.
                </div>
                <form:errors path="code" cssClass="error-message"/>
            </div>

            <div class="form-group mb-3">
                <label for="instructor-email">Email do Instrutor: <span class="text-danger">*</span></label>
                <form:input path="instructorEmail" id="instructor-email" cssClass="form-control" 
                           type="email" placeholder="Ex: instrutor@alura.com.br" required="required"/>
                <form:errors path="instructorEmail" cssClass="error-message"/>
            </div>

            <div class="form-group mb-3">
                <label for="category-id">Categoria: <span class="text-danger">*</span></label>
                <form:select path="categoryId" id="category-id" cssClass="form-control" required="required">
                    <option value="">Selecione uma categoria</option>
                    <c:forEach items="${categories}" var="category">
                        <form:option value="${category.id}">${category.name}</form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="categoryId" cssClass="error-message"/>
            </div>

            <div class="form-group mb-3">
                <label for="course-description">Descrição do Curso: <span class="text-danger">*</span></label>
                <form:textarea path="description" id="course-description" cssClass="form-control" 
                              rows="4" placeholder="Descreva o conteúdo e objetivos do curso..." 
                              required="required" maxlength="500"/>
                <div class="form-text">Máximo 500 caracteres.</div>
                <form:errors path="description" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-primary">Salvar Alterações</button>
                <a href="/admin/courses" class="btn btn-secondary">Cancelar</a>
            </div>
        </form:form>
    </div>
</div>

<script>
    document.getElementById('course-code').addEventListener('input', function(e) {
        let value = e.target.value;
        value = value.toLowerCase().replace(/\s+/g, '-');
        value = value.replace(/[^a-z0-9-]/g, '');
        e.target.value = value;
    });

    const descriptionField = document.getElementById('course-description');
    const maxLength = 500;
    
    function updateCharacterCount() {
        const currentLength = descriptionField.value.length;
        const remaining = maxLength - currentLength;
        const helpText = descriptionField.parentNode.querySelector('.form-text');
        
        if (helpText) {
            helpText.textContent = remaining + ' caracteres restantes (máximo ' + maxLength + ')';
            
            if (remaining < 50) {
                helpText.style.color = '#dc3545';
            } else {
                helpText.style.color = '#6c757d';
            }
        }
    }
    
    if (descriptionField) {
        descriptionField.addEventListener('input', updateCharacterCount);
        updateCharacterCount();
    }
</script>
</body>
</html>
