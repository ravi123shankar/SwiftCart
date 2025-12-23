<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Product Details</title>
    <link rel="stylesheet" href="css/style.css">
</head>

<body>

<!-- NAVBAR -->
<div class="navbar">
    <div class="nav-brand">SwiftCart</div>
    <div class="nav-links">
        <a href="home.jsp">Home</a>
        <a href="hello">Products</a>
        <a href="about.jsp">About</a>
    </div>
</div>

<div class="container">

<%
    String idParam = request.getParameter("id");
    int id = 0;

    try {
        id = Integer.parseInt(idParam);
    } catch (Exception e) {
        id = 0;
    }

    String image;
    if (id > 0 && id <= 5) {
        image = "electronics.png";
    } else if (id <= 10) {
        image = "accessories.png";
    } else if (id <= 15) {
        image = "storage.png";
    } else if (id <= 20) {
        image = "network.png";
    } else if (id > 20) {
        image = "furniture.png";
    } else {
        image = "default.png";
    }
%>

    <div class="product-detail-layout">

        <!-- REQUIRED CONTAINER -->
        <div class="detail-image-box">
            <img
                src="images/<%= image %>"
                alt="Product Image"
                class="product-detail-img">
        </div>

        <div class="detail-info-box">
            <h2 class="detail-title">
                Product ID: <%= id > 0 ? id : "Unknown" %>
            </h2>

            <p class="detail-price">₹ Price shown on products page</p>

            <p class="product-description">
                This page demonstrates a clean product details layout for the
                SwiftCart academic e-commerce project.
            </p>

            <a href="hello" class="product-btn">← Back to Products</a>
        </div>

    </div>

</div>

</body>
</html>
