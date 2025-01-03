package com.dnd.backend.dto;

import com.dnd.backend.domain.DndCharacter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record PlayerDTO(
    Long id,
    String username,
    String email,
    @JsonIgnore String password,
    DndCharacterDTO character
) {}