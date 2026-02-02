<%@ page language="java" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, java.math.BigDecimal" %>
<%@ page import="com.rms.model.Order, com.rms.model.OrderItem, com.rms.model.Payment, com.rms.model.DeliveryMan" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin - Orders</title>

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

    <h3 class="mb-4">📦 Manage Orders</h3>

    <!-- Alerts -->
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

    <%
        List<Order> orders = (List<Order>) request.getAttribute("orderList");
        List<List<OrderItem>> itemsList = (List<List<OrderItem>>) request.getAttribute("itemsList");
        List<Payment> paymentList = (List<Payment>) request.getAttribute("paymentList");
        List<DeliveryMan> deliveryMen = (List<DeliveryMan>) request.getAttribute("deliveryMen");
        List<Integer> assignedList = (List<Integer>) request.getAttribute("assignedList"); 
        // assignedList contains orderId values that are already assigned
    %>

    <% if (orders == null || orders.isEmpty()) { %>
        <div class="alert alert-info">No orders available.</div>
    <% } else { %>

    <%
        for (int i = 0; i < orders.size(); i++) {

            Order order = orders.get(i);
            List<OrderItem> items = itemsList.get(i);
            Payment payment = paymentList.get(i);
            boolean isAssigned = assignedList.contains(order.getOrderId());
    %>

    <div class="card shadow-sm mb-4">

        <div class="card-header bg-dark text-white">
            <h5 class="mb-0">
                Order ID: <%= order.getOrderId() %>
                &nbsp;&nbsp; | &nbsp;&nbsp; Customer ID: <%= order.getCustomerId() %>
            </h5>
            <small>Placed: <%= order.getOrderTime() %></small>
        </div>

        <div class="card-body">

            <!-- ITEMS TABLE -->
            <table class="table table-bordered mb-3">
                <thead class="table-secondary">
                <tr>
                    <th>Dish</th>
                    <th>Price</th>
                    <th>Qty</th>
                    <th>Subtotal</th>
                </tr>
                </thead>

                <tbody>
                <%
                    for (OrderItem it : items) {
                        double subtotal = it.getPrice().doubleValue() * it.getQuantity();
                %>
                <tr>
                    <td><%= it.getDishName() %></td>
                    <td>₹<%= it.getPrice().doubleValue() %></td>
                    <td><%= it.getQuantity() %></td>
                    <td>₹<%= subtotal %></td>
                </tr>
                <% } %>
                </tbody>
            </table>

            <!-- Total -->
            <h5 class="text-end">Total: ₹<%= order.getTotalAmount().doubleValue() %></h5>

            <!-- Status Info -->
            <p><strong>Order Status:</strong> <%= order.getOrderStatus() %></p>
            <p><strong>Payment Mode:</strong> <%= payment.getPaymentMode() %></p>
            <p><strong>Payment Status:</strong> <%= payment.getPaymentStatus() %></p>

            <hr>

            <!-- ADMIN ACTIONS -->
            <div class="text-end">

                <!-- APPROVE BUTTON (only if PLACED) -->
                <% if (order.getOrderStatus().equals("PLACED")) { %>
                    <form action="<%=request.getContextPath()%>/admin/orders" 
                          method="post" style="display:inline;">
                        <input type="hidden" name="action" value="approve">
                        <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
                        <button class="btn btn-success btn-sm">Approve</button>
                    </form>
                <% } %>

                <!-- ASSIGN DELIVERY -->
                <% if (order.getOrderStatus().equals("APPROVED")) { %>

                    <% if (!isAssigned) { %>

                    <form action="<%=request.getContextPath()%>/admin/orders" 
                          method="post" class="d-inline-block">
						
   					 <input type="hidden" name="action" value="assign">
                        <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">

                        <select name="deliveryManId" class="form-select form-select-sm d-inline-block" 
                                style="width:180px; display:inline-block;" required>
                            <option value="">Assign Delivery Man</option>

                            <% for (DeliveryMan d : deliveryMen) { %>
                                <% if (d.isAvailable()) { %>
                                    <option value="<%= d.getDeliveryManId() %>">
                                        <%= d.getName() %>
                                    </option>
                                <% } %>
                            <% } %>
                        </select>

                        <button class="btn btn-primary btn-sm">Assign</button>
                    </form>

                    <% } else { %>
                        <span class="badge bg-info">Delivery Assigned</span>
                    <% } %>

                <% } %>

            </div>

        </div>

    </div>

    <% } } %>

</div>

<%@ include file="../common/footer.jspf" %>

</body>
</html>
