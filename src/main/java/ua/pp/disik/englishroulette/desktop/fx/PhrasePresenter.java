package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.service.ReversoContextService;

@Component
public class PhrasePresenter {
    @Autowired
    private ReversoContextService reversoContextService;

    private String translationDirection = ReversoContextService.EN_RU_TRANSLATION;

    @FXML
    private GridPane main;

    @FXML
    private TextField phraseText;

    @FXML
    private RadioButton fnRadio;

    @FXML
    private RadioButton nfRadio;

    @FXML
    private WebView translationWeb;

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        group.getToggles().setAll(fnRadio, nfRadio);
        group.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                    switch (((Labeled) newValue).getText()) {
                        case "F->N":
                            translationDirection = ReversoContextService.EN_RU_TRANSLATION;
                            break;
                        case "N->F":
                            translationDirection = ReversoContextService.RU_EN_TRANSLATION;
                            break;
                    }
                }
        );
    }

    public void handleSearch(ActionEvent event) {
        String phrase = phraseText.getText();
        if (phrase.length() > 1) {
            ReversoContextService.SimpleReadTranslation simpleTranslation =
                    reversoContextService.translateSimple(translationDirection, phrase);

            StringBuilder content = new StringBuilder();
            if ( ! simpleTranslation.getError() && simpleTranslation.getSuccess()) {
                for (
                        ReversoContextService.SimpleReadTranslation.Source.Translation translation :
                        simpleTranslation.getSources().getFirst().getTranslations()
                ) {
                    content.append("<p><b>");
                    content.append(translation.getTranslation());
                    content.append("</b></p>");

                    for (
                        ReversoContextService.SimpleReadTranslation.Source.Translation.Context context :
                        translation.getContexts()
                    ) {
                        content.append("<p>");
                        content.append(context.getSource());
                        content.append("</p>");

                        content.append("<p>");
                        content.append(context.getTarget());
                        content.append("</p><hr/>");
                    }
                }
            }

            translationWeb.getEngine().loadContent(content.toString());
        }
    }

    public void handleOK(ActionEvent event) {
        main.getScene().getWindow().hide();
    }
}
