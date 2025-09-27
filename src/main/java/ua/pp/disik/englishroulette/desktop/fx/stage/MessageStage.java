package ua.pp.disik.englishroulette.desktop.fx.stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageStage extends Stage {
    public MessageStage(String title, String message, Node root) {
        Label scoreLabel = new Label(message);
        Button okButton = new Button("OK");
        VBox vBox = new VBox(scoreLabel, okButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(5));
        vBox.setSpacing(5);

        Scene scene = new Scene(vBox);

        okButton.setOnAction(event -> scene.getWindow().hide());

        double width = 200;
        double height = 100;

        setScene(scene);
        initModality(Modality.WINDOW_MODAL);
        initOwner(root.getScene().getWindow());
        setWidth(200);
        setHeight(100);
        setX(
                root.getScene().getWindow().getX() +
                (root.getScene().getWindow().getWidth() / 2) -
                (width / 2)
        );
        setY(
                root.getScene().getWindow().getY() +
                (root.getScene().getWindow().getHeight() / 2) -
                (height / 2)
        );
        setTitle(title);
    }
}
