package main.java.retail.order.controllers.order;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.retail.order.SessionManager;
import main.java.retail.order.controllers.BaseController;
import main.java.retail.order.entity.Order;
import main.java.retail.order.entity.User;
import main.java.retail.order.interfaces.OrderInterface;
import main.java.retail.order.models.OrderModel;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrdersController extends BaseController implements Initializable, OrderInterface {
    public TableView<Order> ordersTable;
    public TableColumn<Order, String> customerColumn, orderDateColumn;
    public TableColumn<Order, Integer> totalItemsColumn;
    public TableColumn<Order, Double> priceColumn;
    public TextField searchField;

    private OrderModel orderModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderModel = new OrderModel();
        initTable();
        loadData();
    }

    private void loadData() {
        if (!ORDERLIST.isEmpty()) {
            ORDERLIST.clear();
        }
        User user = SessionManager.getInstance().getLoggedUser();
        if (user.isAdmin()) {
            ORDERLIST.addAll(orderModel.getOrders());
        } else {
            ORDERLIST.addAll(orderModel.getMyOrders(user));
        }
    }

    private void initTable() {

        customerColumn.setCellValueFactory((TableColumn.CellDataFeatures<Order, String> p) ->
                new SimpleStringProperty(orderModel.getCustomerName(p.getValue())));

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        totalItemsColumn.setCellValueFactory((TableColumn.CellDataFeatures<Order, Integer> p) ->
                new SimpleObjectProperty<>(p.getValue().getOrderItems().size()));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        ordersTable.setItems(ORDERLIST);

        filterData();
    }

    private void filterData() {
        FilteredList<Order> searchedData = new FilteredList<>(ORDERLIST, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(order -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (orderModel.getCustomerName(order).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (order.getOrderDate().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });

            SortedList<Order> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(ordersTable.comparatorProperty());
            ordersTable.setItems(sortedData);
        });
    }

    public void addOrderAction(ActionEvent actionEvent) {
        showWindowDialog("src/main/resources/fxml/order/create.fxml", "Create Order");
    }

    public void editOrderAction(ActionEvent actionEvent) {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        int selectedOrderId = ordersTable.getSelectionModel().getSelectedIndex();
        EditOrderController controller = new EditOrderController();
        showWindowDialog("src/main/resources/fxml/order/edit.fxml", "Update Order", controller);
        controller.setOrder(selectedOrder, selectedOrderId);
        ordersTable.getSelectionModel().clearSelection();
    }

    public void deleteOrderAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Order");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();

            orderModel.deleteOrder(selectedOrder);
            ORDERLIST.remove(selectedOrder);
        }

        ordersTable.getSelectionModel().clearSelection();
    }
}
