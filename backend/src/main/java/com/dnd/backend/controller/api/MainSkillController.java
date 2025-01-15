package com.dnd.backend.controller.api;

import com.dnd.backend.constant.Ability;
import com.dnd.backend.dto.MainSkillDTO;
import com.dnd.backend.service.MainSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/skill")
@RequiredArgsConstructor
public class MainSkillController {

    private final MainSkillService skillService;

    @GetMapping
    ResponseEntity<List<MainSkillDTO>> getAllSkills() {
        List<MainSkillDTO> items = skillService.findAllSkills();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    ResponseEntity<MainSkillDTO> getSkillById(@PathVariable Long id) {
        Optional<MainSkillDTO> item = skillService.findSkillById(id);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/abilities")
    ResponseEntity<List<MainSkillDTO>> getSkillByAbilityOrLevel(@RequestParam String ability, @RequestParam int level) {
        Ability givenAbility = Ability.valueOf(ability);
        List<MainSkillDTO> skills = skillService.findSkillByAbilityOrLevel(givenAbility, level);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/bonus/{bonus}")
    ResponseEntity<List<Object[]>> getSkillsWithItemBonus(@PathVariable String bonus) {
        List<Object[]> skills = skillService.findSkillsWithItemBonus(Integer.parseInt(bonus));
        return ResponseEntity.ok(skills);
    }

    // POST, PUT and DELETE requests

    @PostMapping
    public ResponseEntity<MainSkillDTO> createSkill(@RequestBody @Valid MainSkillDTO mainSkillDTO) {
        MainSkillDTO createdItem = skillService.createSkill(mainSkillDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build().toUri();

        return ResponseEntity.created(location).body(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MainSkillDTO> updateSkill(@PathVariable Long id, @RequestBody @Valid MainSkillDTO mainSkillDTO) {
        Optional<MainSkillDTO> updatedItem = skillService.updateSkill(id, mainSkillDTO);
        return updatedItem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long id) {
        Optional<MainSkillDTO> item = skillService.findSkillById(id);
        if (item.isPresent()) {
            skillService.deleteSkill(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
