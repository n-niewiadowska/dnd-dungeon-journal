package com.dnd.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.dnd.backend.constant.GameStatus;
import com.dnd.backend.domain.DungeonMaster;
import com.dnd.backend.domain.Player;

public record CampaignDTO(
    String title,
    String description,
    DungeonMaster dungeonMaster,
    LocalDate beginningDate,
    GameStatus status,
    List<Player> players
) {}
