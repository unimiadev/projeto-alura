<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Editar Categoria</title>
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
            max-width: 600px;
            margin: 2rem auto;
            padding: 2rem;
            background: #f8f9fa;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .color-preview {
            width: 30px;
            height: 30px;
            border-radius: 4px;
            border: 1px solid #ddd;
            display: inline-block;
            margin-left: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="form-container">
        <h2>Editar Categoria</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form:form modelAttribute="editCategoryForm" method="post" action="/admin/category/edit/${category.id}">
            <div class="form-group mb-3">
                <label for="category-name">Nome da Categoria: <span class="text-danger">*</span></label>
                <form:input path="name" id="category-name" cssClass="form-control" 
                           placeholder="Ex: Programação" required="required" maxlength="50"/>
                <form:errors path="name" cssClass="error-message"/>
            </div>

            <div class="form-group mb-3">
                <label for="category-code">Código da Categoria: <span class="text-danger">*</span></label>
                <form:input path="code" id="category-code" cssClass="form-control" 
                           placeholder="Ex: programacao" required="required"
                           pattern="[a-zA-Z0-9\\-]+" maxlength="10"/>
                <div class="form-text">Entre 4 e 10 caracteres. Apenas letras, números e hífens.</div>
                <form:errors path="code" cssClass="error-message"/>
            </div>

            <div class="form-group mb-3">
                <label for="category-color">Cor da Categoria: <span class="text-danger">*</span></label>
                <div class="input-group">
                    <form:input path="color" id="category-color" cssClass="form-control" 
                               type="color" required="required"/>
                    <span class="color-preview" id="color-preview"></span>
                </div>
                <form:errors path="color" cssClass="error-message"/>
            </div>

            <div class="form-group mb-3">
                <label for="category-order">Ordem de Exibição: <span class="text-danger">*</span></label>
                <form:input path="order" id="category-order" cssClass="form-control" 
                           type="number" min="1" required="required"/>
                <div class="form-text">Número que define a ordem de exibição da categoria.</div>
                <form:errors path="order" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-primary">Salvar Alterações</button>
                <a href="/admin/categories" class="btn btn-secondary">Cancelar</a>
            </div>
        </form:form>
    </div>
</div>

<script>
    function updateColorPreview() {
        const colorInput = document.getElementById('category-color');
        const colorPreview = document.getElementById('color-preview');
        colorPreview.style.backgroundColor = colorInput.value;
    }

    document.addEventListener('DOMContentLoaded', function() {
        updateColorPreview();
        document.getElementById('category-color').addEventListener('input', updateColorPreview);
    });
</script>
</body>
</html>
