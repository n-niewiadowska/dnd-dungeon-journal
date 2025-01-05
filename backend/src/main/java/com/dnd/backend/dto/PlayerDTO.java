package com.dnd.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record PlayerDTO(
    Long id,
    String username,
    String email,
    @JsonIgnore String password,
    DndCharacterDTO character
) {}