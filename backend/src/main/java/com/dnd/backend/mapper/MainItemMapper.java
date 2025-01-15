package com.dnd.backend.mapper;

import com.dnd.backend.domain.MainItem;
import com.dnd.backend.dto.MainItemDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface MainItemMapper {

    @Mapping(target = "associatedSkill", ignore = true)
    MainItemDTO mapItemToDto(MainItem mainItem);

    @Mapping(target = "associatedSkill", ignore = true)
    MainItem mapDtoToItem(MainItemDTO mainItemDTO);
}
