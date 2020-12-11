package main.java.retail.order.interfaces;

import javafx.collections.ObservableList;
import main.java.retail.order.entity.Medicine;

public interface IMedicine {

    ObservableList<Medicine> getMedicines();

    Medicine getMedicine(long id);

    Medicine getMedicineByName(String medicineName);

    boolean saveMedicine(Medicine medicine);

    boolean updateMedicine(Medicine medicine);

    void decreaseMedicine(Medicine medicine);

    void deleteMedicine(Medicine medicine);

    ObservableList<String> getMedicineNames();

    void increaseMedicine(Medicine medicine);
}
