package com.dnd.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.backend.domain.DungeonMaster;

public interface DungeonMasterRepository extends JpaRepository<DungeonMaster, Long> {}