package com.dnd.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByCampaignTitle(String campaignTitle);

    @Query("SELECT c.id, c.title, COUNT(s.id) AS sessionCount FROM Session s " +
            "JOIN s.campaign c " +
            "GROUP BY c.id, c.title")
    List<Object[]> findSessionCountByCampaign();
}
