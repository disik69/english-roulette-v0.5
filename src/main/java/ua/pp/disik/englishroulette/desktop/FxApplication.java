package ua.pp.disik.englishroulette.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ua.pp.disik.englishroulette.desktop.fx.EnglishRoulettePresenter;

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
                EnglishRoulettePresenter.class.getResource("EnglishRouletteView.fxml")
        );
        viewLoader.setControllerFactory(clazz -> applicationContext.getBean(clazz));
        BorderPane englishRouletteView = viewLoader.load();

        Scene scene = new Scene(englishRouletteView);

        stage.setScene(scene);
        stage.setWidth(830);
        stage.setHeight(600);
        stage.setTitle("English Roulette");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
