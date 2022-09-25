package Controllers;

import Implements.TrainerStorage;
import com.BindingModels.TrainerBindingModel;
import com.BusinessLogic.TrainerLogic;
import com.ViewModels.TrainerViewModel;
import com.example.fitnessview.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.postgresql.util.PGInterval;

import java.io.IOException;
import java.util.Optional;

public class TrainersController {

    private final TrainerLogic logic;
    @FXML
    private TableView<TrainerViewModel> tableView;
    @FXML
    private TableColumn columnSurname;
    @FXML
    private TableColumn columnExperience;
    @FXML
    private TableColumn columnPhone;
    @FXML
    private TableColumn columnName;

    public TrainersController() {
        logic = new TrainerLogic(new TrainerStorage());
    }

    @FXML
    private void initialize() {
        loadData();
    }

    private void loadData() {
        try {
            var list = logic.read(null);
            for (var trainer : list) {
                trainer.experienceStr = makeExperience(trainer.experience);
            }
            if (list != null) {
                columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
                columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
                columnExperience.setCellValueFactory(new PropertyValueFactory<>("experienceStr"));
                columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                tableView.setItems(FXCollections.observableArrayList(list));
            }
        } catch (Exception ex) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
            alert.showAndWait();
        }
    }

    private String makeExperience(PGInterval interval) {
        int years = interval.getYears();
        String str = "";
        if (years > 0) {
            str += years;
            if (years == 1)
                str += " год";
            else if (years <= 4)
                str += " года";
            else str += " лет";
        }
        int months = interval.getMonths();
        if (months > 0) {
            str += " " + months;
            if (months == 1)
                str += " месяц";
            else if (months <= 4)
                str += " месяца";
            else str += " месяцев";
        }
        return str;
    }

    @FXML
    private void onAddClick() throws IOException {
        var pair = newStage("trainer-view.fxml", "Тренер");
        Stage stage = pair.getKey();
        TrainerController controller = (TrainerController) pair.getValue();
        stage.showAndWait();
        if (controller.isModalResult())
            loadData();
    }

    @FXML
    private void onEditClick() throws IOException {
        var selectedModel = tableView.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            var pair = newStage("trainer-view.fxml", "Тренер");
            Stage stage = pair.getKey();
            TrainerController controller = (TrainerController) pair.getValue();
            controller.setId(selectedModel.id);
            controller.initData();
            stage.showAndWait();
            if (controller.isModalResult())
                loadData();
        }
    }

    @FXML
    private void onDelClick() {
        var selectedModel = tableView.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            Alert alert = alert(Alert.AlertType.CONFIRMATION, "Вопрос", "Удалить запись?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                int id = selectedModel.id;
                try {
                    TrainerBindingModel model = new TrainerBindingModel();
                    model.id = id;
                    logic.delete(model);
                } catch (Exception ex) {
                    Alert newAlert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
                    newAlert.showAndWait();
                }
                loadData();
            }
        }
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

    private Pair<Stage, Object> newStage(String resourceName, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(resourceName));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/img/icon32.png")));
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.tableView.getScene().getWindow());
        TrainerController controller = fxmlLoader.getController();
        return new Pair<>(stage, controller);
    }
}
