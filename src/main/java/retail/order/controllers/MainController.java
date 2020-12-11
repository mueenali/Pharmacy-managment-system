package main.java.retail.order.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.retail.order.SessionManager;
import main.java.retail.order.models.UserModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends BaseController implements Initializable {

    public Button ordersButton, logoutButton, usersButton, medicinesButton;
    public VBox boxContainer;
    private UserModel userModel;
    SessionManager sessionManager = SessionManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userModel = new UserModel();
        if (sessionManager.isAdmin()) {
            usersAction(null);
            medicinesButton.setVisible(false);
            medicinesButton.setManaged(false);
            ordersButton.setVisible(false);
            ordersButton.setManaged(false);
        } else if (sessionManager.isPharmacist()) {
            medicinesAction(null);
            ordersButton.setVisible(false);
            ordersButton.setManaged(false);
        } else if (sessionManager.isCustomer()) {
            ordersAction(null);
            usersButton.setVisible(false);
            usersButton.setManaged(false);
        }
    }


    public void ordersAction(ActionEvent actionEvent) {
        boxContainer.getChildren().clear();
        try {
            boxContainer.getChildren().add(FXMLLoader.load(getPathUrl("src/main/resources/fxml/order/orders.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void medicinesAction(ActionEvent actionEvent) {
        boxContainer.getChildren().clear();
        try {
            boxContainer.getChildren().add(FXMLLoader.load(getPathUrl("src/main/resources/fxml/medicine/medicines.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void usersAction(ActionEvent actionEvent) {
        boxContainer.getChildren().clear();
        try {
            boxContainer.getChildren().add(FXMLLoader.load(getPathUrl("src/main/resources/fxml/user/users.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logoutAction(ActionEvent actionEvent) {
        SessionManager.getInstance().userLogout();
        showWindow("src/main/resources/fxml/login.fxml", "Login");
        ((Stage) ordersButton.getScene().getWindow()).close();
    }
}
