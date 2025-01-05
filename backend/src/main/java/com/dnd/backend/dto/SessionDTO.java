package com.dnd.backend.dto;

import java.time.LocalDate;

public record SessionDTO(
    Long id,
    LocalDate sessionDate,
    String notes,
    CampaignDTO campaign
) {}
