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


public class EditMedicineController extends BaseController implements Initializable, MedicineInterface {
    public TextField nameField, companyField, batchField;
    public TextField quantityField, priceField;
    public DatePicker pickerManufactureDate, pickerExpiryDate;
    public Button saveButton;

    private MedicineModel medicineModel;
    private Medicine medicine;
    private long selectedMedicineId;
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

    public void setMedicine(Medicine medicine, long selectedMedicineId) {
        this.medicine = medicine;
        this.selectedMedicineId = selectedMedicineId;
        setData();
        nameField.setDisable(true);
    }

    private void setData() {
        nameField.setText(medicine.getName());
        companyField.setText(medicine.getCompany());
        batchField.setText(medicine.getBatch());
        pickerManufactureDate.setPromptText(medicine.getDom());
        pickerExpiryDate.setPromptText(medicine.getDoe());
        priceField.setText(String.valueOf(medicine.getPrice()));
        quantityField.setText(String.valueOf(medicine.getQuantity()));
    }

    @FXML
    public void handleSave(ActionEvent event) {
        if (validateInput()) {
            String name = nameField.getText();
            String company = nameField.getText();
            String batch = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            Medicine editedMedicine = new Medicine(name, company, price, batch, dom, doe, quantity);
            if (medicineModel.updateMedicine(editedMedicine)) {
                MEDICINELIST.set((int) selectedMedicineId, editedMedicine);
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
        setData();
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

        if (pickerManufactureDate.getPromptText().isEmpty()) {
            errorMessage += "No valid medicine manufacture date!\n";
        }

        if (pickerExpiryDate.getPromptText().isEmpty()) {
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
