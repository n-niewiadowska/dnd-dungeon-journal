package com.dnd.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long>, JpaSpecificationExecutor<Campaign> {

    Optional<Campaign> findByTitle(String title);

    Optional<Campaign> findByTitleAndStatus(String title, String status);

    @Query(value = "SELECT title FROM Campaign WHERE status = 'PLANNED' AND beginning_date > :date", nativeQuery = true)
    List<String> findPlannedCampaignTitlesAfterDate(@Param("date") LocalDate date);

    @Query("SELECT c.id, c.title, COUNT(cs.id) AS characterCount FROM Campaign c " +
            "JOIN c.characters cs " +
            "GROUP BY c.id, c.title " +
            "HAVING COUNT(cs.id) >= :minCharacters")
    List<Object[]> findCampaignsWithMinimumCharacterCount(@Param("minCharacters") int minCharacters);
} 
