package ua.pp.disik.englishroulette.desktop.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.service.ReversoContextService;

@Component
public class PhrasePresenter {
    @Autowired
    private ReversoContextService reversoContextService;

    @FXML
    private GridPane main;

    @FXML
    private TextField phraseText;

    @FXML
    private WebView translationWeb;

    @FXML
    private void initialize() {
        ReversoContextService.SimpleReadTranslation translation =
                reversoContextService.translateSimple("en-ru", "hand");

        String content = "";
//        if (translation.getSuccess()) {
//            content = translation.getSources().getFirst().getTranslations().stream()
//                    .map(t -> t.)
//        }

        translationWeb.getEngine().loadContent(content);
    }

    public void handleSearch(ActionEvent event) {

    }

    public void handleOK(ActionEvent event) {
        main.getScene().getWindow().hide();
    }
}
