package com.dnd.backend.domain;

import java.time.LocalDate;
import java.util.List;

import com.dnd.backend.constant.GameStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NotNull
    private String title;
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private DungeonMaster dungeonMaster;

    private LocalDate beginningDate;
    private GameStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Player> players;

    public Campaign(String title, String description, DungeonMaster dungeonMaster, LocalDate beginningDate, GameStatus status, List<Player> players) {
        this.title = title;
        this.description = description;
        this.dungeonMaster = dungeonMaster;
        this.beginningDate = beginningDate;
        this.status = status;
        this.players = players;
    }
}
