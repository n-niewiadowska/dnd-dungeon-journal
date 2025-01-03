package com.dnd.backend.mapper;

import com.dnd.backend.domain.DndCharacter;
import com.dnd.backend.dto.DndCharacterDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface DndCharacterMapper {

    DndCharacterDTO mapCharacterToDto(DndCharacter character);
    DndCharacter mapDtoToCharacter(DndCharacterDTO dto);
}