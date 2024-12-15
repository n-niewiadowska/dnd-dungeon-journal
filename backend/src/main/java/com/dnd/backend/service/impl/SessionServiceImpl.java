package com.dnd.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.Session;
import com.dnd.backend.dto.SessionDTO;
import com.dnd.backend.repository.SessionRepository;
import com.dnd.backend.service.SessionService;

import jakarta.transaction.Transactional;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public List<SessionDTO> findAllSessions() {
        return sessionRepository.findAll().stream()
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .toList();
    };

    @Override
    public Optional<SessionDTO> findSessionById(Long id) {
        Optional<Session> foundSession = sessionRepository.findById(id);
        return foundSession.map(session -> modelMapper.map(session, SessionDTO.class));
    }

    @Override
    public SessionDTO createSession(SessionDTO sessionDTO) {
        sessionRepository.save(modelMapper.map(sessionDTO, Session.class));
        return sessionDTO;
    }

    @Override
    @Transactional
    public Optional<SessionDTO> updateSession(Long id, SessionDTO sessionDTO) {
        Optional<Session> existingSession = sessionRepository.findById(id);

        if (existingSession.isPresent()) {
            Session session = existingSession.get();

            session.setSessionDate(sessionDTO.sessionDate());
            session.setNotes(sessionDTO.notes());
            session.setCampaign(sessionDTO.campaign());

            sessionRepository.save(session);
            return Optional.of(sessionDTO);
        }
        
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    public List<SessionDTO> findSessionsByCampaignTitle(String campaignTitle) {
        return sessionRepository.findByCampaignTitle(campaignTitle).stream()
            .map(session -> modelMapper.map(campaignTitle, SessionDTO.class))
            .toList();
    }
}
