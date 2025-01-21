package com.dnd.backend.service;

import com.dnd.backend.domain.MainItem;
import com.dnd.backend.domain.MainSkill;
import com.dnd.backend.dto.MainItemDTO;
import com.dnd.backend.mapper.MainItemMapper;
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
public class MainItemService {

    private final MainItemRepository itemRepository;
    private final MainSkillRepository skillRepository;
    private final MainItemMapper mapper;

    public List<MainItemDTO> findAllItems() {
        return itemRepository.findAll().stream()
                .map(mapper::mapItemToDto)
                .toList();
    }

    public Optional<MainItemDTO> findItemById(Long id) {
        Optional<MainItem> skill = itemRepository.findById(id);
        return skill.map(mapper::mapItemToDto);
    }

    public MainItemDTO createItem(MainItemDTO mainItemDTO) {
        MainSkill skill = mapper.mapDtoToItem(mainItemDTO).getAssociatedSkill();
        MainSkill existingSkill = skill != null ?
                skillRepository.findByName(skill.getName()).orElse(skillRepository.save(skill)) : null;

        MainItem newItem = MainItem.builder()
                .name(mainItemDTO.name())
                .description(mainItemDTO.description())
                .bonusValue(mainItemDTO.bonusValue())
                .associatedSkill(existingSkill)
                .build();

        MainItem savedItem = itemRepository.save(newItem);
        return mapper.mapItemToDto(savedItem);
    }

    public Optional<MainItemDTO> updateItem(Long id, MainItemDTO mainItemDTO) {
        Optional<MainItem> existingItem = itemRepository.findById(id);

        if (existingItem.isPresent()) {
            MainItem item = existingItem.get();
            MainSkill skill = mapper.mapDtoToItem(mainItemDTO).getAssociatedSkill();
            MainSkill existingSkill = skill != null ?
                    skillRepository.findByName(skill.getName()).orElse(skillRepository.save(skill)) : null;

            item.setName(mainItemDTO.name());
            item.setDescription(mainItemDTO.description());
            item.setBonusValue(mainItemDTO.bonusValue());
            if (existingSkill != null) {
                item.setAssociatedSkill(existingSkill);
            }

            itemRepository.save(item);
            return Optional.of(mapper.mapItemToDto(item));
        }

        return Optional.empty();
    }

    public void deleteItem(Long id) {
        Optional<MainItem> item = itemRepository.findById(id);
        if (item.isPresent()) {
            List<MainSkill> skillsWithItem = skillRepository.findByAssociatedItem(item.get());
            for (MainSkill skill : skillsWithItem) {
                skill.setAssociatedItem(null);
                skillRepository.save(skill);
            }
            itemRepository.deleteById(id);
        }
    }

    public Optional<MainItemDTO> findItemByNameAndBonus(String name, int bonus) {
        Optional<MainItem> item = itemRepository.findByNameAndBonusValue(name, bonus);
        return item.map(mapper::mapItemToDto);
    }

    public List<Object[]> findItemsWithSkillEffect(String effect) {
        return itemRepository.findItemsWithSkillEffect(effect);
    }
}
