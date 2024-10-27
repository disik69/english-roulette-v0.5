package ua.pp.disik.englishroulette.desktop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Setting {
    @Id
    @Enumerated(EnumType.STRING)
    private SettingName name;

    @NotNull
    private String value;
}
