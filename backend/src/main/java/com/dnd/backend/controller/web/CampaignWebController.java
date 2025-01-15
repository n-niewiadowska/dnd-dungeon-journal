package com.dnd.backend.controller.web;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dnd.backend.constant.GameStatus;
import lombok.RequiredArgsConstructor;
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
import com.dnd.backend.service.CampaignService;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CampaignWebController {
    
    private final CampaignService campaignService;

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

    @GetMapping("/campaign/search")
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


    @GetMapping("/campaign/new")
    public String showAddCampaignForm(Model model) {
        model.addAttribute("campaign", new CampaignDTO(null, "", "", null, GameStatus.PLANNED, null));
        model.addAttribute("statuses", GameStatus.values());
        return "campaign/form";
    }

    @GetMapping("/campaign/edit/{id}")
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

    @PostMapping("/campaign/new")
    public String addCampaign(@Valid @ModelAttribute CampaignDTO campaignDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "campaign/form";
        }
        campaignService.createCampaign(campaignDTO);
        return "redirect:/campaign";
    }

    @PostMapping("/campaign/edit/{id}")
    public String updateCampaign(@PathVariable Long id, @Valid @ModelAttribute CampaignDTO campaignDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "campaign/form";
        }
        campaignService.updateCampaign(id, campaignDTO);
        return "redirect:/campaign";
    }

    @GetMapping("/campaign/delete/{id}")
    public String deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return "redirect:/campaign";
    }

    @GetMapping("/session/campaign")
    public String getCampaignsForSessions(Model model) {
        List<CampaignDTO> campaigns = campaignService.findAllCampaigns();
        model.addAttribute("campaigns", campaigns);
        model.addAttribute("title", "");
        return "session/find";
    }
}
