package com.dnd.backend.mapper;

import com.dnd.backend.domain.DndCharacter;
import com.dnd.backend.dto.DndCharacterDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MainSkillMapper.class, builder = @Builder(disableBuilder = true))
public interface DndCharacterMapper {

    DndCharacterDTO mapCharacterToDto(DndCharacter character);
    DndCharacter mapDtoToCharacter(DndCharacterDTO dto);
}