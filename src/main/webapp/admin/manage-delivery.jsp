<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.rms.model.DeliveryMan" %>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Delivery Men</title>

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

    <h3 class="mb-4">🚴 Manage Delivery Men</h3>

    <!-- Success / Error message -->
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

    <!-- ADD NEW DELIVERY MAN -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-dark text-white">
            <h5>Add Delivery Man</h5>
        </div>

        <div class="card-body">
            <form action="<%=request.getContextPath()%>/admin/manage-delivery" method="post">
                <input type="hidden" name="action" value="add">
		<div class="col-md-4 mb-3">
   		 <label>Name</label>
    		<input type="text" name="name" required class="form-control">
	</div>

		<div class="col-md-4 mb-3">
    		<label>Email</label>
    			<input type="email" name="email" required class="form-control">
	</div>

		<div class="col-md-4 mb-3">
    		<label>Phone</label>
    			<input type="text" name="phone" required class="form-control">
	</div>
	

	<div class="col-md-4 mb-3">
    	<label>Password</label>
    		<input type="password" name="password" required class="form-control">
	</div>


                <button class="btn btn-success">Add Delivery Man</button>
            </form>
        </div>
    </div>

    <!-- DELIVERY MAN LIST -->
    <h4 class="mb-3">Delivery Man List</h4>

    <%
    List<DeliveryMan> list = (List<DeliveryMan>) request.getAttribute("deliveryMen");


        if (list == null || list.isEmpty()) {
    %>

    <div class="alert alert-info">No delivery men found.</div>

    <% } else { %>

    <table class="table table-bordered table-striped shadow-sm">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Phone</th>
            <th>Availability</th>
            <th width="180">Action</th>
        </tr>
        </thead>

        <tbody>
        <%
            for (DeliveryMan d : list) {
        %>
        <tr>
            <td><%= d.getDeliveryManId() %></td>
            <td><%= d.getName() %></td>
            <td><%= d.getPhone() %></td>
            <td>
                <%= d.isAvailable() ? "<span class='badge bg-success'>Available</span>" 
                                    : "<span class='badge bg-secondary'>Unavailable</span>" %>
            </td>

            <td>

                <!-- Toggle Availability -->
                <form action="<%=request.getContextPath()%>/admin/manage-delivery"
                      method="post" style="display:inline-block;">
                    <input type="hidden" name="action" value="toggle">
                    <input type="hidden" name="deliveryManId" value="<%= d.getDeliveryManId() %>">
                    <button class="btn btn-warning btn-sm">
                        <%= d.isAvailable() ? "Deactivate" : "Activate" %>
                    </button>
                </form>

                <!-- Delete -->
                <form action="<%=request.getContextPath()%>/admin/manage-delivery"
                      method="post" style="display:inline-block;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="deliveryManId" value="<%= d.getDeliveryManId() %>">
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
