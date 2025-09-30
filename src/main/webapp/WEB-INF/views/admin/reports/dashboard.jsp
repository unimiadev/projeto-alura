<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard de Relatórios</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
    <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .report-card {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 1.5rem;
            margin-bottom: 1rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .metric-value {
            font-size: 2rem;
            font-weight: bold;
            color: #007bff;
        }
        .chart-container {
            position: relative;
            height: 400px;
            margin: 2rem 0;
        }
        .top-courses-list {
            max-height: 400px;
            overflow-y: auto;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-12">
            <h1>Dashboard de Relatórios</h1>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="/admin/courses">Admin</a></li>
                    <li class="breadcrumb-item active">Relatórios</li>
                </ol>
            </nav>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="row mb-4">
        <div class="col-md-3">
            <a href="/admin/reports/most-accessed" class="btn btn-primary btn-block">
                📊 Cursos Mais Acessados
            </a>
        </div>
        <div class="col-md-3">
            <a href="/admin/reports/completion" class="btn btn-success btn-block">
                ✅ Taxa de Conclusão
            </a>
        </div>
        <div class="col-md-3">
            <a href="/admin/categories" class="btn btn-info btn-block">
                📂 Por Categoria
            </a>
        </div>
        <div class="col-md-3">
            <a href="/api/reports/summary" class="btn btn-secondary btn-block" target="_blank">
                📈 API Summary
            </a>
        </div>
    </div>

    <!-- Top 5 Most Accessed Courses -->
    <div class="row">
        <div class="col-md-6">
            <div class="report-card">
                <h3>🏆 Top 5 Cursos Mais Acessados</h3>
                <div class="top-courses-list">
                    <c:forEach items="${topCourses}" var="course" varStatus="status" end="4">
                        <div class="d-flex justify-content-between align-items-center py-2 border-bottom">
                            <div>
                                <strong>${status.index + 1}. ${course.courseName}</strong>
                                <br>
                                <small class="text-muted">${course.categoryName} | ${course.instructorEmail}</small>
                            </div>
                            <div class="text-right">
                                <span class="metric-value">${course.totalEnrollments}</span>
                                <br>
                                <small class="text-muted">matrículas</small>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="mt-3">
                    <a href="/admin/reports/most-accessed" class="btn btn-outline-primary btn-sm">
                        Ver todos os cursos
                    </a>
                </div>
            </div>
        </div>

        <!-- Completion Rate Chart -->
        <div class="col-md-6">
            <div class="report-card">
                <h3>📈 Taxa de Conclusão por Curso</h3>
                <div class="chart-container">
                    <canvas id="completionChart"></canvas>
                </div>
            </div>
        </div>
    </div>

    <!-- Summary Statistics -->
    <div class="row">
        <div class="col-md-12">
            <div class="report-card">
                <h3>📊 Estatísticas Gerais</h3>
                <div class="row text-center">
                    <div class="col-md-3">
                        <div class="metric-value">${topCourses.size()}</div>
                        <div>Cursos Ativos</div>
                    </div>
                    <div class="col-md-3">
                        <div class="metric-value">
                            <c:set var="totalEnrollments" value="0" />
                            <c:forEach items="${topCourses}" var="course">
                                <c:set var="totalEnrollments" value="${totalEnrollments + course.totalEnrollments}" />
                            </c:forEach>
                            ${totalEnrollments}
                        </div>
                        <div>Total de Matrículas</div>
                    </div>
                    <div class="col-md-3">
                        <div class="metric-value">
                            <c:set var="totalActive" value="0" />
                            <c:forEach items="${topCourses}" var="course">
                                <c:set var="totalActive" value="${totalActive + course.activeEnrollments}" />
                            </c:forEach>
                            ${totalActive}
                        </div>
                        <div>Matrículas Ativas</div>
                    </div>
                    <div class="col-md-3">
                        <div class="metric-value">
                            <c:set var="totalCompleted" value="0" />
                            <c:forEach items="${topCourses}" var="course">
                                <c:set var="totalCompleted" value="${totalCompleted + course.completedEnrollments}" />
                            </c:forEach>
                            <fmt:formatNumber value="${totalCompleted * 100 / totalEnrollments}" maxFractionDigits="1" />%
                        </div>
                        <div>Taxa de Conclusão Geral</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Completion Rate Chart
const ctx = document.getElementById('completionChart').getContext('2d');
const completionChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: [
            <c:forEach items="${completionReport}" var="course" varStatus="status" end="9">
                '${course.courseName}'<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ],
        datasets: [{
            label: 'Taxa de Conclusão (%)',
            data: [
                <c:forEach items="${completionReport}" var="course" varStatus="status" end="9">
                    ${course.completionRate}<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ],
            backgroundColor: 'rgba(54, 162, 235, 0.6)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            y: {
                beginAtZero: true,
                max: 100,
                ticks: {
                    callback: function(value) {
                        return value + '%';
                    }
                }
            }
        },
        plugins: {
            legend: {
                display: false
            }
        }
    }
});
</script>
</body>
</html>
