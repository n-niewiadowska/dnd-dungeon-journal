package com.dnd.backend.domain;

import com.dnd.backend.constant.CharacterClass;
import com.dnd.backend.constant.CharacterRace;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
public class DndCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Enumerated(EnumType.STRING)
    private CharacterClass dndClass;

    @Enumerated(EnumType.STRING)
    private CharacterRace race;

    @Min(14)
    private int age;
    private boolean canPerformMagic;

    public DndCharacter(String firstName, String lastName, CharacterClass dndClass, CharacterRace race, int age, boolean canPerformMagic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dndClass = dndClass;
        this.race = race;
        this.age = age;
        this.canPerformMagic = canPerformMagic;
    }
}
