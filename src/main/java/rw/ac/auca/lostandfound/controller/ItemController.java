package rw.ac.auca.lostandfound.controller;

import jakarta.validation.Valid;
import rw.ac.auca.lostandfound.model.Item;
import rw.ac.auca.lostandfound.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createItem(@Valid @RequestBody Item item) {
        if (!isValidStatus(item.getStatus())) {
            return ResponseEntity.badRequest().body("Status must be LOST, FOUND, or CLAIMED");
        }
        Item saved = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @Valid @RequestBody Item updatedItem) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    if (!isValidStatus(updatedItem.getStatus())) {
                        return ResponseEntity.badRequest().body("Status must be LOST, FOUND, or CLAIMED");
                    }
                    updatedItem.setId(id);
                    Item saved = itemRepository.save(updatedItem);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (!itemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isValidStatus(String status) {
        return "LOST".equalsIgnoreCase(status)
                || "FOUND".equalsIgnoreCase(status)
                || "CLAIMED".equalsIgnoreCase(status);
    }
}