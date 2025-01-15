package com.dnd.backend.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SessionDTO(
    Long id,
    LocalDate sessionDate,
    String notes,
    CampaignDTO campaign
) {}
