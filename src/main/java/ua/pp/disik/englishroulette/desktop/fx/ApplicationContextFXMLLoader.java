package ua.pp.disik.englishroulette.desktop.fx;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;

import java.net.URL;

public class ApplicationContextFXMLLoader extends FXMLLoader {
    public ApplicationContextFXMLLoader(
            URL location,
            ApplicationContext applicationContext
    ) {
        super(location);

        this.setControllerFactory(clazz -> applicationContext.getBean(clazz));
    }
}
