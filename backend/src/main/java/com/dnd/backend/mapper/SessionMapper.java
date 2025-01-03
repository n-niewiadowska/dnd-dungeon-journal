package com.dnd.backend.mapper;

import com.dnd.backend.domain.Session;
import com.dnd.backend.dto.SessionDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CampaignMapper.class, builder = @Builder(disableBuilder = true))
public interface SessionMapper {

//    @Mapping(target = "attendees", ignore = true)
    SessionDTO mapSessionToDto(Session session);

    @Mapping(target = "attendees", ignore = true)
    Session mapDtoToSession(SessionDTO dto);
}