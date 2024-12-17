package com.dnd.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByCampaignTitle(String campaignTitle);

    @Query("SELECT s.id, COUNT(a) FROM Session s LEFT JOIN s.attendees a GROUP BY s.id")
    List<Object[]> countAttendeesForEverySession();

}
