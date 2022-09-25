package Controllers;

import com.example.fitnessview.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Button button;

    @FXML
    private void onStatusesClick() throws IOException {
        Stage stage = newStage("statuses-view.fxml", "Статусы");
        stage.showAndWait();
    }

    @FXML
    private void onTrainersClick() throws IOException {
        Stage stage = newStage("trainers-view.fxml", "Тренеры");
        stage.showAndWait();
    }

    @FXML
    private void onClassesClick() throws IOException {
        Stage stage = newStage("classes-view.fxml", "Групповые занятия");
        stage.showAndWait();
    }

    @FXML
    private void onClientsClick() throws IOException {
        Stage stage = newStage("clients-view.fxml", "Клиенты");
        stage.showAndWait();
    }

    private Stage newStage(String resourceName, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(resourceName));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/img/icon32.png")));
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.button.getScene().getWindow());
        return stage;
    }

}
