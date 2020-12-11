package main.java.retail.order.controllers.order;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.retail.order.SessionManager;
import main.java.retail.order.controllers.BaseController;
import main.java.retail.order.entity.Medicine;
import main.java.retail.order.entity.Order;
import main.java.retail.order.entity.OrderItem;
import main.java.retail.order.interfaces.MedicineInterface;
import main.java.retail.order.interfaces.OrderInterface;
import main.java.retail.order.models.MedicineModel;
import main.java.retail.order.models.OrderModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditOrderController extends BaseController implements Initializable, MedicineInterface, OrderInterface {
    public Label totalPriceLabel;


    public TableView<OrderItem> tableOrderItems;
    public TableColumn<OrderItem, String> orderItemNameColumn;
    public TableColumn<OrderItem, Integer> orderItemQuantityColumn;
    public TableColumn<OrderItem, Double> orderItemPriceColumn, orderItemTotalColumn;
    public Button buttonAddToOrder;

    public TextField quantityField;
    public TextField priceField;
    public TextField medicineField;

    public TableView<Medicine> tableMedicines;
    public TableColumn<Medicine, Integer> medicineQuantityColumn;
    public TableColumn<Medicine, Double> medicinePriceColumn;
    public TableColumn<Medicine, String> medicineNameColumn;
    public TextField searchField;
    public Label orderDateLabel;
    public Button deleteOrderItemButton;
    private MedicineModel medicineModel;
    private OrderModel orderModel;

    private Order currentOrder = new Order();
    private long selectedOrderId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        medicineModel = new MedicineModel();
        orderModel = new OrderModel();
        loadData();
        initMedicinesTable();
        initOrderListTable();
    }

    public void setOrder(Order order, long selectedOrderId) {
        this.currentOrder = order;
        this.selectedOrderId = selectedOrderId;
        setData();
    }

    private void setData() {
        ORDERITEMLIST.addAll(currentOrder.getOrderItems());
        orderDateLabel.setText(currentOrder.getOrderDate());
        totalPriceLabel.setText(String.valueOf(currentOrder.getTotalPrice()));
    }

    private void loadData() {
        if (!MEDICINELIST.isEmpty()) {
            MEDICINELIST.clear();
        }
        MEDICINELIST.addAll(medicineModel.getMedicines());
        if (!ORDERITEMLIST.isEmpty()) {
            ORDERITEMLIST.clear();
        }
        ORDERITEMLIST.addAll(orderModel.getOrderItems(-1));

        ORDERITEMLIST.addListener((ListChangeListener<OrderItem>) c -> {
            updateCurrentOrderData();
            totalPriceLabel.setText(String.valueOf(currentOrder.getTotalPrice()));
        });
    }

    private void updateCurrentOrderData() {
        currentOrder.setOrderItems(getOrderList());
        double totalPrice = 0;
        for (OrderItem orderItem : ORDERITEMLIST) {
            totalPrice += orderItem.getTotalPrice();
        }
        currentOrder.setTotalPrice(totalPrice);
        currentOrder.setCustomerId(SessionManager.getInstance().getLoggedUser().getId());
    }

    private void initOrderListTable() {
        deleteOrderItemButton.setDisable(true);
        tableOrderItems.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteOrderItemButton.setDisable(newSelection == null);
        });

        orderItemNameColumn.setCellValueFactory((TableColumn.CellDataFeatures<OrderItem, String> p) ->
                new SimpleStringProperty(p.getValue().getMedicine().getName())
        );
        orderItemPriceColumn.setCellValueFactory((TableColumn.CellDataFeatures<OrderItem, Double> p) ->
                new SimpleObjectProperty<>(p.getValue().getMedicine().getPrice()));
        orderItemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderItemTotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        tableOrderItems.setItems(ORDERITEMLIST);
    }

    private void initMedicinesTable() {
        buttonAddToOrder.setDisable(true);
        tableMedicines.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            buttonAddToOrder.setDisable(newSelection == null);
            if (newSelection != null) {
                medicineField.setText(newSelection.getName());
                priceField.setText(String.valueOf(newSelection.getPrice()));
            } else {
                resetMedicineItemFields();
            }
        });

        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        medicinePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        medicineQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//        medicineIsFragileColumn.setCellValueFactory((TableColumn.CellDataFeatures<Medicine, String> p) ->
//                new SimpleStringProperty(p.getValue().isFragile() ? "Yes" : "No"));
        tableMedicines.setItems(MEDICINELIST);

        filterData();
    }

    private void resetMedicineItemFields() {
        medicineField.setText("");
        priceField.setText("");
        quantityField.setText("");
    }

    private void filterData() {
        FilteredList<Medicine> searchedData = new FilteredList<>(MEDICINELIST, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(medicine -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (medicine.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return String.valueOf(medicine.getId()).contains(lowerCaseFilter);
                });
            });
            SortedList<Medicine> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(tableMedicines.comparatorProperty());
            tableMedicines.setItems(sortedData);
        });
    }

    public void onAddItemToOrder(ActionEvent actionEvent) {
        if (validateAddToOrderInput()) {
            Medicine medicine = tableMedicines.getSelectionModel().getSelectedItem();
            OrderItem orderItem = new OrderItem(medicine, Integer.valueOf(quantityField.getText()));
            ORDERITEMLIST.add(orderItem);
            resetMedicineItemFields();
        }
    }

    public void onConfirmOrder(ActionEvent actionEvent) {
        if (validateCreateOrderInput()) {
            if (orderModel.updateOrder(currentOrder)) {
                ORDERLIST.set((int) selectedOrderId, currentOrder);
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setContentText("Medicine is added successfully");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Order exists");
                alert.setContentText("Order already exists, please add new medicine");
                alert.showAndWait();
            }
        }
    }

    public void onDeleteOrderItem(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Order Item");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            OrderItem orderItem = tableOrderItems.getSelectionModel().getSelectedItem();

            ORDERITEMLIST.remove(orderItem);
        }

        tableMedicines.getSelectionModel().clearSelection();
    }

    public void onCloseClick(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    private boolean validateCreateOrderInput() {
        String errorMessage = "";
        if (currentOrder.getOrderItems() == null) {
            errorMessage += "Please add items to the order!\n";
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

    private boolean validateAddToOrderInput() {
        String errorMessage = "";
        if (tableMedicines.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Please Select medicine!\n";
        }

        if (quantityField.getText().isEmpty()) {
            errorMessage += "No valid order item quantity!\n";
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

    public ArrayList<OrderItem> getOrderList() {
        return new ArrayList<>(ORDERITEMLIST);
    }
}

