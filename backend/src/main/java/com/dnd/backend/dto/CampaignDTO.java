package com.dnd.backend.dto;

import java.time.LocalDate;

import com.dnd.backend.constant.GameStatus;

public record CampaignDTO(
    Long id,
    String title,
    String description,
    DungeonMasterDTO dungeonMaster,
    LocalDate beginningDate,
    GameStatus status
) {}
