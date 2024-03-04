package TEST;

import Controllers.AjouterUtilisateurController;
import Controllers.EditerleprofilController;
import Controllers.SeconnecterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Seconnecter.fxml"));


        Parent root = loader.load();
        SeconnecterController controller = loader.getController();

        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome to ESPRIT MARKET");
        primaryStage.show();


    }

}
