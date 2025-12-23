<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.swiftcart.model.Product" %>

<!DOCTYPE html>
<html>
<head>
    <title>SwiftCart Products</title>
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
    <h1>SwiftCart Products</h1>

    <div class="product-grid">
        <%
            List<Product> products =
                    (List<Product>) request.getAttribute("products");

            if (products != null) {
                for (Product p : products) {

                    String imageName;
                    if (p.getId() <= 5) {
                        imageName = "electronics.png";
                    } else if (p.getId() <= 10) {
                        imageName = "accessories.png";
                    } else if (p.getId() <= 15) {
                        imageName = "storage.png";
                    } else if (p.getId() <= 20) {
                        imageName = "network.png";
                    } else {
                        imageName = "furniture.png";
                    }
        %>

        <div class="product-card">

            <!-- REQUIRED CONTAINER -->
            <div class="product-image-box">
                <img
                    src="images/<%= imageName %>"
                    alt="<%= p.getName() %>"
                    class="product-card-img">
            </div>

            <div class="product-details">
                <h2 class="product-name"><%= p.getName() %></h2>
                <p class="product-price">₹ <%= p.getPrice() %></p>

                <a href="product-details.jsp?id=<%= p.getId() %>"
                   class="product-btn">
                    View Details
                </a>
            </div>

        </div>

        <%
                }
            }
        %>
    </div>
</div>

</body>
</html>
