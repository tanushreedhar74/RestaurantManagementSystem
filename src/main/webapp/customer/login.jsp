<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Login</title>

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

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-5">

            <div class="card shadow">
                <div class="card-header text-center bg-primary text-white">
                    <h4>CUSTOMER LOGIN</h4>
                </div>

                <div class="card-body">

                    <!-- ERROR MESSAGE (NO JSTL) -->
                    <% 
                        String error = (String) request.getAttribute("error");
                        if (error != null) { 
                    %>
                        <div class="alert alert-danger"><%= error %></div>
                    <% 
                        } 
                    %>

                    <form action="<%=request.getContextPath()%>/customer/auth" method="post">
                        <input type="hidden" name="action" value="login">

                        <div class="mb-3">
                            <label>Email</label>
                            <input type="email" name="email" required class="form-control">
                        </div>

                        <div class="mb-3">
                            <label>Password</label>
                            <input type="password" name="password" required class="form-control">
                        </div>

                        <button class="btn btn-primary w-100">Login</button>
                    </form>

                    <p class="mt-3 text-center">
                        New user? 
                        <a href="<%=request.getContextPath()%>/customer/register.jsp">
                            Create an account
                        </a>
                    </p>

                </div>
            </div>

        </div>
    </div>
</div>


<%@ include file="../common/footer.jspf" %>

</body>
</html>
