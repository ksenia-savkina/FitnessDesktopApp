package Controllers;

import Implements.ClientStorage;
import com.BindingModels.ClientBindingModel;
import com.BusinessLogic.ClientLogic;
import com.ViewModels.ClientViewModel;
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

import java.io.IOException;
import java.util.Optional;

public class ClientsController {

    private final ClientLogic logic;

    @FXML
    private TableView<ClientViewModel> tableView;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnSurname;
    @FXML
    private TableColumn columnBirthdate;
    @FXML
    private TableColumn columnTestRequest;
    @FXML
    private TableColumn columnStatus;
    @FXML
    private TableColumn columnPhone;
    @FXML
    private TableColumn columnLogin;
    @FXML
    private TableColumn columnPassword;

    public ClientsController() {
        logic = new ClientLogic(new ClientStorage());
    }

    @FXML
    private void initialize() {
        loadData();
    }

    private void loadData() {
        try {
            var list = logic.read(null);
            for (var client : list) {
                if (client.testRequest)
                    client.testRequestStr = "есть";
                else
                    client.testRequestStr = "нет";
            }
            if (list != null) {
                columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
                columnSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
                columnBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
                columnTestRequest.setCellValueFactory(new PropertyValueFactory<>("testRequestStr"));
                columnStatus.setCellValueFactory(new PropertyValueFactory<>("statusName"));
                columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
                columnLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
                columnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                tableView.setItems(FXCollections.observableArrayList(list));
            }
        } catch (Exception ex) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onAddClick() throws IOException {
        var pair = newStage("client-view.fxml", "Клиент");
        Stage stage = pair.getKey();
        ClientController controller = (ClientController) pair.getValue();
        stage.showAndWait();
        if (controller.isModalResult())
            loadData();
    }

    @FXML
    private void onEditClick() throws IOException {
        var selectedModel = tableView.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            var pair = newStage("client-view.fxml", "Клиент");
            Stage stage = pair.getKey();
            ClientController controller = (ClientController) pair.getValue();
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
                    ClientBindingModel clientBindingModel = new ClientBindingModel();
                    clientBindingModel.id = id;
                    logic.delete(clientBindingModel);
                } catch (Exception ex) {
                    Alert newAlert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
                    newAlert.showAndWait();
                }
                loadData();
            }
        }
    }

    @FXML
    private void onResultsClick() throws IOException {
        var selectedModel = tableView.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            var pair = newStage("results-view.fxml", "Результаты теста клиента с ФИО: " + selectedModel.surname + " " + selectedModel.name);
            Stage stage = pair.getKey();
            ResultsController controller = (ResultsController) pair.getValue();
            controller.setId(selectedModel.id);
            controller.initData();
            stage.showAndWait();
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
        Object controller = fxmlLoader.getController();
        return new Pair<>(stage, controller);
    }
}

