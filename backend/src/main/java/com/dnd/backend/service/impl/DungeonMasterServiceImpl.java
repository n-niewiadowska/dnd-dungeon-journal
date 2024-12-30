package com.dnd.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.domain.DungeonMaster;
import com.dnd.backend.dto.DungeonMasterDTO;
import com.dnd.backend.repository.CampaignRepository;
import com.dnd.backend.repository.DungeonMasterRepository;
import com.dnd.backend.service.DungeonMasterService;

import jakarta.transaction.Transactional;

public class DungeonMasterServiceImpl implements DungeonMasterService {
    
    private final DungeonMasterRepository dungeonMasterRepository;
    private final CampaignRepository campaignRepository;

    private final ModelMapper modelMapper;

    public DungeonMasterServiceImpl(DungeonMasterRepository dungeonMasterRepository, CampaignRepository campaignRepository, ModelMapper modelMapper) {
        this.dungeonMasterRepository = dungeonMasterRepository;
        this.campaignRepository = campaignRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DungeonMasterDTO> findAllDungeonMasters() {
        return dungeonMasterRepository.findAll().stream()
            .map(dungeonMaster -> modelMapper.map(dungeonMaster, DungeonMasterDTO.class))
            .toList();
    }

    @Override
    public Optional<DungeonMasterDTO> findDungeonMasterById(Long id) {
        Optional<DungeonMaster> dungeonMaster = dungeonMasterRepository.findById(id);
        return dungeonMaster.map(master -> modelMapper.map(master, DungeonMasterDTO.class));
    }

    @Override
    public DungeonMasterDTO createDungeonMaster(DungeonMasterDTO dungeonMasterDTO) {
        DungeonMaster dm = dungeonMasterRepository.save(modelMapper.map(dungeonMasterDTO, DungeonMaster.class));
        return modelMapper.map(dm, DungeonMasterDTO.class);
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
            return Optional.of(dungeonMasterDTO);
        }
        
        return Optional.empty();
    }

    @Override
    public void deleteDungeonMaster(Long id) {
        dungeonMasterRepository.deleteById(id);
    }

    public Optional<DungeonMasterDTO> findDungeonMasterByUsername(String username) {
        Optional<DungeonMaster> dungeonMaster = dungeonMasterRepository.findByUsername(username);
        return dungeonMaster.map(master -> modelMapper.map(master, DungeonMasterDTO.class));
    }

    @Transactional
    public Optional<DungeonMasterDTO> addHostedCampaign(Long dmId, Long campaignId) {
        Optional<DungeonMaster> dungeonMaster = dungeonMasterRepository.findById(dmId);
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);

        if (dungeonMaster.isPresent() && campaign.isPresent()) {
            dungeonMaster.get().addHostedCampaign(campaign.get());

            campaignRepository.save(campaign.get());

            return dungeonMaster.map(dm -> modelMapper.map(dm, DungeonMasterDTO.class));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<DungeonMasterDTO> removeHostedCampaign(Long dmId, Long campaignId) {
        Optional<DungeonMaster> dungeonMaster = dungeonMasterRepository.findById(dmId);

        if (dungeonMaster.isPresent()) {
            dungeonMaster.get().removeHostedCampaign(campaignId);
            dungeonMasterRepository.save(dungeonMaster.get());

            return dungeonMaster.map(dm -> modelMapper.map(dm, DungeonMasterDTO.class));
        }
        
        return Optional.empty();
    }
}
