package com.dnd.backend.controller.api;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
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
import com.dnd.backend.service.CampaignService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/campaign")
@RequiredArgsConstructor
public class CampaignController {
    
    private final CampaignService campaignService;

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

    @GetMapping("/planned")
    public ResponseEntity<List<String>> getPlannedCampaignTitles(@RequestParam("date") String date) {
        List<String> titles = campaignService.findPlannedCampaignTitlesInFuture(LocalDate.parse(date));
        return ResponseEntity.ok(titles);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CampaignDTO>> searchCampaignsWithFilterSortAndPagination(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(campaignService.searchCampaignsWithFilterSortAndPagination(params));
    }

    @GetMapping("/title")
    public ResponseEntity<CampaignDTO> getCampaignByTitle(@RequestParam String title) {
        Optional<CampaignDTO> campaign = campaignService.findCampaignByTitle(title);
        return campaign.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/characters/{count}")
    public ResponseEntity<List<Object[]>> getCampainsWithMinimumCharacters(@PathVariable String count) {
        List<Object[]> campaigns = campaignService.findCampaignsByCharacterCount(Integer.parseInt(count));
        return ResponseEntity.ok(campaigns);
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

    @PostMapping("/{campaignId}/characters/{characterId}")
    public ResponseEntity<CampaignDTO> addCharacterToCampaign(@PathVariable Long campaignId, @PathVariable Long characterId) {
        Optional<CampaignDTO> updatedCampaign = campaignService.addCharacterToCampaign(campaignId, characterId);
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

    @DeleteMapping("/{campaignId}/characters/{characterId}")
    public ResponseEntity<?> removeCharacterFromCampaign(@PathVariable Long campaignId, @PathVariable Long characterId) {
        campaignService.removeCharacterFromCampaign(campaignId, characterId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{campaignId}/sessions/{sessionId}")
    public ResponseEntity<?> removeSessionFromCampaign(@PathVariable Long campaignId, @PathVariable Long sessionId) {
        campaignService.removeSessionFromCampaign(campaignId, sessionId);
        return ResponseEntity.noContent().build();
    }
}
