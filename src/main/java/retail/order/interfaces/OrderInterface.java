package main.java.retail.order.interfaces;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.retail.order.entity.Order;
import main.java.retail.order.entity.OrderItem;

public interface OrderInterface {

    ObservableList<Order> ORDERLIST = FXCollections.observableArrayList();

    ObservableList<OrderItem> ORDERITEMLIST = FXCollections.observableArrayList();
}
