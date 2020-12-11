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
import main.java.retail.order.entity.User;
import main.java.retail.order.interfaces.UserInterface;
import main.java.retail.order.models.UserModel;

import java.net.URL;
import java.util.ResourceBundle;

import static main.java.retail.order.entity.User.*;


public class EditUserController extends BaseController implements Initializable, UserInterface {

    public TextField nameField, userNameField, passwordField, contactNumberField, addressField;
    public ChoiceBox<String> userTypeChoiceBox;
    public Button saveButton;

    private UserModel userModel;
    private User user;
    private long selectedUserId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userModel = new UserModel();
        userTypeChoiceBox.setItems(FXCollections.observableArrayList(TYPE_ADMIN, TYPE_PHARMACIST, TYPE_CUSTOMER));
        userNameField.setDisable(true);
        userNameField.setDisable(true);
    }


    public void setUser(User user, long selectedUserId) {
        this.user = user;
        this.selectedUserId = selectedUserId;
        setData();
    }

    private void setData() {
        nameField.setText(user.getName());
        userNameField.setText(String.valueOf(user.getUsername()));
        passwordField.setText(String.valueOf(user.getPassword()));
        contactNumberField.setText(String.valueOf(user.getContactNumber()));
        addressField.setText(String.valueOf(user.getAddress()));
        userTypeChoiceBox.setValue(String.valueOf(user.getRole()));
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
            User editedUser = new User(name, contactNumber, address, userName, password);
            editedUser.setRole(userType);
//            if (userType.equals(TYPE_ADMIN)) {
//                editedUser = new Admin(name, contactNumber, address, userName, password);
//            } else if (userType.equals(TYPE_PHARMACIST)) {
//                editedUser = new Pharmacist(name, contactNumber, address, userName, password);
//            } else {
//                editedUser = new Customer(name, contactNumber, address, userName, password);
//            }
            if (userModel.updateUser(editedUser)) {
                USERLIST.set((int) selectedUserId, editedUser);
                ((Stage) saveButton.getScene().getWindow()).close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setContentText("User is updated successfully");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User exists");
                alert.setContentText("User already exists, please add new User");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        setData();
//        nameField.setText("");
//        userNameField.setText("");
//        passwordField.setText("");
//        addressField.setText("");
//        contactNumberField.setText("");
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
