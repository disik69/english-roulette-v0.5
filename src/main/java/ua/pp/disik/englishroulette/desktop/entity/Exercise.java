package ua.pp.disik.englishroulette.desktop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int readingCount;
    private int memoryCount;
    private long checkedAt;
    private long updatedAt;

    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Word> foreignWords;

    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Word> nativeWords;

    public Exercise() {
        this.readingCount = 7;
        this.memoryCount = 7;
        this.checkedAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.nativeWords = new ArrayList<>();
        this.foreignWords = new ArrayList<>();
    }
}
