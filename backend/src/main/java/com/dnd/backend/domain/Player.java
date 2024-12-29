package com.dnd.backend.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
public class Player extends User {

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private DndCharacter character;

    @Builder.Default
    @ManyToMany(mappedBy = "players", fetch = FetchType.LAZY)
    private List<Campaign> playedCampaigns = new ArrayList<>();

    public Player(String username, String email, String password, DndCharacter character) {
        super(username, email, password);
        this.character = character;
    }

    public void addPlayedCampaign(Campaign campaign) {
        this.playedCampaigns.add(campaign);
    }

    public void removePlayedCampaign(Long id) {
        this.playedCampaigns.removeIf(campaign -> campaign.getId() == id);
    }
}