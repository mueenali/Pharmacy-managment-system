package main.java.retail.order;

import javafx.stage.Stage;
import main.java.retail.order.entity.User;

import static main.java.retail.order.entity.User.*;

public class SessionManager {
    private Stage stage;
    private User loggedUser;

    private static SessionManager instance;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void userLogout() {
        loggedUser = null;
    }

    public boolean isAdmin() {
        return loggedUser != null && loggedUser.getRole().equals(TYPE_ADMIN) || (loggedUser != null && loggedUser.isAdmin());
    }

    public boolean isPharmacist() {
        return loggedUser != null && loggedUser.getRole().equals(TYPE_PHARMACIST);
    }

    public boolean isCustomer() {
        return loggedUser != null && loggedUser.getRole().equals(TYPE_CUSTOMER);
    }
}
