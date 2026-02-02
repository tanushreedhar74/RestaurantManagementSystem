<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.rms.model.Admin" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>

    <!-- Bootstrap -->
    <link 
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" 
      rel="stylesheet">

    <script 
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js">
    </script>

    <style>
        body {
            background-color: #f7f7f7;
        }
        .card {
            border-radius: 12px;
        }
        .card:hover {
            transform: translateY(-4px);
            transition: 0.3s;
        }
    </style>
</head>

<body>

<%@ include file="../common/header-admin.jspf" %>

<%
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
        return;
    }
%>

<div class="container mt-5">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3>Welcome, <%= admin.getName() %> 👋</h3>

        <form action="<%=request.getContextPath()%>/logout" method="post">
            <button class="btn btn-danger btn-sm">Logout</button>
        </form>
    </div>


    <div class="row">

        <!-- Manage Dishes -->
        <div class="col-md-4 mb-4">
            <a href="<%=request.getContextPath()%>/admin/dishes" style="text-decoration:none; color:#000;">
                <div class="card shadow text-center p-4">
                    <h4>🍛 Manage Dishes</h4>
                    <p>Add, update, or delete dishes.</p>
                </div>
            </a>
        </div>

        <!-- Orders -->
        <div class="col-md-4 mb-4">
            <a href="<%=request.getContextPath()%>/admin/orders" style="text-decoration:none; color:#000;">
                <div class="card shadow text-center p-4">
                    <h4>📦 Orders</h4>
                    <p>Approve orders & track status.</p>
                </div>
            </a>
        </div>

        <!-- Delivery Men -->
        <div class="col-md-4 mb-4">
            <a href="<%=request.getContextPath()%>/admin/manage-delivery"
   style="text-decoration:none; color:#000;">

                <div class="card shadow text-center p-4">
                    <h4>🚴 Delivery Men</h4>
                    <p>Manage delivery staff & availability.</p>
                </div>
            </a>
        </div>

    </div>

</div>

<%@ include file="../common/footer.jspf" %>

</body>
</html>
