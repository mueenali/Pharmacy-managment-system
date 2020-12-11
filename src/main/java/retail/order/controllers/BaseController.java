package main.java.retail.order.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class BaseController {


    void showWindow(String path, String title) {
        Parent root = null;
        try {
            URL url = new File(path).toURI().toURL();
            root = FXMLLoader.load(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    protected void showWindowDialog(String path, String title) {
        Parent root = null;
        try {
            URL url = new File(path).toURI().toURL();
            root = FXMLLoader.load(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset.set(e.getSceneX());
            yOffset.set(e.getSceneY());
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset.get());
            stage.setY(e.getScreenY() - yOffset.get());
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    protected void showWindowDialog(String path, String title, BaseController controller) {
        Parent root = null;
        try {
            URL url = new File(path).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset.set(e.getSceneX());
            yOffset.set(e.getSceneY());
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset.get());
            stage.setY(e.getScreenY() - yOffset.get());
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    URL getPathUrl(String path){
        URL url = null;
        try {
            url = new File(path).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
