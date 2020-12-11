package main.java.retail.order.controllers.user;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.retail.order.controllers.BaseController;
import main.java.retail.order.entity.User;
import main.java.retail.order.interfaces.UserInterface;
import main.java.retail.order.models.UserModel;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController extends BaseController implements Initializable, UserInterface {

    public TableView<User> tableUsers;
    public TableColumn<User, String> usernameColumn, nameColumn, contactNumberColumn, addressColumn, roleColumn;
    public TextField searchField;
    public Button buttonAdd, buttonEdit, buttonDelete;

    private UserModel userModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userModel = new UserModel();
        loadData();
        initTableView();
        USERLIST.addListener((ListChangeListener<User>) c -> {
            tableUsers.setItems(USERLIST);
            tableUsers.refresh();
        });
    }

    private void loadData() {
        if (!USERLIST.isEmpty()) {
            USERLIST.clear();
        }
        USERLIST.addAll(userModel.getUsers());
    }

    private void initTableView() {
        buttonEdit.setDisable(true);
        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            buttonEdit.setDisable(newSelection == null);
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        contactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        tableUsers.setItems(USERLIST);

        filterData();
    }

    private void filterData() {

        FilteredList<User> searchedData = new FilteredList<>(USERLIST, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return user.getName().toLowerCase().contains(lowerCaseFilter) || user.getUsername().toLowerCase().contains(lowerCaseFilter);
                });
            });

            SortedList<User> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(tableUsers.comparatorProperty());
            tableUsers.setItems(sortedData);
        });
    }

    public void addAction(ActionEvent actionEvent) {
        showWindowDialog("src/main/resources/fxml/user/Add.fxml", "New User");
    }

    public void editAction(ActionEvent actionEvent) {
        User selectedUser = tableUsers.getSelectionModel().getSelectedItem();
        int selectedUserId = tableUsers.getSelectionModel().getSelectedIndex();
        EditUserController controller = new EditUserController();
        showWindowDialog("src/main/resources/fxml/user/Edit.fxml", "Update User", controller);
        controller.setUser(selectedUser, selectedUserId);
        tableUsers.getSelectionModel().clearSelection();
    }

    public void deleteAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete User");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            User selectedUser = tableUsers.getSelectionModel().getSelectedItem();

            userModel.deleteUser(selectedUser);
            USERLIST.remove(selectedUser);
        }

        tableUsers.getSelectionModel().clearSelection();
    }
}
