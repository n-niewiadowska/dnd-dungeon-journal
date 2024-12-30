package com.dnd.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.domain.Player;
import com.dnd.backend.dto.PlayerDTO;
import com.dnd.backend.repository.CampaignRepository;
import com.dnd.backend.repository.PlayerRepository;
import com.dnd.backend.service.PlayerService;

import jakarta.transaction.Transactional;

public class PlayerServiceImpl implements PlayerService {
    
    private final PlayerRepository playerRepository;
    private final CampaignRepository campaignRepository;

    private final ModelMapper modelMapper;

    public PlayerServiceImpl(PlayerRepository playerRepository, CampaignRepository campaignRepository, ModelMapper modelMapper) {
        this.playerRepository = playerRepository;
        this.campaignRepository = campaignRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PlayerDTO> findAllPlayers() {
        return playerRepository.findAll().stream()
            .map(player -> modelMapper.map(player, PlayerDTO.class))
            .toList();
    }

    @Override
    public Optional<PlayerDTO> findPlayerById(Long id) {
        Optional<Player> playerOptional = playerRepository.findById(id);
        return playerOptional.map(player -> modelMapper.map(player, PlayerDTO.class));
    }

    @Override
    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        Player savedPlayer = playerRepository.save(modelMapper.map(playerDTO, Player.class));
        return modelMapper.map(savedPlayer, PlayerDTO.class);
    }

    @Override
    public Optional<PlayerDTO> updatePlayer(Long id, PlayerDTO playerDTO) {
        Optional<Player> existingPlayer = playerRepository.findById(id);

        if (existingPlayer.isPresent()) {
            Player player = existingPlayer.get();

            player.setUsername(playerDTO.username());
            player.setPassword(playerDTO.password());
            player.setEmail(playerDTO.email());
            player.setCharacter(playerDTO.character());

            playerRepository.save(player);
            return Optional.of(modelMapper.map(player, PlayerDTO.class));
        }
        
        return Optional.empty();
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    public Optional<PlayerDTO> findPlayerByUsername(String username) {
        Optional<Player> playerOptional = playerRepository.findByUsername(username);
        return playerOptional.map(player -> modelMapper.map(player, PlayerDTO.class));
    }

    @Transactional
    public Optional<PlayerDTO> addPlayedCampaign(Long playerId, Long campaignId) {
        Optional<Player> player = playerRepository.findById(playerId);
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);

        if (player.isPresent() && campaign.isPresent()) {
            player.get().addPlayedCampaign(campaign.get());

            campaignRepository.save(campaign.get());

            return player.map(p -> modelMapper.map(p, PlayerDTO.class));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<PlayerDTO> removePlayerFromCampaign(Long playerId, Long campaignId) {
        Optional<Player> player = playerRepository.findById(playerId);

        if (player.isPresent()) {
            player.get().removePlayedCampaign(campaignId);
            playerRepository.save(player.get());

            return player.map(p -> modelMapper.map(p, PlayerDTO.class));
        }
        
        return Optional.empty();
    }
}
