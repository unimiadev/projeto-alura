<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alura - Login</title>
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
    <link rel="stylesheet" type="text/css" href="/assets/css/login.css">
</head>
<body>
    <div class="background-circle-1"></div>
    
    <div class="container">
        <div class="login-section">
            <div class="login-box">
                <h2>Já estuda com a gente?</h2>
                <p>Faça seu login e boa aula!</p>
                <a href="/admin/courses" class="btn-login">ENTRAR</a>
            </div>
        </div>

        <div class="categories-section">
            <div class="categories-header">
                <h2>Ainda não estuda com a gente?</h2>
                <p>São mais de mil cursos nas seguintes áreas:</p>
            </div>

            <div class="categories-grid">
                <c:forEach items="${categories}" var="category">
                    <div class="category-card category-${fn:toLowerCase(category.code)}">
                        <c:choose>
                            <c:when test="${fn:toLowerCase(category.code) == 'programacao'}">
                                <img src="/assets/images/icon-color-programacao.png" alt="Programação" class="category-icon">
                            </c:when>
                            <c:when test="${fn:toLowerCase(category.code) == 'front-end'}">
                                <img src="/assets/images/icon-color-frontend.png" alt="Front-end" class="category-icon">
                            </c:when>
                            <c:when test="${fn:toLowerCase(category.code) == 'data-science'}">
                                <img src="/assets/images/icon-color-data-science.png" alt="Data Science" class="category-icon">
                            </c:when>
                            <c:when test="${fn:toLowerCase(category.code) == 'devops'}">
                                <img src="/assets/images/icon-color-devops.png" alt="DevOps" class="category-icon">
                            </c:when>
                            <c:when test="${fn:toLowerCase(category.code) == 'mobile'}">
                                <img src="/assets/images/icon-color-mobile.png" alt="Mobile" class="category-icon">
                            </c:when>
                            <c:when test="${fn:toLowerCase(category.code) == 'ux-design'}">
                                <img src="/assets/images/icon-color-ux-design.png" alt="UX & Design" class="category-icon">
                            </c:when>
                            <c:when test="${fn:toLowerCase(category.code) == 'inovacao-gestao'}">
                                <img src="/assets/images/icon-color-inovacao-gestao.png" alt="Inovação & Gestão" class="category-icon">
                            </c:when>
                            <c:when test="${fn:toLowerCase(category.code) == 'inteligencia-artificial'}">
                                <img src="/assets/images/icon-color-ai.png" alt="Inteligência Artificial" class="category-icon">
                            </c:when>
                            <c:otherwise>
                                <img src="/assets/images/icon-color-programacao.png" alt="${category.name}" class="category-icon">
                            </c:otherwise>
                        </c:choose>
                        
                        <div class="category-title">
                            <span class="category-school">Escola_</span><br>
                            <span class="category-name">${fn:toUpperCase(category.name)}</span>
                        </div>
                        
                        <div class="category-description">
                            <c:choose>
                                <c:when test="${fn:toLowerCase(category.code) == 'programacao'}">
                                    Lógica de programação, .NET, Automação e Produtividade
                                </c:when>
                                <c:when test="${fn:toLowerCase(category.code) == 'front-end'}">
                                    HTML, CSS, Svelte, VueJS
                                </c:when>
                                <c:when test="${fn:toLowerCase(category.code) == 'data-science'}">
                                    SQL e Banco de Dados, Engenharia de Dados, Análise de dados
                                </c:when>
                                <c:when test="${fn:toLowerCase(category.code) == 'devops'}">
                                    Linux, FinOps, Automação de processos
                                </c:when>
                                <c:when test="${fn:toLowerCase(category.code) == 'mobile'}">
                                    Flutter, Android, iOS
                                </c:when>
                                <c:when test="${fn:toLowerCase(category.code) == 'ux-design'}">
                                    UI Design, Design System, UX Writing
                                </c:when>
                                <c:when test="${fn:toLowerCase(category.code) == 'inovacao-gestao'}">
                                    Agilidade, Liderança, Ensino e Aprendizagem
                                </c:when>
                                <c:when test="${fn:toLowerCase(category.code) == 'inteligencia-artificial'}">
                                    IA para Criativos, IA para Programação, IA para Negócios
                                </c:when>
                                <c:otherwise>
                                    Cursos diversos da área
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</body>
</html>
