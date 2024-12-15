package com.dnd.backend.dto;

import java.time.LocalDate;

import com.dnd.backend.constant.GameStatus;
import com.dnd.backend.domain.DungeonMaster;

public record CampaignDTO(
    String title,
    String description,
    DungeonMaster dungeonMaster,
    LocalDate beginningDate,
    GameStatus status
) {}
