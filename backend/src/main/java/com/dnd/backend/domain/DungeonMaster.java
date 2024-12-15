package com.dnd.backend.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
public class DungeonMaster extends User {

    @OneToMany(mappedBy = "dungeonMaster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Campaign> hostedCampaigns;

    public void addHostedCampaign(Campaign campaign) {
        this.hostedCampaigns.add(campaign);
    }

    public void removeHostedCampaign(Long id) {
        this.hostedCampaigns.removeIf(campaign -> campaign.getId() == id);
    }
}