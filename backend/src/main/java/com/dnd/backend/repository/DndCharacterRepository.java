package com.dnd.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dnd.backend.constant.CharacterClass;
import com.dnd.backend.domain.DndCharacter;

@Repository
public interface DndCharacterRepository extends JpaRepository<DndCharacter, Long> {

    @Query(value = "SELECT AVG(CASE WHEN can_perform_magic = true THEN 1 ELSE 0 END) FROM dnd_character WHERE dnd_class = :dndClass", nativeQuery = true)
    Double findAvgOfMagicalCharactersForClass(@Param("dndClass") String dndClass);

}
