package main.java.retail.order.entity;

import java.io.Serializable;

public class Pharmacist extends User implements Serializable {
    public Pharmacist(String name, String contactNumber, String address, String userName, String password) {
        super(name, contactNumber, address, userName, password);
        setRole(TYPE_PHARMACIST);
    }
}
