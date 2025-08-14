package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import ua.pp.disik.englishroulette.desktop.entity.Phrase;

import java.util.List;
import java.util.function.Function;

@Slf4j
public class PhraseVBox extends VBox {
    private List<Phrase> phrases;
    private Function<String, Phrase> phraseConverter;

    private ObservableList<StringProperty> textList = FXCollections.observableArrayList(property -> {
        return new Observable[] {property};
    });

    private VBox vBox = new VBox(5);

    public PhraseVBox(List<Phrase> phrases, Function<String, Phrase> phraseConverter) {
        this.phrases = phrases;
        this.phraseConverter = phraseConverter;

        textList.addListener(this::changeListener);

        setPadding(new Insets(5));
        setSpacing(5);

        Button additionButton = new Button("+");
        additionButton.setOnAction(event -> addHBox());
        getChildren().addAll(vBox, additionButton);

        phrases.forEach(phrase -> addHBox(phrase.getBody()));
    }

    private void addHBox(String text) {
        HBox hBox = new HBox();

        TextField inputField = new TextField(text);

        Button deletionButton = new Button("-");
        deletionButton.setOnAction(event -> removeHBox(hBox));

        hBox.getChildren().addAll(inputField, deletionButton);

        vBox.getChildren().add(hBox);

        textList.add(inputField.textProperty());
    }

    private void addHBox() {
        addHBox("");
    }

    private void removeHBox(HBox hBox) {
        int index = vBox.getChildren().indexOf(hBox);

        textList.remove(index);

        vBox.getChildren().remove(hBox);
    }

    private void changeListener(ListChangeListener.Change<? extends StringProperty> change) {
        while (change.next()) {
            int index = change.getFrom();
            if (change.wasUpdated()) {
                phrases.set(index, phraseConverter.apply(
                        textList.get(index).get()
                ));
            } else if (change.wasAdded()) {
                if ((phrases.size() - 1) < index) {
                    phrases.add(phraseConverter.apply(
                            textList.getLast().get()
                    ));
                }
            } else if (change.wasRemoved()) {
                phrases.remove(index);
            }
        }

        log.debug(phrases.toString());
    }
}
