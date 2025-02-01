package com.dnd.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.dnd.backend.constant.GameStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record CampaignDTO(
    Long id,
    @NotNull(message = "Title is required")
    @NotBlank(message = "Title is required")
    String title,
    String description,
    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginningDate,
    GameStatus status,
    @JsonIgnore List<DndCharacterDTO> characters
) {}
