package com.dnd.backend.dto;

import com.dnd.backend.constant.CharacterClass;
import com.dnd.backend.constant.CharacterRace;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DndCharacterDTO(
    Long id,
    @NotNull(message = "First name is required")
    String firstName,
    @NotNull(message = "Last name is required")
    String lastName,
    CharacterClass dndClass,
    CharacterRace race,
    @Min(15)
    int age,
    boolean canPerformMagic,
    MainSkillDTO skill
) {}
