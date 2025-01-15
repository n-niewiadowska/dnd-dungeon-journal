package com.dnd.backend.mapper;

import com.dnd.backend.domain.MainSkill;
import com.dnd.backend.dto.MainSkillDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MainItemMapper.class, builder = @Builder(disableBuilder = true))
public interface MainSkillMapper {

    MainSkillDTO mapSkillToDto(MainSkill mainSkill);
    MainSkill mapDtoToSkill(MainSkillDTO mainSkillDTO);
}
