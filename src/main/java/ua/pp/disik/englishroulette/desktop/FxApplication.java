package ua.pp.disik.englishroulette.desktop;

import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ua.pp.disik.englishroulette.desktop.fx.ApplicationContextFXMLLoader;
import ua.pp.disik.englishroulette.desktop.fx.EnglishRouletteController;

@SpringBootApplication
public class FxApplication extends Application {
    ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = SpringApplication.run(FxApplication.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ApplicationContextFXMLLoader viewLoader = new ApplicationContextFXMLLoader(
                EnglishRouletteController.class.getResource("EnglishRouletteView.fxml"),
                applicationContext
        );
        BorderPane englishRouletteView = viewLoader.load();

        Scene scene = new Scene(englishRouletteView);

        stage.setScene(scene);
        stage.setWidth(830);
        stage.setHeight(600);
        stage.setTitle("English Roulette");
        stage.show();
    }
}
