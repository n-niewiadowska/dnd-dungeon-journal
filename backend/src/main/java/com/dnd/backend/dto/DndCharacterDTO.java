package com.dnd.backend.dto;

import com.dnd.backend.constant.CharacterClass;
import com.dnd.backend.constant.CharacterRace;

public record DndCharacterDTO(
    String firstName,
    String lastName,
    CharacterClass dndClass,
    CharacterRace race,
    int age,
    boolean canPerformMagic
) {}
