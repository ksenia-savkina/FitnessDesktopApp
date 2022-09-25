package Controllers;

import com.example.fitnessview.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Data;

import java.util.Map;
import java.util.TreeMap;
@Data
public class ClassClientsController {
    private TreeMap<Integer, Pair<String, String>> classClients;
    @FXML
    private TableView<Map.Entry<Integer, Pair<String, String>>> tableView;
    @FXML
    private TableColumn<Map.Entry<Integer, Pair<String, String>>, String> columnFIO;
    @FXML
    private TableColumn<Map.Entry<Integer, Pair<String, String>>, String> columnPhone;

    public void initData() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try {
            if (classClients != null) {
                tableView.getItems().addAll(classClients.entrySet());
                columnFIO.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getValue().getKey()));
                columnPhone.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getValue().getValue()));
            }
        } catch (Exception ex) {
            Alert alert = alert(Alert.AlertType.ERROR, "Ошибка", ex.getMessage());
            alert.showAndWait();
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
}
