package Controllers;

import Implements.StatusStorage;
import com.BindingModels.StatusBindingModel;
import com.BusinessLogic.StatusLogic;
import com.example.fitnessview.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class StatusController {

    private Integer id;

    private boolean modalResult;

    private final StatusLogic logic;

    @FXML
    private TextField textField;

    public StatusController() {
        modalResult = false;
        this.logic = new StatusLogic(new StatusStorage());
    }

    public void initData() {
        if (id != null) {
            try {
                var model = new StatusBindingModel();
                model.id = id;
                var view = logic.read(model).get(0);
                if (view != null) {
                    textField.setText(view.name);
                }
            } catch (Exception ex) {
                Alert alert = alert(AlertType.ERROR, "Ошибка", ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onSaveClick(ActionEvent actionEvent) {
        if (checkFields()) {
            try {
                StatusBindingModel statusBindingModel = new StatusBindingModel();
                statusBindingModel.id = id;
                statusBindingModel.name = textField.getText();
                logic.createOrUpdate(statusBindingModel);

                Alert alert = alert(AlertType.INFORMATION, "Информация", "Сохранение прошло успешно");
                alert.showAndWait();

                this.modalResult = true;
                ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
            } catch (Exception ex) {
                Alert alert = alert(AlertType.ERROR, "Ошибка", ex.getMessage());
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
        if (textField.getText().length() == 0) {
            Alert alert = alert(AlertType.ERROR, "Ошибка", "Заполните название!");
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