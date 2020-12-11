package main.java.retail.order.interfaces;

import javafx.collections.ObservableList;
import main.java.retail.order.entity.Customer;
import main.java.retail.order.entity.User;

public interface IUser {

    ObservableList<User> getUsers();

    User getUser(long id);

    User getUser(String userName);

    String getUserType(String username);

    boolean saveUser(User user);

    boolean updateUser(User user);

    void deleteUser(User user);

    boolean checkPassword(String username, String password);

    boolean checkUser(String username);
}
