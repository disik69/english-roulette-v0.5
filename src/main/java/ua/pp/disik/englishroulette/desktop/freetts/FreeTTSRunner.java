package ua.pp.disik.englishroulette.desktop.freetts;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//@Component
public class FreeTTSRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice kevin = voiceManager.getVoice("kevin16");
        kevin.allocate();
        kevin.speak("1, 2, 3. Oops! Hello world! I'm listening you... Indeed I can't.");
        kevin.deallocate();
    }
}
