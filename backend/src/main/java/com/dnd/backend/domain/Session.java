package com.dnd.backend.domain;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate sessionDate;

    private String notes;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Campaign campaign;

    public Session(LocalDate sessionDate, String notes, Campaign campaign) {
        this.sessionDate = sessionDate;
        this.notes = notes;
        this.campaign = campaign;
    }
}
