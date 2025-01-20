package com.dnd.backend.repository;

import com.dnd.backend.constant.Ability;
import com.dnd.backend.domain.MainItem;
import com.dnd.backend.domain.MainSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MainSkillRepository extends JpaRepository<MainSkill, Long> {

    Optional<MainSkill> findByName(String name);

    List<MainSkill> findByAssociatedItem(MainItem item);

    List<MainSkill> findByRelatedAbilityOrLevel(Ability relatedAbility, int level);

    @Query("SELECT skill.id, skill.name, skill.relatedAbility, skill.level, item.name AS itemName, item.bonusValue " +
            "FROM MainSkill skill " +
            "JOIN skill.associatedItem item " +
            "WHERE item.bonusValue = :bonusValue")
    List<Object[]> findSkillsWithItemBonus(@Param("bonusValue") int bonusValue);
}
