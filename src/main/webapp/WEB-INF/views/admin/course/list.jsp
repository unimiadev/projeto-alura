<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Lista de Cursos</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
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
            <a class="btn btn-info new-button" href="/admin/course/new">Cadastrar novo</a>
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
                                <br><small>Inativado em: <fmt:formatDate value="${course.inactivatedAt}" pattern="dd/MM/yyyy HH:mm"/></small>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><fmt:formatDate value="${course.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                    <td>
                        <a class="btn btn-primary btn-sm" href="/admin/course/edit/${course.code}">Editar</a>
                        <c:if test="${course.active}">
                            <button class="btn btn-warning btn-sm btn-inactivate" 
                                    onclick="inactivateCourse('${course.code}')">Inativar</button>
                        </c:if>
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
        fetch('/course/' + courseCode + '/inactive', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(response => {
            if (response.ok) {
                alert('Curso inativado com sucesso!');
                location.reload();
            } else {
                return response.text().then(text => {
                    alert('Erro ao inativar curso: ' + text);
                });
            }
        })
        .catch(error => {
            alert('Erro ao inativar curso: ' + error.message);
        });
    }
}
</script>

</html>
