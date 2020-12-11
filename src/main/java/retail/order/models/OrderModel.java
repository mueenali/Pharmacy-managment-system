package main.java.retail.order.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.retail.order.entity.Order;
import main.java.retail.order.entity.OrderItem;
import main.java.retail.order.entity.Medicine;
import main.java.retail.order.entity.User;
import main.java.retail.order.interfaces.IOrder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderModel implements IOrder {

    private MedicineModel medicineModel;
    private UserModel userModel;

    public OrderModel() {
        medicineModel = new MedicineModel();
        userModel = new UserModel();
    }

    @Override
    public ObservableList<Order> getOrders() {
        List<Order> orders = readOrdersFromFile();
        return FXCollections.observableArrayList(orders);
    }

    @Override
    public ObservableList<Order> getMyOrders(User user) {
        List<Order> orders = getOrders();
        List<Order> myOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomerId() == user.getId()) {
                myOrders.add(order);
            }
        }
        return FXCollections.observableArrayList(myOrders);
    }

    @Override
    public Order getOrder(long id) {
        for (Order order : getOrders()) {
            if (order.getId() == id) {
                return order;
            }
        }

        return null;
    }

    @Override
    public ObservableList<OrderItem> getOrderItems(long id) {
        for (Order order : getOrders()) {
            if (order.getId() == id) {
                return FXCollections.observableArrayList(order.getOrderItems());
            }
        }

        return FXCollections.observableArrayList(new ArrayList<OrderItem>());
    }

    @Override
    public boolean saveOrder(Order newOrder) {
        List<Order> orders = readOrdersFromFile();
        boolean isNew = true;
        for (Order order : orders) {
            if (newOrder.getId() == order.getId()) {
                isNew = false;
            }
        }
        if (isNew || orders.isEmpty()) {
            long id = orders.isEmpty() ? 0 : orders.get(orders.size() - 1).getId() + 1;
            newOrder.setOrderDate(new Date().toString());
            newOrder.setId(id);
            orders.add(newOrder);
        }
        writeOrdersToFile(orders);
        return isNew;
    }

    @Override
    public boolean updateOrder(Order updateOrder) {
        List<Order> orders = readOrdersFromFile();
        int index = -1;
        Order oldOrder = null;
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getId() == updateOrder.getId()) {
                index = i;
                oldOrder = order;
            }
        }

        if (index != -1) {
            updateOrder.setId(oldOrder.getId());
            orders.set(index, updateOrder);
            writeOrdersToFile(orders);
        }
        return index != -1;
    }

    @Override
    public void deleteOrder(Order deleteOrder) {
        List<Order> orders = readOrdersFromFile();
        int index = -1;
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.getId() == deleteOrder.getId()) {
                index = i;
            }
        }

        if (index != -1) {
            orders.remove(index);
        }
        writeOrdersToFile(orders);
    }

    @Override
    public void decreaseMedicine(Medicine medicine) {
        medicineModel.decreaseMedicine(medicine);
    }

    @Override
    public String getCustomerName(Order order) {
        User user = userModel.getUser(order.getCustomerId());
        if (user != null) {
            return user.getName();
        } else {
            return "Name";
        }
    }

    private void writeOrdersToFile(List<Order> list) {
        String filePath = "/src/main/data/orders.dat";
        Path currentRelativePath = Paths.get("");
        String projectPath = currentRelativePath.toAbsolutePath().toString();
        filePath = projectPath + filePath;
        ObjectOutputStream outStream = null;
        try {
            outStream = new ObjectOutputStream(new FileOutputStream(filePath));
            for (Order p : list) {
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

    private List<Order> readOrdersFromFile() {
        String filePath = "/src/main/data/orders.dat";
        Path currentRelativePath = Paths.get("");
        String projectPath = currentRelativePath.toAbsolutePath().toString();
        filePath = projectPath + filePath;
        ArrayList<Order> list = new ArrayList<>();
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(filePath));
            while (true) {
                Order order = (Order) inputStream.readObject();
                list.add(order);
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
