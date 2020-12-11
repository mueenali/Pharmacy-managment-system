package main.java.retail.order.controllers.user;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.retail.order.controllers.BaseController;
import main.java.retail.order.entity.Admin;
import main.java.retail.order.entity.Customer;
import main.java.retail.order.entity.Pharmacist;
import main.java.retail.order.entity.User;
import main.java.retail.order.interfaces.UserInterface;
import main.java.retail.order.models.UserModel;

import java.net.URL;
import java.util.ResourceBundle;

import static main.java.retail.order.entity.User.*;


public class AddUserController extends BaseController implements Initializable, UserInterface {
    public TextField nameField, userNameField, passwordField, contactNumberField, addressField;
    public ChoiceBox<String> userTypeChoiceBox;
    public Button saveButton;
    private UserModel userModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userModel = new UserModel();
        userTypeChoiceBox.setItems(FXCollections.observableArrayList(TYPE_ADMIN, TYPE_PHARMACIST, TYPE_CUSTOMER));
        userTypeChoiceBox.setValue(TYPE_CUSTOMER);
    }

    @FXML
    public void handleSave(ActionEvent event) {
        if (validateInput()) {
            String name = nameField.getText();
            String userName = userNameField.getText();
            String contactNumber = contactNumberField.getText();
            String address = addressField.getText();
            String password = passwordField.getText();
            String userType = userTypeChoiceBox.getValue();
            User user = new User(name, contactNumber, address, userName, password);
            user.setRole(userType);
//            if (userType.equals(TYPE_ADMIN)) {
//                user = new Admin(name, contactNumber, address, userName, password);
//            } else if (userType.equals(TYPE_PHARMACIST)) {
//                user = new Pharmacist(name, contactNumber, address, userName, password);
//            } else {
//                user = new Customer(name, contactNumber, address, userName, password);
//            }
            if (userModel.saveUser(user)) {
                USERLIST.add(user);
                USERLIST.clear();
                USERLIST.addAll(userModel.getUsers());
                ((Stage) saveButton.getScene().getWindow()).close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setContentText("User is added successfully");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User exists");
                alert.setContentText("User already exists, please add new user");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        nameField.setText("");
        userNameField.setText("");
        passwordField.setText("");
        addressField.setText("");
        contactNumberField.setText("");
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (nameField.getText().isEmpty()) {
            errorMessage += "No valid user name!\n";
        }

        if (userNameField.getText().isEmpty()) {
            errorMessage += "No valid user username!\n";
        }

        if (passwordField.getText().isEmpty()) {
            errorMessage += "No valid user password!\n";
        }

        if (addressField.getText().isEmpty()) {
            errorMessage += "No valid user address!\n";
        }
        if (contactNumberField.getText().isEmpty()) {
            errorMessage += "No valid user contact number!\n";
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }

    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
