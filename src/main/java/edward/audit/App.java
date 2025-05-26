package edward.audit;

import edward.audit.audit.QueriesManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Audit");
        InputStream inputStream = App.class.getResourceAsStream("logo.png");

        if (inputStream == null) {
            throw new IOException("Icon file not found");
        }

        Image image = new Image(inputStream);
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        QueriesManager.main(args);
        launch();
    }
}