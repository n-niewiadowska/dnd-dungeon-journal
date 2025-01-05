package com.dnd.backend.service.impl;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.constant.UserRole;
import com.dnd.backend.mapper.DungeonMasterMapper;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.domain.DungeonMaster;
import com.dnd.backend.dto.DungeonMasterDTO;
import com.dnd.backend.repository.CampaignRepository;
import com.dnd.backend.repository.DungeonMasterRepository;
import com.dnd.backend.service.DungeonMasterService;

import jakarta.transaction.Transactional;

@Service
public class DungeonMasterServiceImpl implements DungeonMasterService {
    
    private final DungeonMasterRepository dungeonMasterRepository;
    private final CampaignRepository campaignRepository;

    private final DungeonMasterMapper mapper;

    public DungeonMasterServiceImpl(
            DungeonMasterRepository dungeonMasterRepository,
            CampaignRepository campaignRepository,
            DungeonMasterMapper mapper
    ) {
        this.dungeonMasterRepository = dungeonMasterRepository;
        this.campaignRepository = campaignRepository;
        this.mapper = mapper;
    }

    @Override
    public List<DungeonMasterDTO> findAllDungeonMasters() {
        return dungeonMasterRepository.findAll().stream()
            .map(mapper::mapDungeonMasterToDto)
            .toList();
    }

    @Override
    public Optional<DungeonMasterDTO> findDungeonMasterById(Long id) {
        Optional<DungeonMaster> dungeonMaster = dungeonMasterRepository.findById(id);
        return dungeonMaster.map(mapper::mapDungeonMasterToDto);
    }

    @Override
    public DungeonMasterDTO createDungeonMaster(DungeonMasterDTO dungeonMasterDTO) {
        DungeonMaster newDm = DungeonMaster.builder()
                .username(dungeonMasterDTO.username())
                .email(dungeonMasterDTO.email())
                .password(dungeonMasterDTO.password())
                .role(UserRole.DUNGEON_MASTER)
                .build();
        DungeonMaster savedDm = dungeonMasterRepository.save(newDm);
        return mapper.mapDungeonMasterToDto(savedDm);
    }

    @Override
    public Optional<DungeonMasterDTO> updateDungeonMaster(Long id, DungeonMasterDTO dungeonMasterDTO) {
        Optional<DungeonMaster> existingDungeonMaster = dungeonMasterRepository.findById(id);

        if (existingDungeonMaster.isPresent()) {
            DungeonMaster dungeonMaster = existingDungeonMaster.get();

            dungeonMaster.setUsername(dungeonMasterDTO.username());
            dungeonMaster.setPassword(dungeonMasterDTO.password());
            dungeonMaster.setEmail(dungeonMasterDTO.email());

            dungeonMasterRepository.save(dungeonMaster);
            return Optional.of(mapper.mapDungeonMasterToDto(dungeonMaster));
        }
        
        return Optional.empty();
    }

    @Override
    public void deleteDungeonMaster(Long id) {
        dungeonMasterRepository.deleteById(id);
    }

    public Optional<DungeonMasterDTO> findDungeonMasterByUsername(String username) {
        Optional<DungeonMaster> dungeonMaster = dungeonMasterRepository.findByUsername(username);
        return dungeonMaster.map(mapper::mapDungeonMasterToDto);
    }

    @Transactional
    public Optional<DungeonMasterDTO> addHostedCampaign(Long dmId, Long campaignId) {
        Optional<DungeonMaster> dungeonMaster = dungeonMasterRepository.findById(dmId);
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);

        if (dungeonMaster.isPresent() && campaign.isPresent()) {
            dungeonMaster.get().addHostedCampaign(campaign.get());

            campaignRepository.save(campaign.get());

            return dungeonMaster.map(mapper::mapDungeonMasterToDto);
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<DungeonMasterDTO> removeHostedCampaign(Long dmId, Long campaignId) {
        Optional<DungeonMaster> dungeonMaster = dungeonMasterRepository.findById(dmId);

        if (dungeonMaster.isPresent()) {
            dungeonMaster.get().removeHostedCampaign(campaignId);
            dungeonMasterRepository.save(dungeonMaster.get());

            return dungeonMaster.map(mapper::mapDungeonMasterToDto);
        }
        
        return Optional.empty();
    }
}
