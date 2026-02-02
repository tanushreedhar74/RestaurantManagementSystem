<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Delivery Man Login</title>

    <!-- Bootstrap -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
      rel="stylesheet">

    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js">
    </script>

    <style>
        body {
            background: #f2f2f2;
        }
    </style>
</head>

<body>

<%@ include file="../common/header-delivery.jspf" %>

<div class="container mt-5">
    <div class="row justify-content-center">

        <div class="col-md-4">

            <div class="card shadow-lg">
                <div class="card-header bg-success text-white text-center">
                    <h4>Delivery Login</h4>
                </div>

                <div class="card-body">

                    <!-- ERROR MESSAGE -->
                    <%
                        String error = (String) request.getAttribute("error");
                        if (error != null) {
                    %>
                        <div class="alert alert-danger"><%= error %></div>
                    <%
                        }
                    %>

                    <form action="<%=request.getContextPath()%>/delivery/login" method="post">

                        <div class="mb-3">
                           <label>Email</label>
		<input type="email" name="email" class="form-control" required>

                        </div>

                        <div class="mb-3">
                            <label>Password</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>

                        <button class="btn btn-success w-100">Login</button>

                    </form>

                </div>
            </div>

        </div>

    </div>
</div>

<%@ include file="../common/footer.jspf" %>

</body>
</html>
