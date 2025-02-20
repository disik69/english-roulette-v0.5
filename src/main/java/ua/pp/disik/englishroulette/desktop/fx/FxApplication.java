package ua.pp.disik.englishroulette.desktop.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.pp.disik.englishroulette.desktop.init.SettingCreator;
import ua.pp.disik.englishroulette.desktop.init.SeedCreator;

public class FxApplication extends Application {
    ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new AnnotationConfigApplicationContext("ua.pp.disik.englishroulette.desktop");
        applicationContext.registerShutdownHook();

        applicationContext.getBean(SettingCreator.class).run();
        applicationContext.getBean(SeedCreator.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader viewLoader = new FXMLLoader(
                FxApplication.class.getResource("EnglishRouletteView.fxml")
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
