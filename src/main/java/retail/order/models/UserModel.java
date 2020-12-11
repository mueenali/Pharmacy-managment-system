package main.java.retail.order.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.retail.order.entity.User;
import main.java.retail.order.interfaces.IUser;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static main.java.retail.order.entity.User.TYPE_CUSTOMER;

public class UserModel implements IUser {


    public UserModel() {
    }

    @Override
    public ObservableList<User> getUsers() {

        List<User> users = readUsersFromFile();

        return FXCollections.observableArrayList(users);
    }

    @Override
    public User getUser(long id) {
        for (User user : getUsers()) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUser(String userName) {
        for (User user : getUsers()) {
            if (user.getUsername().equalsIgnoreCase(userName)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public String getUserType(String username) {
        return TYPE_CUSTOMER;
    }

    @Override
    public boolean saveUser(User newUser) {
        List<User> users = readUsersFromFile();
        boolean isNew = true;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(newUser.getUsername())) {
                isNew = false;
            }
        }
        if (isNew || users.isEmpty()) {
            long id = users.isEmpty() ? 0 : users.get(users.size() - 1).getId() + 1;
            newUser.setId(id);
            users.add(newUser);
        }
        writeUsersToFile(users);
        return isNew;
    }

    @Override
    public boolean updateUser(User updatedUser) {
        List<User> users = readUsersFromFile();
        int index = -1;
        User oldUser = null;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equalsIgnoreCase(updatedUser.getUsername())) {
                index = i;
                oldUser = user;
            }
        }

        if (index != -1) {
            updatedUser.setId(oldUser.getId());
            users.set(index, updatedUser);
        }
        writeUsersToFile(users);
        return index != -1;
    }

    @Override
    public void deleteUser(User deletedUser) {
        List<User> users = readUsersFromFile();
        boolean isExit = false;
        int index = -1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equalsIgnoreCase(deletedUser.getUsername())) {
                index = i;
                isExit = true;
            }
        }

        if (isExit) {
            users.remove(index);
        } else {
            // TODO: 04/03/2018  return boolean to show error
        }
        writeUsersToFile(users);
    }

    @Override
    public boolean checkPassword(String username, String password) {
        if (username.equals("admin") && password.equals("123")) {
            return true;
        }
        List<User> users = readUsersFromFile();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equalsIgnoreCase(username)) {
                if (user.getPassword().equalsIgnoreCase(password)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean checkUser(String username) {
        if (username.equals("admin")) {
            return true;
        }
        List<User> users = readUsersFromFile();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    private void writeUsersToFile(List<User> list) {
        String filePath = "/src/main/data/users.dat";

        Path currentRelativePath = Paths.get("");
        String projectPath = currentRelativePath.toAbsolutePath().toString();
        filePath = projectPath + filePath;
        ObjectOutputStream outStream = null;
        try {
            outStream = new ObjectOutputStream(new FileOutputStream(filePath));
            for (User p : list) {
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

    private List<User> readUsersFromFile() {
        String filePath = "/src/main/data/users.dat";

        Path currentRelativePath = Paths.get("");
        String projectPath = currentRelativePath.toAbsolutePath().toString();
        filePath = projectPath + filePath;
        ArrayList<User> list = new ArrayList<>();
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(filePath));
            while (true) {
                User user = (User) inputStream.readObject();
                list.add(user);
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
