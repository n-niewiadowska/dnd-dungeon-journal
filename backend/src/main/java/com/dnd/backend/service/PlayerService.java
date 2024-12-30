package com.dnd.backend.service;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.dto.PlayerDTO;

public interface PlayerService {

    List<PlayerDTO> findAllPlayers();

    Optional<PlayerDTO> findPlayerById(Long id);

    PlayerDTO createPlayer(PlayerDTO playerDTO);

    Optional<PlayerDTO> updatePlayer(Long id, PlayerDTO playerDTO);

    void deletePlayer(Long id);
}
