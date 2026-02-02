<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, java.math.BigDecimal" %>
<%@ page import="com.rms.model.Order, com.rms.model.OrderItem, com.rms.model.Payment" %>

<!DOCTYPE html>
<html>
<head>
    <title>Your Orders</title>

    <!-- Bootstrap -->
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

    <h3 class="text-center mb-4">Your Orders</h3>

    <%
        List<Order> orderList = (List<Order>) request.getAttribute("orderList");
        List<Payment> paymentList = (List<Payment>) request.getAttribute("paymentList");
        List<List<OrderItem>> itemsList = (List<List<OrderItem>>) request.getAttribute("itemsList");

        if (orderList == null || orderList.isEmpty()) {
    %>

    <div class="alert alert-info text-center">You have no orders yet.</div>
    <div class="text-center">
        <a href="<%=request.getContextPath()%>/customer/menu" class="btn btn-primary">Order Now</a>
    </div>

    <%
        } else {

        for (int i = 0; i < orderList.size(); i++) {

            Order o = orderList.get(i);
            Payment p = paymentList.get(i);
            List<OrderItem> items = itemsList.get(i);
    %>

    <!-- ORDER CARD -->
    <div class="card shadow-sm mb-4">

        <div class="card-header bg-dark text-white">
            <h5 class="mb-0">
                Order ID: <%= o.getOrderId() %>
            </h5>
            <small>Placed on: <%= o.getOrderTime() %></small>
        </div>

        <div class="card-body">

            <!-- ITEMS TABLE -->
            <table class="table table-bordered">
                <thead class="table-secondary">
                <tr>
                    <th>Dish</th>
                    <th>Price (₹)</th>
                    <th>Qty</th>
                    <th>Subtotal (₹)</th>
                </tr>
                </thead>

                <tbody>
                <%
                    for (OrderItem it : items) {
                        double subtotal = it.getPrice().doubleValue() * it.getQuantity();
                %>
                <tr>
                    <td><%= it.getDishName() %></td>
                    <td><%= it.getPrice().doubleValue() %></td>
                    <td><%= it.getQuantity() %></td>
                    <td><%= subtotal %></td>
                </tr>
                <% } %>
                </tbody>
            </table>

            <!-- TOTAL -->
            <h5 class="text-end">Total Amount: ₹<%= o.getTotalAmount().doubleValue() %></h5>

            <p><strong>Order Status:</strong> <%= o.getOrderStatus() %></p>
            <p><strong>Payment Mode:</strong> <%= p.getPaymentMode() %></p>
            <p><strong>Payment Status:</strong> <%= p.getPaymentStatus() %></p>

            <div class="text-end">

                <!-- CANCEL BUTTON (only when order is PLACED or APPROVED) -->
                <%
                    if (o.getOrderStatus().equals("PLACED") ||
                        o.getOrderStatus().equals("APPROVED")) {
                %>
                <form action="<%=request.getContextPath()%>/customer/order" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="cancel">
                    <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
                    <button class="btn btn-danger btn-sm">Cancel Order</button>
                </form>
                <% } %>

                <!-- PAY NOW BUTTON (ONLINE + PENDING) -->
                <%
                    if (p.getPaymentMode().equals("ONLINE") &&
                        p.getPaymentStatus().equals("PENDING")) {
                %>
                <form action="<%=request.getContextPath()%>/customer/payment" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="onlineSuccess">
                    <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
                    <button class="btn btn-success btn-sm">Pay Now</button>
                </form>
                <% } %>

            </div>

        </div>
    </div>

    <% 
        } // end for
      } // end else
    %>

</div>

<%@ include file="../common/footer.jspf" %>

</body>
</html>
