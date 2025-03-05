package ua.pp.disik.englishroulette.desktop.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

//@Component
public class FxApplicationRunner extends Application implements ApplicationRunner {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader viewLoader = new FXMLLoader(
                FxApplicationRunner.class.getResource("EnglishRouletteView.fxml")
        );
        VBox englishRoulette = viewLoader.load();

        Scene scene = new Scene(englishRoulette);

        stage.setScene(scene);
        stage.setWidth(830);
        stage.setHeight(600);
        stage.setTitle("English Roulette");
        stage.show();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        launch(args.getSourceArgs());
    }
}
