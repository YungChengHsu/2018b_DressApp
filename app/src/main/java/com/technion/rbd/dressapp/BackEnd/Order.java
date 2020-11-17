package com.technion.rbd.dressapp.BackEnd;

/*orders:
ordererID
ownerID
itemID
orderRatingID
orderSataus*/

public class Order {
    private String OrderItemId;
    private String OrderOwnerId;
    private String OrderBuyerId;
    private String OrderStatus;
    private double OrderRating;

    public Order(String orderItemId, String orderOwnerId, String orderBuyerId) {
        OrderItemId = orderItemId;
        OrderOwnerId = orderOwnerId;
        OrderBuyerId = orderBuyerId;
        OrderStatus = "Ordered";
        OrderRating = 0;
    }

    public String getOrderItemId() {
        return OrderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        OrderItemId = orderItemId;
    }

    public String getOrderOwnerId() {
        return OrderOwnerId;
    }

    public void setOrderOwnerId(String orderOwnerId) {
        OrderOwnerId = orderOwnerId;
    }

    public String getOrderBuyerId() {
        return OrderBuyerId;
    }

    public void setOrderBuyerId(String orderBuyerId) {
        OrderBuyerId = orderBuyerId;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public double getOrderRating() {
        return OrderRating;
    }

    public void setOrderRating(double orderRating) {
        OrderRating = orderRating;
    }
}
