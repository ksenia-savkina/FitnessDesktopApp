package Controllers;

import Implements.TrainerStorage;
import com.BindingModels.TrainerBindingModel;
import com.BusinessLogic.TrainerLogic;
import com.example.fitnessview.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Data;
import org.postgresql.util.PGInterval;

@Data
public class TrainerController {

    private Integer id;

    private boolean modalResult;

    private final TrainerLogic logic;

    @FXML
    private Spinner spinnerExperienceYears;
    @FXML
    private Spinner spinnerExperienceMonths;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldSurname;
    @FXML
    private TextField textFieldPhone;

    public TrainerController() {
        modalResult = false;
        this.logic = new TrainerLogic(new TrainerStorage());
    }

    public void initData() {
        if (id != null) {
            try {
                var model = new TrainerBindingModel();
                model.id = id;
                var view = logic.read(model).get(0);
                if (view != null) {
                    textFieldName.setText(view.name);
                    textFieldSurname.setText(view.surname);
                    textFieldPhone.setText(view.phone);
                    spinnerExperienceYears.getValueFactory().setValue(view.experience.getYears());
                    spinnerExperienceMonths.getValueFactory().setValue(view.experience.getMonths());
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
                TrainerBindingModel model = new TrainerBindingModel();
                model.id = id;
                model.name = textFieldName.getText();
                model.surname = textFieldSurname.getText();
                model.phone = textFieldPhone.getText();
                model.experience = new PGInterval((Integer) spinnerExperienceYears.getValue(), (Integer) spinnerExperienceMonths.getValue(), 0, 0, 0, 0);
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
        if (textFieldName.getText().length() == 0) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните имя!");
            alert.showAndWait();
            return false;
        }
        if (textFieldSurname.getText().length() == 0) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните фамилию!");
            alert.showAndWait();
            return false;
        }
        if (spinnerExperienceMonths.getValue().equals(0) && spinnerExperienceYears.getValue().equals(0)) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните стаж!");
            alert.showAndWait();
            return false;
        }
        if (textFieldPhone.getText().length() == 0) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните номер телефона!");
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
