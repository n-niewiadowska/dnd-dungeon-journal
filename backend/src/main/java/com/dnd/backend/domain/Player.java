package com.dnd.backend.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.dnd.backend.constant.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
public class Player extends DndUser {

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private DndCharacter character;

    @Builder.Default
    @ManyToMany(mappedBy = "players", fetch = FetchType.LAZY)
    private List<Campaign> playedCampaigns = new ArrayList<>();

    public Player(String username, String email, String password, DndCharacter character) {
        super(username, email, password, UserRole.PLAYER);
        this.character = character;
    }

    public Player(String username, String email, String password) {
        super(username, email, password, UserRole.PLAYER);
    }

    public void addPlayedCampaign(Campaign campaign) {
        this.playedCampaigns.add(campaign);
    }

    public void removePlayedCampaign(Long id) {
        this.playedCampaigns.removeIf(campaign -> Objects.equals(campaign.getId(), id));
    }
}