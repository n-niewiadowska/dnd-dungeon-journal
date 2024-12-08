package com.dnd.backend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CharacterClass {
    BARBARIAN("Barbarian", "A fierce warrior fueled by primal rage."),
    BARD("Bard", "A versatile performer who uses music and magic to inspire and control."),
    DRUID("Druid", "A nature mage with shapeshifting and elemental powers."),
    FIGHTER("Fighter", "A master of combat and weapons, adaptable to any battle."),
    MONK("Monk", "A disciplined martial artist harnessing physical and spiritual energy."),
    PALADIN("Paladin", "A holy knight devoted to justice and divine magic."),
    ROGUE("Rogue", "A stealthy and cunning adventurer excelling in precision and deception."),
    SORCERER("Sorcerer", "A spellcaster with innate magical abilities."),
    WARLOCK("Warlock", "A wielder of magic granted by a powerful patron."),
    WIZARD("Wizard", "A learned spellcaster mastering arcane knowledge.");

    private final String dndClass;
    private final String description;
}
