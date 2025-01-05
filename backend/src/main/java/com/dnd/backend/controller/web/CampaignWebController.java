package com.dnd.backend.controller.web;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dnd.backend.constant.GameStatus;
import com.dnd.backend.domain.DungeonMaster;
import com.dnd.backend.dto.DungeonMasterDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnd.backend.dto.CampaignDTO;
import com.dnd.backend.service.impl.CampaignServiceImpl;
import com.dnd.backend.service.impl.DungeonMasterServiceImpl;

import jakarta.validation.Valid;

@Controller
public class CampaignWebController {
    
    private final CampaignServiceImpl campaignService;
    private final DungeonMasterServiceImpl dungeonMasterService;

    public CampaignWebController(CampaignServiceImpl campaignService, DungeonMasterServiceImpl dungeonMasterService) {
        this.campaignService = campaignService;
        this.dungeonMasterService = dungeonMasterService;
    }

    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/campaign")
    public String getAllCampaigns(Model model) {
        List<CampaignDTO> campaigns = campaignService.findAllCampaigns();
        model.addAttribute("campaigns", campaigns);
        return "campaign/list";
    }

    @GetMapping("/search")
    public String searchCampaignsWithFilterSortAndPagination(@RequestParam Map<String, String> params, Model model) {
        Page<CampaignDTO> campaigns = campaignService.searchCampaignsWithFilterSortAndPagination(params);

        model.addAttribute("campaigns", campaigns.getContent()); 
        model.addAttribute("totalPages", campaigns.getTotalPages());
        model.addAttribute("currentPage", campaigns.getNumber());
        model.addAttribute("totalElements", campaigns.getTotalElements());
        model.addAttribute("pageSize", campaigns.getSize());
        model.addAttribute("filters", params);
        model.addAttribute("statuses", GameStatus.values());

        return "campaign/search";
    }

    @GetMapping("/new")
    public String showAddCampaignForm(Model model) {
        DungeonMasterDTO dm = dungeonMasterService.findDungeonMasterById(1L).get();
        model.addAttribute("campaign", new CampaignDTO(null, "", "", dm, null, GameStatus.PLANNED));
        model.addAttribute("statuses", GameStatus.values());
        return "campaign/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditCampaignForm(@PathVariable Long id, Model model) {
        Optional<CampaignDTO> existingCampaign = campaignService.findCampaignById(id);
        if (existingCampaign.isPresent()) {
            CampaignDTO campaign = existingCampaign.get();
            model.addAttribute("campaign", campaign);
            model.addAttribute("statuses", GameStatus.values());
            return "campaign/form";
        }
        return "redirect:/campaign";
    }

    @PostMapping("/new")
    public String addCampaign(@Valid @ModelAttribute CampaignDTO campaignDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "campaign/form";
        }
        campaignService.createCampaign(campaignDTO);
        return "redirect:/campaign";
    }


    @PostMapping("/edit/{id}")
    public String updateCampaign(@PathVariable Long id, @Valid @ModelAttribute CampaignDTO campaignDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "campaign/form";
        }
        campaignService.updateCampaign(id, campaignDTO);
        return "redirect:/campaign";
    }

    @GetMapping("/delete/{id}")
    public String deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        // dungeonMasterService.removeHostedCampaign(id, id);
        return "redirect:/campaign";
    }
}
