package main.java.retail.order.controllers.medicine;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.retail.order.SessionManager;
import main.java.retail.order.controllers.BaseController;
import main.java.retail.order.entity.Medicine;
import main.java.retail.order.interfaces.MedicineInterface;
import main.java.retail.order.models.MedicineModel;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


public class MedicinesController extends BaseController implements Initializable, MedicineInterface {

    public TableView<Medicine> tableMedicines;

    public TableColumn<Medicine, String> nameColumn, companyColumn, batchColumn, domColumn, doeColumn;
    public TableColumn<Medicine, Integer> quantityColumn;
    public TableColumn<Medicine, Double> priceColumn;

    public TextField searchField;
    public CheckBox cbExpiredMedicines;

    public Button buttonAdd, buttonEdit, buttonDelete;

    private MedicineModel medicineModel;
    private SessionManager sessionManager = SessionManager.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        medicineModel = new MedicineModel();
        loadData();
        initTableView();
        MEDICINELIST.addListener((ListChangeListener<Medicine>) c -> {
            tableMedicines.setItems(MEDICINELIST);
            tableMedicines.refresh();
        });
    }

    private void loadData() {
        if (!MEDICINELIST.isEmpty()) {
            MEDICINELIST.clear();
        }
        MEDICINELIST.addAll(medicineModel.getMedicines());
    }

    private void initTableView() {
        cbExpiredMedicines.setDisable(sessionManager.isCustomer());
        buttonAdd.setDisable(sessionManager.isCustomer());
        buttonDelete.setDisable(sessionManager.isCustomer());
        buttonEdit.setDisable(true);
        tableMedicines.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            buttonEdit.setDisable(newSelection == null || sessionManager.isCustomer());
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
        batchColumn.setCellValueFactory(new PropertyValueFactory<>("batch"));
        domColumn.setCellValueFactory(new PropertyValueFactory<>("dom"));
        doeColumn.setCellValueFactory(new PropertyValueFactory<>("doe"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableMedicines.setItems(MEDICINELIST);

        filterData();
        filterExpiredMedicines();
    }

    private void filterExpiredMedicines() {
        FilteredList<Medicine> expiredDate = new FilteredList<>(MEDICINELIST, e -> true);
        cbExpiredMedicines.setOnAction(e -> {
            expiredDate.setPredicate(medicine -> {
                if (!cbExpiredMedicines.isSelected()) {
                    return true;
                }
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(medicine.getDoe());
                    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(LocalDate.now());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                    return true;
                }
            });
            SortedList<Medicine> sortedData = new SortedList<>(expiredDate);
            sortedData.comparatorProperty().bind(tableMedicines.comparatorProperty());
            tableMedicines.setItems(sortedData);

        });
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
                    return medicine.getName().toLowerCase().contains(lowerCaseFilter) || String.valueOf(medicine.getId()).toLowerCase().contains(lowerCaseFilter);
                });
            });

            SortedList<Medicine> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(tableMedicines.comparatorProperty());
            tableMedicines.setItems(sortedData);
        });
    }

    public void addAction(ActionEvent actionEvent) {
        showWindowDialog("src/main/resources/fxml/medicine/Add.fxml", "New Medicine");
    }

    public void editAction(ActionEvent actionEvent) {
        Medicine selectedMedicine = tableMedicines.getSelectionModel().getSelectedItem();
        int selectedMedicineId = tableMedicines.getSelectionModel().getSelectedIndex();
        EditMedicineController controller = new EditMedicineController();
        showWindowDialog("src/main/resources/fxml/medicine/Edit.fxml", "Update Medicine", controller);
        controller.setMedicine(selectedMedicine, selectedMedicineId);
        tableMedicines.getSelectionModel().clearSelection();
    }

    public void deleteAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Medicine");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Medicine selectedMedicine = tableMedicines.getSelectionModel().getSelectedItem();

            medicineModel.deleteMedicine(selectedMedicine);
            MEDICINELIST.remove(selectedMedicine);
        }

        tableMedicines.getSelectionModel().clearSelection();
    }
}
