<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.rms.model.Dish" %>
<!DOCTYPE html>
<html>
<head>
    <title>Menu</title>

    <!-- Bootstrap 5 CDN -->
    <link 
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" 
      rel="stylesheet">

    <script 
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js">
    </script>
</head>

<body class="bg-light">

<%@ include file="../common/header-customer.jspf" %>

<div class="container mt-4">

    <h3 class="text-center mb-4">Available Dishes</h3>
    <div class="text-end mb-3">
    <a href="<%=request.getContextPath()%>/customer/cart"
       class="btn btn-success">
        View Cart 🛒
    </a>
</div>
    

    <!-- FETCH DISHES FROM REQUEST -->
    <%
        List<Dish> dishes = (List<Dish>) request.getAttribute("dishes");
        if (dishes == null || dishes.isEmpty()) {
    %>
        <div class="alert alert-info text-center">No dishes available right now.</div>
    <%
        } else {
    %>

    <div class="row">
        <%
            for (Dish d : dishes) {
        %>
        <div class="col-md-4 mb-4">
            <div class="card shadow-sm">

                <div class="card-body">
                    <h5 class="card-title"><%= d.getDishName() %></h5>
                    <p class="card-text">
                        Cuisine: <strong><%= d.getCuisine() %></strong><br>
                        Price: <strong>₹<%= d.getPrice() %></strong>
                    </p>

                    <!-- Add to Cart Form -->
                    <form action="<%=request.getContextPath()%>/customer/cart" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="dishId" value="<%= d.getDishId() %>">

                        <div class="input-group mb-2">
                            <input type="number" name="quantity" min="1" value="1"
                                   class="form-control" required>
                            <button class="btn btn-primary">Add to Cart</button>
                        </div>
                    </form>
                </div>

            </div>
        </div>
        <% } %>
    </div>

    <% } %>

</div>

<%@ include file="../common/footer.jspf" %>

</body>
</html>
