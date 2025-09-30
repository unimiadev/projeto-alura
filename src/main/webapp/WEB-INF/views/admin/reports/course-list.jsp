<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>${reportTitle}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
    <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
    <style>
        .completion-bar {
            height: 20px;
            background-color: #e9ecef;
            border-radius: 10px;
            overflow: hidden;
        }
        .completion-fill {
            height: 100%;
            background: linear-gradient(90deg, #28a745, #20c997);
            transition: width 0.3s ease;
        }
        .metric-badge {
            display: inline-block;
            padding: 0.25rem 0.5rem;
            font-size: 0.875rem;
            font-weight: 500;
            border-radius: 0.375rem;
        }
        .badge-enrollments {
            background-color: #007bff;
            color: white;
        }
        .badge-active {
            background-color: #ffc107;
            color: #212529;
        }
        .badge-completed {
            background-color: #28a745;
            color: white;
        }
        .course-row:hover {
            background-color: #f8f9fa;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-12">
            <h1>${reportTitle}</h1>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="/admin/courses">Admin</a></li>
                    <li class="breadcrumb-item"><a href="/admin/reports">Relatórios</a></li>
                    <li class="breadcrumb-item active">${reportTitle}</li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row mb-3">
        <div class="col-md-6">
            <a href="/admin/reports" class="btn btn-secondary">
                ← Voltar ao Dashboard
            </a>
        </div>
        <div class="col-md-6 text-right">
            <div class="btn-group" role="group">
                <a href="/admin/reports/most-accessed" class="btn btn-outline-primary btn-sm">
                    Mais Acessados
                </a>
                <a href="/admin/reports/completion" class="btn btn-outline-success btn-sm">
                    Taxa de Conclusão
                </a>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-body">
            <c:choose>
                <c:when test="${empty courses}">
                    <div class="text-center py-5">
                        <h4 class="text-muted">Nenhum curso encontrado</h4>
                        <p class="text-muted">Não há dados para exibir neste relatório.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead class="thead-light">
                                <tr>
                                    <th>#</th>
                                    <th>Curso</th>
                                    <th>Categoria</th>
                                    <th>Instrutor</th>
                                    <th>Matrículas</th>
                                    <th>Taxa de Conclusão</th>
                                    <th>Progresso</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${courses}" var="course" varStatus="status">
                                    <tr class="course-row">
                                        <td>
                                            <strong>${status.index + 1}</strong>
                                        </td>
                                        <td>
                                            <div>
                                                <strong>${course.courseName}</strong>
                                                <br>
                                                <small class="text-muted">
                                                    <code>${course.courseCode}</code>
                                                </small>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="badge badge-info">${course.categoryName}</span>
                                        </td>
                                        <td>
                                            <small>${course.instructorEmail}</small>
                                        </td>
                                        <td>
                                            <div class="d-flex flex-wrap gap-1">
                                                <span class="metric-badge badge-enrollments">
                                                    📊 ${course.totalEnrollments} total
                                                </span>
                                                <span class="metric-badge badge-active">
                                                    ⏳ ${course.activeEnrollments} ativas
                                                </span>
                                                <span class="metric-badge badge-completed">
                                                    ✅ ${course.completedEnrollments} concluídas
                                                </span>
                                            </div>
                                        </td>
                                        <td>
                                            <strong>
                                                <fmt:formatNumber value="${course.completionRate}" maxFractionDigits="1" />%
                                            </strong>
                                        </td>
                                        <td style="width: 150px;">
                                            <div class="completion-bar">
                                                <div class="completion-fill" 
                                                     style="width: ${course.completionRate}%"
                                                     title="Taxa de conclusão: ${course.completionRate}%">
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- Summary Statistics -->
                    <div class="row mt-4 pt-3 border-top">
                        <div class="col-md-3 text-center">
                            <div class="h4 text-primary">${courses.size()}</div>
                            <small class="text-muted">Cursos</small>
                        </div>
                        <div class="col-md-3 text-center">
                            <div class="h4 text-info">
                                <c:set var="totalEnrollments" value="0" />
                                <c:forEach items="${courses}" var="course">
                                    <c:set var="totalEnrollments" value="${totalEnrollments + course.totalEnrollments}" />
                                </c:forEach>
                                ${totalEnrollments}
                            </div>
                            <small class="text-muted">Total Matrículas</small>
                        </div>
                        <div class="col-md-3 text-center">
                            <div class="h4 text-warning">
                                <c:set var="totalActive" value="0" />
                                <c:forEach items="${courses}" var="course">
                                    <c:set var="totalActive" value="${totalActive + course.activeEnrollments}" />
                                </c:forEach>
                                ${totalActive}
                            </div>
                            <small class="text-muted">Ativas</small>
                        </div>
                        <div class="col-md-3 text-center">
                            <div class="h4 text-success">
                                <c:set var="totalCompleted" value="0" />
                                <c:forEach items="${courses}" var="course">
                                    <c:set var="totalCompleted" value="${totalCompleted + course.completedEnrollments}" />
                                </c:forEach>
                                <c:choose>
                                    <c:when test="${totalEnrollments > 0}">
                                        <fmt:formatNumber value="${totalCompleted * 100 / totalEnrollments}" maxFractionDigits="1" />%
                                    </c:when>
                                    <c:otherwise>0%</c:otherwise>
                                </c:choose>
                            </div>
                            <small class="text-muted">Taxa Conclusão</small>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script src="/assets/external-libs/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>
