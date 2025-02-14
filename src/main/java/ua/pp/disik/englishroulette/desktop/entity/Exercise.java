package ua.pp.disik.englishroulette.desktop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int readingCount;
    private int memoryCount;
    private int priority;
    private long checkedAt;
    private long updatedAt;

    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Phrase> foreignPhrases;

    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Phrase> nativePhrases;

    public Exercise(int readingCount, int memoryCount, int priority) {
        this.readingCount = readingCount;
        this.memoryCount = memoryCount;
        this.checkedAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.nativePhrases = new ArrayList<>();
        this.foreignPhrases = new ArrayList<>();
    }
}
