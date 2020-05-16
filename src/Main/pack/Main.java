package Main.pack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI/Gui.fxml"));
        primaryStage.setTitle("Max-Min Wrapper");
        primaryStage.setScene(new Scene(root, 927, 570));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
