package com.dnd.backend.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.dnd.backend.constant.GameStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String title;
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private DungeonMaster dungeonMaster;

    private LocalDate beginningDate;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Player> players = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.REMOVE)
    private List<Session> sessions = new ArrayList<>();

    public Campaign(String title, String description, DungeonMaster dungeonMaster, LocalDate beginningDate, GameStatus status) {
        this.title = title;
        this.description = description;
        this.dungeonMaster = dungeonMaster;
        this.beginningDate = beginningDate;
        this.status = status;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Long id) {
        this.players.removeIf(player -> player.getId() == id);
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }

    public void removeSession(Long id) {
        this.sessions.removeIf(session -> session.getId() == id);
    }
}
