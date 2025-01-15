package com.dnd.backend.service;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.domain.MainSkill;
import com.dnd.backend.mapper.DndCharacterMapper;
import com.dnd.backend.repository.CampaignRepository;
import com.dnd.backend.repository.MainSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.DndCharacter;
import com.dnd.backend.dto.DndCharacterDTO;
import com.dnd.backend.repository.DndCharacterRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DndCharacterService {
    
    private final DndCharacterRepository dndCharacterRepository;
    private final MainSkillRepository skillRepository;
    private final CampaignRepository campaignRepository;

    private final DndCharacterMapper mapper;

    public List<DndCharacterDTO> findAllCharacters() {
        return dndCharacterRepository.findAll().stream()
            .map(mapper::mapCharacterToDto)
            .toList();
    }

    public Optional<DndCharacterDTO> findCharacterById(Long id) {
        Optional<DndCharacter> dndCharacter = dndCharacterRepository.findById(id);
        return dndCharacter.map(mapper::mapCharacterToDto);
    }

    public DndCharacterDTO createCharacter(DndCharacterDTO dndCharacterDTO) {
        MainSkill skill = dndCharacterDTO.skill() == null ?
                null : skillRepository.findById(dndCharacterDTO.skill().id()).orElse(null);

        DndCharacter newCharacter = DndCharacter.builder()
                .firstName(dndCharacterDTO.firstName())
                .lastName(dndCharacterDTO.lastName())
                .dndClass(dndCharacterDTO.dndClass())
                .race(dndCharacterDTO.race())
                .age(dndCharacterDTO.age())
                .canPerformMagic(dndCharacterDTO.canPerformMagic())
                .skill(skill)
                .build();

        DndCharacter savedCharacter = dndCharacterRepository.save(newCharacter);
        return mapper.mapCharacterToDto(savedCharacter);
    }

    public Optional<DndCharacterDTO> updateCharacter(Long id, DndCharacterDTO dndCharacterDTO) {
        Optional<DndCharacter> existingCharacter = dndCharacterRepository.findById(id);

        if (existingCharacter.isPresent()) {
            DndCharacter dndCharacter = existingCharacter.get();
            MainSkill skill = dndCharacterDTO.skill() == null ?
                    null : skillRepository.findById(dndCharacterDTO.skill().id()).orElse(null);

            dndCharacter.setFirstName(dndCharacterDTO.firstName());
            dndCharacter.setLastName(dndCharacterDTO.lastName());
            dndCharacter.setDndClass(dndCharacterDTO.dndClass());
            dndCharacter.setRace(dndCharacterDTO.race());
            dndCharacter.setAge(dndCharacterDTO.age());
            dndCharacter.setCanPerformMagic(dndCharacterDTO.canPerformMagic());
            if (skill != null) {
                dndCharacter.setSkill(skill);
            }

            dndCharacterRepository.save(dndCharacter);
            return Optional.of(mapper.mapCharacterToDto(dndCharacter));
        }
        
        return Optional.empty();
    }

    public void deleteCharacter(Long id) {
        Optional<DndCharacter> character = dndCharacterRepository.findById(id);
        if (character.isPresent()) {
            List<Campaign> campaigns = campaignRepository.findAll();
            for (Campaign campaign : campaigns) {
                campaign.removeCharacter(id);
                campaignRepository.save(campaign);
            }
            character.get().setSkill(null);
            dndCharacterRepository.deleteById(id);
        }
    }

    public Double findAverageOfMagicalCharactersFromClass(String dndClass) {
        return dndCharacterRepository.findAvgOfMagicalCharactersForClass(dndClass);
    }
}
