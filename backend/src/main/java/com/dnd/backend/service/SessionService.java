package com.dnd.backend.service;

import java.util.List;
import java.util.Optional;

import com.dnd.backend.dto.SessionDTO;

public interface SessionService {
    
    List<SessionDTO> findAllSessions();

    Optional<SessionDTO> findSessionById(Long id);

    SessionDTO createSession(SessionDTO sessionDTO);

    Optional<SessionDTO> updateSession(Long id, SessionDTO sessionDTO);

    void deleteSession(Long id);
}
