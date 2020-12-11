package main.java.retail.order.entity;

import java.io.Serializable;

public class User implements Serializable {
    public static final String TYPE_ADMIN = "admin";
    public static final String TYPE_PHARMACIST = "pharmacist";
    public static final String TYPE_CUSTOMER = "customer";

    private long id;
    private String name;
    private String contactNumber;
    private String address;
    private String username;
    private String password;
    private String role;

    public User(String name, String contactNumber, String address, String username, String password) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.address = address;
        this.username = username;
        this.password = password;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public boolean isAdmin() {
        return username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("123");
    }
}
