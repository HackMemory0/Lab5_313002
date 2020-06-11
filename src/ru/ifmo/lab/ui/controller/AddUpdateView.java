package ru.ifmo.lab.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.ifmo.lab.models.City;
import ru.ifmo.lab.models.Coordinates;
import ru.ifmo.lab.models.Government;
import ru.ifmo.lab.models.Human;
import ru.ifmo.lab.ui.NetworkManager;
import ru.ifmo.lab.ui.listener.EventListener;
import ru.ifmo.lab.ui.model.TableCityModel;
import ru.ifmo.lab.utils.I18N;
import ru.ifmo.lab.utils.NumUtil;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class AddUpdateView implements Initializable {
    public TextField id_tb;
    public TextField name_tb;
    public TextField x_tb;
    public TextField y_tb;
    public TextField date_tb;
    public TextField area_tb;
    public TextField population_tb;
    public TextField mas_tb;
    public TextField timezone_tb;
    public ComboBox capital_cb;
    public ComboBox goverment_cb;
    public TextField governor_tb;
    public Button add_btn;
    private ResourceBundle resources;


    private TableCityModel currentCity = new TableCityModel();
    private boolean isUpdate = false;
    private Integer errorCount = 0;


    public void btnOnClick(ActionEvent actionEvent) {
        if(errorCount != 0) showErrorDialog(I18N.get("error.input_ua"));
        else {
            /*
            UPDATE
             */
            if(isUpdate){
                City city = new City(
                        currentCity.getId(),
                        name_tb.getText(),
                        new Coordinates(Float.parseFloat(x_tb.getText()), Double.parseDouble(y_tb.getText())),
                        currentCity.getCreationDate(),
                        Double.parseDouble(area_tb.getText()),
                        Long.parseLong(population_tb.getText()),
                        Long.parseLong(mas_tb.getText()),
                        Double.parseDouble(timezone_tb.getText()),
                        Boolean.parseBoolean(capital_cb.getSelectionModel().getSelectedItem().toString()),
                        Government.valueOf(goverment_cb.getSelectionModel().getSelectedItem().toString()),
                        new Human(Integer.parseInt(governor_tb.getText())),
                        currentCity.getUserID(),
                        currentCity.getUsername()
                );

                NetworkManager.getInstance().update(currentCity.getId(), city, new EventListener() {
                    @Override
                    public void onResponse(Object event) {
                        showErrorDialog((String) event);
                    }

                    @Override
                    public void onError(Object message) {
                        showErrorDialog((String) message);
                    }
                });
            }else{
                /*
                ADD
                 */
                City city = new City(
                        name_tb.getText(),
                        new Coordinates(Float.parseFloat(x_tb.getText()), Double.parseDouble(y_tb.getText())),
                        Double.parseDouble(area_tb.getText()),
                        Long.parseLong(population_tb.getText()),
                        Long.parseLong(mas_tb.getText()),
                        Double.parseDouble(timezone_tb.getText()),
                        Boolean.parseBoolean(capital_cb.getSelectionModel().getSelectedItem().toString()),
                        Government.valueOf(goverment_cb.getSelectionModel().getSelectedItem().toString()),
                        new Human(Integer.parseInt(governor_tb.getText()))
                );

                NetworkManager.getInstance().add(city, new EventListener() {
                    @Override
                    public void onResponse(Object event) {
                        showErrorDialog((String) event);
                    }

                    @Override
                    public void onError(Object message) {
                        showErrorDialog((String) message);
                    }
                });
            }
        }
    }

    void transferData(TableCityModel city, boolean isUpdate){
        this.currentCity = city;
        this.isUpdate = isUpdate;

        add_btn.textProperty().bind(I18N.createStringBinding(isUpdate ? "key.update" : "key.add"));

        capital_cb.getSelectionModel().select(0);
        goverment_cb.getSelectionModel().select(0);

        if(isUpdate) {
            id_tb.setText(Long.valueOf(currentCity.getId()).toString());
            name_tb.setText(currentCity.getName());
            x_tb.setText(Float.valueOf(currentCity.getX()).toString());
            y_tb.setText(Double.valueOf(currentCity.getY()).toString());
            date_tb.setText(currentCity.getCreationDate().toString());
            area_tb.setText(Double.valueOf(currentCity.getArea()).toString());
            population_tb.setText(Long.valueOf(currentCity.getPopulation()).toString());
            mas_tb.setText(Long.valueOf(currentCity.getMetersAboveSeaLevel()).toString());
            timezone_tb.setText(Double.valueOf(currentCity.getTimezone()).toString());
            capital_cb.getSelectionModel().select(Boolean.valueOf(currentCity.isCapital()).toString());
            goverment_cb.getSelectionModel().select(currentCity.getGovernment());
            governor_tb.setText(Integer.valueOf(currentCity.getGovernor()).toString());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        capital_cb.getItems().removeAll(capital_cb.getItems());
        capital_cb.getItems().addAll("true", "false");
        goverment_cb.getItems().removeAll(goverment_cb.getItems());
        goverment_cb.getItems().addAll(Government.values());

        validateRange(x_tb, NumUtil.FLOAT_MIN, NumUtil.FLOAT_MAX, true);
        validateRange(y_tb, new BigDecimal(-587.0), NumUtil.DOUBLE_MAX, true);
        validateRange(area_tb, new BigDecimal(0), NumUtil.DOUBLE_MAX, true);
        validateRange(population_tb, new BigDecimal(0), NumUtil.LONG_MAX, false);
        validateRange(timezone_tb, new BigDecimal(-13.0), new BigDecimal(15.0), true);
        validateRange(mas_tb, NumUtil.LONG_MIN, NumUtil.LONG_MAX, false);
        validateRange(governor_tb, new BigDecimal(0), NumUtil.INTEGER_MAX, false);
        name_tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(name_tb.getText().isEmpty()) setErrorInput(name_tb);
                else removeErrorInput(name_tb);
            }
        });
    }

    private void validateRange(TextField tb, BigDecimal min, BigDecimal max, boolean isFloat){
        String intPattern = "^[-]?\\d*$";
        String decimalPattern = "^[-]?\\d+([.,]\\d+)?$";
        tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                try {
                    if(tb.getText().matches(isFloat ? decimalPattern : intPattern) && !tb.getText().isEmpty()) {
                        NumberFormat format = NumberFormat.getInstance();
                        if (!NumUtil.isInRange(format.parse(tb.getText().replace(".", ",")), min, max)) {
                            setErrorInput(tb);
                        } else removeErrorInput(tb);
                    }else setErrorInput(tb);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setErrorInput(TextField tb){
        if(tb.getStyle().isEmpty()) {
            tb.setStyle("-fx-text-fill:red; -fx-border-color: red;");
            errorCount++;
        }
    }

    private void removeErrorInput(TextField tb){
        if(!tb.getStyle().isEmpty()) {
            tb.setStyle(null);
            errorCount--;
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
