package main.java.retail.order.entity;

import java.io.Serializable;

public class Customer extends User implements Serializable {

    public Customer(String name, String contactNumber, String address, String userName, String password) {
        super(name, contactNumber, address, userName, password);
        setRole(TYPE_CUSTOMER);
    }
}
