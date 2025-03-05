package ua.pp.disik.englishroulette.desktop.fx.entity;

import lombok.Data;
import org.springframework.stereotype.Component;
import ua.pp.disik.englishroulette.desktop.lesson.Lesson;

@Component
@Data
public class CurrentLesson {
    private Lesson lesson;
}
