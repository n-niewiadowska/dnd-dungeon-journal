package com.dnd.backend.service;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.dto.DndCharacterDTO;

public interface DndCharacterService {

    List<DndCharacterDTO> findAllCharacters();

    Optional<DndCharacterDTO> findCharacterById(Long id);

    DndCharacterDTO createCharacter(DndCharacterDTO dndCharacterDTO);

    Optional<DndCharacterDTO> updateCharacter(Long id, DndCharacterDTO dndCharacterDTO);

    void deleteCharacter(Long id);
}
