package com.dnd.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.Player;
import com.dnd.backend.domain.Session;
import com.dnd.backend.dto.SessionDTO;
import com.dnd.backend.repository.PlayerRepository;
import com.dnd.backend.repository.SessionRepository;
import com.dnd.backend.service.SessionService;

import jakarta.transaction.Transactional;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final PlayerRepository playerRepository;

    private final ModelMapper modelMapper;

    public SessionServiceImpl(
            SessionRepository sessionRepository, 
            PlayerRepository playerRepository, 
            ModelMapper modelMapper
    ) {
        this.sessionRepository = sessionRepository;
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
    }
    
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
        Session savedSession = sessionRepository.save(modelMapper.map(sessionDTO, Session.class));
        return modelMapper.map(savedSession, SessionDTO.class);
    }

    @Override
    @Transactional
    public Optional<SessionDTO> updateSession(Long id, SessionDTO sessionDTO) {
        Optional<Session> existingSession = sessionRepository.findById(id);

        if (existingSession.isPresent()) {
            Session session = existingSession.get();

            session.toBuilder()
                .sessionDate(sessionDTO.sessionDate())
                .notes(sessionDTO.notes())
                .campaign(sessionDTO.campaign())
                .build();

            sessionRepository.save(session);
            return Optional.of(modelMapper.map(session, SessionDTO.class));
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

    @Transactional
    public Optional<SessionDTO> addAttendeeToSession(Long sessionId, Long playerId) {
        Optional<Session> session = sessionRepository.findById(sessionId);
        Optional<Player> player = playerRepository.findById(playerId);

        if (session.isPresent() && session.isPresent()) {
            session.get().addAttendee(player.get());

            sessionRepository.save(session.get());
            return session.map(s -> modelMapper.map(s, SessionDTO.class));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<SessionDTO> removePlayerFromSession(Long sessionId, Long playerId) {
        Optional<Session> session = sessionRepository.findById(sessionId);

        if (session.isPresent()) {
            session.get().removeAttendee(playerId);
            sessionRepository.save(session.get());

            return session.map(s -> modelMapper.map(s, SessionDTO.class));
        }
        
        return Optional.empty();
    }

    public List<Object[]> getAttendeesCountForEverySession() {
        return sessionRepository.countAttendeesForEverySession();
    }
}
