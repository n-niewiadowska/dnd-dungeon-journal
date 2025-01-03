package com.dnd.backend.controller.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.dnd.backend.dto.DndCharacterDTO;
import com.dnd.backend.service.impl.DndCharacterServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/character")
public class DndCharacterController {
    
    DndCharacterServiceImpl dndCharacterService;

    public DndCharacterController(DndCharacterServiceImpl dndCharacterService) {
        this.dndCharacterService = dndCharacterService;
    }

    // GET requests

    @GetMapping
    ResponseEntity<List<DndCharacterDTO>> getAllCharacters() {
        List<DndCharacterDTO> characters = dndCharacterService.findAllCharacters();
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/{id}")
    ResponseEntity<DndCharacterDTO> getCharacterById(@PathVariable Long id) {
        Optional<DndCharacterDTO> dndCharacter = dndCharacterService.findCharacterById(id);
        return dndCharacter.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/avg-magical/{dndClass}")
    ResponseEntity<Double> getAverageOfMagicalCharactersForClass(@PathVariable String dndClass) {
        try {
            Double average = dndCharacterService.findAverageOfMagicalCharactersFromClass(dndClass);
            return ResponseEntity.ok(average);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // POST, PUT and DELETE requests

    @PostMapping
    public ResponseEntity<DndCharacterDTO> createCharacter(@RequestBody @Valid DndCharacterDTO dndCharacterDTO) {
        DndCharacterDTO createdCharacter = dndCharacterService.createCharacter(dndCharacterDTO);
        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .build().toUri();

        return ResponseEntity.created(location).body(createdCharacter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DndCharacterDTO> updateCharacter(@PathVariable Long id, @RequestBody @Valid DndCharacterDTO dndCharacterDTO) {
        Optional<DndCharacterDTO> updatedCharacter = dndCharacterService.updateCharacter(id, dndCharacterDTO);
        return updatedCharacter.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable Long id) {
        Optional<DndCharacterDTO> dndCharacter = dndCharacterService.findCharacterById(id);
        if (dndCharacter.isPresent()) {
            dndCharacterService.deleteCharacter(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
