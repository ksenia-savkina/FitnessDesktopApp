package Controllers;

import Implements.ClientStorage;
import Implements.StatusStorage;
import com.BindingModels.ClientBindingModel;
import com.BusinessLogic.ClientLogic;
import com.BusinessLogic.StatusLogic;
import com.ViewModels.StatusViewModel;
import com.example.fitnessview.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Data;

import java.sql.Date;
@Data
public class ClientController {

    private Integer id;

    private boolean modalResult;

    private final ClientLogic _clientLogic;

    private final StatusLogic _statusLogic;

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldSurname;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXToggleButton toggleButton;
    @FXML
    private ComboBox<StatusViewModel> comboBox;
    @FXML
    private TextField textFieldPhone;
    @FXML
    private TextField textFieldLogin;
    @FXML
    private TextField textFieldPassword;

    public ClientController() {
        this._clientLogic = new ClientLogic(new ClientStorage());
        this._statusLogic = new StatusLogic(new StatusStorage());
        modalResult = false;
    }

    @FXML
    private void initialize() {
        try {
            var statusesList = _statusLogic.read(null);
            if (statusesList != null) {
                comboBox.setItems(FXCollections.observableArrayList(statusesList));
                comboBox.setCellFactory(param -> new ListCell<>() {
                    @Override
                    protected void updateItem(StatusViewModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.name == null) {
                            setText(null);
                        } else {
                            setText(item.name);
                        }
                    }
                });
                comboBox.setConverter(new StringConverter<>() {
                    @Override
                    public String toString(StatusViewModel item) {
                        if (item == null) {
                            return null;
                        } else {
                            return item.name;
                        }
                    }

                    @Override
                    public StatusViewModel fromString(String string) {
                        return null;
                    }
                });
            }
        } catch (Exception ex) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
            alert.showAndWait();
        }
    }

    public void initData() {
        if (id != null) {
            try {
                var model = new ClientBindingModel();
                model.id = id;
                var view = _clientLogic.read(model).get(0);
                if (view != null) {
                    textFieldName.setText(view.name);
                    textFieldSurname.setText(view.surname);
                    datePicker.setValue(view.birthdate.toLocalDate());
                    toggleButton.setSelected(view.testRequest);
                    comboBox.setValue(comboBoxValue(view.statusId));
                    textFieldPhone.setText(view.phone);
                    textFieldLogin.setText(view.login);
                    textFieldPassword.setText(view.password);
                }
            } catch (Exception ex) {
                Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void onSaveClick(ActionEvent actionEvent) {
        if (checkFields()) {
            try {
                ClientBindingModel model = new ClientBindingModel();
                model.id = id;
                model.name = textFieldName.getText();
                model.surname = textFieldSurname.getText();
                model.birthdate = Date.valueOf(datePicker.getValue());
                model.testRequest = toggleButton.isSelected();
                model.statusId = comboBox.getSelectionModel().getSelectedItem().id;
                model.phone = textFieldPhone.getText();
                model.login = textFieldLogin.getText();
                model.password = textFieldPassword.getText();
                _clientLogic.createOrUpdate(model);

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

    public void onCancelClick(ActionEvent actionEvent) {
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
        if (datePicker.getValue() == null) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните дату рождения!");
            alert.showAndWait();
            return false;
        }
        if (comboBox.getValue() == null) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Выберите статус!");
            alert.showAndWait();
            return false;
        }
        if (textFieldPhone.getText().length() == 0) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните номер телефона!");
            alert.showAndWait();
            return false;
        }
        if (textFieldLogin.getText().length() == 0) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните логин!");
            alert.showAndWait();
            return false;
        }
        if (textFieldPassword.getText().length() == 0) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните пароль!");
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

    private StatusViewModel comboBoxValue(int value) {
        for (var item : comboBox.getItems())
            if (item.id == value)
                return item;
        return null;
    }
}
