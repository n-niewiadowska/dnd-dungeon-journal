package com.dnd.backend.domain;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    private Campaign campaign;

    @ManyToMany
    private List<Player> attendees;

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
