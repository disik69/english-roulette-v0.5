package ua.pp.disik.englishroulette.desktop.fx.sandbox;

import javafx.application.Application;
import javafx.stage.Stage;


public class FxApplicationLifecycle extends Application {
    public FxApplicationLifecycle() {
        System.out.println("FxSandboxRunner() constructor: " + Thread.currentThread().getName());
    }

    @Override
    public void init() throws Exception {
        System.out.println("init() method: " + Thread.currentThread().getName());
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("start() method: " + Thread.currentThread().getName());

        stage.setTitle("Sandbox");
        stage.show();
    }

	public static void main(String[] args) throws Exception {
		Application.launch(args);
	}

    @Override
    public void stop() throws Exception {
        System.out.println("stop() method: " + Thread.currentThread().getName());
    }
}
