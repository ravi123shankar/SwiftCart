<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>SwiftCart - Home</title>
    <link rel="stylesheet" href="css/style.css">
</head>

<body class="home-page">

<!-- NAVBAR -->
<div class="navbar">
    <div class="nav-brand">
        <img src="<%= request.getContextPath() %>/images/swiftcart-logo.png"
             alt="SwiftCart Logo"
             class="navbar-logo">
        <span>SwiftCart</span>
    </div>
    <div class="nav-links">
        <a href="home.jsp">Home</a>
        <a href="hello">Products</a>
        <a href="about.jsp">About</a>
    </div>
</div>

<!-- HERO SECTION -->
<section class="hero">
    <h1>SwiftCart – Shop Smart, Learn Faster</h1>

    <p class="hero-tagline">
        An academic e-commerce application built using Java, JSP, Servlets,
        JDBC, and MySQL to demonstrate clean MVC architecture.
    </p>

    <a href="hello" class="hero-btn">Browse Products</a>
</section>

<!-- WHY SWIFTCART -->
<section class="section light-bg">
    <h2 class="section-title">Why SwiftCart?</h2>

    <div class="why-grid">
        <div class="why-card">MVC Architecture</div>
        <div class="why-card">JSP & Servlets</div>
        <div class="why-card">JDBC + MySQL</div>
        <div class="why-card">Clean Academic Design</div>
    </div>
</section>

<!-- FOOTER -->
<footer class="footer">
    <p><strong>SwiftCart</strong> – Academic E-Commerce Project</p>
    <p>Java • JSP • Servlets • JDBC • MySQL</p>
</footer>

</body>
</html>
