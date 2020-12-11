package main.java.retail.order.models;

import com.sun.istack.internal.Nullable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.retail.order.entity.Medicine;
import main.java.retail.order.interfaces.IMedicine;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MedicineModel implements IMedicine {

    public MedicineModel() {
    }

    @Override
    public ObservableList<Medicine> getMedicines() {
        List<Medicine> medicines = readMedicinesFromFile();
        return FXCollections.observableArrayList(medicines);
    }

    @Override
    @Nullable
    public Medicine getMedicine(long id) {
        for (Medicine medicine : getMedicines()) {
            if (medicine.getId() == id) {
                return medicine;
            }
        }
        return null;
    }

    @Override
    public Medicine getMedicineByName(String medicineName) {
        for (Medicine medicine : getMedicines()) {
            if (medicine.getName().equalsIgnoreCase(medicineName)) {
                return medicine;
            }
        }
        return null;
    }

    @Override
    public boolean saveMedicine(Medicine newMedicine) {
        List<Medicine> medicines = readMedicinesFromFile();
        boolean isNew = true;
        for (Medicine medicine : medicines) {
            if (newMedicine.getName().equalsIgnoreCase(medicine.getName())) {
                isNew = false;
            }
        }
        if (isNew || medicines.isEmpty()) {
            long id = medicines.isEmpty() ? 0 : medicines.get(medicines.size() - 1).getId() + 1;
            newMedicine.setId(id);
            medicines.add(newMedicine);
        }
        writeMedicinesToFile(medicines);
        return isNew;
    }

    @Override
    public boolean updateMedicine(Medicine updateMedicine) {
        List<Medicine> medicines = readMedicinesFromFile();
        int index = -1;
        Medicine oldMedicine = null;
        for (int i = 0; i < medicines.size(); i++) {
            Medicine medicine = medicines.get(i);
            if (medicine.getName().equalsIgnoreCase(updateMedicine.getName())) {
                index = i;
                oldMedicine = medicine;
            }
        }

        if (index != -1) {
            updateMedicine.setId(oldMedicine.getId());
            medicines.set(index, updateMedicine);
            writeMedicinesToFile(medicines);
        }
        return index != -1;
    }

    @Override
    public void decreaseMedicine(Medicine medicine) {
        updateMedicine(medicine);
    }

    @Override
    public void deleteMedicine(Medicine deleteMedicine) {
        List<Medicine> medicines = readMedicinesFromFile();
        int index = -1;
        for (int i = 0; i < medicines.size(); i++) {
            Medicine medicine = medicines.get(i);
            if (medicine.getId() == deleteMedicine.getId()) {
                index = i;
            }
        }

        if (index != -1) {
            medicines.remove(index);
        }
        writeMedicinesToFile(medicines);
    }

    @Override
    public ObservableList<String> getMedicineNames() {
        return null;
    }

    @Override
    public void increaseMedicine(Medicine medicine) {
        updateMedicine(medicine);
    }

    private void writeMedicinesToFile(List<Medicine> list) {
        String filePath = "/src/main/data/medicines.dat";
        Path currentRelativePath = Paths.get("");
        String projectPath = currentRelativePath.toAbsolutePath().toString();
        filePath = projectPath + filePath;
        ObjectOutputStream outStream = null;
        try {
            outStream = new ObjectOutputStream(new FileOutputStream(filePath));
            for (Medicine p : list) {
                outStream.writeObject(p);
            }

        } catch (IOException ioException) {
            System.err.println("Error opening file.");
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("Invalid input.");
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException ioException) {
                System.err.println("Error closing file.");
            }
        }
    }

    private List<Medicine> readMedicinesFromFile() {
        String filePath = "/src/main/data/medicines.dat";
        Path currentRelativePath = Paths.get("");
        String projectPath = currentRelativePath.toAbsolutePath().toString();
        filePath = projectPath + filePath;
        ArrayList<Medicine> list = new ArrayList<>();
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(filePath));
            while (true) {
                Medicine medicine = (Medicine) inputStream.readObject();
                list.add(medicine);
            }
        } catch (EOFException eofException) {
            return list;
        } catch (ClassNotFoundException classNotFoundException) {
            System.err.println("Object creation failed.");
        } catch (IOException ioException) {
            System.err.println("Error opening file.");
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ioException) {
                System.err.println("Error closing file.");
            }
        }
        return list;
    }
}
