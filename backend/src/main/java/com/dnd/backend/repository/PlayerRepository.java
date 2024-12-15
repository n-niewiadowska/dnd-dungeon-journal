package com.dnd.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    List<Player> findByUsername(String username);
}
