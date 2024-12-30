package com.dnd.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.DungeonMaster;

@Repository
public interface DungeonMasterRepository extends JpaRepository<DungeonMaster, Long> {

    Optional<DungeonMaster> findByUsername(String username);
}