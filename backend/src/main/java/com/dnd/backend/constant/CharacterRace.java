package com.dnd.backend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CharacterRace {
    DRAGONBORN("Dragonborn", "Proud, draconic humanoids with breath weapons."),
    DWARF("Dwarf", "Stout, resilient, and skilled in craftsmanship."),
    ELF("Elf", "Graceful, long-lived beings attuned to magic and nature."),
    GNOME("Gnome", "Curious and inventive, with a knack for trickery."),
    HALFLING("Halfling", "Cheerful and nimble, thriving on luck and bravery."),
    HUMAN("Human", "Versatile and ambitious, adaptable to any role."),
    ORC("Orc", "Strong, fierce warriors with a tribal heritage."),
    TIEFLING("Tiefling", "Fiendish heritage with infernal powers and charm.");

    private final String race;
    private final String description;
}
