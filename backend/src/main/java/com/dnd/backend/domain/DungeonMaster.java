package com.dnd.backend.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.dnd.backend.constant.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
public class DungeonMaster extends DndUser {

    @Builder.Default
    @OneToMany(mappedBy = "dungeonMaster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Campaign> hostedCampaigns = new ArrayList<>();

    public DungeonMaster(String username, String email, String password) {
        super(username, email, password, UserRole.DUNGEON_MASTER);
    }

    public void addHostedCampaign(Campaign campaign) {
        this.hostedCampaigns.add(campaign);
    }

    public void removeHostedCampaign(Long id) {
        this.hostedCampaigns.removeIf(campaign -> Objects.equals(campaign.getId(), id));
    }
}