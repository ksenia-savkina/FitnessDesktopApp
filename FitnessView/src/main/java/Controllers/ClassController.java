package Controllers;

import Implements.ClassStorage;
import Implements.StatusStorage;
import Implements.TrainerStorage;
import com.BindingModels.ClassBindingModel;
import com.BusinessLogic.ClassLogic;
import com.BusinessLogic.StatusLogic;
import com.BusinessLogic.TrainerLogic;
import com.ViewModels.StatusViewModel;
import com.ViewModels.TrainerViewModel;
import com.example.fitnessview.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.TreeMap;
@Data
public class ClassController {


    private Integer id;

    private boolean modalResult;

    private final ClassLogic _classLogic;

    private final TrainerLogic _trainerLogic;

    private final StatusLogic _statusLogic;

    @FXML
    private TextField textFieldName;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker timePicker;
    @FXML
    private ComboBox<TrainerViewModel> comboBox;
    @FXML
    private ListView<StatusViewModel> listView;

    public ClassController() {
        modalResult = false;
        this._classLogic = new ClassLogic(new ClassStorage());
        this._trainerLogic = new TrainerLogic(new TrainerStorage());
        this._statusLogic = new StatusLogic(new StatusStorage());
    }

    @FXML
    private void initialize() {
        try {
            var trainersList = _trainerLogic.read(null);
            var statusesList = _statusLogic.read(null);
            if (trainersList != null) {
                comboBox.setItems(FXCollections.observableArrayList(trainersList));
                comboBox.setCellFactory(param -> new ListCell<TrainerViewModel>() {
                    @Override
                    protected void updateItem(TrainerViewModel item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null || item.name == null) {
                            setText(null);
                        } else {
                            setText(item.surname + " " + item.name);
                        }
                    }
                });
                comboBox.setConverter(new StringConverter<TrainerViewModel>() {
                    @Override
                    public String toString(TrainerViewModel trainer) {
                        if (trainer == null) {
                            return null;
                        } else {
                            return trainer.surname + " " + trainer.name;
                        }
                    }

                    @Override
                    public TrainerViewModel fromString(String string) {
                        return null;
                    }
                });
            }
            if (statusesList != null) {
                listView.setItems(FXCollections.observableArrayList(statusesList));
                listView.setCellFactory(param -> new ListCell<StatusViewModel>() {
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
                listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            }

        } catch (Exception ex) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
            alert.showAndWait();
        }
    }

    public void initData() {
        if (id != null) {
            try {
                var model = new ClassBindingModel();
                model.id = id;
                var view = _classLogic.read(model).get(0);

                if (view != null) {
                    textFieldName.setText(view.name);
                    datePicker.setValue(view.date.toLocalDateTime().toLocalDate());
                    timePicker.setValue(view.date.toLocalDateTime().toLocalTime());
                    comboBox.setValue(comboBoxValue(view.trainerId));
                    for (var status : view.classStatuses.keySet())
                        listView.getSelectionModel().select(listViewValues(status));
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
                ClassBindingModel model = new ClassBindingModel();
                model.id = id;
                model.name = textFieldName.getText();
                var date = datePicker.getValue();
                var time = timePicker.getValue();
                model.date = Timestamp.valueOf(LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getHour(), time.getMinute(), time.getSecond()));
                model.trainerId = comboBox.getSelectionModel().getSelectedItem().id;
                var map = new TreeMap<Integer, String>();
                for (var status : listView.getSelectionModel().getSelectedItems())
                    map.put(status.id, status.name);
                model.classStatuses = map;
                _classLogic.createOrUpdate(model);

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
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Заполните название!");
            alert.showAndWait();
            return false;
        }
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
        if (comboBox.getValue() == null) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Выберите тренера!");
            alert.showAndWait();
            return false;
        }
        if (listView.getSelectionModel().getSelectedItems().size() == 0) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", "Выберите статусы!");
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

    private TrainerViewModel comboBoxValue(int value) {
        for (var item : comboBox.getItems())
            if (item.id == value)
                return item;
        return null;
    }

    private StatusViewModel listViewValues(int value) {
        for (var item : listView.getItems())
            if (item.id == value)
                return item;
        return null;
    }
}