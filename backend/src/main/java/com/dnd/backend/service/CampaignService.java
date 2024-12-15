package com.dnd.backend.service;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.dto.CampaignDTO;

public interface CampaignService {

    List<CampaignDTO> findAllCampaigns();

    Optional<CampaignDTO> findCampaignById(Long id);

    CampaignDTO createCampaign(CampaignDTO campaignDTO);

    Optional<CampaignDTO> updateCampaign(Long id, CampaignDTO campaignDTO);

    void deleteCampaign(Long campaignId);
}
