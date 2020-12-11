package main.java.retail.order.entity;

import java.io.Serializable;

public class Medicine implements Serializable {

    private long id;
    private String name;
    private double price;
    private String company;
    private String batch;
    private String dom;
    private String doe;
    private int quantity;

    public Medicine(String name, String company, double price, String batch, String dom, String doe, int quantity) {
        this.name = name;
        this.company = company;
        this.price = price;
        this.batch = batch;
        this.dom = dom;
        this.doe = doe;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", company='" + company + '\'' +
                ", batch='" + batch + '\'' +
                ", dom='" + dom + '\'' +
                ", doe='" + doe + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
