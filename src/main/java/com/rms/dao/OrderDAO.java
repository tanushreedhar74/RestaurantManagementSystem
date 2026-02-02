package com.rms.dao;

import com.rms.model.Order;
import com.rms.model.OrderItem;
import com.rms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // 🔹 Create order (returns generated order_id)
    public int createOrder(Order order) {
        String sql = "INSERT INTO orders (customer_id, order_status, total_amount, payment_status) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getCustomerId());
            ps.setString(2, order.getOrderStatus());
            ps.setBigDecimal(3, order.getTotalAmount());
            ps.setString(4, order.getPaymentStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // order_id
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public List<OrderItem> getItemsByOrderId(int orderId) {

        List<OrderItem> list = new ArrayList<>();

        String sql = """
            SELECT oi.order_item_id,
                   oi.order_id,
                   oi.dish_id,
                   oi.quantity,
                   oi.price,
                   d.dish_name
            FROM order_items oi
            JOIN dishes d ON oi.dish_id = d.dish_id
            WHERE oi.order_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setDishId(rs.getInt("dish_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setDishName(rs.getString("dish_name"));

                list.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    //get order by order_id
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setCustomerId(rs.getInt("customer_id"));
                o.setOrderStatus(rs.getString("order_status"));
                o.setTotalAmount(rs.getBigDecimal("total_amount"));
                o.setPaymentStatus(rs.getString("payment_status"));
                o.setOrderTime(rs.getTimestamp("order_time"));
                return o;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Get orders by customer
    public List<Order> getOrdersByCustomer(int customerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ? ORDER BY order_time DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setCustomerId(rs.getInt("customer_id"));
                o.setOrderStatus(rs.getString("order_status"));
                o.setTotalAmount(rs.getBigDecimal("total_amount"));
                o.setPaymentStatus(rs.getString("payment_status"));
                o.setOrderTime(rs.getTimestamp("order_time"));
                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Get all orders (Admin)
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_time DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setCustomerId(rs.getInt("customer_id"));
                o.setOrderStatus(rs.getString("order_status"));
                o.setTotalAmount(rs.getBigDecimal("total_amount"));
                o.setPaymentStatus(rs.getString("payment_status"));
                o.setOrderTime(rs.getTimestamp("order_time"));
                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Update order status (Admin / Delivery Man)
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Update payment status (COD after delivery / online)
    public boolean updatePaymentStatus(int orderId, String paymentStatus) {
        String sql = "UPDATE orders SET payment_status = ? WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cancel order (Customer – only before PREPARING)
    public boolean cancelOrder(int orderId) {
        String sql = """
            UPDATE orders
            SET order_status = 'CANCELLED'
            WHERE order_id = ?
              AND order_status IN ('PLACED', 'APPROVED')
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	
}