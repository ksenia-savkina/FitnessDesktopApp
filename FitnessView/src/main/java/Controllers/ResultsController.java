package Controllers;

import Implements.TestResultStorage;
import com.BindingModels.TestResultBindingModel;
import com.BusinessLogic.TestResultLogic;
import com.ViewModels.TestResultViewModel;
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
import lombok.Data;

import java.io.IOException;
import java.util.Optional;

@Data
public class ResultsController {
    private Integer id;

    private final TestResultLogic logic;
    @FXML
    private TableView<TestResultViewModel> tableView;
    @FXML
    private TableColumn columnResult;
    @FXML
    private TableColumn columnDate;

    public ResultsController() {
        logic = new TestResultLogic(new TestResultStorage());
    }

    public void initData() {
        if (id != null) {
            try {
                var model = new TestResultBindingModel();
                model.clientId = id;
                var list = logic.read(model);
                for (var result : list) {
                    if (result.result)
                        result.resultStr = "Статус повышен";
                    else
                        result.resultStr = "Статус не изменился";
                }
                if (list != null) {
                    columnResult.setCellValueFactory(new PropertyValueFactory<>("resultStr"));
                    columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    tableView.setItems(FXCollections.observableArrayList(list));
                }
            } catch (Exception ex) {
                Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onAddClick() throws IOException {
        var pair = newStage("result-view.fxml", "Результат");
        Stage stage = pair.getKey();
        ResultController controller = (ResultController) pair.getValue();
        controller.setClientId(id);
        stage.showAndWait();
        if (controller.isModalResult())
            initData();
    }

    @FXML
    private void onEditClick() throws IOException {
        var selectedModel = tableView.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            var pair = newStage("result-view.fxml", "Результат");
            Stage stage = pair.getKey();
            ResultController controller = (ResultController) pair.getValue();
            controller.setClientId(id);
            controller.setId(selectedModel.id);
            controller.initData();
            stage.showAndWait();
            if (controller.isModalResult())
                initData();
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
                    TestResultBindingModel testResultBindingModel = new TestResultBindingModel();
                    testResultBindingModel.id = id;
                    logic.delete(testResultBindingModel);
                } catch (Exception ex) {
                    Alert newAlert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
                    newAlert.showAndWait();
                }
                initData();
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
        ResultController controller = fxmlLoader.getController();
        return new Pair<>(stage, controller);
    }
}
