package com.dnd.backend.domain;

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
public class MainItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String description;
    private int bonusValue;

    @OneToOne(mappedBy = "associatedItem", cascade = CascadeType.MERGE)
    private MainSkill associatedSkill;

    public MainItem(String name, String description, int bonusValue, MainSkill associatedSkill) {
        this.name = name;
        this.description = description;
        this.bonusValue = bonusValue;
        this.associatedSkill = associatedSkill;
    }
}
