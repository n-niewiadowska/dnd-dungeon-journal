package com.dnd.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.dto.CampaignDTO;
import com.dnd.backend.repository.CampaignRepository;
import com.dnd.backend.service.CampaignService;

import jakarta.transaction.Transactional;

@Service
public class CampaignServiceImpl implements CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CampaignDTO> findAllCampaigns() {
        return campaignRepository.findAll().stream()
            .map(campaign -> modelMapper.map(campaign, CampaignDTO.class))
            .toList();
    };

    @Override
    public Optional<CampaignDTO> findCampaignById(Long id) {
        Optional<Campaign> foundCampaign = campaignRepository.findById(id);
        return foundCampaign.map(campaign -> modelMapper.map(campaign, CampaignDTO.class));
    }
    
    @Override
    public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
        campaignRepository.save(modelMapper.map(campaignDTO, Campaign.class));
        return campaignDTO;
    }

    @Override
    @Transactional
    public Optional<CampaignDTO> updateCampaign(Long id, CampaignDTO campaignDTO) {
        Optional<Campaign> existingCampaign = campaignRepository.findById(id);

        if (existingCampaign.isPresent()) {
            Campaign campaign = existingCampaign.get();

            campaign.setTitle(campaignDTO.title());
            campaign.setDescription(campaignDTO.description());
            campaign.setDungeonMaster(campaignDTO.dungeonMaster());
            campaign.setBeginningDate(campaignDTO.beginningDate());
            campaign.setStatus(campaignDTO.status());

            campaignRepository.save(campaign);
            return Optional.of(campaignDTO);
        }
        
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteCampaign(Long campaignId) {
        campaignRepository.deleteById(campaignId);
    }

    public List<CampaignDTO> findCampaignsByDungeonMasterUsername(String username) {
        return campaignRepository.findByDungeonMasterUsername(username).stream()
            .map(campaign -> modelMapper.map(campaign, CampaignDTO.class))
            .toList();
    }
}
