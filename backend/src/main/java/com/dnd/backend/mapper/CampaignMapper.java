package com.dnd.backend.mapper;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.dto.CampaignDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DndCharacterMapper.class, builder = @Builder(disableBuilder = true))
public interface CampaignMapper {

   CampaignDTO mapCampaignToDto(Campaign campaign);

   @Mapping(target = "sessions", ignore = true)
   Campaign mapDtoToCampaign(CampaignDTO dto);
}
