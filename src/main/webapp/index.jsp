<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Restaurant Management System</title>

    <!-- Bootstrap CDN -->
    <link 
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" 
      rel="stylesheet">

    <script 
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js">
    </script>

    <style>
        body {
            background: #f8f9fa;
        }
        .hero-box {
            padding: 60px 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            background: white;
        }
    </style>
</head>

<body>
<%@ include file="common/header-guest.jspf" %>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">

            <div class="hero-box text-center">

                <h2 class="mb-3">🍽️ Restaurant Management System</h2>
                <p class="lead">
                    Welcome! Please select a portal to continue.
                </p>

                <hr>

                <div class="row mt-4">

                    <!-- Customer -->
                    <div class="col-md-4 mb-3">
                        <div class="card shadow-sm">
                            <div class="card-body text-center">
                                <h5>Customer</h5>
                                <p>Order food & view your history.</p>
                                <a href="<%=request.getContextPath()%>/customer/login.jsp" 
                                   class="btn btn-primary btn-sm w-100">Customer Login</a>
                            </div>
                        </div>
                    </div>

                    <!-- Admin -->
                    <div class="col-md-4 mb-3">
                        <div class="card shadow-sm">
                            <div class="card-body text-center">
                                <h5>Admin</h5>
                                <p>Manage dishes, orders & delivery.</p>
                                <a href="<%=request.getContextPath()%>/admin/login.jsp" 
                                   class="btn btn-dark btn-sm w-100">Admin Login</a>
                            </div>
                        </div>
                    </div>

                    <!-- Delivery -->
                    <div class="col-md-4 mb-3">
                        <div class="card shadow-sm">
                            <div class="card-body text-center">
                                <h5>Delivery Man</h5>
                                <p>View assigned orders & update status.</p>
                                <a href="<%=request.getContextPath()%>/delivery/login.jsp" 
                                   class="btn btn-success btn-sm w-100">Delivery Login</a>
                            </div>
                        </div>
                    </div>

                </div>

            </div>

        </div>
    </div>
</div>

<%@ include file="../common/footer.jspf" %>

</body>
</html>
