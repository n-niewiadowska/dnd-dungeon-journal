package com.dnd.backend.mapper;

import com.dnd.backend.domain.Session;
import com.dnd.backend.dto.SessionDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CampaignMapper.class, builder = @Builder(disableBuilder = true))
public interface SessionMapper {

    SessionDTO mapSessionToDto(Session session);
    Session mapDtoToSession(SessionDTO dto);
}