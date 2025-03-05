package ua.pp.disik.englishroulette.desktop.freetts;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.springframework.stereotype.Component;

@Component
public class FreeTTSSpeaker {
    public void speak(String text) {
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice kevin = voiceManager.getVoice("kevin16");
        kevin.allocate();
        kevin.speak(text);
        kevin.deallocate();
    }
}
