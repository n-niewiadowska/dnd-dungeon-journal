package com.dnd.backend.domain;

import com.dnd.backend.constant.Ability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
public class MainSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private Ability relatedAbility;
    private String description;
    private int level;
    private String effect;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private MainItem associatedItem;

    public MainSkill(String name, Ability relatedAbility, String description, int level, String effect, MainItem associatedItem) {
        this.name = name;
        this.relatedAbility = relatedAbility;
        this.description = description;
        this.level = level;
        this.effect = effect;
        this.associatedItem = associatedItem;
    }
}
