package main.java.retail.order.entity;

import java.io.Serializable;

public class OrderItem implements Serializable {

    private long id;
    private Medicine medicine;
    private int quantity;
    private double totalPrice;

    public OrderItem(Medicine medicine, int quantity) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.totalPrice = medicine.getPrice() * quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
