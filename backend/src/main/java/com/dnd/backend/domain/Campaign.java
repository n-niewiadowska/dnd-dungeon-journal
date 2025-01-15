package com.dnd.backend.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.dnd.backend.constant.GameStatus;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;
    private String description;

    private LocalDate beginningDate;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Builder.Default
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<DndCharacter> characters = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Session> sessions = new ArrayList<>();

    public Campaign(String title, String description, LocalDate beginningDate, GameStatus status) {
        this.title = title;
        this.description = description;
        this.beginningDate = beginningDate;
        this.status = status;
    }

    public void addCharacter(DndCharacter player) {
        this.characters.add(player);
    }

    public void removeCharacter(Long id) {
        this.characters.removeIf(character -> Objects.equals(character.getId(), id));
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }

    public void removeSession(Long id) {
        this.sessions.removeIf(session -> Objects.equals(session.getId(), id));
    }
}
