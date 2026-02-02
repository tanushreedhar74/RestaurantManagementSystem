<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.rms.model.Cart" %>
<!DOCTYPE html>
<html>
<head>
    <title>Your Cart</title>

    <!-- Bootstrap CDN -->
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

    <h3 class="mb-4 text-center">Your Cart</h3>

    <%
        List<Cart> cartList = (List<Cart>) request.getAttribute("cartList");
        Double totalAmount = (Double) request.getAttribute("totalAmount");
        
        if (cartList == null || cartList.isEmpty()) {
    %>

    <div class="alert alert-info text-center">
        Your cart is empty.
    </div>

    <div class="text-center">
        <a href="<%=request.getContextPath()%>/customer/menu" class="btn btn-primary">
            Go to Menu
        </a>
    </div>

    <%
        } else {
    %>

    <table class="table table-bordered shadow">
        <thead class="table-dark">
            <tr>
                <th>Dish</th>
                <th>Price (₹)</th>
                <th>Quantity</th>
                <th>Subtotal (₹)</th>
                <th>Action</th>
            </tr>
        </thead>

        <tbody>
            <%
                for (Cart c : cartList) {
            %>
            <tr>
                <td><%= c.getDishName() %></td>
                <td><%= c.getPrice() %></td>
                <td><%= c.getQuantity() %></td>
                <td><%= c.getPrice().doubleValue() * c.getQuantity() %></td>

                <td>
                    <form action="<%=request.getContextPath()%>/customer/cart" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="remove">
                        <input type="hidden" name="cartId" value="<%= c.getCartId() %>">

                        <button class="btn btn-danger btn-sm">Remove</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>

    <div class="text-end">
        <h4>Total: ₹<%= totalAmount %></h4>
    </div>

<div class="text-end mt-3">
    <form action="<%=request.getContextPath()%>/customer/order" method="post">

        <input type="hidden" name="action" value="place">

        <!-- PAYMENT MODE -->
        <div class="mb-3 text-start">
            <label class="form-label fw-bold">Payment Method</label><br>

            <input type="radio" name="paymentMode" value="COD" checked>
            Cash on Delivery<br>

            <input type="radio" name="paymentMode" value="ONLINE">
            Online Payment
        </div>

        <button class="btn btn-success btn-lg">
            Place Order
        </button>
    </form>
</div>


    <% } %>

</div>

<%@ include file="../common/footer.jspf" %>

</body>
</html>
