package com.dnd.backend.controller.web;

import com.dnd.backend.dto.DndCharacterDTO;
import com.dnd.backend.service.DndCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DndCharacterWebController {

    private final DndCharacterService characterService;

    @GetMapping("/character")
    public String getAllCharacters(Model model) {
        List<DndCharacterDTO> characters = characterService.findAllCharacters();
        model.addAttribute("characters", characters);
        return "character/list";
    }

    @GetMapping("/character/{id}")
    public String getCharacterDetails(@PathVariable Long id, Model model) {
        Optional<DndCharacterDTO> characterOptional = characterService.findCharacterById(id);
        if (characterOptional.isPresent()) {
            DndCharacterDTO character = characterOptional.get();
            model.addAttribute("character", character);
        }
        return "character/details";
    }

    @GetMapping("/character/delete/{id}")
    public String deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacter(id);
        return "redirect:/character";
    }
}
