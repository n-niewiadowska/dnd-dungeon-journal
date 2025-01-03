package com.dnd.backend.mapper;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.dto.CampaignDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DungeonMasterMapper.class, builder = @Builder(disableBuilder = true))
public interface CampaignMapper {

//   @Mapping(target = "players", ignore = true)
//   @Mapping(target = "sessions", ignore = true)
   CampaignDTO mapCampaignToDto(Campaign campaign);

   @Mapping(target = "players", ignore = true)
   @Mapping(target = "sessions", ignore = true)
   Campaign mapDtoToCampaign(CampaignDTO dto);
}
