package ua.pp.disik.englishroulette.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FxApplication extends Application {
    ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = SpringApplication.run(FxApplication.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader viewLoader = new FXMLLoader(
                FxApplication.class.getResource("fx/EnglishRouletteView.fxml")
        );
        viewLoader.setControllerFactory(clazz -> applicationContext.getBean(clazz));
        VBox englishRoulette = viewLoader.load();

        Scene scene = new Scene(englishRoulette);

        stage.setScene(scene);
        stage.setWidth(600);
        stage.setHeight(400);
        stage.setTitle("English Roulette");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
