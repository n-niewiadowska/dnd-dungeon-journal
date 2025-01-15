package com.dnd.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record MainItemDTO(
        Long id,
        @NotNull
        @Length(min = 3, max = 30)
        String name,
        String description,
        int bonusValue,
        @JsonIgnore MainSkillDTO associatedSkill
) {}
