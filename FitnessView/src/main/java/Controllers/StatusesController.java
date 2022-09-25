package Controllers;

import Implements.StatusStorage;
import com.BindingModels.StatusBindingModel;
import com.BusinessLogic.StatusLogic;
import com.ViewModels.StatusViewModel;
import com.example.fitnessview.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

public class StatusesController {

    private final StatusLogic _statusLogic;

    @FXML
    private TableView<StatusViewModel> tableView;

    @FXML
    private TableColumn<StatusViewModel, String> columnName;


    public StatusesController() {
        _statusLogic = new StatusLogic(new StatusStorage());
    }

    @FXML
    private void initialize() {
        loadData();
    }

    private void loadData() {
        try {
            var list = _statusLogic.read(null);
            if (list != null) {
                columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                tableView.setItems(FXCollections.observableArrayList(list));
            }
        } catch (Exception ex) {
            Alert alert = alert(AlertType.ERROR, "Ошибка", ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onAddClick() throws IOException {
        var pair = newStage("status-view.fxml", "Статус");
        Stage stage = pair.getKey();
        StatusController controller = (StatusController) pair.getValue();
        stage.showAndWait();
        if (controller.isModalResult())
            loadData();
    }

    @FXML
    private void onEditClick() throws IOException {
        var selectedModel = tableView.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            var pair = newStage("status-view.fxml", "Статус");
            Stage stage = pair.getKey();
            StatusController controller = (StatusController) pair.getValue();
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

            Alert alert = alert(AlertType.CONFIRMATION, "Вопрос", "Удалить запись?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                int id = selectedModel.id;
                try {
                    StatusBindingModel statusBindingModel = new StatusBindingModel();
                    statusBindingModel.id = id;
                    _statusLogic.delete(statusBindingModel);
                } catch (Exception ex) {
                    Alert newAlert = alert(AlertType.ERROR, "Ошибка", ex.getMessage());
                    newAlert.showAndWait();
                }
                loadData();
            }
        }
    }

    private Alert alert(AlertType alertType, String title, String headerText) {
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
        StatusController controller = fxmlLoader.getController();
        return new Pair<>(stage, controller);
    }
}