package com.dnd.backend.dto;

import com.dnd.backend.constant.Ability;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record MainSkillDTO(
        Long id,
        @NotNull
        @Length(min = 3, max = 30)
        String name,
        Ability relatedAbility,
        String description,
        int level,
        String effect,
        @JsonIgnore MainItemDTO associatedItem
) {}
