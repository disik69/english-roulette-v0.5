package ua.pp.disik.englishroulette.desktop.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
        stage.setWidth(600);
        stage.setHeight(400);
        stage.setTitle("English Roulette");
        stage.show();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        launch(args.getSourceArgs());
    }
}
