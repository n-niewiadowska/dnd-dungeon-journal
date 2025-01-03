package com.dnd.backend.mapper;

import com.dnd.backend.domain.Player;
import com.dnd.backend.dto.PlayerDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DndCharacterMapper.class, builder = @Builder(disableBuilder = true))
public interface PlayerMapper {

    @Mapping(target = "password", ignore = true)
    PlayerDTO mapPlayerToDto(Player player);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "playedCampaigns", ignore = true)
    Player mapDtoToPlayer(PlayerDTO dto);
}