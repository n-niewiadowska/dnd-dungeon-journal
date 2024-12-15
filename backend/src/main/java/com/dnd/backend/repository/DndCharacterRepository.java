package com.dnd.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.DndCharacter;

@Repository
public interface DndCharacterRepository extends JpaRepository<DndCharacter, Long> {}
