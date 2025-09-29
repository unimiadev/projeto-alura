<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cadastrar novo Curso</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
    <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .error-message {
            color: #dc3545;
            font-size: 0.875em;
            margin-top: 5px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .code-help {
            font-size: 0.875em;
            color: #6c757d;
            margin-top: 5px;
        }
    </style>
</head>

<div class="container">
    <section class="panel panel-primary vertical-space">
        <div class="panel-heading">
            <h1>Cadastrar novo curso</h1>
            <a class="btn btn-default" href="/admin/courses">← Voltar para lista</a>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>

        <form:form modelAttribute="newCourseForm" cssClass="form-horizontal panel-body" action="/admin/course/new" method="post">
            <div class="form-group">
                <div class="col-md-9">
                    <label for="course-name">Nome do Curso: <span class="text-danger">*</span></label>
                    <form:input path="name" id="course-name" cssClass="form-control" 
                               placeholder="Ex: Spring Boot Avançado" required="required"/>
                    <form:errors path="name" cssClass="error-message"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-9">
                    <label for="course-code">Código do Curso: <span class="text-danger">*</span></label>
                    <form:input path="code" id="course-code" cssClass="form-control" 
                               placeholder="Ex: spring-boot-avancado" required="required"
                               pattern="[a-zA-Z0-9\\-]+" maxlength="10"/>
                    <div class="code-help">
                        Entre 4 e 10 caracteres. Apenas letras, números e hífens. Espaços serão convertidos automaticamente.
                    </div>
                    <form:errors path="code" cssClass="error-message"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-9">
                    <label for="course-instructorEmail">Email do Instrutor: <span class="text-danger">*</span></label>
                    <form:input path="instructorEmail" type="email" id="course-instructorEmail" 
                               cssClass="form-control" placeholder="instrutor@alura.com.br" required="required"/>
                    <form:errors path="instructorEmail" cssClass="error-message"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-9">
                    <label for="course-category">Categoria: <span class="text-danger">*</span></label>
                    <form:select path="categoryId" id="course-category" cssClass="form-control" required="required">
                        <option value="">Selecione uma categoria</option>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}">${category.name} (${category.code})</option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="categoryId" cssClass="error-message"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-9">
                    <label for="course-description">Descrição:</label>
                    <form:textarea path="description" id="course-description" cssClass="form-control" 
                                  rows="4" placeholder="Descrição detalhada do curso (opcional)"/>
                    <form:errors path="description" cssClass="error-message"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-md-9">
                    <input class="btn btn-success" type="submit" value="Salvar Curso"/>
                    <a class="btn btn-default" href="/admin/courses">Cancelar</a>
                </div>
            </div>
        </form:form>
    </section>
</div>

<script src="/assets/external-libs/bootstrap/js/bootstrap.min.js"></script>
<script>
document.getElementById('course-code').addEventListener('input', function(e) {
    let value = e.target.value;
    value = value.toLowerCase()
                 .replace(/\s+/g, '-')
                 .replace(/[^a-z0-9-]/g, '');
    e.target.value = value;
});

document.getElementById('course-code').addEventListener('blur', function(e) {
    let value = e.target.value;
    if (value.length < 4) {
        e.target.setCustomValidity('O código deve ter pelo menos 4 caracteres');
    } else if (value.length > 10) {
        e.target.setCustomValidity('O código deve ter no máximo 10 caracteres');
    } else {
        e.target.setCustomValidity('');
    }
});
</script>

</html>
