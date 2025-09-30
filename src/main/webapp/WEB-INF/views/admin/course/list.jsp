<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Lista de Cursos</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
    <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .status-active {
            color: #28a745;
            font-weight: bold;
        }
        .status-inactive {
            color: #dc3545;
            font-weight: bold;
        }
        .btn-inactivate {
            margin-left: 5px;
        }
        .course-description {
            max-width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    </style>
</head>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h1>Cursos</h1>
            <div class="btn-group" role="group">
                <a class="btn btn-info new-button" href="/admin/course/new">Cadastrar novo</a>
                <a class="btn btn-secondary" href="/admin/reports">📊 Relatórios</a>
                <a class="btn btn-outline-secondary" href="/admin/categories">📂 Categorias</a>
            </div>
        </div>
        <table class="panel-body table table-hover">
            <thead>
            <tr>
                <th>Nome</th>
                <th>Código</th>
                <th>Instrutor</th>
                <th>Categoria</th>
                <th>Descrição</th>
                <th>Status</th>
                <th>Data Criação</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${courses}" var="course">
                <tr>
                    <td>${course.name}</td>
                    <td><code>${course.code}</code></td>
                    <td>${course.instructorEmail}</td>
                    <td>${course.categoryName}</td>
                    <td class="course-description" title="${course.description}">${course.description}</td>
                    <td>
                        <c:choose>
                            <c:when test="${course.active}">
                                <span class="status-active">ATIVO</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-inactive">INATIVO</span>
                                <c:if test="${course.inactivatedAt != null}">
                                    <br><small>Inativado em: ${course.inactivatedAt}</small>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${course.createdAt}</td>
                    <td>
                        <a class="btn btn-primary btn-sm" href="/admin/course/edit/${course.code}">Editar</a>
                        <c:choose>
                            <c:when test="${course.active}">
                                <button class="btn btn-warning btn-sm btn-inactivate" 
                                        onclick="inactivateCourse('${course.code}')">Inativar</button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-success btn-sm btn-activate" 
                                        onclick="activateCourse('${course.code}')">Ativar</button>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty courses}">
                <tr>
                    <td colspan="8" class="text-center">Nenhum curso cadastrado</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script src="/assets/external-libs/bootstrap/js/bootstrap.min.js"></script>
<script>
function inactivateCourse(courseCode) {
    if (confirm('Tem certeza que deseja inativar o curso "' + courseCode + '"?')) {
        toggleCourseStatus(courseCode, 'inactive', 'inativado');
    }
}

function activateCourse(courseCode) {
    if (confirm('Tem certeza que deseja ativar o curso "' + courseCode + '"?')) {
        toggleCourseStatus(courseCode, 'active', 'ativado');
    }
}

function toggleCourseStatus(courseCode, action, actionText) {
    fetch('/course/' + courseCode + '/' + action, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => {
        if (response.ok) {
            return response.text().then(text => {
                alert('Curso ' + actionText + ' com sucesso!');
                location.reload();
            });
        } else {
            return response.text().then(text => {
                alert('Erro ao ' + actionText.replace('ado', 'ar') + ' curso: ' + text);
            });
        }
    })
    .catch(error => {
        alert('Erro ao ' + actionText.replace('ado', 'ar') + ' curso: ' + error.message);
    });
}
</script>

</html>
