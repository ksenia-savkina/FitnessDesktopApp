package Controllers;

import Implements.TestResultStorage;
import com.BindingModels.TestResultBindingModel;
import com.BusinessLogic.TestResultLogic;
import com.example.fitnessview.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Data
public class ResultController {

    private Integer id;

    private Integer clientId;

    private boolean modalResult;

    private final TestResultLogic logic;

    @FXML
    private JFXToggleButton toggleButton;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker timePicker;

    public ResultController() {
        modalResult = false;
        this.logic = new TestResultLogic(new TestResultStorage());
    }

    public void initData() {
        if (id != null) {
            try {
                var model = new TestResultBindingModel();
                model.id = id;
                var view = logic.read(model).get(0);
                if (view != null) {
                    toggleButton.setSelected(view.result);
                    datePicker.setValue(view.date.toLocalDateTime().toLocalDate());
                    timePicker.setValue(view.date.toLocalDateTime().toLocalTime());
                }
            } catch (Exception ex) {
                Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onSaveClick(ActionEvent actionEvent) {
        if (checkFields()) {
            try {
                TestResultBindingModel model = new TestResultBindingModel();
                model.id = id;
                model.result = toggleButton.isSelected();
                var date = datePicker.getValue();
                var time = timePicker.getValue();
                model.date = Timestamp.valueOf(LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getHour(), time.getMinute(), time.getSecond()));
                model.clientId = clientId;
                logic.createOrUpdate(model);
                Alert alert = alert(Alert.AlertType.INFORMATION, "Информация", "Сохранение прошло успешно");
                alert.showAndWait();
                this.modalResult = true;
                ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
            } catch (Exception ex) {
                Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onCancelClick(ActionEvent actionEvent) {
        this.modalResult = false;
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    private boolean checkFields() {
        if (datePicker.getValue() == null) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните дату!");
            alert.showAndWait();
            return false;
        }
        if (timePicker.getValue() == null) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните время!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private Alert alert(Alert.AlertType alertType, String title, String headerText) {
        Alert alert = new Alert(alertType);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("/img/icon32.png")));
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        return alert;
    }
}
