package ua.pp.disik.englishroulette.desktop.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//@Component
public class FxApplicationRunner extends Application implements ApplicationRunner {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                FxApplicationRunner.class.getResource("SearchController.fxml")
        );
        AnchorPane page = loader.load();
        Scene scene = new Scene(page);

        primaryStage.setTitle("Title goes here");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        launch(args.getSourceArgs());
    }
}
