package ua.pp.disik.englishroulette.desktop.entity;

import jakarta.annotation.Nullable;
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

    @Nullable
    private Long checkedAt;

    private long updatedAt;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    }, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Phrase> foreignPhrases;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    }, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Phrase> nativePhrases;

    public Exercise(int readingCount, int memoryCount, int priority) {
        this.readingCount = readingCount;
        this.memoryCount = memoryCount;
        this.priority = priority;
        this.updatedAt = System.currentTimeMillis();
        this.foreignPhrases = new ArrayList<>();
        this.nativePhrases = new ArrayList<>();
    }
}
