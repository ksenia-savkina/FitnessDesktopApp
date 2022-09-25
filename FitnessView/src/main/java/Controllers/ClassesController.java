package Controllers;

import Implements.ClassStorage;
import com.BindingModels.ClassBindingModel;
import com.BusinessLogic.ClassLogic;
import com.ViewModels.ClassViewModel;
import com.example.fitnessview.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ClassesController {

    private int rowsPerPage = 1;

    private final ClassLogic logic;
    @FXML
    private Pagination pagination;

    @FXML
    private TableView<ClassViewModel> tableView;

    @FXML
    private TableColumn columnName;

    @FXML
    private TableColumn columnDate;

    @FXML
    private TableColumn columnTrainer;

    public ClassesController() {
        logic = new ClassLogic(new ClassStorage());
    }

    private List<ClassViewModel> list;

    @FXML
    private void initialize() {
        loadData();
    }

    private void loadData() {
        try {
            list = logic.read(null);

            if (list != null) {
                columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
                columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                columnTrainer.setCellValueFactory(new PropertyValueFactory<>("trainerFIO"));
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                tableView.setItems(FXCollections.observableArrayList(list));
            }

//            pagination.setPageCount(list.size());
//            pagination = new Pagination((list.size() / rowsPerPage + 1), 0);
//            pagination.setPageFactory(this::createPage);

        } catch (Exception ex) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
            alert.showAndWait();
        }

    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, list.size());
        tableView.setItems(FXCollections.observableArrayList(list.subList(fromIndex, toIndex)));
        return new BorderPane(tableView);
    }

    @FXML
    private void onAddClick() throws IOException {
        var pair = newStage("class-view.fxml", "Групповое занятие");
        Stage stage = pair.getKey();
        ClassController controller = (ClassController) pair.getValue();
        stage.showAndWait();
        if (controller.isModalResult())
            loadData();
    }

    @FXML
    private void onEditClick() throws IOException {
        var selectedModel = tableView.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            var pair = newStage("class-view.fxml", "Групповое занятие");
            Stage stage = pair.getKey();
            ClassController controller = (ClassController) pair.getValue();
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
                    ClassBindingModel model = new ClassBindingModel();
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

    @FXML
    private void onClientsClick() throws IOException {
        var selectedModel = tableView.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            var pair = newStage("class-clients-view.fxml", "Клиенты занятия");
            Stage stage = pair.getKey();
            ClassClientsController controller = (ClassClientsController) pair.getValue();
            controller.setClassClients(selectedModel.classClients);
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


    public void onLoadClick(ActionEvent actionEvent) {
    }
}
