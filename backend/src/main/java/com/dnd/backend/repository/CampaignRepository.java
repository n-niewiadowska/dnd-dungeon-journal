package com.dnd.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long>, JpaSpecificationExecutor<Campaign> {

    List<Campaign> findByDungeonMasterUsername(String dungeonMasterUsername);

    @Query("SELECT COUNT(c) FROM Campaign c JOIN c.dungeonMaster dm WHERE dm.id = :dmId")
    Integer countCampaignsByDungeonMaster(@Param("dmId") Long dmId);

    @Query(value = "SELECT title FROM Campaign WHERE status = 'PLANNED' AND beginning_date > :date", nativeQuery = true)
    List<String> findPlannedCampaignTitlesAfterDate(@Param("date") LocalDate date);

    @Query("""
        SELECT AVG(playersCount)
        FROM (
            SELECT COUNT(a) AS playersCount
            FROM Campaign c
            JOIN c.sessions s
            JOIN s.attendees a
            WHERE c.id = :campaignId
            GROUP BY s.id
        ) AS sessionCounts
    """)
    Double findAverageNumberOfPlayersPerSessionInCampaign(@Param("campaignId") Long campaignId);

} 
