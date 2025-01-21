package com.dnd.backend.service;

import com.dnd.backend.constant.Ability;
import com.dnd.backend.domain.DndCharacter;
import com.dnd.backend.domain.MainItem;
import com.dnd.backend.domain.MainSkill;
import com.dnd.backend.dto.MainSkillDTO;
import com.dnd.backend.mapper.MainSkillMapper;
import com.dnd.backend.repository.DndCharacterRepository;
import com.dnd.backend.repository.MainItemRepository;
import com.dnd.backend.repository.MainSkillRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MainSkillService {

    private final MainSkillRepository skillRepository;
    private final MainItemRepository itemRepository;
    private final DndCharacterRepository dndCharacterRepository;
    private final MainSkillMapper mapper;

    public List<MainSkillDTO> findAllSkills() {
        return skillRepository.findAll().stream()
                .map(mapper::mapSkillToDto)
                .toList();
    }

    public Optional<MainSkillDTO> findSkillById(Long id) {
        Optional<MainSkill> skill = skillRepository.findById(id);
        return skill.map(mapper::mapSkillToDto);
    }

    public MainSkillDTO createSkill(MainSkillDTO mainSkillDTO) {
        MainItem item = mapper.mapDtoToSkill(mainSkillDTO).getAssociatedItem();
        MainItem existingItem = item != null ?
                itemRepository.findByName(item.getName()).orElse(itemRepository.save(item)) : null;

        MainSkill newSkill = MainSkill.builder()
                .name(mainSkillDTO.name())
                .relatedAbility(mainSkillDTO.relatedAbility())
                .description(mainSkillDTO.description())
                .level(mainSkillDTO.level())
                .effect(mainSkillDTO.effect())
                .associatedItem(existingItem)
                .build();

        MainSkill savedSkill = skillRepository.save(newSkill);
        return mapper.mapSkillToDto(savedSkill);
    }

    public Optional<MainSkillDTO> updateSkill(Long id, MainSkillDTO mainSkillDTO) {
        Optional<MainSkill> existingSkill = skillRepository.findById(id);

        if (existingSkill.isPresent()) {
            MainSkill skill = existingSkill.get();
            MainItem item = mapper.mapDtoToSkill(mainSkillDTO).getAssociatedItem();
            MainItem existingItem = item != null ?
                    itemRepository.findByName(item.getName()).orElse(itemRepository.save(item)) : null;

            skill.setName(mainSkillDTO.name());
            skill.setRelatedAbility(mainSkillDTO.relatedAbility());
            skill.setDescription(mainSkillDTO.description());
            skill.setLevel(mainSkillDTO.level());
            skill.setEffect(mainSkillDTO.effect());
            if (existingItem != null) {
                skill.setAssociatedItem(existingItem);
            }

            skillRepository.save(skill);
            return Optional.of(mapper.mapSkillToDto(skill));
        }

        return Optional.empty();
    }

    public void deleteSkill(Long id) {
        Optional<MainSkill> skill = skillRepository.findById(id);
        if (skill.isPresent()) {
            List<DndCharacter> charactersWithSkill = dndCharacterRepository.findBySkill(skill.get());

            for (DndCharacter character : charactersWithSkill) {
                character.setSkill(null);
                dndCharacterRepository.save(character);
            }
            skillRepository.deleteById(id);
        }
    }

    public List<MainSkillDTO> findSkillByAbilityOrLevel(Ability relatedAbility, int level) {
        return skillRepository.findByRelatedAbilityOrLevel(relatedAbility, level).stream()
                .map(mapper::mapSkillToDto).toList();
    }

    public List<Object[]> findSkillsWithItemBonus(int bonusValue) {
        return skillRepository.findSkillsWithItemBonus(bonusValue);
    }
}
