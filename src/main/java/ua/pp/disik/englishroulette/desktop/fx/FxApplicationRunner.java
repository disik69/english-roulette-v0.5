package ua.pp.disik.englishroulette.desktop.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FxApplicationRunner extends Application implements ApplicationRunner {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader layoutLoader = new FXMLLoader(
                FxApplicationRunner.class.getResource("LayoutController.fxml")
        );
        AnchorPane layout = layoutLoader.load();

        Scene scene = new Scene(layout);

        primaryStage.setTitle("English Roulette");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        launch(args.getSourceArgs());
    }
}
