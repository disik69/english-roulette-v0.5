package ua.pp.disik.englishroulette.desktop.init;

import org.springframework.boot.CommandLineRunner;

public abstract class Creator {
    public void run(String... args) {
        boolean runnable = Boolean.parseBoolean(System.getProperty("creators"));
        if (runnable) {
            create();
        }
    }

    public abstract void create();
}
