package com.dnd.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnd.backend.constant.CharacterClass;
import com.dnd.backend.domain.DndCharacter;
import com.dnd.backend.dto.DndCharacterDTO;
import com.dnd.backend.repository.DndCharacterRepository;
import com.dnd.backend.service.DndCharacterService;

import jakarta.transaction.Transactional;

@Service
public class DndCharacterServiceImpl implements DndCharacterService {
    
    @Autowired
    private DndCharacterRepository dndCharacterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<DndCharacterDTO> findAllCharacters() {
        return dndCharacterRepository.findAll().stream()
            .map(character -> modelMapper.map(character, DndCharacterDTO.class))
            .toList();
    }

    @Override
    public Optional<DndCharacterDTO> findCharacterById(Long id) {
        Optional<DndCharacter> dndCharacter = dndCharacterRepository.findById(id);
        return dndCharacter.map(character -> modelMapper.map(character, DndCharacterDTO.class));
    }

    @Override
    public DndCharacterDTO createCharacter(DndCharacterDTO dndCharacterDTO) {
        dndCharacterRepository.save(modelMapper.map(dndCharacterDTO, DndCharacter.class));
        return dndCharacterDTO;
    }

    @Override
    @Transactional
    public Optional<DndCharacterDTO> updateCharacter(Long id, DndCharacterDTO dndCharacterDTO) {
        Optional<DndCharacter> existingCharacter = dndCharacterRepository.findById(id);

        if (existingCharacter.isPresent()) {
            DndCharacter dndCharacter = existingCharacter.get();

            dndCharacter.setFirstName(dndCharacterDTO.firstName());
            dndCharacter.setLastName(dndCharacterDTO.lastName());
            dndCharacter.setDndClass(dndCharacterDTO.dndClass());
            dndCharacter.setRace(dndCharacterDTO.race());
            dndCharacter.setAge(dndCharacterDTO.age());
            dndCharacter.setCanPerformMagic(dndCharacterDTO.canPerformMagic());

            dndCharacterRepository.save(dndCharacter);
            return Optional.of(dndCharacterDTO);
        }
        
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteCharacter(Long id) {
        dndCharacterRepository.deleteById(id);
    }

    public Double findAverageOfMagicalCharactersFromClass(CharacterClass dndClass) {
        return dndCharacterRepository.findAvgOfMagicalCharactersForClass(dndClass);
    }
}
