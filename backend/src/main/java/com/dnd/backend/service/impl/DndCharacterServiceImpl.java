package com.dnd.backend.service.impl;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.mapper.DndCharacterMapper;
import org.springframework.stereotype.Service;

import com.dnd.backend.constant.CharacterClass;
import com.dnd.backend.domain.DndCharacter;
import com.dnd.backend.dto.DndCharacterDTO;
import com.dnd.backend.repository.DndCharacterRepository;
import com.dnd.backend.service.DndCharacterService;

import jakarta.transaction.Transactional;

@Service
public class DndCharacterServiceImpl implements DndCharacterService {
    
    private final DndCharacterRepository dndCharacterRepository;

    private final DndCharacterMapper mapper;

    public DndCharacterServiceImpl(DndCharacterRepository dndCharacterRepository, DndCharacterMapper mapper) {
        this.dndCharacterRepository = dndCharacterRepository;
        this.mapper = mapper;
    }

    @Override
    public List<DndCharacterDTO> findAllCharacters() {
        return dndCharacterRepository.findAll().stream()
            .map(mapper::mapCharacterToDto)
            .toList();
    }

    @Override
    public Optional<DndCharacterDTO> findCharacterById(Long id) {
        Optional<DndCharacter> dndCharacter = dndCharacterRepository.findById(id);
        return dndCharacter.map(mapper::mapCharacterToDto);
    }

    @Override
    public DndCharacterDTO createCharacter(DndCharacterDTO dndCharacterDTO) {
        DndCharacter newCharacter = DndCharacter.builder()
                .firstName(dndCharacterDTO.firstName())
                .lastName(dndCharacterDTO.lastName())
                .dndClass(dndCharacterDTO.dndClass())
                .race(dndCharacterDTO.race())
                .age(dndCharacterDTO.age())
                .canPerformMagic(dndCharacterDTO.canPerformMagic())
                .build();

        DndCharacter savedCharacter = dndCharacterRepository.save(newCharacter);
        return mapper.mapCharacterToDto(savedCharacter);
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
            return Optional.of(mapper.mapCharacterToDto(dndCharacter));
        }
        
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteCharacter(Long id) {
        dndCharacterRepository.deleteById(id);
    }

    public Double findAverageOfMagicalCharactersFromClass(String dndClass) {
        return dndCharacterRepository.findAvgOfMagicalCharactersForClass(dndClass);
    }
}
