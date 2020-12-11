package main.java.retail.order.entity;

import java.io.Serializable;

public class Admin extends User implements Serializable {
    public Admin(String name, String contactNumber, String address, String userName, String password) {
        super(name, contactNumber, address, userName, password);
        setRole(TYPE_ADMIN);
    }
}
