<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.rms.model.Dish" %>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Dishes</title>

    <link 
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" 
      rel="stylesheet">

    <script 
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js">
    </script>
</head>

<body class="bg-light">

<%@ include file="../common/header-admin.jspf" %>

<div class="container mt-4">

    <h3 class="mb-3">🍛 Manage Dishes</h3>

    <!-- SUCCESS / ERROR ALERTS -->
    <%
        String msg = (String) request.getAttribute("msg");
        String error = (String) request.getAttribute("error");

        if (msg != null) {
    %>
        <div class="alert alert-success"><%= msg %></div>
    <% } %>

    <% if (error != null) { %>
        <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <!-- ========================================================= -->
    <!-- ADD NEW DISH FORM -->
    <!-- ========================================================= -->

    <div class="card shadow-sm mb-4">
        <div class="card-header bg-dark text-white">
            <h5 class="mb-0">Add New Dish</h5>
        </div>

        <div class="card-body">
            <form action="<%=request.getContextPath()%>/admin/dishes" method="post">
                <input type="hidden" name="action" value="add">

                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label>Dish Name</label>
                        <input type="text" name="dishName" class="form-control" required>
                    </div>

                    <div class="col-md-3 mb-3">
                        <label>Cuisine</label>
                        <input type="text" name="cuisine" class="form-control" required>
                    </div>

                    <div class="col-md-3 mb-3">
                        <label>Price (₹)</label>
                        <input type="number" step="0.01" name="price" class="form-control" required>
                    </div>

                    <div class="col-md-2 mb-3">
                        <label>Status</label>
                        <!-- Dish Name -->


		<!-- Status -->
			<select name="availability" class="form-control">
    			<option value="true">Available</option>
    				<option value="false">Unavailable</option>
    
			</select>
                        
                    </div>
                </div>

                <button class="btn btn-success">Add Dish</button>
            </form>
        </div>
    </div>

    <!-- ========================================================= -->
    <!-- DISH LIST TABLE -->
    <!-- ========================================================= -->

    <h4 class="mb-3">Available Dishes</h4>

    <%
        List<Dish> dishes = (List<Dish>) request.getAttribute("dishList");

        if (dishes == null || dishes.isEmpty()) {
    %>

    <div class="alert alert-info">No dishes found.</div>

    <% } else { %>

    <table class="table table-bordered table-striped shadow-sm">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Cuisine</th>
            <th>Price</th>
            <th>Status</th>
            <th width="160">Action</th>
        </tr>
        </thead>

        <tbody>
        <%
            for (Dish d : dishes) {
        %>
        <tr>
            <td><%= d.getDishId() %></td>
            <td><%= d.getDishName() %></td>
            <td><%= d.getCuisine() %></td>
            <td>₹<%= d.getPrice().doubleValue() %></td>
            <td><%= d.isAvailability() %></td>

            <td>
                <!-- EDIT BUTTON -->
                <form action="<%=request.getContextPath()%>/admin/dishes" method="post" style="display:inline-block;">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="dishId" value="<%= d.getDishId() %>">

                    <button class="btn btn-warning btn-sm">Edit</button>
                </form>

                <!-- DELETE BUTTON -->
                <form action="<%=request.getContextPath()%>/admin/dishes" method="post" style="display:inline-block;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="dishId" value="<%= d.getDishId() %>">

                    <button class="btn btn-danger btn-sm">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>

    <% } %>

</div>

<%@ include file="../common/footer.jspf" %>

</body>
</html>
