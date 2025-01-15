package com.dnd.backend.domain;

import com.dnd.backend.constant.CharacterClass;
import com.dnd.backend.constant.CharacterRace;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
public class DndCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private CharacterClass dndClass;

    @Enumerated(EnumType.STRING)
    private CharacterRace race;

    private int age;
    private boolean canPerformMagic;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private MainSkill skill;

    public DndCharacter(
            String firstName, String lastName,
            CharacterClass dndClass, CharacterRace race,
            int age, boolean canPerformMagic, MainSkill skill
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dndClass = dndClass;
        this.race = race;
        this.age = age;
        this.canPerformMagic = canPerformMagic;
        this.skill = skill;
    }
}
