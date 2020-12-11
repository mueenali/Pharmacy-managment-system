package main.java.retail.order.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.retail.order.entity.Admin;
import main.java.retail.order.entity.User;
import main.java.retail.order.models.UserModel;
import main.java.retail.order.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BaseController implements Initializable {
    public Button buttonLogin;
    public TextField textFieldUserName;
    public PasswordField textFieldPassword;
    public Label labelError;

    private UserModel userModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userModel = new UserModel();
        textFieldUserName.textProperty().addListener((observable, oldValue, newValue) -> {
            labelError.setVisible(false);
        });
        textFieldPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            labelError.setVisible(false);
        });
    }

    public void onLoginClick(ActionEvent actionEvent) {

        String userName = textFieldUserName.getText();
        String password = textFieldPassword.getText();
        if (userName.isEmpty() || password.isEmpty()) {
            showLoginError("Username or password cannot be empty");
        } else {
            if (userModel.checkUser(userName)) {
                if (userModel.checkPassword(userName, password)) {
                    User user = userModel.getUser(userName);
                    if (user == null) {
                        user = new Admin("admin", "0111111", "address", userName, password);
                        userModel.saveUser(user);
                    }
                    synchronized (this) {
                        SessionManager.getInstance().setLoggedUser(user);
                    }
                    showWindow("src/main/resources/fxml/main.fxml", "Order System");
                    ((Stage) textFieldPassword.getScene().getWindow()).close();
                } else {
                    showLoginError("Username or password is wrong");
                }
            } else {
                showLoginError("This user does not exist");
            }
        }
    }

    private void showLoginError(String errorMessage) {
        labelError.setText(errorMessage);
        labelError.setVisible(true);
    }
}
