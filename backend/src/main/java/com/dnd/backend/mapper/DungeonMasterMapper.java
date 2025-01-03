package com.dnd.backend.mapper;

import com.dnd.backend.domain.DungeonMaster;
import com.dnd.backend.dto.DungeonMasterDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface DungeonMasterMapper {

    @Mapping(target = "password", ignore = true)
    DungeonMasterDTO mapDungeonMasterToDto(DungeonMaster dm);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "hostedCampaigns", ignore = true)
    DungeonMaster mapDtoToDungeonMaster(DungeonMasterDTO dto);
}