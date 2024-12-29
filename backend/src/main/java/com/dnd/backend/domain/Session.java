package com.dnd.backend.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.PastOrPresent;
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
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PastOrPresent
    private LocalDate sessionDate;

    private String notes;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Campaign campaign;

    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "session_attendees",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> attendees = new ArrayList<>();

    public Session(LocalDate sessionDate, String notes, Campaign campaign) {
        this.sessionDate = sessionDate;
        this.notes = notes;
        this.campaign = campaign;
    }

    public void addAttendee(Player attendee) {
        this.attendees.add(attendee);
    }

    public void removeAttendee(Long id) {
        this.attendees.removeIf(attendee -> attendee.getId() == id);
    }
}
