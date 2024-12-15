package com.dnd.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record PlayerDTO(
    String username,
    String email,
    @JsonIgnore String password,
    Character character
) {}