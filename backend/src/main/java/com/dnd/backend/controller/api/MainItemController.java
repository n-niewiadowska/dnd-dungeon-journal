package com.dnd.backend.controller.api;

import com.dnd.backend.dto.MainItemDTO;
import com.dnd.backend.service.MainItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class MainItemController {

    private final MainItemService itemService;

    @GetMapping
    ResponseEntity<List<MainItemDTO>> getAllItems() {
        List<MainItemDTO> items = itemService.findAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    ResponseEntity<MainItemDTO> getItemById(@PathVariable Long id) {
        Optional<MainItemDTO> item = itemService.findItemById(id);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/skills")
    ResponseEntity<List<Object[]>> getItemsWithSkillEffect(@RequestParam String effect) {
        List<Object[]> items = itemService.findItemsWithSkillEffect(effect);
        return ResponseEntity.ok(items);
    }

    // POST, PUT and DELETE requests

    @PostMapping
    public ResponseEntity<MainItemDTO> createItem(@RequestBody @Valid MainItemDTO mainItemDTO) {
        MainItemDTO createdItem = itemService.createItem(mainItemDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build().toUri();

        return ResponseEntity.created(location).body(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MainItemDTO> updateItem(@PathVariable Long id, @RequestBody @Valid MainItemDTO mainItemDTO) {
        Optional<MainItemDTO> updatedItem = itemService.updateItem(id, mainItemDTO);
        return updatedItem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        Optional<MainItemDTO> item = itemService.findItemById(id);
        if (item.isPresent()) {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
