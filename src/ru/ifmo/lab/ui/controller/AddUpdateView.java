package ru.ifmo.lab.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.ifmo.lab.models.City;
import ru.ifmo.lab.models.Government;
import ru.ifmo.lab.ui.model.TableCityModel;
import ru.ifmo.lab.utils.NumUtil;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

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

    private TableCityModel currentCity = new TableCityModel();
    private boolean isUpdate = false;


    public void btnOnClick(ActionEvent actionEvent) {

    }

    void transferData(TableCityModel city, boolean isUpdate){
        this.currentCity = city;
        this.isUpdate = isUpdate;

        capital_cb.getItems().removeAll(capital_cb.getItems());
        capital_cb.getItems().addAll("true", "false");

        goverment_cb.getItems().removeAll(goverment_cb.getItems());
        goverment_cb.getItems().addAll(Government.values());

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
            capital_cb.getSelectionModel().select(Boolean.valueOf(currentCity.getCapital()).toString());
            goverment_cb.getSelectionModel().select(currentCity.getGovernment());
            governor_tb.setText(Integer.valueOf(currentCity.getGovernor()).toString());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validateRange(y_tb, new BigDecimal(-587.0), NumUtil.DOUBLE_MAX);
        validateRange(area_tb, new BigDecimal(0), NumUtil.DOUBLE_MAX);
        validateRange(population_tb, new BigDecimal(0), NumUtil.LONG_MAX);
        validateRange(timezone_tb, new BigDecimal(-13.0), new BigDecimal(15.0));
        validateRange(governor_tb, new BigDecimal(-13.0), new BigDecimal(15.0));
    }

    private void validateRange(TextField tb, BigDecimal min, BigDecimal max){
        tb.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // when focus lost
                try {
                    if(tb.getText().replace(",", ".").matches("^[+-]?\\d*\\.\\d+$|^[+-]?\\d+(\\.\\d*)?$")) {
                        NumberFormat format = NumberFormat.getInstance();
                        if (!NumUtil.isInRange(format.parse(tb.getText()), min, max)) {
                            tb.setStyle("-fx-text-fill:red; -fx-border-color: red;");
                        } else tb.setStyle(null);
                    }else tb.setStyle("-fx-text-fill:red; -fx-border-color: red;");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
