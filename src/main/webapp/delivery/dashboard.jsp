<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, java.math.BigDecimal" %>
<%@ page import="com.rms.model.Order, com.rms.model.OrderItem, com.rms.model.Payment, com.rms.model.DeliveryMan" %>

<!DOCTYPE html>
<html>
<head>
    <title>Delivery Dashboard</title>

    <!-- Bootstrap -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
      rel="stylesheet">

    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js">
    </script>
</head>

<body class="bg-light">

<%@ include file="../common/header-delivery.jspf" %>

<%
    DeliveryMan dm = (DeliveryMan) session.getAttribute("deliveryMan");
    if (dm == null) {
        response.sendRedirect(request.getContextPath() + "/delivery/login.jsp");
        return;
    }
%>

<div class="container mt-4">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3>Welcome, <%= dm.getName() %> 🚴‍♂️</h3>

        <form action="<%=request.getContextPath()%>/logout" method="post">
            <button class="btn btn-danger btn-sm">Logout</button>
        </form>
    </div>

    <h4 class="mb-3">Assigned Orders</h4>

    <!-- success/error messages -->
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
        List<Order> orderList = (List<Order>) request.getAttribute("orderList");
        List<List<OrderItem>> itemsList = (List<List<OrderItem>>) request.getAttribute("itemsList");
        List<Payment> paymentList = (List<Payment>) request.getAttribute("paymentList");

        if (orderList == null || orderList.isEmpty()) {
    %>

        <div class="alert alert-info">No orders assigned to you currently.</div>

    <% } else { %>

    <%
        for (int i = 0; i < orderList.size(); i++) {
            Order o = orderList.get(i);
            List<OrderItem> items = itemsList.get(i);
            Payment p = paymentList.get(i);
    %>

    <div class="card shadow-sm mb-4">

        <div class="card-header bg-success text-white">
            <h5 class="mb-0">Order ID: <%= o.getOrderId() %></h5>
            <small>Assigned to: <%= dm.getName() %></small>
        </div>

        <div class="card-body">

            <p><strong>Customer ID:</strong> <%= o.getCustomerId() %></p>
            <p><strong>Order Status:</strong> <%= o.getOrderStatus() %></p>
            <p><strong>Payment Mode:</strong> <%= p.getPaymentMode() %></p>
            <p><strong>Payment Status:</strong> <%= p.getPaymentStatus() %></p>

            <!-- ITEMS TABLE -->
            <table class="table table-bordered">
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

            <h5 class="text-end">Total: ₹<%= o.getTotalAmount().doubleValue() %></h5>

            <hr>

            <!-- DELIVERY STATUS UPDATE -->
            <div class="text-end">

                <% if (o.getOrderStatus().equals("APPROVED")) { %>
                    <!-- ACCEPT ORDER -->
                    <form action="<%=request.getContextPath()%>/delivery/status"
                          method="post" style="display:inline-block;">
                   <input type="hidden" name="status" value="PICKED_UP">

                        <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
                        <button class="btn btn-primary btn-sm">Accept Order</button>
                    </form>
                <% } %>

                <% if (o.getOrderStatus().equals("PREPARING")) { %>
                    <!-- START DELIVERY -->
                    <form action="<%=request.getContextPath()%>/delivery/status"
                          method="post" style="display:inline-block;">
                        <input type="hidden" name="status" value="OUT_FOR_DELIVERY">

                        <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
                        <button class="btn btn-warning btn-sm">Out for Delivery</button>
                    </form>
                <% } %>

                <% if (o.getOrderStatus().equals("OUT_FOR_DELIVERY")) { %>
                    <!-- MARK DELIVERED -->
                    <form action="<%=request.getContextPath()%>/delivery/status"
                          method="post" style="display:inline-block;">
                      <input type="hidden" name="status" value="DELIVERED">

                        <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
                        <button class="btn btn-success btn-sm">Mark Delivered</button>
                    </form>
                <% } %>

                <% 
                   // COD Payment: Delivery boy collects cash
                   if (p.getPaymentMode().equals("COD") && 
                       o.getOrderStatus().equals("DELIVERED") && 
                       p.getPaymentStatus().equals("PENDING")) { 
                %>
                    <form action="<%=request.getContextPath()%>/delivery/status"
                          method="post" style="display:inline-block;">
                       <input type="hidden" name="status" value="DELIVERED">
						<input type="hidden" name="paymentMode" value="COD">

                        <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
                        <button class="btn btn-dark btn-sm">Collect Cash</button>
                    </form>
                <% } %>

            </div>

        </div>

    </div>

    <% } } %>

</div>

<%@ include file="../common/footer.jspf" %>

</body>
</html>
