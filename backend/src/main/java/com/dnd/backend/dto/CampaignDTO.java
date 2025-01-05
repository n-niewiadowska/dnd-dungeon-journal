package com.dnd.backend.dto;

import java.time.LocalDate;

import com.dnd.backend.constant.GameStatus;
import org.springframework.format.annotation.DateTimeFormat;

public record CampaignDTO(
    Long id,
    String title,
    String description,
    DungeonMasterDTO dungeonMaster,
    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginningDate,
    GameStatus status
) {}
