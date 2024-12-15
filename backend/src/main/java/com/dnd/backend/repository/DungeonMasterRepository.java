package com.dnd.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.DungeonMaster;

@Repository
public interface DungeonMasterRepository extends CrudRepository<DungeonMaster, Long> {

    List<DungeonMaster> findByUsername(String username);
}