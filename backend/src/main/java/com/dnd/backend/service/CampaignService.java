package com.dnd.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dnd.backend.domain.DndCharacter;
import com.dnd.backend.mapper.CampaignMapper;
import com.dnd.backend.repository.DndCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.domain.Session;
import com.dnd.backend.dto.CampaignDTO;
import com.dnd.backend.repository.CampaignRepository;
import com.dnd.backend.repository.SessionRepository;
import com.dnd.backend.specification.CampaignSpecification;

import jakarta.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final DndCharacterRepository dndCharacterRepository;
    private final SessionRepository sessionRepository;

    private final CampaignMapper mapper;

    public List<CampaignDTO> findAllCampaigns() {
        return campaignRepository.findAll().stream()
            .map(mapper::mapCampaignToDto)
            .toList();
    }

    public Optional<CampaignDTO> findCampaignById(Long id) {
        Optional<Campaign> foundCampaign = campaignRepository.findById(id);
        return foundCampaign.map(mapper::mapCampaignToDto);
    }

    public Page<CampaignDTO> searchCampaignsWithFilterSortAndPagination(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", "2"));
        Sort sort = CampaignSpecification.getSort(params.get("sort"));

        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Campaign> specification = CampaignSpecification.getSpecification(params);

        return campaignRepository.findAll(specification, pageable)
            .map(mapper::mapCampaignToDto);
    }

    public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
        Campaign newCampaign = Campaign.builder()
                .title(campaignDTO.title())
                .description(campaignDTO.description())
                .beginningDate(campaignDTO.beginningDate())
                .status(campaignDTO.status())
                .build();
        Campaign savedCampaign = campaignRepository.save(newCampaign);
        return mapper.mapCampaignToDto(savedCampaign);
    }

    public Optional<CampaignDTO> updateCampaign(Long id, CampaignDTO campaignDTO) {
        Optional<Campaign> existingCampaign = campaignRepository.findById(id);

        if (existingCampaign.isPresent()) {
            Campaign campaign = existingCampaign.get();

            campaign.setTitle(campaignDTO.title());
            campaign.setDescription(campaignDTO.description());
            campaign.setBeginningDate(campaignDTO.beginningDate());
            campaign.setStatus(campaignDTO.status());

            campaignRepository.save(campaign);
            return Optional.of(mapper.mapCampaignToDto(campaign));
        }

        return Optional.empty();
    }

    public void deleteCampaign(Long campaignId) {
        campaignRepository.deleteById(campaignId);
    }

    public Optional<CampaignDTO> addCharacterToCampaign(Long campaignId, Long characterId) {
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        Optional<DndCharacter> dndCharacter = dndCharacterRepository.findById(characterId);

        if (campaign.isPresent() && dndCharacter.isPresent()) {
            campaign.get().addCharacter(dndCharacter.get());

            campaignRepository.save(campaign.get());
            return campaign.map(mapper::mapCampaignToDto);
        }

        return Optional.empty();
    }

    public void removeCharacterFromCampaign(Long campaignId, Long characterId) {
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);

        if (campaign.isPresent()) {
            campaign.get().removeCharacter(characterId);
            campaignRepository.save(campaign.get());
        }
    }

    public Optional<CampaignDTO> addSessionToCampaign(Long campaignId, Long sessionId) {
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        Optional<Session> session = sessionRepository.findById(sessionId);

        if (campaign.isPresent() && session.isPresent()) {
            campaign.get().addSession(session.get());

            campaignRepository.save(campaign.get());
            return campaign.map(mapper::mapCampaignToDto);
        }

        return Optional.empty();
    }

    public void removeSessionFromCampaign(Long campaignId, Long sessionId) {
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);

        if (campaign.isPresent()) {
            campaign.get().removeSession(sessionId);
            campaignRepository.save(campaign.get());
        }
    }

    public Optional<CampaignDTO> findCampaignByTitle(String title) {
        Optional<Campaign> campaign = campaignRepository.findByTitle(title);
        return campaign.map(mapper::mapCampaignToDto);
    }

    public Optional<CampaignDTO> findCampaignByTitleAndStatus(String title, String status) {
        Optional<Campaign> campaign = campaignRepository.findByTitleAndStatus(title, status);
        return campaign.map(mapper::mapCampaignToDto);
    }

    public List<String> findPlannedCampaignTitlesInFuture(LocalDate date) {
        return campaignRepository.findPlannedCampaignTitlesAfterDate(date);
    }

    public List<Object[]> findCampaignsByCharacterCount(int minCampaigns) {
        return campaignRepository.findCampaignsWithMinimumCharacterCount(minCampaigns);
    }
}
