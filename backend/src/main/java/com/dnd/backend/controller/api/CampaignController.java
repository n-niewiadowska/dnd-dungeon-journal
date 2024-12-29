package com.dnd.backend.controller.api;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dnd.backend.dto.CampaignDTO;
import com.dnd.backend.service.impl.CampaignServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/campaign")
public class CampaignController {
    
    CampaignServiceImpl campaignService;

    public CampaignController(CampaignServiceImpl campaignService) {
        this.campaignService = campaignService;
    }

    // GET requests

    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {
        List<CampaignDTO> campaigns = campaignService.findAllCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable Long id) {
        Optional<CampaignDTO> campaign = campaignService.findCampaignById(id);
        return campaign.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/dm/{username}")
    public ResponseEntity<List<CampaignDTO>> getCampaignsByDungeonMaster(@PathVariable String username) {
        List<CampaignDTO> campaigns = campaignService.findCampaignsByDungeonMasterUsername(username);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/dm/count/{dmId}")
    public ResponseEntity<Integer> getCampaignCountForDungeonMaster(@PathVariable Long dmId) {
        Integer count = campaignService.getCountOfCampaignsForDungeonMaster(dmId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/planned")
    public ResponseEntity<List<String>> getPlannedCampaignTitles(@RequestParam("date") String date) {
        List<String> titles = campaignService.findPlannedCampaignTitlesInFuture(LocalDate.parse(date));
        return ResponseEntity.ok(titles);
    }

    @GetMapping("/{campaignId}/players/avg")
    public ResponseEntity<Double> getAveragePlayersInCampaignSessions(@PathVariable Long campaignId) {
        Double average = campaignService.getAverageOfPlayersPresenceInCampaignSessions(campaignId);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CampaignDTO>> searchCampaignsWithFilterSortAndPagination(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(campaignService.searchCampaignsWithFilterSortAndPagination(params));
    }

    // POST and PUT requests

    @PostMapping
    public ResponseEntity<CampaignDTO> createCampaign(@RequestBody @Valid CampaignDTO campaignDTO) {
        CampaignDTO createdCampaign = campaignService.createCampaign(campaignDTO);
        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .build().toUri();

        return ResponseEntity.created(location).body(createdCampaign);
    }

    @PostMapping("/{campaignId}/players/{playerId}")
    public ResponseEntity<CampaignDTO> addPlayerToCampaign(@PathVariable Long campaignId, @PathVariable Long playerId) {
        Optional<CampaignDTO> updatedCampaign = campaignService.addPlayerToCampaign(campaignId, playerId);
        return updatedCampaign.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{campaignId}/sessions/{sessionId}")
    public ResponseEntity<CampaignDTO> addSessionToCampaign(@PathVariable Long campaignId, @PathVariable Long sessionId) {
        Optional<CampaignDTO> updatedCampaign = campaignService.addSessionToCampaign(campaignId, sessionId);
        return updatedCampaign.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignDTO> updateCampaign(@PathVariable Long id, @RequestBody @Valid CampaignDTO campaignDTO) {
        Optional<CampaignDTO> updatedCampaign = campaignService.updateCampaign(id, campaignDTO);
        return updatedCampaign.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE requests

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCampaign(@PathVariable Long id) {
        Optional<CampaignDTO> campaign = campaignService.findCampaignById(id);
        if (campaign.isPresent()) {
            campaignService.deleteCampaign(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{campaignId}/players/{playerId}")
    public ResponseEntity<CampaignDTO> removePlayerFromCampaign(@PathVariable Long campaignId, @PathVariable Long playerId) {
        Optional<CampaignDTO> updatedCampaign = campaignService.removePlayerFromCampaign(campaignId, playerId);
        return updatedCampaign.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{campaignId}/sessions/{sessionId}")
    public ResponseEntity<CampaignDTO> removeSessionFromCampaign(@PathVariable Long campaignId, @PathVariable Long sessionId) {
        Optional<CampaignDTO> updatedCampaign = campaignService.removeSessionFromCampaign(campaignId, sessionId);
        return updatedCampaign.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
