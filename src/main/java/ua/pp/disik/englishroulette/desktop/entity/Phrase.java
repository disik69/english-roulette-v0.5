package ua.pp.disik.englishroulette.desktop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(
        indexes = {
                @Index(columnList = "body", unique = true)
        }
)
@Data
@NoArgsConstructor
public class Phrase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String body;

    public Phrase(String body) {
        this.body = body;
    }
}
