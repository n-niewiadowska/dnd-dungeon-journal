package com.dnd.backend.controller.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dnd.backend.dto.SessionDTO;
import com.dnd.backend.service.impl.SessionServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    
    SessionServiceImpl sessionService;

    public SessionController(SessionServiceImpl sessionService) {
        this.sessionService = sessionService;
    }

    // GET requests

    @GetMapping
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        List<SessionDTO> sessions = sessionService.findAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id) {
        Optional<SessionDTO> session = sessionService.findSessionById(id);
        return session.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/campaign/{title}")
    public ResponseEntity<List<SessionDTO>> getSessionsByCampaignTitle(@PathVariable String title) {
        List<SessionDTO> sessions = sessionService.findSessionsByCampaignTitle(title);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/attendees-count")
    public ResponseEntity<List<?>> getAttendeesCount() {
        List<Object[]> count = sessionService.getAttendeesCountForEverySession();
        return ResponseEntity.ok(count);
    }

    // POST, PUT and DELETE requests

    @PostMapping
    public ResponseEntity<SessionDTO> createSession(@RequestBody @Valid SessionDTO sessionDTO) {
        SessionDTO createdSession = sessionService.createSession(sessionDTO);
        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .build().toUri();

        return ResponseEntity.created(location).body(createdSession);
    }

    @PostMapping("/{sessionId}/attendees/{playerId}")
    public ResponseEntity<SessionDTO> addAttendee(@PathVariable Long sessionId, @PathVariable Long playerId) {
        Optional<SessionDTO> updatedSession = sessionService.addAttendeeToSession(sessionId, playerId);
        return updatedSession.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionDTO> updateSession(@PathVariable Long id, @RequestBody @Valid SessionDTO sessionDTO) {
        Optional<SessionDTO> updatedSession = sessionService.updateSession(id, sessionDTO);
        return updatedSession.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable Long id) {
        Optional<SessionDTO> dndCharacter = sessionService.findSessionById(id);
        if (dndCharacter.isPresent()) {
            sessionService.deleteSession(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{sessionId}/attendees/{playerId}")
    public ResponseEntity<SessionDTO> removeAttendee(@PathVariable Long sessionId, @PathVariable Long playerId) {
        Optional<SessionDTO> updatedSession = sessionService.removePlayerFromSession(sessionId, playerId);
        return updatedSession.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
