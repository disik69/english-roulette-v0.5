package ua.pp.disik.englishroulette.desktop.fx.stage;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import ua.pp.disik.englishroulette.desktop.fx.ApplicationContextFXMLLoader;
import ua.pp.disik.englishroulette.desktop.fx.Constants;
import ua.pp.disik.englishroulette.desktop.fx.WritingCheckLessonController;

public class WritingCheckLessonStage extends Stage {
    @SneakyThrows
    public WritingCheckLessonStage(
            ApplicationContext applicationContext,
            Node root
    ) {
        ApplicationContextFXMLLoader viewLoader = new ApplicationContextFXMLLoader(
                WritingCheckLessonController.class.getResource("WritingCheckLessonView.fxml"),
                applicationContext
        );
        GridPane lessonView = viewLoader.load();

        Scene scene = new Scene(lessonView);
        scene.getStylesheets().add(
                WritingCheckLessonController.class.getResource("writing-check-lesson-controller.css").toExternalForm()
        );
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown()) {
                switch (event.getCode()) {
                    case KeyCode.T -> {
                        ((WritingCheckLessonController) viewLoader.getController()).handleTurn(null);
                    }
                    case KeyCode.N -> {
                        ((WritingCheckLessonController) viewLoader.getController()).handleNext(null);
                    }
                }
            }
        });
        this.setScene(scene);

        this.setOnHidden(event -> {
            ((WritingCheckLessonController) viewLoader.getController()).close();
        });

        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(root.getScene().getWindow());
        this.setWidth(root.getScene().getWindow().getWidth());
        this.setHeight(root.getScene().getWindow().getHeight());
        this.setX(root.getScene().getWindow().getX() + Constants.WINDOW_OFFSET);
        this.setY(root.getScene().getWindow().getY() + Constants.WINDOW_OFFSET);
        this.setTitle("Writing Check Lesson");
    }
}
