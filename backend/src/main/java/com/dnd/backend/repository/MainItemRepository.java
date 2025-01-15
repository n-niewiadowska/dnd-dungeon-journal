package com.dnd.backend.repository;

import com.dnd.backend.domain.MainItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MainItemRepository extends JpaRepository<MainItem, Long> {

    Optional<MainItem> findByNameAndBonusValue(String name, int bonusValue);

    @Query("SELECT item.id, item.name, item.bonusValue, skill.name AS skillName, skill.effect " +
            "FROM MainItem item " +
            "JOIN item.associatedSkill skill " +
            "WHERE skill.effect LIKE :effect")
    List<Object[]> findItemsWithSkillEffect(@Param("effect") String effect);
}
