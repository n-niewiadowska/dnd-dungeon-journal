package com.dnd.backend.controller.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dnd.backend.dto.SessionDTO;
import com.dnd.backend.service.SessionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {
    
    private final SessionService sessionService;

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

    @GetMapping("/campaign")
    public ResponseEntity<List<SessionDTO>> getSessionsByCampaignTitle(@RequestParam String title) {
        List<SessionDTO> sessions = sessionService.findSessionsByCampaignTitle(title);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/count")
    public ResponseEntity<List<Object[]>> getSessionCountByCampaign() {
        List<Object[]> counts = sessionService.findSessionCountByCampaign();
        return ResponseEntity.ok(counts);
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
}
