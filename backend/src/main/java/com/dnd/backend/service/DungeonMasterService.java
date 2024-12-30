package com.dnd.backend.service;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.dto.DungeonMasterDTO;

public interface DungeonMasterService {
    
    List<DungeonMasterDTO> findAllDungeonMasters();

    Optional<DungeonMasterDTO> findDungeonMasterById(Long id);

    DungeonMasterDTO createDungeonMaster(DungeonMasterDTO dungeonMasterDTO);

    Optional<DungeonMasterDTO> updateDungeonMaster(Long id, DungeonMasterDTO dungeonMasterDTO);

    void deleteDungeonMaster(Long id);
}
