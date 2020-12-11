package main.java.retail.order.controllers.medicine;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.retail.order.controllers.BaseController;
import main.java.retail.order.entity.Medicine;
import main.java.retail.order.interfaces.MedicineInterface;
import main.java.retail.order.models.MedicineModel;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class AddMedicineController extends BaseController implements Initializable, MedicineInterface {
    public TextField nameField, companyField, batchField;
    public TextField quantityField, priceField;
    public DatePicker pickerManufactureDate, pickerExpiryDate;


    public Button saveButton;
    private MedicineModel medicineModel;
    private String dom, doe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        medicineModel = new MedicineModel();
        pickerManufactureDate.setOnAction(t -> {
            LocalDate date = pickerManufactureDate.getValue();
            dom = pickerManufactureDate.getChronology().date(date).toString();
        });

        pickerExpiryDate.setOnAction(t -> {
            LocalDate date = pickerExpiryDate.getValue();
            doe = pickerExpiryDate.getChronology().date(date).toString();
        });
    }

    @FXML
    public void handleSave(ActionEvent event) {
        if (validateInput()) {
            String name = nameField.getText();
            String company = companyField.getText();
            String batch = batchField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            Medicine medicine = new Medicine(name, company, price, batch, dom, doe, quantity);
            if (medicineModel.saveMedicine(medicine)) {
                MEDICINELIST.add(medicine);
                MEDICINELIST.clear();
                MEDICINELIST.addAll(medicineModel.getMedicines());
                ((Stage) saveButton.getScene().getWindow()).close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful");
                alert.setContentText("Medicine is added successfully");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Medicine exists");
                alert.setContentText("Medicine already exists, please add new medicine");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        nameField.setText("");
        companyField.setText("");
        batchField.setText("");
        priceField.setText("");
        pickerManufactureDate.setPromptText("");
        pickerExpiryDate.setPromptText("");
        quantityField.setText("");
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (nameField.getText().isEmpty()) {
            errorMessage += "No valid medicine name!\n";
        }
        if (companyField.getText().isEmpty()) {
            errorMessage += "No valid medicine company!\n";
        }

        if (batchField.getText() == null || batchField.getText().length() == 0) {
            errorMessage += "No valid medicine Batch!\n";
        }

        if (priceField.getText() == null || priceField.getText().length() == 0) {
            errorMessage += "No valid medicine price!\n";
        }

        if (dom == null) {
            errorMessage += "No valid medicine manufacture date!\n";
        }

        if (doe == null) {
            errorMessage += "No valid medicine expiry date!\n";
        }

        if (quantityField.getText() == null || quantityField.getText().length() == 0) {
            errorMessage += "No valid medicine quantity!\n";
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
