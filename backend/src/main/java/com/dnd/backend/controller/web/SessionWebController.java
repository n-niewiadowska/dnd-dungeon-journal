package com.dnd.backend.controller.web;

import com.dnd.backend.dto.SessionDTO;
import com.dnd.backend.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SessionWebController {

    private final SessionService sessionService;

    @GetMapping("/session")
    public String getSessionsForCampaign(@RequestParam String title, Model model) {
        List<SessionDTO> sessions = sessionService.findSessionsByCampaignTitle(title);
        model.addAttribute("sessions", sessions);
        return "session/list";
    }
}
