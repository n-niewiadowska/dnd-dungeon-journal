package com.dnd.backend.dto;

import java.util.List;

import com.dnd.backend.domain.Campaign;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record DungeonMasterDTO(
    String username,
    String email,
    @JsonIgnore String password,
    List<Campaign> hostedCampaigns
) {}
