package com.dnd.backend.service.impl;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.constant.UserRole;
import com.dnd.backend.mapper.PlayerMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.domain.Player;
import com.dnd.backend.dto.PlayerDTO;
import com.dnd.backend.repository.CampaignRepository;
import com.dnd.backend.repository.PlayerRepository;
import com.dnd.backend.service.PlayerService;

import jakarta.transaction.Transactional;

@Service
public class PlayerServiceImpl implements PlayerService {
    
    private final PlayerRepository playerRepository;
    private final CampaignRepository campaignRepository;

    private final PlayerMapper mapper;

    public PlayerServiceImpl(PlayerRepository playerRepository, CampaignRepository campaignRepository, PlayerMapper mapper) {
        this.playerRepository = playerRepository;
        this.campaignRepository = campaignRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PlayerDTO> findAllPlayers() {
        return playerRepository.findAll().stream()
            .map(mapper::mapPlayerToDto)
            .toList();
    }

    @Override
    public Optional<PlayerDTO> findPlayerById(Long id) {
        Optional<Player> playerOptional = playerRepository.findById(id);
        return playerOptional.map(mapper::mapPlayerToDto);
    }

    @Override
    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        Player newPlayer = Player.builder()
                .username(playerDTO.username())
                .email(playerDTO.email())
                .password(playerDTO.password())
                .role(UserRole.PLAYER)
                .character(mapper.mapDtoToPlayer(playerDTO).getCharacter())
                .build();
        Player savedPlayer = playerRepository.save(newPlayer);
        return mapper.mapPlayerToDto(savedPlayer);
    }

    @Override
    public Optional<PlayerDTO> updatePlayer(Long id, PlayerDTO playerDTO) {
        Optional<Player> existingPlayer = playerRepository.findById(id);

        if (existingPlayer.isPresent()) {
            Player player = existingPlayer.get();

            player.setUsername(playerDTO.username());
            player.setPassword(playerDTO.password());
            player.setEmail(playerDTO.email());
            player.setCharacter(mapper.mapDtoToPlayer(playerDTO).getCharacter());

            playerRepository.save(player);
            return Optional.of(mapper.mapPlayerToDto(player));
        }
        
        return Optional.empty();
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    public Optional<PlayerDTO> findPlayerByUsername(String username) {
        Optional<Player> playerOptional = playerRepository.findByUsername(username);
        return playerOptional.map(mapper::mapPlayerToDto);
    }

    @Transactional
    public Optional<PlayerDTO> addPlayedCampaign(Long playerId, Long campaignId) {
        Optional<Player> player = playerRepository.findById(playerId);
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);

        if (player.isPresent() && campaign.isPresent()) {
            player.get().addPlayedCampaign(campaign.get());

            campaignRepository.save(campaign.get());

            return player.map(mapper::mapPlayerToDto);
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<PlayerDTO> removePlayerFromCampaign(Long playerId, Long campaignId) {
        Optional<Player> player = playerRepository.findById(playerId);

        if (player.isPresent()) {
            player.get().removePlayedCampaign(campaignId);
            playerRepository.save(player.get());

            return player.map(mapper::mapPlayerToDto);
        }
        
        return Optional.empty();
    }
}
