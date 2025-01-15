package com.dnd.backend.service;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.domain.Campaign;
import com.dnd.backend.mapper.SessionMapper;
import com.dnd.backend.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.Session;
import com.dnd.backend.dto.SessionDTO;
import com.dnd.backend.repository.SessionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final CampaignRepository campaignRepository;

    private final SessionMapper mapper;

    public List<SessionDTO> findAllSessions() {
        return sessionRepository.findAll().stream()
                .map(mapper::mapSessionToDto)
                .toList();
    }

    public Optional<SessionDTO> findSessionById(Long id) {
        Optional<Session> foundSession = sessionRepository.findById(id);
        return foundSession.map(mapper::mapSessionToDto);
    }

    public SessionDTO createSession(SessionDTO sessionDTO) {
        Campaign campaign = sessionDTO.campaign() == null ?
                null : campaignRepository.findById(sessionDTO.campaign().id()).orElse(null);

        Session newSession = Session.builder()
                .sessionDate(sessionDTO.sessionDate())
                .notes(sessionDTO.notes())
                .campaign(campaign)
                .build();
        Session savedSession = sessionRepository.save(newSession);
        return mapper.mapSessionToDto(savedSession);
    }

    public Optional<SessionDTO> updateSession(Long id, SessionDTO sessionDTO) {
        Optional<Session> existingSession = sessionRepository.findById(id);

        if (existingSession.isPresent()) {
            Session session = existingSession.get();
            Campaign campaign = sessionDTO.campaign() == null ?
                    null : campaignRepository.findById(sessionDTO.campaign().id()).orElse(null);

            session.setSessionDate(sessionDTO.sessionDate());
            session.setNotes(sessionDTO.notes());
            if (campaign != null) {
                session.setCampaign(campaign);
            }

            sessionRepository.save(session);
            return Optional.of(mapper.mapSessionToDto(session));
        }
        
        return Optional.empty();
    }

    public void deleteSession(Long id) {
        List<Campaign> campaigns = campaignRepository.findAll();
        for (Campaign campaign : campaigns) {
            campaign.removeSession(id);
            campaignRepository.save(campaign);
        }
        sessionRepository.deleteById(id);
    }

    public List<SessionDTO> findSessionsByCampaignTitle(String campaignTitle) {
        return sessionRepository.findByCampaignTitle(campaignTitle).stream()
            .map(mapper::mapSessionToDto)
            .toList();
    }

    public List<Object[]> findSessionCountByCampaign() {
        return sessionRepository.findSessionCountByCampaign();
    }
}
