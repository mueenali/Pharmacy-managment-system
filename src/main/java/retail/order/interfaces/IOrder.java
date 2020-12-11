package main.java.retail.order.interfaces;

import javafx.collections.ObservableList;
import main.java.retail.order.entity.Medicine;
import main.java.retail.order.entity.Order;
import main.java.retail.order.entity.OrderItem;
import main.java.retail.order.entity.User;

public interface IOrder {

    ObservableList<Order> getOrders();

    ObservableList<Order> getMyOrders(User user);

    Order getOrder(long id);

    ObservableList<OrderItem> getOrderItems(long id);

    boolean saveOrder(Order newOrder);

    boolean updateOrder(Order order);

    void deleteOrder(Order order);

    void decreaseMedicine(Medicine medicine);

    Object getCustomerName(Order order);
}
