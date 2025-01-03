package com.dnd.backend.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dnd.backend.mapper.CampaignMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.domain.Player;
import com.dnd.backend.domain.Session;
import com.dnd.backend.dto.CampaignDTO;
import com.dnd.backend.repository.CampaignRepository;
import com.dnd.backend.repository.PlayerRepository;
import com.dnd.backend.repository.SessionRepository;
import com.dnd.backend.service.CampaignService;
import com.dnd.backend.specification.CampaignSpecification;

import jakarta.transaction.Transactional;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final PlayerRepository playerRepository;
    private final SessionRepository sessionRepository;

    private final CampaignMapper mapper;

    public CampaignServiceImpl(
            CampaignRepository campaignRepository, 
            PlayerRepository playerRepository,
            SessionRepository sessionRepository,
            CampaignMapper mapper
    ) {
        this.campaignRepository = campaignRepository;
        this.playerRepository = playerRepository;
        this.sessionRepository = sessionRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CampaignDTO> findAllCampaigns() {
        return campaignRepository.findAll().stream()
            .map(mapper::mapCampaignToDto)
            .toList();
    }

    @Override
    public Optional<CampaignDTO> findCampaignById(Long id) {
        Optional<Campaign> foundCampaign = campaignRepository.findById(id);
        return foundCampaign.map(mapper::mapCampaignToDto);
    }
    
    @Override
    public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
        Campaign newCampaign = Campaign.builder()
                .title(campaignDTO.title())
                .description(campaignDTO.description())
                .dungeonMaster(mapper.mapDtoToCampaign(campaignDTO).getDungeonMaster())
                .beginningDate(campaignDTO.beginningDate())
                .status(campaignDTO.status())
                .build();
        Campaign savedCampaign = campaignRepository.save(newCampaign);
        return mapper.mapCampaignToDto(savedCampaign);
    }

    @Override
    @Transactional
    public Optional<CampaignDTO> updateCampaign(Long id, CampaignDTO campaignDTO) {
        Optional<Campaign> existingCampaign = campaignRepository.findById(id);

        if (existingCampaign.isPresent()) {
            Campaign campaign = existingCampaign.get();

            campaign.setTitle(campaignDTO.title());
            campaign.setDescription(campaignDTO.description());
            campaign.setDungeonMaster(mapper.mapDtoToCampaign(campaignDTO).getDungeonMaster());
            campaign.setBeginningDate(campaignDTO.beginningDate());
            campaign.setStatus(campaignDTO.status());

            campaignRepository.save(campaign);
            return Optional.of(mapper.mapCampaignToDto(campaign));
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
            .map(mapper::mapCampaignToDto)
            .toList();
    }

    @Transactional
    public Optional<CampaignDTO> addPlayerToCampaign(Long campaignId, Long playerId) {
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        Optional<Player> player = playerRepository.findById(playerId);

        System.out.println("Campaign found: " + campaign.isPresent());
        System.out.println("Player found: " + player.isPresent());

        if (campaign.isPresent() && player.isPresent()) {
            campaign.get().addPlayer(player.get());

            campaignRepository.save(campaign.get());

            return campaign.map(mapper::mapCampaignToDto);
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<CampaignDTO> removePlayerFromCampaign(Long campaignId, Long playerId) {
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);

        if (campaign.isPresent()) {
            campaign.get().removePlayer(playerId);
            campaignRepository.save(campaign.get());

            return campaign.map(mapper::mapCampaignToDto);
        }
        
        return Optional.empty();
    }

    @Transactional
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

    @Transactional
    public Optional<CampaignDTO> removeSessionFromCampaign(Long campaignId, Long sessionId) {
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);

        if (campaign.isPresent()) {
            campaign.get().removeSession(sessionId);
            campaignRepository.save(campaign.get());

            return campaign.map(mapper::mapCampaignToDto);
        }
        
        return Optional.empty();
    }

    public Integer getCountOfCampaignsForDungeonMaster(Long dmId) {
        return campaignRepository.countCampaignsByDungeonMaster(dmId);
    }

    public List<String> findPlannedCampaignTitlesInFuture(LocalDate date) {
        return campaignRepository.findPlannedCampaignTitlesAfterDate(date);
    }

    public Double getAverageOfPlayersPresenceInCampaignSessions(Long campaignId) {
        return campaignRepository.findAverageNumberOfPlayersPerSessionInCampaign(campaignId);
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
}
