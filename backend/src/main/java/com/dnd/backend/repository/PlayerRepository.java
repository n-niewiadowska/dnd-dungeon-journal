package com.dnd.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.backend.domain.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {}
