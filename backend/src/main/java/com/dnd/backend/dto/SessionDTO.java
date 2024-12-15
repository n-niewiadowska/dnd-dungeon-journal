package com.dnd.backend.dto;

import java.time.LocalDate;

import com.dnd.backend.domain.Campaign;

public record SessionDTO(
    LocalDate sessionDate,
    String notes,
    Campaign campaign
) {}
