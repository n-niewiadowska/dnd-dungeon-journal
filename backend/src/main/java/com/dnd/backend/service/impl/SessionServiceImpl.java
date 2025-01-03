package com.dnd.backend.service.impl;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.mapper.SessionMapper;
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

    private final SessionMapper mapper;

    public SessionServiceImpl(
            SessionRepository sessionRepository,
            PlayerRepository playerRepository,
            SessionMapper mapper
    ) {
        this.sessionRepository = sessionRepository;
        this.playerRepository = playerRepository;
        this.mapper = mapper;
    }
    
    @Override
    public List<SessionDTO> findAllSessions() {
        return sessionRepository.findAll().stream()
                .map(mapper::mapSessionToDto)
                .toList();
    }

    @Override
    public Optional<SessionDTO> findSessionById(Long id) {
        Optional<Session> foundSession = sessionRepository.findById(id);
        return foundSession.map(mapper::mapSessionToDto);
    }

    @Override
    public SessionDTO createSession(SessionDTO sessionDTO) {
        Session newSession = Session.builder()
                .sessionDate(sessionDTO.sessionDate())
                .notes(sessionDTO.notes())
                .campaign(mapper.mapDtoToSession(sessionDTO).getCampaign())
                .build();
        Session savedSession = sessionRepository.save(newSession);
        return mapper.mapSessionToDto(savedSession);
    }

    @Override
    @Transactional
    public Optional<SessionDTO> updateSession(Long id, SessionDTO sessionDTO) {
        Optional<Session> existingSession = sessionRepository.findById(id);

        if (existingSession.isPresent()) {
            Session session = existingSession.get();

            session.setSessionDate(sessionDTO.sessionDate());
            session.setNotes(sessionDTO.notes());
            session.setCampaign(mapper.mapDtoToSession(sessionDTO).getCampaign());

            sessionRepository.save(session);
            return Optional.of(mapper.mapSessionToDto(session));
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
            .map(mapper::mapSessionToDto)
            .toList();
    }

    @Transactional
    public Optional<SessionDTO> addAttendeeToSession(Long sessionId, Long playerId) {
        Optional<Session> session = sessionRepository.findById(sessionId);
        Optional<Player> player = playerRepository.findById(playerId);

        if (session.isPresent() && player.isPresent()) {
            session.get().addAttendee(player.get());

            sessionRepository.save(session.get());
            return session.map(mapper::mapSessionToDto);
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<SessionDTO> removePlayerFromSession(Long sessionId, Long playerId) {
        Optional<Session> session = sessionRepository.findById(sessionId);

        if (session.isPresent()) {
            session.get().removeAttendee(playerId);
            sessionRepository.save(session.get());

            return session.map(mapper::mapSessionToDto);
        }
        
        return Optional.empty();
    }

    public List<Object[]> getAttendeesCountForEverySession() {
        return sessionRepository.countAttendeesForEverySession();
    }
}
